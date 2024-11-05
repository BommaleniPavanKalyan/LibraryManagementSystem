package com.javaproject.lms;

public class Book {
 private int bookId;
 private String title;
 private String author;
 private boolean isAvailable;

 public Book(int bookId, String title, String author, boolean isAvailable) {
     this.bookId = bookId;
     this.title = title;
     this.author = author;
     this.isAvailable = isAvailable;
 }

 public int getBookId() { return bookId; }
 public String getTitle() { return title; }
 public String getAuthor() { return author; }
 public boolean isAvailable() { return isAvailable; }

 public void setAvailable(boolean available) { isAvailable = available; }
}

