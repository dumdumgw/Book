package com.edwin.book.view;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.edwin.book.R;
import com.edwin.book.network.VolleySingleton;

public class MainActivity extends FragmentActivity implements
        TabHost.OnTabChangeListener, BookListFragment.OnLikeClickListener,
        BookGridFragment.OnAddBookListener {

    private static final String TAB_HOME_ID = "HOME";
    private static final String TAB_BOOK_ID = "BOOK";

    private BookListFragment bookListFragment;
    private BookGridFragment bookFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTab();

        bookFragment = new BookGridFragment();
        bookFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.tab_shopping, bookFragment).commit();
    }

    private void initTab() {
        TabHost tabHost = (TabHost) findViewById(R.id.tab_host);
        tabHost.setOnTabChangedListener(this);
        tabHost.setup();
        Point point = new Point();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(point);
        int displayWidth = point.x;
        int resWidth = displayWidth / 2;

        TabWidget tabWidget = (TabWidget) findViewById(android.R.id.tabs);
        View tabHomeMenuView = LayoutInflater.from(this).inflate(R.layout.tab_home, tabWidget, false);
        tabHomeMenuView.getLayoutParams().width = resWidth;

        TabHost.TabSpec tabShopping = tabHost.newTabSpec(TAB_HOME_ID).setContent(R.id.tab_shopping).setIndicator(tabHomeMenuView);
        tabHost.addTab(tabShopping);

        View tabBookMenuView = LayoutInflater.from(this).inflate(R.layout.tab_book, tabWidget, false);
        tabBookMenuView.getLayoutParams().width = resWidth;

        TabHost.TabSpec tabBook = tabHost.newTabSpec(TAB_BOOK_ID).setContent(R.id.tab_book).setIndicator(tabBookMenuView);
        tabHost.addTab(tabBook);
    }

    @Override
    public void onTabChanged(String tabId) {
        FragmentManager transaction = getSupportFragmentManager();
        if (tabId.equals(TAB_BOOK_ID)) {
            if (bookListFragment == null) {
                bookListFragment = new BookListFragment();
                transaction.beginTransaction().replace(R.id.tab_book, bookListFragment, tabId).commit();
            }
        }
    }

    @Override
    public void onBookSelected(String isbn) {
        bookFragment.addLikeBook(isbn);
    }

    @Override
    public void onAddBook() {
        TabHost tabHost = (TabHost) findViewById(R.id.tab_host);
        tabHost.setCurrentTabByTag(TAB_BOOK_ID);
//        onTabChanged();
//        FragmentManager transaction = getSupportFragmentManager();
//        if (bookListFragment == null) {
//            bookListFragment = new BookListFragment();
//            transaction.beginTransaction().replace(R.id.tab_book, bookListFragment, TAB_BOOK_ID).commit();
//        }
    }
}
