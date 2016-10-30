package com.edwin.book.model;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BooKSharedPreference {

    private Context context;
    private static final String FAVORITE_BOOK = "favorite_book";
    private static final String KEY = "isdn";

    public BooKSharedPreference(Context context) {
        this.context = context;
    }

    public void saveFavoriteBook(String isdn) {
        SharedPreferences settings = context.getSharedPreferences(FAVORITE_BOOK, 0);
        String book = settings.getString(isdn, null);
        if (book == null) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(isdn, isdn);
            editor.commit();
        }
    }

    public List<String> loadFavoriteBooks() {
        SharedPreferences settings = context.getSharedPreferences(FAVORITE_BOOK, 0);
        Map<String, String> data = (Map<String, String>) settings.getAll();
        Collection<String> values = data.values();
        Iterator<String> itr = values.iterator();
        List<String> books = new ArrayList<String>();
        while (itr.hasNext()) {
            String book = itr.next();
            if (book != null) {
                books.add(book);
            }
        }
        return books;
    }
}
