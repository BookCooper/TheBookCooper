import '../styles/LandingPage.css';
import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import axios from 'axios';

function ShowResults() {
    const { user, isLoading } = useUser();
    const [input, setInput] = useState("");
    const [loading, setLoading] = useState(false);
    const [listings, setListings] = useState([]);

    const getListings = async (input) => {
        if (!user) {
            setListings([]);
            return;
        }

        setLoading(true);
        try {
            const token = await user.getIdToken();
            const headers = { Authorization: `Bearer ${token}` };
            const listingResponse = await axios.get(`/listings/search?title=${encodeURIComponent(input)}`, { headers });

            // get book data
            const booksData = await Promise.all(listingResponse.data.map(async (listing) => {
                const bookResponse = await axios.get(`/books/${listing.bookId}`, { headers });
                return { ...listing, book: bookResponse.data };
            }));

            setListings(booksData);
        } catch (error) {
            console.error('Failed to fetch listings or books:', error);
            setListings([]);
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <input
                type="text"
                value={input}
                onChange={e => setInput(e.target.value)}
                placeholder="Enter your query"
            />
            <button onClick={() => getListings(input)} disabled={isLoading || !user || !input}>Look up</button>
            
            {loading ? (
                <p>Loading...</p>
            ) : listings.length > 0 ? (
                <div>
                    {listings.map((listing) => (
                        <div key={listing.listingId}>
                        <a key={listing.listingId} href={`/listings/${listing.listingId}`}>
                            <h3>{listing.book.title}</h3> </a>
                            <p>Author: {listing.book.author}</p>
                            <p>{listing.price} B-Bucks</p>
                        </div>
                    ))}
                </div>
            ) : (
                <p>No listings found or user is not authenticated.</p>
            )}
        </>
    );
}


const Search = () => {
    return (
        <div>
            <h2> Search Page </h2>
            {ShowResults()}
        </div>
    );
};

export default Search;
