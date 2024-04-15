import '../styles/LandingPage.css';
import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import axios from 'axios';
import { useDetails } from '../hooks/useDetails';

function NewListing() {
    const { user, isLoading } = useUser();
    const [loading, setLoading] = useState(false);

    const [bookId, setBookId] = useState("");
    const [bookCondition, setBookCondition] = useState("");
    const [price, setPrice] = useState("");

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
        } catch (error) {
            console.error('Failed to create book:', error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
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
            /><br/>

            <button onClick={newListing} disabled={isLoading || !user || !bookId || !bookCondition || !price}>Create Book</button>
        </>
    );
}

export default NewListing;
