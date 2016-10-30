package com.edwin.book.view;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edwin.book.model.BookSearchResult;
import com.edwin.book.R;
import com.edwin.book.network.VolleySingleton;
import com.edwin.book.view.adapter.BookListAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button searchButton;
    private EditText searchQuery;

    private BookListAdapter booksAdapter;
    private List<BookSearchResult.Book> searchResult = new ArrayList<BookSearchResult.Book>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
    }

    private void setupViews() {
        searchButton = (Button) findViewById(R.id.button_search);
        searchButton.setOnClickListener(this);
        searchButton.setEnabled(false);

        searchQuery = (EditText) findViewById(R.id.input_search_query);
        searchQuery.addTextChangedListener(new SearchFieldWatcher());

        booksAdapter = new BookListAdapter(this, R.layout.item_list_book, searchResult, LayoutInflater.from(this));

        ListView searchResults = (ListView) findViewById(R.id.list_result_search);
        searchResults.setAdapter(booksAdapter);
        searchResults.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private class SearchFieldWatcher implements TextWatcher {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            searchButton.setEnabled(s.length() > 0);
        }

        public void afterTextChanged(Editable s) {
        }
    }

    @Override
    public void onClick(View v) {
        searchBook();
    }

    private void searchBook() {
        String url = "https://apis.daum.net/search/book?apikey=00e3228ef984771309c88ff96cc38de9&q=%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C&output=json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, successListener(), errorListener());
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private Response.Listener<JSONObject> successListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONObject("channel").getJSONArray("item");
                    Gson gson = new Gson();

                    for (int i=0; i<array.length(); i++) {
                        BookSearchResult.Book result = gson.fromJson(array.get(i).toString(), BookSearchResult.Book.class);
                        searchResult.add(result);
                    }
                    booksAdapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        };
    }

    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("test");
            }
        };
    }
 }
