import '../styles/LandingPage.css';
import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import axios from 'axios';
import { useParams } from 'react-router-dom';

function ShowListing() {

    //variables foor listing and book
    const [listing, setListing] = useState(null);
    const [book, setBook] = useState(null);
    const [seller, setSeller] = useState(null);

    const {user, isLoading} = useUser();
    const { listingId } = useParams();

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
            } catch (error) {
                setListing("UNAUTHORIZED " + error.message);
            }
        };

        if (user) {
            loadListingDetails();
        }
    }, [listingId, isLoading, user]);

    if (listing)
        return (
            <>
                <pre>{JSON.stringify(listing, null, 2)}</pre>
                <pre>{JSON.stringify(book, null, 2)}</pre>
                <pre>{JSON.stringify(seller, null, 2)}</pre>
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
