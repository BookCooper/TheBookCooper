import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; 
import '../styles/Header.css';


const SearchListings = ({ placeholder = "Search for a book" }) => {
    const [input, setInput] = useState("");
    const navigate = useNavigate();
    const host = window.location.host;

    const handleSearch = (e) => {
        e.preventDefault();
        if (input.trim() !== "") {
            navigate(`http://` + host + `:8080/search?title=${encodeURIComponent(input)}`);
        }
    };

    return (
        <div className="search-box">
            <form onSubmit={handleSearch} className="form-container">
                <input
                    className="search-input"
                    type="text"
                    value={input}
                    onChange={e => setInput(e.target.value)}
                    placeholder={placeholder}
                    aria-label="Search books"
                />
            </form>
        </div>
    );
};

export default SearchListings;
