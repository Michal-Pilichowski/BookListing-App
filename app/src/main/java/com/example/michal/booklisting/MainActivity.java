package com.example.michal.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/*
Main activity containing list view and search input on main action bar
Activity displays books relevant to submitted topic or info if no book was found
 activity also displays round progress bar to indicate progress on slow internet connections
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    private String requestUrlPart1 = "https://www.googleapis.com/books/v1/volumes?q=";
    private String requestUrlPart2 = "&maxResults=10";
    private String submittedQuery;
    private ArrayList<Book> books;
    private BookArrayAdapter bookArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting default welcoming message and empty list view

        books = new ArrayList<>();

        bookArrayAdapter = new BookArrayAdapter(this, books);

        ListView listView = findViewById(R.id.book_info_list_view);
        listView.setAdapter(bookArrayAdapter);

        TextView textView = findViewById(R.id.empty_list_text_view);
        textView.setText("Enter query to search for books.");
        ProgressBar progressBar = findViewById(R.id.loading_books_progress);
        progressBar.setVisibility(View.GONE);

    }

    /*
    Handling menu action bar with search text input and implementing its listener
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) search.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            /*
            when user submits query to search list of found books is displayed
             */
            @Override
            public boolean onQueryTextSubmit(String s) {
                submittedQuery = s;
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                /*
                Checking for internet connection
                 */
                if (networkInfo != null && networkInfo.isConnected()){
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                } else {
                    ProgressBar progressBar = findViewById(R.id.loading_books_progress);
                    progressBar.setVisibility(View.GONE);
                    TextView textView = findViewById(R.id.empty_list_text_view);
                    textView.setText("No internet connection.");
                }
                return true;
            }

            /*
            Changing displayed books when user changes search query
             */
            @Override
            public boolean onQueryTextChange(String s) {

                submittedQuery = s;
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()){
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                } else {
                    ProgressBar progressBar = findViewById(R.id.loading_books_progress);
                    progressBar.setVisibility(View.GONE);
                    TextView textView = findViewById(R.id.empty_list_text_view);
                    textView.setText("No internet connection.");
                }

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /*
    Methods handling loader which downloads data from internet
     */

    //Creating new loader
    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(MainActivity.this, requestUrlPart1 + submittedQuery + requestUrlPart2);
    }

    /*
    Displaying downloaded book info or message if no data was found
     */
    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        ProgressBar loadingEarthquakes = findViewById(R.id.loading_books_progress);
        loadingEarthquakes.setVisibility(View.GONE);

        if (books.isEmpty()){
            TextView emptyListMessage = findViewById(R.id.empty_list_text_view);
            emptyListMessage.setText("Sorry, but there's nothing to display");
        } else {
            TextView emptyListMessage = findViewById(R.id.empty_list_text_view);
            emptyListMessage.setText("");
        }

        if (bookArrayAdapter!=null){
            bookArrayAdapter.clear();
        }

        if (books!=null && !books.isEmpty()){
            bookArrayAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        bookArrayAdapter.clear();
    }



}
