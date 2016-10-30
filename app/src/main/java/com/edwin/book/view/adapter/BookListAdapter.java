package com.edwin.book.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.edwin.book.model.BookSearchResult;
import com.edwin.book.R;
import com.edwin.book.network.VolleySingleton;

import java.util.List;

public class BookListAdapter extends ArrayAdapter<BookSearchResult.Book> {
    public static class ViewHolder {
        ImageView cover;
        TextView title;
        TextView author;
        public ImageView like;
        BookSearchResult book;
    }

    public BookListAdapter(Context context, int resource, List<BookSearchResult.Book> list, LayoutInflater mLayoutInflater) {
        super(context, resource, list);
        this.layoutInflater = mLayoutInflater;
    }

    private final LayoutInflater layoutInflater;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_list_book, parent, false);

            holder = new ViewHolder();
            holder.cover = (ImageView) convertView.findViewById(R.id.image_cover);
            holder.title = (TextView) convertView.findViewById(R.id.label_title);
            holder.author = (TextView) convertView.findViewById(R.id.label_author);
            holder.like = (ImageView) convertView.findViewById(R.id.icon_like);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BookSearchResult.Book book = getItem(position);

        NetworkImageView networkImageView = (NetworkImageView) holder.cover;
        ImageLoader imageLoader = VolleySingleton.getInstance(getContext()).getImageLoader();
        networkImageView.setImageUrl(book.getCover_s_url(), imageLoader);

        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor_t());
        if (book.isLike()) {
            holder.like.setImageResource(R.drawable.icon_like_selected);
        } else {
            holder.like.setImageResource(R.drawable.icon_like);
        }
        return convertView;
    }
}
