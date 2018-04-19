package com.example.michal.booklisting;

import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal on 4/15/18.
 */

/*
Custom class for downloading book info from Google books api
 */
public class QueryBook {

    /*
    Private constructor
     */
    private QueryBook(){

    }

    /*
    Method extracting values from downloaded Json
     */
    private static ArrayList<Book> extractFeatureFromJson(String Json){
        ArrayList<Book> books = new ArrayList<>();

        if (TextUtils.isEmpty(Json)){
            return books;
        }

        try {

            JSONObject jsonObject = new JSONObject(Json);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            for (int i=0;i<itemsArray.length();i++){

                JSONObject currentObject = itemsArray.getJSONObject(i);

                if (currentObject.has("volumeInfo")){
                    JSONObject volumeInfo = currentObject.getJSONObject("volumeInfo");

                    if (volumeInfo.has("authors")&&volumeInfo.has("imageLinks")){
                        JSONArray authors = volumeInfo.getJSONArray("authors");
                        JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                        //Setting default values to display when searched field is not found
                        String author = "NN";
                        String title = "NN";
                        String image = "";
                        String page = "NN";
                        double rating = 0;

                        /*
                        Checking if searched value keys exist
                         */
                        try {
                            if (imageLinks.has("thumbnail")){
                                image = imageLinks.getString("thumbnail");
                            }

                            if (!authors.isNull(0)){
                                author = authors.getString(0);
                            }

                            if (volumeInfo.has("title")){
                                title = volumeInfo.getString("title");
                            }

                            if (volumeInfo.has("averageRating")){
                                rating = volumeInfo.getDouble("averageRating");
                            }

                            if (volumeInfo.has("pageCount")){
                                page = volumeInfo.getString("pageCount");
                            }

                            books.add(new Book(image, author, title, rating, page));
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

        return books;
    }

    /*
    Creating Url from given link
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /*
    Makes http connection
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(40000);
            urlConnection.setConnectTimeout(60000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /*
    Converts input stream into String json
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /*
    Public method executing online search for data
     */
    public static List<Book> fetchBookData(String requestURL){
        URL url = createUrl(requestURL);

        String  jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Book> books = extractFeatureFromJson(jsonResponse);

        return books;
    }

}
