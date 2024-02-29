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
import java.util.*;

@RestController
public class BookInfoController {

    private static final long COMPARE = 9780000000000L;

    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("db", 5432, "thebookcooper", "BCdev", "password");
    
    // GET HTTP request to a specific url
    // returns a json file as a string
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
    
    // currently finds the price and ISBN number, but could maybe extend it to find author, publisher and everything else
    // can possibly split this up into multiple function in the future
    //      such as one for updating its price, updating isbn, etc., all seperately
    private Book findISBNPrice(Book book) {
        
        ObjectMapper objectMapper = new ObjectMapper();
        String req; 
        
        String title = book.getTitle(); 
        String author = book.getAuthor(); 

        // if the isbn of the book is unknown try to find it given title using google books API
        // if the isbn is <= 978 with 10 zeros
        if(book.getISBN() <= COMPARE) {
            
            try {
                req = "https://www.googleapis.com/books/v1/volumes?q=" +
                      "intitle:" + title.replaceAll(" ", "+") +       // search by title of book replacing spaces with +
                      "+inauthor:" + author.replaceAll(" ", "+") +    // search by author of book replacing spcaces with +
                      "&key=AIzaSyCxVGv72jNMy6IVrxHmml5_HLGXi8T0SpU"; // api key (can we hide this on github? does it matter?)

                Map<String, Object> inputMap = objectMapper.readValue(getHTML(req), Map.class);

                List<Map<String, Object>> items = (List<Map<String, Object>>) inputMap.get("items");
                if (items != null && !items.isEmpty()) {
                    Map<String, Object> volumeInfo = (Map<String, Object>) items.get(0).get("volumeInfo");
                    List<Map<String, String>> identifiers = (List<Map<String, String>>) volumeInfo.get("industryIdentifiers");

                    for (Map<String, String> identifier : identifiers) {
                        if ("ISBN_13".equals(identifier.get("type"))) {
                            long isbn = Long.parseLong(identifier.get("identifier"));
                            book.setISBN(isbn);
                            break; 
                        }
                    }
                }
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // get price if no price is given
        if(book.getPrice() == 0) {
        
            // now that we have ISBN, find price using BooksRun API
            req = "https://booksrun.com/api/v3/price/buy/" + book.getISBN() + 
                "?key=0helymen654k0w3dk43z";

            try {
                Map inputMap = objectMapper.readValue(getHTML(req), Map.class);
                Map result = (Map) inputMap.get("result");
                Map offers = (Map) result.get("offers");
                Map booksrun = (Map) offers.get("booksrun");

                // can potentially add logic to make it so that if used doesn't exist
                // then it'll set the price to the new price and vice versa

                // Check if "new" price exists
                if(book.getBookCondition().equals("new")) {
                    
                    Map newPrice = (Map) booksrun.get("new");
                    try {
                        double newPriceValue = (double) newPrice.get("price");
                        book.setPrice(newPriceValue);
                    } catch (NumberFormatException | ClassCastException e) {
                        book.setPrice(0.0);
                        throw new IllegalArgumentException("New price for book not found or invalid format");
                    }
                }
                // check if used price exists
                else if(book.getBookCondition().equals("used")){
                    Map usedPrice = (Map) booksrun.get("used");
                    try {
                        double usedPriceValue = (double) usedPrice.get("price");
                        book.setPrice(usedPriceValue);
                    } catch (NumberFormatException | ClassCastException e) {
                        book.setPrice(0.0);
                        throw new IllegalArgumentException("Used price for book not found or invalid format");
                    }
                }
                // if neither "new" nor "used" price exists, set the price to 0.0
                else {
                    book.setPrice(0.0);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return book;
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
            
            // update the book with isbn and price if not given
            Book updatedBook = findISBNPrice(newBook);

            //calls create function from dao/BookInfoDAO to insert listing
            return bookDAO.create(updatedBook);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create the book", e);
        }
    }

    @GetMapping("/books/count")
    public String countBooks() {
        try (Connection connection = dcm.getConnection();
             Statement statement = connection.createStatement();b
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
