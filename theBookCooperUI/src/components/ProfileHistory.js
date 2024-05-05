import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import '../styles/LandingPage.css';

import axios from 'axios';
import { useDetails } from "../hooks/useDetails";

const ProfileHistory = () => {
    const { user, isLoading } = useUser();
    const { userId } = useDetails();

    const [buyTransactions, setBuyTransactions] = useState([]);
    const [sellTransactions, setSellTransactions] = useState([]);
    const [pointTransactions, setPointTransactions] = useState([]);

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
                
                //can remove this, but can also use it to display B-Buck balance, etc.
                const userResponse = await axios.get(`/users/${userId}`, { headers });
                setLoggedUser(userResponse.data);

                const buyResponse = await axios.get(`/book-transactions/buyer`, { 
                    params: { buyerId: userId },
                    headers 
                });
                setBuyTransactions(buyResponse.data);
                
                const sellResponse = await axios.get(`/book-transactions/seller`, { 
                    params: { sellerId: userId },
                    headers 
                });
                setSellTransactions(sellResponse.data);
                
                const pointResponse = await axios.get(`/point-transactions/user`, { 
                    params: { userId: userId },
                    headers 
                });
                setPointTransactions(pointResponse.data);

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
            <h1> Profile History </h1>

            <a> Point Transactions </a> <br/>
            <pre>{JSON.stringify(pointTransactions, null, 2)}</pre>

            <a> Buyer Transactions </a> <br/>
            <pre>{JSON.stringify(buyTransactions, null, 2)}</pre>

            <a> Seller Transactions </a> <br/>
            <pre>{JSON.stringify(sellTransactions, null, 2)}</pre>
            
        </div>
    );
}
export default ProfileHistory;

