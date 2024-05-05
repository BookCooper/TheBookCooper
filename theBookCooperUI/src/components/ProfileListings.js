import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import '../styles/LandingPage.css';

import axios from 'axios';
import { useDetails } from "../hooks/useDetails";

const ProfileListings = () => {
    const { user, isLoading } = useUser();
    const { userId } = useDetails();

    const [listings, setListings] = useState([]);
    const [loggedUser, setLoggedUser] = useState(null);

    const [loadingDetails, setLoadingDetails] = useState(false);
    const [error, setError] = useState('');

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

                const userResponse = await axios.get(`/users/${userId}`, { headers });
                setLoggedUser(userResponse.data);

                const listingResponse = await axios.get(`/listings/user`, { 
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
        <div>
            <h1> Profile Listings </h1>
            <pre>{JSON.stringify(listings, null, 2)}</pre>
        </div>
    );
}
export default ProfileListings;

