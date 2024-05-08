import '../styles/ListingDetails.css';
import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import axios from 'axios';
import { useParams } from 'react-router-dom';
import { useDetails } from '../hooks/useDetails';

function ShowListing() {
    const [listing, setListing] = useState(null);
    const [book, setBook] = useState(null);
    const [seller, setSeller] = useState(null);
    const [buyer, setBuyer] = useState(null);
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState('');
    const [isLoading, setLoading] = useState(false);

    const { user } = useUser();
    const { userId } = useDetails();
    const { listingId } = useParams();

    const [transaction, setTransaction] = useState('');
    const host = window.location.host;

    useEffect(() => {
        const loadListingDetails = async () => {
            if (!user) {
                setError('No user logged in.');
                return;
            }
            setLoading(true);
            try {
                const token = await user.getIdToken();
                const headers = token ? { Authorization: `Bearer ${token}` } : {};
                
                // Get listing data
                const listingResponse = await axios.get(`http://` + host + `:8080/listings/${listingId}`, { headers });
                setListing(listingResponse.data);
                
                // Get book data from listing response
                const bookResponse = await axios.get(`http://` + host + `:8080/books/${listingResponse.data.bookId}`, { headers });
                setBook(bookResponse.data);
                
                // Get seller
                const sellerResponse = await axios.get(`http://` + host + `:8080/users/${listingResponse.data.userId}`, { headers });
                setSeller(sellerResponse.data);

                // Get buyer
                const buyerResponse = await axios.get(`http://` + host + `:8080/users/${userId}`, { headers });
                setBuyer(buyerResponse.data);
            } catch (error) {
                setError("Error fetching data: " + error.message);
            } finally {
                setLoading(false);
            }
        };

        if (user) {
            loadListingDetails();
        }
    }, [user, userId, listingId]);

    if (isLoading) return <p>Loading...</p>;
    if (error) return <p>{error}</p>;

    if (!listing || !book || !seller || !buyer) {
        return <p>Missing data. Please check the details and try again.</p>;
    }

    //has transaction id, buyer id, seller id, transaction date, listing id, transaction price, transaction status
    const newTransaction = async () => {

        if (!user) {
            setTransaction('No user logged in.');
            return;
        }

        if(userId === seller.userId) {
            setTransaction("Cannot buy your own book!");
            console.log("Cannot buy your own book!");
            return;
        }

        if(buyer.bbucksBalance < listing.price) {
            setTransaction("Insufficient funds!");
            console.log("Insufficient funds!");
            return;
        }

        try {
            const token = await user.getIdToken();
            const headers = token ? { Authorization: `Bearer ${token}` } : {};

            console.log(token)

            //get listing data
            const response = await axios.post(`http://` + host + `:8080/book-transactions/create`, {
                buyerId: userId,
                sellerId: seller.userId,
                listingId: listing.listingId,
                transactionPrice: listing.price ? parseFloat(listing.price) : 0,
                transactionStatus: "success"
            }, {headers});

            console.log('Transaction created successfully:', response.data);

            //update the book's status/
            const updateResponse = await axios.put(`http://` + host + `:8080/listings/update/${listingId}`, {
                userId: listing.userId,
                bookId: listing.bookId,
                price: listing.price,
                bookCondition: listing.bookCondition,
                listingStatus: "completed"
            }, { headers });

            setSuccess(true)

        } catch (error) {
            setTransaction("UNAUTHORIZED " + error.message);
        }
    };


    return (
        <div className="create-details-container">  
            <div className="create-details-white-box">
                <h1>{book.title}</h1>
                <div className="create-details-gray-box">
                    <h2>Book details</h2>
                    <a> <b>Title</b>: {book.title}</a>
                    <a> <b>Author</b>: {book.author}</a>
                    <a> <b>Genre</b>: {book.genre}</a>
                    <a> <b>ISBN</b>: {book.isbn < 978000000000 ? "No ISBN found" : book.isbn}</a>
                    <a> <b>Date Published</b>: {new Date(book.publishDate).toLocaleDateString()}</a>
                </div> <br/>

                <div className="create-details-gray-box">
                    <h2>Listing details</h2>
                    <a> <b>Book Price</b>: {listing.price} B-Bucks</a>
                    <a> <b>Condition</b>: {listing.bookCondition}</a>
                    <a style={{ color: listing.listingStatus === 'active' ? 'green' : 'red' }}>
                        <b>Status</b>: {listing.listingStatus}
                    </a>
                    <a> <b>Date Listed</b>: {new Date(listing.listingDate).toLocaleDateString()}</a>
                </div> <br/>

               <div className="create-details-gray-box">
                    {listing.listingStatus === "active" ? (
                        <>
                            <h2>Your details</h2>
                            <a><b>You currently have {buyer.bbucksBalance} B-Bucks</b></a>
                            <a><b>{buyer.bbucksBalance >= listing.price ? "You can afford this book!" : "You cannot afford this book! Please buy more B-Bucks"}</b></a>
                        </>
                    ) : (
                        <b>This book has been purchased</b>
                    )}
                </div>
                
                <br/><br/>
                {success ? <a>You successfully Bought the book!</a> : <a>{transaction}</a>}
                <br/>
                <button className = "buy-book-button" onClick={newTransaction} disabled={success || listing.listingStatus !== "active" || buyer.bbucksBalance < listing.price}>Buy Book</button> <br/>
            </div>
        </div>
    );
}

export default ShowListing;
