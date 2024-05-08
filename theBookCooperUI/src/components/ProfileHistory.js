import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import '../styles/ProfileHistory.css';

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
    const host = window.location.host;

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

                const buyResponse = await axios.get(`http://` + host + `:8080/book-transactions/buyer`, { 
                    params: { buyerId: userId },
                    headers 
                });
                setBuyTransactions(buyResponse.data);
                
                const sellResponse = await axios.get(`http://` + host + `:8080/book-transactions/seller`, { 
                    params: { sellerId: userId },
                    headers 
                });
                setSellTransactions(sellResponse.data);
                
                const pointResponse = await axios.get(`http://` + host + `:8080/point-transactions/user`, { 
                    params: { userId: userId },
                    headers 
                });
                const filteredTransactions = pointResponse.data.filter(transaction => transaction.transactionType === "Point");
                setPointTransactions(filteredTransactions);

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
        <div className = "create-history-white-box">
            
            <h1> Profile History </h1>
            
            <div className="create-history-gray-box">
                <a className="label-text">Point Transactions</a><br/>
                {pointTransactions.length > 0 ? (
                    pointTransactions.map((transaction, index) => (
                        <div key={index}>
                            <p>Transaction Amount: {transaction.amount} B-Bucks </p>
                            <p>New Balance: {transaction.currentBalance} B-Bucks </p>
                            <p>Date Purchased: {new Date(transaction.transactionDate).toLocaleDateString()}</p>
                            <hr/>
                        </div>
                    ))
                ) : (
                    <p>No transactions available.</p>
                )}
            </div>
            <div className="create-history-gray-box">
                <a className="label-text"> Buyer Transactions</a><br/>
                {buyTransactions.length > 0 ? (
                    buyTransactions.map((transaction, index) => (
                        <div key={index}>
                            <p>Transaction ID #{transaction.transactionId}</p>
                            <p>Bought for: {transaction.transactionPrice} B-Bucks</p>
                            <p>Date Purchased: {new Date(transaction.transactionDate).toLocaleDateString()}</p>
                            <a href={`/listings/${transaction.listingId}`}> Click here to view the transaction! </a>
                            <hr/>
                        </div>
                    ))
                ) : (
                    <p>No listings available.</p>
                )}
            </div>
           <div className="create-history-gray-box">
                <a className="label-text">Seller Transactions</a><br/>
                {sellTransactions.length > 0 ? (
                    sellTransactions.map((transaction, index) => (
                        <div key={index}>
                            <p>Transaction ID #{transaction.transactionId}</p>
                            <p>Sold for: {transaction.transactionPrice}</p>
                            <p>Date Sold: {new Date(transaction.transactionDate).toLocaleDateString()}</p>
                            <a href={`/listings/${transaction.listingId}`}> Click here to view the transaction! </a>
                            <hr/>
                        </div>
                    ))
                ) : (
                    <p>No seller transactions available.</p>
                )}
            </div>
        </div>
    );
}
export default ProfileHistory;

