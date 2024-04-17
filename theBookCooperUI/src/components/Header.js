import React from 'react';
import '../styles/Header.css';
import Logout from './Logout.js';
import { useNavigate } from "react-router-dom";
import useUser from "../hooks/useUser";
import SearchBar from "./SearchBar.js"

const Header = () => {

    const navigate = useNavigate();
    const { user, isLoading } = useUser();

    return (
        <header>
            
            <img src="/new_logo.png" alt="New Logo" onClick={() => navigate('/')} />
            <h1 style={{cursor:'pointer'}} onClick={() => navigate('/')}>TheBookCooper</h1>

            {user
                ?   
                    <div className="search-and-buttons-container">
                        <SearchBar />
                        <button className="contact-button" onClick={() => navigate('/create-listing')}>Create Listing</button>
                        <button className="store-button" onClick={() => navigate('/store')}>Store</button>
                        <Logout />
                    </div>
                :
                    <div className = "header-buttons">
                        <button className="signup-button" onClick={() => navigate('/signup')}>Sign Up</button>
                        <Logout />
                    </div>
            }
        </header>
    );
};


export default Header;
