import React from 'react';
import '../styles/LandingPage.css';
import Logout from './Logout.js';
import { useNavigate } from "react-router-dom";
import useUser from "../hooks/useUser";

const Header = () => {

    const navigate = useNavigate();
    const { user, isLoading } = useUser();

    return (
        <header>
            <h1 style={{cursor:'pointer'}} onClick={() => navigate('/')}>TheBookCooper</h1>

            {user
                ?
                    <div className = "header-buttons">
                        <button className="about-button" onClick={() => navigate('/about')}>About Us</button>
                        <button className="contact-button" onClick={() => navigate('/contact')}>Contact Us</button>
                        <Logout />
                    </div>
                :
                    <div className = "header-buttons">
                        <button className="contact-button" onClick={() => navigate('/signup')}>Sign Up</button>
                        <Logout />
                    </div>
            }
        </header>
    );
};


export default Header;

