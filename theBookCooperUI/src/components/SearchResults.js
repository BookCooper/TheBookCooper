import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import axios from 'axios';
import { useLocation } from 'react-router-dom';
import SearchListings from './SearchListings';
import '../styles/SearchResults.css';

function ShowResults() {
    const { user, isLoading } = useUser();
    const [loading, setLoading] = useState(false);
    const [listings, setListings] = useState([]);
    const location = useLocation(); // Get location
    const query = new URLSearchParams(location.search).get('title'); // Get the title query parameter
    const host = window.location.host;

    useEffect(() => {
        if (query) {
            getListings(query);
        }
    }, [query, user]); // Add `user` to dependencies to rerun when user changes

    const getListings = async (input) => {
        if (!user) {
            setListings([]);
            return;
        }

        setLoading(true);
        try {
            const token = await user.getIdToken();
            const headers = { Authorization: `Bearer ${token}` };
            const listingResponse = await axios.get(`http://` + host + `:8080/listings/search?title=${encodeURIComponent(input)}`, { headers });

            const activeListings = listingResponse.data.filter((listing) => listing.listingStatus === 'active');
            const booksData = await Promise.all(activeListings.map(async (listing) => {
                const bookResponse = await axios.get(`http://` + host + `:8080/books/${listing.bookId}`, { headers });
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
        
        <div className = "create-results-container">
            <div className = "create-results-white-box">
                <SearchListings initialValue={query} /> <br/> <br/>
                <a className = "label-text"> Search Results for: {query} </a> <br/>
                {loading ? (
                    <p>Loading...</p>
                ) : listings.length > 0 ? (
                    <div>
                    {listings.map((listing) => (
                        <div key={listing.listingId}>
                        <a href={`/listings/${listing.listingId}`}>
                            <h3>{listing.book.title}</h3>
                        </a>
                        <p>Author: {listing.book.author}</p>
                        <p>Price: {listing.price} B-Bucks</p>
                        <p>Date Listed: {new Date(listing.listingDate).toLocaleDateString()}</p>
                        <hr/>
                        </div>
                    ))}
                    </div>
                ) : (
                    <p>No listings found or user is not authenticated.</p>
                )}
            </div>
        </div>
    );
}

const SearchResults = () => {
    return (
        <div>
            {ShowResults()}
        </div>
    );
};

export default SearchResults;
