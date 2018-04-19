package com.example.michal.booklisting;

/**
 * Created by michal on 4/15/18.
 */

/*
Custom class for storing information about searched books
 */
public class Book {
    private String bookCoverUrl;
    private String bookAuthor;
    private String bookTitle;
    private double bookRating;
    private String bookPageCount;

    /*
    Constructor
     */
    public Book(String cover, String author, String title, double rating, String pageCount){
        bookTitle = title;
        bookAuthor = author;
        bookCoverUrl = cover;
        bookRating = rating;
        bookPageCount = pageCount;
    }

    /*
    Getters
     */

    public String getBookCoverUrl(){
        return bookCoverUrl;
    }

    public String getBookAuthor(){
        return bookAuthor;
    }

    public String getBookTitle(){
        return bookTitle;
    }

    public double getBookRating(){
        return bookRating;
    }

    public String getBookPageCount(){
        return bookPageCount;
    }

}
