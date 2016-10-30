package com.edwin.book.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class BookSearchResult {

    public class Book {
        private String isbn13;
        private String title;
        private String description;
        private String cover_s_url;
        private String cover_l_url;
        private String author_t;
        @Expose(serialize = false, deserialize = false)
        private boolean isLike;

        public String getTitle() {
            return title.replaceAll("(?s)&lt;.*?&gt;", "");
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCover_s_url() {
            return cover_s_url;
        }

        public void setCover_s_url(String cover_s_url) {
            this.cover_s_url = cover_s_url;
        }

        public String getAuthor_t() {
            return author_t;
        }

        public void setAuthor_t(String author_t) {
            this.author_t = author_t;
        }

        public String getCover_l_url() {
            return cover_l_url;
        }

        public void setCover_l_url(String cover_l_url) {
            this.cover_l_url = cover_l_url;
        }

        public String getIsbn13() {
            return isbn13;
        }

        public void setIsbn13(String isbn13) {
            this.isbn13 = isbn13;
        }

        public boolean isLike() {
            return isLike;
        }

        public void setIsLike(boolean isLike) {
            this.isLike = isLike;
        }
    }

    public class Channel {
        private List<Book> item = new ArrayList<Book>();

        public List<Book> getItem() {
            return item;
        }

        public void setItem(List<Book> item) {
            this.item = item;
        }
    }

    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
