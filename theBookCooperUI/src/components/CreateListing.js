import '../styles/CreateListing.css';
import React, { useState } from 'react';
import useUser from "../hooks/useUser";
import axios from 'axios';
import { useDetails } from '../hooks/useDetails';
import { useNavigate } from 'react-router-dom';

function NewListing() {
    const { user, isLoading } = useUser();
    const [error, setError] = useState("");
    const [input, setInput] = useState("");

    const [bookId, setBookId] = useState(null);
    const [bookCondition, setBookCondition] = useState("");
    const [price, setPrice] = useState("");

    const [books, setBooks] = useState([]);
    const [found, setFound] = useState(false);
    const [searchPerformed, setSearchPerformed] = useState(false);
    const [createPerformed, setCreatePerformed] = useState(false);

    const { userId } = useDetails();
    const host = window.location.hostname;
    const navigate = useNavigate();

    const handleInputChange = (value) => {
        if (value.length <= 100) {
            setInput(value);
            setError("");
        } else {
            setError('Input exceeds the maximum length of 100 characters.');
        }
    };

    const handlePriceInput = (value) => {
        if (value.length <= 10) {
            const validatedPrice = parseFloat(value);
            if (!isNaN(validatedPrice) || value.trim() === "") {
                setPrice(value);
                setError("");
            }
        } else {
            setError('Price input cant exceed 9999999999 B-Bucks.');
        }
    };

    const validateAllInputs = () => {
        let isValid = true;
        if (!bookId) {
            setError('Please select a book from the search results.');
            isValid = false;
        }
        if (!bookCondition) {
            setError('Please select the book condition.');
            isValid = false;
        }
        if (price.trim() === "") {
            setError('Price cannot be empty.');
            isValid = false;
        }
        const validatedPrice = parseFloat(price);
        if (isNaN(validatedPrice) || validatedPrice < 0 || validatedPrice > 9999999999) {
            setError('Listing price must be between 0 and 9999999999 B-Bucks.');
            isValid = false;
        }
        return isValid;
    };

    const newListing = async () => {
        if (!user) {
            setError('Please log in to continue.');
            return;
        }

        setError("");  // Clear previous errors
        if (!validateAllInputs()) {
            return;
        }

        setCreatePerformed(true);
        try {
            const token = await user.getIdToken();
            console.log(token);
            const headers = { Authorization: `Bearer ${token}` };
            const listingData = {
                userId,
                bookId,
                listingStatus: "active",
                bookCondition,
                price: parseFloat(price)
            };

            const response = await axios.post(`http://` + host + `:8080/listings/create`, listingData, { headers });
            console.log('Listing created successfully:', response.data);
            setTimeout(() => {
                navigate(`/listings/${response.data.listingId}`);
            }, 500);
        } catch (error) {
            console.error('Failed to create listing:', error);
            setError('Failed to create listing. Please try again.');
        } finally {
            setCreatePerformed(false);
        }
    };

    const getBooks = async (query) => {
        if (!user) {
            setError('Please log in to search for books.');
            setBooks([]);
            setFound(false);
            setSearchPerformed(true);
            return;
        }

        if (!query) {
            setError('Please enter a book title to search.');
            return;
        }

        setError(""); // Clear previous errors
        setSearchPerformed(false);

        try {
            const token = await user.getIdToken();
            const headers = { Authorization: `Bearer ${token}` };
            const response = await axios.get(`http://${host}:8080/books/search?title=${encodeURIComponent(query)}`, { headers });
            if (response.data.length > 0) {
                setBooks(response.data);
                setFound(true);
            } else {
                setBooks([]);
                setFound(false);
                setError('No books were found with that title.');
            }
        } catch (error) {
            console.error('Failed to fetch books:', error);
            setError('Failed to search for books. Please try again.');
            setBooks([]);
            setFound(false);
            setSearchPerformed(true);
        }
    };

    return (
        <div className="create-listing-container">
            <div className="create-listing-white-box">
                <b><a className="create-listing-label">Create A Listing</a></b>
                <div className="look-up-container">
                    <input
                        type="text"
                        className="input-field"
                        value={input}
                        onChange={e => handleInputChange(e.target.value)}
                        placeholder="What book would you like to list?"
                        maxLength={100}
                    /><br/>
                    <button className="look-up-button" onClick={() => getBooks(input)} disabled={isLoading || !user || !input.trim()}>Look up</button>
                </div>
                {searchPerformed && (
                    found ? books.map((book) => (
                        <div key={book.bookId}>
                            <h3>{book.title}</h3>
                            <p>Author: {book.author}</p>
                            <p>Genre: {book.genre}</p>
                            <p>Publish Date: {book.publishDate}</p>
                            <p>Recommended Listing Price: {book.price} B-Bucks</p>
                            <button className="my-book-button" onClick={() => {setBookId(book.bookId); setPrice(book.price.toString());}} disabled={isLoading || !user}>This is the book!</button>
                            <hr/>
                        </div>
                    )) : <div><br/><b><p style={{
                        textAlign: "center"
                    }}>No books were found!</p></b></div>
                )}
                <br/><hr/>
                <a href="/create-book">Can't find your book? Click here to create one!</a>
                <br/>
                <a className="label-text">Condition</a>
                <select
                    value={bookCondition}
                    className="select-field"
                    onChange={e => setBookCondition(e.target.value)}
                    placeholder="Select the book's condition"
                >
                    <option value="" disabled selected>Select the Condition</option>
                    <option value="new">New</option>
                    <option value="used">Used</option>
                </select>
                <a className="label-text">Listing Price (B-Bucks):</a>
                <input
                    type="number"
                    value={price}
                    onChange={e => handlePriceInput(e.target.value)}
                    placeholder="Enter the book's price (Enter 0 for autocompletion)"
                    className="input-field"
                    maxLength={10}
                /><br/>
                {createPerformed && (
                    <div>
                        <a>You have listed the book! Redirecting you to your listing soon...</a>
                    </div>
                )}
                {error && <p className="error-message">{error}</p>}
                <button onClick={newListing} className="create-listing-button" disabled={isLoading || !user || createPerformed}>Create Listing</button><br/><br/>
            </div>
        </div>
    );
}

export default NewListing;
