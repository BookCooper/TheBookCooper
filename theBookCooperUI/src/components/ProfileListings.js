import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import '../styles/ProfileListings.css';

import axios from 'axios';
import { useDetails } from "../hooks/useDetails";

const ProfileListings = () => {
    const { user, isLoading } = useUser();
    const { userId } = useDetails();

    const [listings, setListings] = useState([]);
    const [loggedUser, setLoggedUser] = useState(null);

    const [loadingDetails, setLoadingDetails] = useState(false);
    const [error, setError] = useState('');
    const host = window.location.hostname;

    useEffect(() => {
        const loadListingDetails = async () => {
            if (!user) {
                setLoggedUser(null);
                return;
            }
            setLoadingDetails(true);
            try {
                const token = await user.getIdToken();
                const headers = token ? { Authorization: `Bearer ${token}` } : {};
                
                //can remove this, but can also use it to display B-Buck balance, etc.
                const userResponse = await axios.get(`http://` + host + `:8080/users/${userId}`, { headers });
                setLoggedUser(userResponse.data);

                const listingResponse = await axios.get(`http://` + host + `:8080/listings/user`, { 
                    params: { userId: userId },
                    headers 
                });
                setListings(listingResponse.data);

            } catch (error) {
                setError("UNAUTHORIZED " + error.message);
            } finally {
                setLoadingDetails(false);
            }
        };

        if (user) {
            loadListingDetails();
        }
    }, [user, userId]);

    if (isLoading || loadingDetails) return <p>Loading...</p>;
    if (error) return <p>Error: {error}</p>;
    if (!user || !loggedUser) return <p>Please log in.</p>;

    return (
        <div className = "create-listings-container">
            <div className = "create-listings-white-box">
                <h1>Profile Listings</h1>
                {listings.length > 0 ? (
                    listings.map((listing, index) => (
                        <div key={listing.listingId}>
                            <p>Listing ID: {listing.listingId}</p>
                            <p>Price: {listing.price} B-Bucks</p>
                            <a style={{ color: listing.listingStatus === 'active' ? 'green' : 'red' }}>
                                <b>Status</b>: {listing.listingStatus}
                            </a>
                            <p>Date Listed: {new Date(listing.listingDate).toLocaleDateString()}</p>
                            <a href={`/listings/${listing.listingId}`}> Click here to view your listings! </a>
                            <hr/>
                        </div>
                    ))
                ) : (
                    <p>No listings available.</p>
                )}            
            </div>
        </div>
    );
}
export default ProfileListings;

