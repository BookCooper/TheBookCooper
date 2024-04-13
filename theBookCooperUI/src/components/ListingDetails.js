import '../styles/LandingPage.css';
import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import axios from 'axios';
import { useParams } from 'react-router-dom';


function ShowListing() {
    const [data, setData] = useState(null);
    const {user, isLoading} = useUser();
    const { listingId } = useParams();

    useEffect(() => {
        const loadListingDetails = async () => {
            if (!user) {
                setData('No user logged in.');
                return;
            }
            try {
                const token = await user.getIdToken();
                const headers = token ? { Authorization: `Bearer ${token}` } : {};
                const response = await axios.get(`/listings/${listingId}`, { headers });
                setData(response.data);
            } catch (error) {
                setData("UNAUTHORIZED " + error.message);
            }
        };

        if (user) {
            loadListingDetails();
        }
    }, [listingId, isLoading, user]);
    if (data)
        return (
            <>
                <pre>{JSON.stringify(data, null, 2)}</pre>
            </>
        );
    return <h1>Boo! (k)</h1>;
}


const ListingDetails = () => {
    return (
        <div>
            {ShowListing()}
            <h2> Listing Details </h2>
        </div>
    );
};

export default ListingDetails;
