import '../styles/LandingPage.css';
import React, { useState } from 'react';
import useUser from "../hooks/useUser";
import axios from 'axios';


function ShowResults() {
    const [data, setData] = useState(null);
    const {user, isLoading} = useUser();
    const [input, setInput] = useState("");
    const [loading, setLoading] = useState(false);

    const getBooks = async () => {
    
        if (!user) {
            setData('No user logged in.');
            return;
        }

        setLoading(true);
        setData(null); 
        try {
            const token = await user.getIdToken();
            const headers = { Authorization: `Bearer ${token}` };
            const response = await axios.get(`/books/search?title=${encodeURIComponent(input)}`, { headers });
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
            <button onClick={getBooks} disabled={isLoading || !user || !input}>Look up</button>
            
            {loading 
            ? (
                <p>Loading...</p>
            ) 
            : 
                data ? (
                    <div>
                        {data.map((book) => (
                            <a key={book.bookId} href={`/books/${book.bookId}`} className="book-listing">
                            <div>
                            <h3>{book.title}</h3>
                            <p>{book.author}</p>
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
