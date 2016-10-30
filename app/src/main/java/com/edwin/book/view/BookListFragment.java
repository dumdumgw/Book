package com.edwin.book.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edwin.book.model.BooKSharedPreference;
import com.edwin.book.model.BookSearchResult;
import com.edwin.book.R;
import com.edwin.book.network.VolleySingleton;
import com.edwin.book.view.adapter.BookListAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BookListFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private BookListAdapter bookListAdapter;
    private List<BookSearchResult.Book> searchResult = new ArrayList<BookSearchResult.Book>();
    private OnLikeClickListener callback;
    private BooKSharedPreference booKSharedPreference;

    TextView txtSosul;
    TextView txtIT;
    TextView txtLang;
    ImageView likeImg;
    private BooKSharedPreference bookData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_book_list, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_books);
        txtSosul =  (TextView) rootView.findViewById(R.id.txt_sosul);
        txtIT =  (TextView) rootView.findViewById(R.id.txt_it);
        txtLang =  (TextView) rootView.findViewById(R.id.txt_lang);


        bookListAdapter = new BookListAdapter(getActivity(), R.layout.item_list_book, searchResult, inflater);
        listView.setAdapter(bookListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addFavorite(searchResult.get(position).getIsbn13(), view);
            }
        });
        txtSosul.setOnClickListener(this);
        txtIT.setOnClickListener(this);
        txtLang.setOnClickListener(this);
        txtSosul.setSelected(true);

        bookData = new BooKSharedPreference(getActivity());
        booKSharedPreference = new BooKSharedPreference(getActivity());
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        searchBook("소설");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void searchBook(String category) {
        if (searchResult != null) {
            searchResult.clear();
        }
        try {
            category = URLEncoder.encode(category, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String url = "https://apis.daum.net/search/book?apikey=00e3228ef984771309c88ff96cc38de9&q="
                + category + "&output=json&sort=popular&result=20";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, successListener(), errorListener());
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private Response.Listener<JSONObject> successListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<String> likeBooks = booKSharedPreference.loadFavoriteBooks();
                try {
                    JSONArray array = response.getJSONObject("channel").getJSONArray("item");
                    Gson gson = new Gson();

                    for (int i=0; i<array.length(); i++) {
                        BookSearchResult.Book result = gson.fromJson(array.get(i).toString(), BookSearchResult.Book.class);
                        for (String isbn : likeBooks) {
                            if (isbn.equals(result.getIsbn13())) {
                                result.setIsLike(true);
                            }
                        }
                        searchResult.add(result);
                    }
                    bookListAdapter.notifyDataSetChanged();
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
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onClick(View v) {
        txtSosul.setSelected(false);
        txtIT.setSelected(false);
        txtLang.setSelected(false);
        v.setSelected(true);
        switch (v.getId()) {
            case R.id.txt_sosul:
                searchBook("소설");
                break;
            case R.id.txt_it:
                searchBook("컴퓨터/IT");
                break;
            case R.id.txt_lang:
                searchBook("외국어");
                break;
        }
    }

    private void addFavorite(String id, View view) {
//        BookDataManager dataMng = BookDataManager.getInstance(getActivity());
//        if (!dataMng.searchExists(id)) {
//            if (dataMng.insertBook(id)) {
//                BookListAdapter.ViewHolder holder = (BookListAdapter.ViewHolder) view.getTag();
//                holder.like.setImageResource(R.drawable.icon_like_selected);
//                callback.onBookSelected(id);
//            }
//        }
        bookData.saveFavoriteBook(id);
        BookListAdapter.ViewHolder holder = (BookListAdapter.ViewHolder) view.getTag();
        holder.like.setImageResource(R.drawable.icon_like_selected);
        callback.onBookSelected(id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (OnLikeClickListener) activity;
    }

    public interface OnLikeClickListener {
        public void onBookSelected(String id);
    }

}
