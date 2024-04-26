import '../styles/LandingPage.css';
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
        } catch (error) {
            console.error('Failed to create book:', error);
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
        <>
            <div>
                <div className="search-box">
                    <input
                        type="text"
                        value={input}
                        onChange={e => setInput(e.target.value)}
                        placeholder="What book would you like to list?"
                    />
                    <button onClick={() => getBooks(input)} disabled={isLoading || !user || !input}>Look up</button>
                </div>
                <div>
                {searchPerformed && (
                    found ? 
                    <div>
                    {books.map((book) => (
                        <div key={book.bookId}>
                        <a href={`/books/${book.bookId}`}>
                        <h3>{book.title}</h3>
                        </a>
                        <p>Author: {book.author}</p>
                        <p>{book.price} B-Bucks</p>
                        <button onClick={() => setBookId(book.bookId)} disabled={isLoading || !user || !input}>This is the book!</button>
                        
                        </div>
                    ))}
                    </div>
                    : <p>No books were found!</p>
                )}
                </div>
            </div>

            <br/>
            <a> Book ID: </a> <input
                type="text"
                value={bookId}
                onChange={e => setBookId(e.target.value)}
                placeholder="Enter the book's ID"
            /><br/>
            <a> Condition: </a> <input
                type="text"
                value={bookCondition}
                onChange={e => setBookCondition(e.target.value)}
                placeholder="Enter the book's condition"
            /><br/>
            <a> Price: </a> <input
                type="text"
                value={price}
                onChange={e => setPrice(e.target.value)}
                placeholder="Enter the book's price"
            /><br/> <br/>


            {success ? <a> You have listed the book! </a>
                     : <a> </a>
            }
            <br/>


            <button onClick={newListing} disabled={isLoading || !user || !bookId || !bookCondition || !price}>Create Book</button><br/><br/>


            <a href="/create-book"> Can't find your book? Click here to create one! </a> 
        </>
    );
}

export default NewListing;
