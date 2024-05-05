/* File used to search for books but that feature has since been implemented
 * in the create listing component. This component still exists in case it
 * needs to be used for future use
*/

/*import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; 

import '../styles/LandingPage.css';
import useUser from "../hooks/useUser";
import axios from 'axios';


const SearchBooks = ({ placeholder = "Search for a book" }) => {
    const [input, setInput] = useState("");
    const [books, setBooks] = useState([]);
    const [found, setFound] = useState(false);
    const [searchPerformed, setSearchPerformed] = useState(false); // State to track if search has been performed
    const { user, isLoading } = useUser();
    const navigate = useNavigate();

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
        <div>
            <div className="search-box">
                <input
                    type="text"
                    value={input}
                    onChange={e => setInput(e.target.value)}
                    placeholder={placeholder}
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
                            </div>
                        ))}
                        </div>
                    : <p>No books were found!</p>
                )}
            </div>
        </div>
    );
};

export default SearchBooks;*/
