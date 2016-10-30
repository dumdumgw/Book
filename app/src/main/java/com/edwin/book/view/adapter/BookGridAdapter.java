package com.edwin.book.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.edwin.book.model.BookSearchResult;
import com.edwin.book.R;
import com.edwin.book.network.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

public class BookGridAdapter extends BaseAdapter {

    private static class GridViewHolder {
        ImageView cover;
        TextView author;
    }

    private List<BookSearchResult.Book> listData = new ArrayList<BookSearchResult.Book>();
    private final LayoutInflater layoutInflater;
    private final Context context;

    public BookGridAdapter(Context context, List<BookSearchResult.Book> listData, LayoutInflater layoutInflater) {
        this.listData = listData;
        this.layoutInflater = layoutInflater;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_shopping_item, parent, false);

            holder = new GridViewHolder();
            holder.cover = (ImageView) convertView.findViewById(R.id.grid_image);
            holder.author = (TextView) convertView.findViewById(R.id.txt_author);
            convertView.setTag(holder);
        } else {
            holder = (GridViewHolder) convertView.getTag();
        }

        final BookSearchResult.Book book = (BookSearchResult.Book) getItem(position);

        NetworkImageView networkImageView = (NetworkImageView) holder.cover;
        ImageLoader imageLoader = VolleySingleton.
                getInstance(layoutInflater.getContext()).getImageLoader();
        networkImageView.setImageUrl(book.getCover_l_url(), imageLoader);

        holder.author.setText(book.getAuthor_t());
        return convertView;
    }
}
