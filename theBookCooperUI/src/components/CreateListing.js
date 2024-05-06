//import '../styles/LandingPage.css';
import '../styles/CreateListing.css';
import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import axios from 'axios';
import { useDetails } from '../hooks/useDetails';

function NewListing() {
    const { user, isLoading } = useUser();
    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState(false);
    const [input, setInput] = useState("");

    const [bookId, setBookId] = useState("");
    const [bookCondition, setBookCondition] = useState("");
    const [price, setPrice] = useState("");

    const [books, setBooks] = useState([]);
    const [found, setFound] = useState(false);

    const [searchPerformed, setSearchPerformed] = useState(false); // State to track if search has been performed
    const [createPerformed, setCreatePerformed] = useState(false); // State to track if listing create has been performed

    //listing id, user id, book id, listing status, book condition, price, listing date
    //user input: book id, book condition, price

    const { userId, setUserId } = useDetails();

    const newListing = async () => {
        if (!user) {
            console.error('No user logged in or token not retrieved.');
            return;
        }

        setLoading(true);
        try {
            const token = await user.getIdToken();
            console.log(token);
            const headers = { Authorization: `Bearer ${token}` };

            const listingData = {
                userId,
                bookId,
                listingStatus: "active",
                bookCondition,
                price: price ? parseFloat(price) : 0
            };

            const response = await axios.post(`/listings/create`, listingData, { headers });
            console.log('Listing created successfully:', response.data);
            setSuccess(true);
            setCreatePerformed(true);
        } catch (error) {
            console.error('Failed to create book:', error);
            setCreatePerformed(true);
        } finally {
            setLoading(false);
        }
    };

    const getBooks = async (input) => {
        if (!user) {
            setBooks([]);
            setFound(false);
            setSearchPerformed(true);
            return;
        }

        try {
            const token = await user.getIdToken();
            const headers = { Authorization: `Bearer ${token}` };
            const response = await axios.get(`/books/search?title=${encodeURIComponent(input)}`, { headers });

            if (response.data.length > 0) {
                setBooks(response.data);
                setFound(true);
            } else {
                setBooks([]);
                setFound(false);
            }
            setSearchPerformed(true);
        } catch (error) {
            console.error('Failed to fetch books:', error);
            setBooks([]);
            setFound(false);
            setSearchPerformed(true);
        }
    };

    return (
        <div className = "create-listing-container">
            <div className = "create-listing-white-box">
                <br/> <b> <a className="create-listing-label">Create A Listing</a> </b> 
                <br/> <br/>
                <div>
                    <div className="look-up-container">
                        <input
                            type="text"
                            className="input-field"
                            value={input}
                            onChange={e => setInput(e.target.value)}
                            placeholder="What book would you like to list?"
                        /> <br/>
                        <button className="look-up-button" onClick={() => getBooks(input)} disabled={isLoading || !user || !input}>Look up</button>
                    </div>
                    <div>
                    {searchPerformed && (
                        found ? 
                        <div className="book-result-container">
                        {books.map((book) => (
                            <div key={book.bookId}>
                                {/*<a href={`/books/${book.bookId}`}> <h3>{book.title}</h3> </a>*/}
                                <h3>{book.title}</h3> 
                                <p>Author: {book.author}</p>
                                <p>Genre: {book.genre}</p>
                                <p>Publish Date: {book.publishDate}</p>
                                <p>Recommended Listing Price: {book.price} B-Bucks</p>
                                <button className="my-book-button" onClick={() => {setBookId(book.bookId); setPrice(book.price);}} disabled={isLoading || !user || !input}>This is the book!</button>
                                <hr/>
                            </div>
                        ))}
                        </div>
                        : <div> <br/> <b> <p style={{textAlign: "center"}}>No books were found!</p> </b> </div>
                    )}
                    </div>
                </div>

                <br/> <hr/>
                <a href="/create-book"> Can't find your book? Click here to create one! </a> 

                <br/>
                {/*
                <a> Book ID: </a> <input
                    type="text"
                    value={bookId}
                    className="input-field"
                    onChange={e => setBookId(e.target.value)}
                    placeholder="Enter the book's ID"
                /><br/>  */}
                <a className="label-text"> Condition </a> <select
                    value={bookCondition}
                    className="select-field"
                    onChange={e => setBookCondition(e.target.value)}
                    placeholder="Select the book's condition"
                >
                    <option value="" disabled selected> Select the Condition </option>
                    <option value="new">New</option>
                    <option value="used">Used</option>
                </select> 
                <a className="label-text"> Price (B-Bucks) </a> <input
                    type="number"
                    value={price}
                    className="input-field"
                    onChange={e => setPrice(e.target.value)}
                    placeholder="Enter the book's price"
                /><br/> <br/>

                {createPerformed && ( found
                    ? <a> You have listed the book! </a>
                    : <a> There was an error trying to list your book. Please try again </a>
                )}

                <button onClick={newListing} className = "create-listing-button" disabled={isLoading || !user || !bookId || !bookCondition || !price}>Create Listing</button><br/><br/>
            </div>
        </div>
    );
}

export default NewListing;
