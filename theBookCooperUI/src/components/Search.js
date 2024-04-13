import '../styles/LandingPage.css';
import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import axios from 'axios';
import BookDetails from './BookDetails'


function ShowResults() {
    const [data, setData] = useState(null);
    const {user, isLoading} = useUser();
    const [input, setInput] = useState("");
    const [loading, setLoading] = useState(false);
    const [token, setToken] = useState(null);

    useEffect(() => {
        const fetchToken = async () => {
            if (user) {
                const fetchedToken = await user.getIdToken();
                setToken(fetchedToken);
            }
        };

        fetchToken();
    }, [user, isLoading]);

    const getListings = async () => {
    
        if (!token) {
            setData('No user logged in or token not retrieved.');
            return;
        }

        setLoading(true);
        setData(null); 
        try {
            const headers = { Authorization: `Bearer ${token}` };
            const response = await axios.get(`/listings/search?title=${encodeURIComponent(input)}`, { headers });
            setData(response.data);
        } catch (error) {
            setData(error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <input
                type="text"
                value={input}
                onChange={e => setInput(e.target.value)}
                placeholder="Enter your query"
            />

            {/*disable the look up button when its loading, user isnt logged in, or no input*/}
            <button onClick={getListings} disabled={isLoading || !user || !input}>Look up</button>
            
            {loading 
            ? (
                <p>Loading...</p>
            ) 
            : 
                data ? (
                    <div>
                        {data.map((listing) => (
                            <a key={listing.listingId} href={`/listings/${listing.listingId}`}>
                            <div>
                            <BookDetails bookId={listing.bookId} token={token} />
                            <p>{listing.price} B-Bucks</p>
                            </div>
                            </a>
                        ))}
                    </div>
                ) 
                : (<p>No data or user is not authenticated</p>)
            }
        </>
    );
}


const Search = () => {
    return (
        <div>
            <h2> Search Page </h2>
            {ShowResults()}
        </div>
    );
};

export default Search;
