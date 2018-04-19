package com.example.michal.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by michal on 4/15/18.
 */

/*
Custom adapter for populating list view containing searched books
 */
public class BookArrayAdapter extends ArrayAdapter<Book> {

    /*
    Constructor
     */
    public BookArrayAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    /*
    Method getting current view and populating its elements with proper data
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_element, parent, false);
        }

        TextView author = convertView.findViewById(R.id.author_text_view);
        TextView title = convertView.findViewById(R.id.title_text_view);

        author.setText(book.getBookAuthor());
        title.setText(book.getBookTitle());

        ImageView cover = convertView.findViewById(R.id.book_cover);
        Picasso.with(getContext()).load(book.getBookCoverUrl()).into(cover);

        TextView average = convertView.findViewById(R.id.book_rating);
        average.setText("" + book.getBookRating());

        TextView page = convertView.findViewById(R.id.page_count_text_view);
        page.setText(book.getBookPageCount());

        return convertView;
    }

    }
