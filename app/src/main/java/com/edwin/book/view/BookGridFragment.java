package com.edwin.book.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edwin.book.model.BooKSharedPreference;
import com.edwin.book.model.BookSearchResult;
import com.edwin.book.R;
import com.edwin.book.network.VolleySingleton;
import com.edwin.book.view.adapter.BookGridAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookGridFragment extends Fragment {

    OnAddBookListener callback;
    public interface OnAddBookListener {
        public void onAddBook();
    }

    private View rootView;
    private BookGridAdapter gridAdapter;
    private List<BookSearchResult.Book> searchResult = new ArrayList<BookSearchResult.Book>();
    private BooKSharedPreference bookData;

    public static BookGridFragment newInstance(String param1, String param2) {
        BookGridFragment fragment = new BookGridFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BookGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (OnAddBookListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shopping, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_shopping_item);
        gridAdapter = new BookGridAdapter(getActivity(), searchResult, inflater);
        gridView.setAdapter(gridAdapter);

        ImageView plusIcon = (ImageView) rootView.findViewById(R.id.plus_img);
        gridView.setEmptyView(plusIcon);
        plusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onAddBook();
            }
        });
        bookData = new BooKSharedPreference(getActivity());
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        searchBook();
    }

    private void searchBook() {
        searchResult.clear();
//        List<String> likeBooks = BookDataManager.getInstance(getActivity()).searchAllBooks();
        List<String> likeBooks = bookData.loadFavoriteBooks();
        for (String isbn : likeBooks) {
            String url = "https://apis.daum.net/search/book?apikey=00e3228ef984771309c88ff96cc38de9&q=" + isbn + "&output=json";
            JsonObjectRequest request = new
                    JsonObjectRequest(Request.Method.GET, url, successListener(), errorListener());
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
        }
    }

    private Response.Listener<JSONObject> successListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONObject("channel").getJSONArray("item");
                    Gson gson = new Gson();
                    for (int i=0; i<array.length(); i++) {
                        BookSearchResult.Book result = gson.fromJson(array.get(i).toString(),
                                BookSearchResult.Book.class);
                        searchResult.add(result);
                    }
                    gridAdapter.notifyDataSetChanged();
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

    public void addLikeBook(String isbn) {
        String url = "https://apis.daum.net/search/book?apikey=00e3228ef984771309c88ff96cc38de9&q=" + isbn + "&output=json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, successListener(), errorListener());
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
}
