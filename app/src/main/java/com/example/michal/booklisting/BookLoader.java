package com.example.michal.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by michal on 4/15/18.
 */

/*
Custom loader for handling internet operations such as downnloading book info on different thread
than main thread
 */
public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private static String BOOK_REQUEST_URL;

    public BookLoader(Context context, String url) {
        super(context);
        BOOK_REQUEST_URL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /*
    Book info are fetched in background thread
     */
    @Override
    public List<Book> loadInBackground() {

        List<Book> results = QueryBook.fetchBookData(BOOK_REQUEST_URL);

        return results;
    }



}
