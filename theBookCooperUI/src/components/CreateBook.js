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

    const host = window.location.hostname;

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

            const response = await axios.post(`http://` + host + `:8080/books/create`, bookData, { headers });
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
                <br/> <b> <a className = "create-book-label"> Create a Book </a> </b>
                <br/>
                <a className = "label-text"> Title: </a> <input
                    type="text"
                    value={title}
                    onChange={e => setTitle(e.target.value)}
                    placeholder="Enter the book's title"
                    className="input-field"
                /><br/>
                <a className = "label-text"> Author: </a> <input
                    type="text"
                    value={author}
                    onChange={e => setAuthor(e.target.value)}
                    placeholder="Enter the book's author"
                    className="input-field"
                /><br/>
                <a className = "label-text"> Genre: </a> <input
                    type="text"
                    value={genre}
                    onChange={e => setGenre(e.target.value)}
                    placeholder="Enter the book's genre"
                    className="input-field"
                /><br/>
                <a className = "label-text"> Publish Date: </a> <input
                    type="date"
                    value={publishDate}
                    onChange={e => setPublishDate(e.target.value)}
                    placeholder="Enter the publish date"
                    className="input-field"
                /><br/>
                <a className = "label-text"> Condition: </a> <select
                    value={bookCondition}
                    onChange={e => setBookCondition(e.target.value)}
                    placeholder="Select the book's condition"
                    className="input-field"
                    >
                    <option value="new">New</option>
                    <option value="used">Used</option>
                </select><br/>
                <a className = "label-text"> ISBN: </a> <input
                    type="number"
                    value={isbn}
                    onChange={e => setIsbn(e.target.value)}
                    placeholder="Enter the book's ISBN (leave empty for autocompletion)"
                    className="input-field"
                /><br/>
                <a className = "label-text"> Recommended Price (B-Bucks): </a> <input
                    type="number"
                    value={price}
                    onChange={e => setPrice(e.target.value)}
                    placeholder="Enter the book's price (leave empty for autocompletion)"
                    className="input-field"
                /><br/>

                <button className = "create-book-button" onClick={newBook} disabled={isLoading || !user || !title || !author || !bookCondition || success}>Create Book</button>

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
