package com.thebookcooper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.Map;

import com.thebookcooper.model.*;
import com.thebookcooper.dao.*;

import java.time.LocalDate;

//stuff for price
import java.io.*;
import java.net.*; 

@RestController
public class BookInfoController {

    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("db", 5432, "thebookcooper", "BCdev", "password");
    
    //GET HTTP request to a specific url
    public static String getHTML(String urlToRead) throws Exception {

        StringBuilder result = new StringBuilder();
        URI uri = new URI(urlToRead);
        URL url = uri.toURL();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        }
        return result.toString();
    }

    private double findPrice(Book book) {
        
        ObjectMapper objectMapper = new ObjectMapper();
        String req; 

        //if the isbn of the book is unknown try to find it given title using google books API
        if(!book.getISBN()) {
            
            //String title = book.getTitle();
            //String author = book.getAuthor(); 

            try {
                req = "https://www.googleapis.com/books/v1/volumes?q=" +
                      "intitle:" + book.getTitle() +       // search by title of book
                      "+inauthor:" + book.getAuthor() +    // search by author of book
                      "&key=AIzaSyCxVGv72jNMy6IVrxHmml5_HLGXi8T0SpU"; //api key
                
                Map inputMap = objectMapper.readValue(getHTML(req), Map.class);
                Map volumeInfo = (Map) inputMap.get("volumeInfo");
                List<Map<String, String>> identifiers = (List<Map<String, String>>) volumeInfo.get("industryIdentifiers");

                // Loop through the identifiers and set the ISBN-13 number
                for (Map<String, String> identifier : identifiers) {
                    String type = identifier.get("type");
                    if ("ISBN_13".equals(type)) {
                        String isbn13 = identifier.get("identifier");

                        // You may want to handle NumberFormatException in case the identifier is not a valid long value
                        try {
                            long isbn = Long.parseLong(isbn13);
                            book.setISBN(isbn);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid ISBN format: " + isbn13);
                        }
                    }
                }
            }
            catch(/*doesn't exist error?*/) {}
        }
        
        //now that we have ISBN, find price using BooksRun API
        req = "https://booksrun.com/api/v3/price/buy/" + book.getISBN() + 
              "?key=0helymen654k0w3dk43z";


        

        

    }

    @PostMapping("/books/create")
    public Book createBook(@RequestBody String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map inputMap = objectMapper.readValue(json, Map.class);

        Book newBook = new Book();

        try {
            Connection connection = dcm.getConnection();
            BookInfoDAO bookDAO = new BookInfoDAO(connection);

            //get inputs from user and assign them to a new object
            newBook.setTitle((String) inputMap.get("title"));
            //newBook.setPublishDate(Date.valueOf(LocalDate.now())); <-- set the date to be the current day
            newBook.setPublishDate(Date.valueOf((String) inputMap.get("publishDate"))); //date has to be of form "YYYY-MM-DD"
            newBook.setAuthor((String) inputMap.get("author"));
            newBook.setGenre((String) inputMap.get("genre"));
            newBook.setBookCondition((String) inputMap.get("bookCondition"));
            newBook.setPrice((double) inputMap.get("price"));

            Object isbnObject = inputMap.get("isbn");
            if (isbnObject instanceof Integer) {
                newBook.setISBN(((Integer) isbnObject).longValue()); // Convert Integer to Long
            } else if (isbnObject instanceof Long) {
                newBook.setISBN((Long) isbnObject);
            } else {
                throw new IllegalArgumentException("ISBN must be a number");
            }

            //calls create function from dao/BookInfoDAO to insert listing
            return bookDAO.create(newBook);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create the book", e);
        }
    }

    @GetMapping("/books/count")
    public String countBooks() {
        try (Connection connection = dcm.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM book_info")) {
            if (resultSet.next()) {
                return "Number of books: " + resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving book count";
        }
        return "No books found";
    }

    @GetMapping("/books/{id}")
    public Book getBookById(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            BookInfoDAO infoDAO = new BookInfoDAO(connection);
            return infoDAO.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider creating and returning a custom error object or message
        }
        return null; // Or return an appropriate response/entity indicating not found or error
    }

    @PutMapping("/books/update/{id}")
    public Book updateBook(@PathVariable("id") long id, @RequestBody String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map inputMap = objectMapper.readValue(json, Map.class);

        Book updatedBook = new Book();
        try {
            Connection connection = dcm.getConnection();
            BookInfoDAO infoDAO = new BookInfoDAO(connection);

            //get inputs from user and assign them to a new object
            updatedBook.setBookId(id);
            updatedBook.setTitle((String) inputMap.get("title"));
            updatedBook.setISBN((int) inputMap.get("isbn"));
            updatedBook.setPublishDate(Date.valueOf((String) inputMap.get("publishDate"))); //date has to be of form "YYYY-MM-DD"
            updatedBook.setAuthor((String) inputMap.get("author"));
            updatedBook.setGenre((String) inputMap.get("genre"));
            updatedBook.setBookCondition((String) inputMap.get("bookCondition"));
            updatedBook.setPrice((double) inputMap.get("price"));

            //calls update function from dao/BookInfoDAO to update listing
            return infoDAO.update(updatedBook);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update the book", e);
        }
    }

    @DeleteMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            BookInfoDAO infoDAO = new BookInfoDAO(connection);
            infoDAO.delete(id);
            return "Book deleted";
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete the book", e);
        }
    }
}
