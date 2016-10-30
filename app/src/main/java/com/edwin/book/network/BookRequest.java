package com.edwin.book.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.List;

public class BookRequest {
    private final Context ctx;

    private final String bookSearchUrl = "https://apis.daum.net/search/book?apikey=00e3228ef984771309c88ff96cc38de9&q=%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C";
    private final String outputOption = "&output=json&result=30";

    public BookRequest(Context ctx) {
        this.ctx = ctx;
    }

//    public List<?> searchPopularBook() {
//        String url = bookSearchUrl + outputOption + "&sort=popular";
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, successListener(), errorListener());
//        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
//    }
}
