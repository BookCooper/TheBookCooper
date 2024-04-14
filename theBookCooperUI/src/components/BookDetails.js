import React, { useState, useEffect } from 'react';
import axios from 'axios';

const BookDetails = ({ bookId, token }) => {

    const [book, setBook] = useState(null);

    useEffect(() => {
        const fetchBookDetails = async () => {
            try {
                const headers = { Authorization: `Bearer ${token}` };
                const response = await axios.get(`/books/${bookId}`, { headers });
                setBook(response.data);
            } catch (e) {
                console.error(e);
            }
        };

        if (bookId && token) {
            fetchBookDetails();
        }

    }, [bookId, token]);

    if (!book) {
        return <div>Loading book details...</div>;
    }

    return (
        <div>
        <h3>{book.title}</h3>
        <p>Author: {book.author}</p>
        </div>
    );
};

export default BookDetails;
