import '../styles/LandingPage.css';
import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import axios from 'axios';
import { useParams } from 'react-router-dom';
import { useDetails } from '../hooks/useDetails';

function ShowListing() {

    //variables foor listing and book
    const [listing, setListing] = useState(null);
    const [book, setBook] = useState(null);
    const [seller, setSeller] = useState(null);
    const [buyer, setBuyer] = useState(null);
    const [success, setSuccess] = useState(null);

    const {user, isLoading} = useUser();
    const { userId, setUserId } = useDetails();
    const { listingId } = useParams();

    const [transaction, setTransaction] = useState(null);

    useEffect(() => {
        const loadListingDetails = async () => {
            if (!user) {
                setListing('No user logged in.');
                return;
            }
            try {
                const token = await user.getIdToken();
                const headers = token ? { Authorization: `Bearer ${token}` } : {};
                
                //get listing data
                const listingResponse = await axios.get(`/listings/${listingId}`, { headers });
                setListing(listingResponse.data);
                
                //get book data from listing response
                const bookResponse = await axios.get(`/books/${listingResponse.data.bookId}`, { headers });
                setBook(bookResponse.data);
                
                //get seller
                const sellerResponse = await axios.get(`/users/${listingResponse.data.userId}`, { headers });
                setSeller(sellerResponse.data);

                //get buyer
                const buyerResponse = await axios.get(`/users/${userId}`, { headers });
                setBuyer(buyerResponse.data);
            } catch (error) {
                setListing("UNAUTHORIZED " + error.message);
            }
        };

        if (user) {
            loadListingDetails();
        }
    }, [listingId, isLoading, user]);
    
    
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
            const response = await axios.post('/book-transactions/create', {
                buyerId: userId,
                sellerId: seller.userId,
                listingId: listing.listingId,
                transactionPrice: listing.price ? parseFloat(listing.price) : 0,
                transactionStatus: "success"
            }, {headers});

            console.log('Transaction created successfully:', response.data);

            //update the book's status/
            const updateResponse = await axios.put(`/listings/update/${listingId}`, {
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

    if (listing)
        return (
            <>  
                <pre>{JSON.stringify(listing, null, 2)}</pre>
                <pre>{JSON.stringify(book, null, 2)}</pre>
                <pre>{JSON.stringify(seller, null, 2)}</pre>
                <pre>{JSON.stringify(buyer, null, 2)}</pre>

                <a> you have B-Bucks </a> <br/> <br/>

                <button onClick={newTransaction} disabled={success} >Buy Book</button>
                {success ? <a> You Bought the book! {JSON.stringify(transaction, null, 2)} </a>
                         : <a> </a>
                }
            </>
        );
    return <h1>Boo! (k)</h1>;
}


const ListingDetails = () => {
    return (
        <div>
            <h2> Listing Details </h2>
            {ShowListing()}
        </div>
    );
};

export default ListingDetails;
