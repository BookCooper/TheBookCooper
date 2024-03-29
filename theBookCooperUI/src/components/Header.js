import React from 'react';
import '../styles/LandingPage.css';
import Logout from './Logout.js';
import { useNavigate } from "react-router-dom";

const Header = () => {

    const navigate = useNavigate();

    return (
        <header>
            <h1 style={{cursor:'pointer'}} onClick={() => navigate('/')}>TheBookCooper</h1>
            <div className = "header-buttons">
                <button className="login-button" onClick={() => navigate('/about')}>About Us</button>
                <button className="login-button" onClick={() => navigate('/contact')}>Contact Us</button>
            </div>
            <Logout />
        </header>
    );
};


export default Header;

