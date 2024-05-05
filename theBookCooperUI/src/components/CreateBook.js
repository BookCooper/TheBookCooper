import '../styles/CreateBook.css';
import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import axios from 'axios';

function NewBook() {
    const { user, isLoading } = useUser();
    const [loading, setLoading] = useState(false);

    const [title, setTitle] = useState("");
    const [author, setAuthor] = useState("");
    const [genre, setGenre] = useState("");
    const [publishDate, setPublishDate] = useState("");
    const [bookCondition, setBookCondition] = useState("");
    const [isbn, setIsbn] = useState("");
    const [price, setPrice] = useState("");
    const [success, setSuccess] = useState(false);

    const newBook = async () => {
        if (!user) {
            console.error('No user logged in or token not retrieved.');
            return;
        }

        setLoading(true);
        try {
            const token = await user.getIdToken();
            console.log(token);
            const headers = { Authorization: `Bearer ${token}` };

            const bookData = {
                title,
                publishDate,
                author,
                genre,
                bookCondition,
                isbn: isbn ? parseInt(isbn, 10) : 0,
                price: price ? parseFloat(price) : 0
            };

            console.log(bookData);
            setSuccess(true)

            const response = await axios.post(`/books/create`, bookData, { headers });
            console.log('Book created successfully:', response.data);
        } catch (error) {
            console.error('Failed to create book:', error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className = "create-book-container">
            <div className = "create-book-white-box">
                <a> Title: </a> <input
                    type="text"
                    value={title}
                    onChange={e => setTitle(e.target.value)}
                    placeholder="Enter the book's title"
                /><br/>
                <a> Author: </a> <input
                    type="text"
                    value={author}
                    onChange={e => setAuthor(e.target.value)}
                    placeholder="Enter the book's author"
                /><br/>
                <a> Genre: </a> <input
                    type="text"
                    value={genre}
                    onChange={e => setGenre(e.target.value)}
                    placeholder="Enter the book's genre"
                /><br/>
                <a> Publish Date: </a> <input
                    type="date"
                    value={publishDate}
                    onChange={e => setPublishDate(e.target.value)}
                    placeholder="Enter the publish date"
                /><br/>
                <a> Condition: </a> <select
                    value={bookCondition}
                    onChange={e => setBookCondition(e.target.value)}
                    placeholder="Select the book's condition"
                    >
                    <option value="new">New</option>
                    <option value="used">Used</option>
                </select><br/>
                <a> ISBN: </a> <input
                    type="text"
                    value={isbn}
                    onChange={e => setIsbn(e.target.value)}
                    placeholder="Enter the book's ISBN (optional)"
                /><br/>
                <a> Price: </a> <input
                    type="text"
                    value={price}
                    onChange={e => setPrice(e.target.value)}
                    placeholder="Enter the book's price (leave empty for autocompletion)"
                /><br/>

                <button onClick={newBook} disabled={isLoading || !user || !title || !author || !bookCondition || success}>Create Book</button>

                <br/> <br/>

                {success ? <a> You have created a book! </a>
                         : <a> </a>
                }
                <br/>
            </div>
        </div>
    );
}

export default NewBook;
