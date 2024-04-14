import '../styles/LandingPage.css';
import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import axios from 'axios';
import { useParams } from 'react-router-dom';

function ShowUser() {
    const [data, setData] = useState(null);
    const {user, isLoading} = useUser();
    const { userId } = useParams();
    const [token, setToken] = useState(null);

    useEffect(() => {
        const loadUserDetails = async () => {
            if (!user) {
                setData('No user logged in.');
                return;
            }
            try {
                const token = await user.getIdToken();
                const headers = token ? { Authorization: `Bearer ${token}` } : {};
                const response = await axios.get(`/listings/${listingId}`, { headers });
                setData(response.data);
                setToken(token); 
            } catch (error) {
                setData("UNAUTHORIZED " + error.message);
            }
        };

        if (user) {
            loadListingDetails();
        }
    }, [listingId, isLoading, user]);
    if (data && token)
        return (
            <>
                <pre>{JSON.stringify(data, null, 2)}</pre>
                <BookDetails bookId={data.bookId} token={token} />
            </>
        );
    return <h1>Boo! (k)</h1>;
}


const UserDetails = () => {
    return (
        <div>
            <h2> User Details </h2>
            {ShowUser()}
        </div>
    );
};

export default UserDetails;
