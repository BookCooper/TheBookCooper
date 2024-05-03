import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import axios from 'axios';
import { useLocation } from 'react-router-dom';

function ShowResults() {
    const { user, isLoading } = useUser();
    const [loading, setLoading] = useState(false);
    const [listings, setListings] = useState([]);
    const location = useLocation(); // Get location
    const query = new URLSearchParams(location.search).get('title'); // Get the title query parameter

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
            const listingResponse = await axios.get(`/listings/search?title=${encodeURIComponent(input)}`, { headers });

            const activeListings = listingResponse.data.filter((listing) => listing.listingStatus === 'active');
            const booksData = await Promise.all(activeListings.map(async (listing) => {
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
            {loading ? (
                <p>Loading...</p>
            ) : listings.length > 0 ? (
                <div>
                    {listings.map((listing) => (
                        <div key={listing.listingId} style={{ border: '1px solid #ccc', padding: '10px', marginBottom: '10px' }}>
                            <a href={`/listings/${listing.listingId}`}>
                                <h3>{listing.book.title}</h3>
                            </a>
                            <p><strong>Author:</strong> {listing.book.author}</p>
                            <p><strong>Price:</strong> {listing.price} B-Bucks</p>
                        </div>
                    ))}
                </div>
            ) : (
                <p>No listings found or user is not authenticated.</p>
            )}
        </>
    );
}

const SearchResults = () => {
    return (
        <div>
            <h2>Search Page</h2>
            <ShowResults />
        </div>
    );
};

export default SearchResults;
