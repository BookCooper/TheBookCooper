import React from 'react';
import '../styles/LandingPage.css';
import Logout from './Logout.js';
import Search from './Search.js'
import { useNavigate } from "react-router-dom";
import useUser from "../hooks/useUser";

const Header = () => {

    const navigate = useNavigate();
    const { user, isLoading } = useUser();

    return (
        <header>
            
            <img src="/new_logo.png" alt="New Logo" style={{cursor: 'pointer', width: "115px"}} onClick={() => navigate('/')} />
            <h1 style={{cursor:'pointer'}} onClick={() => navigate('/')}>TheBookCooper</h1>

            {user
                ?
                    <div className = "header-buttons">
                        <button className="search-button" onClick={() => navigate('/search')}>Search</button>
                        <button className="contact-button" onClick={() => navigate('/create-listing')}>Create Listing</button>
                        <button className="search-button" onClick={() => navigate('/store')}>Store</button>
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
