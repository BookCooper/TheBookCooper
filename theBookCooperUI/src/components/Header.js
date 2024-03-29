import React from 'react';
import '../styles/LandingPage.css';
import Logout from './Logout.js';
//import Link from "react-router-dom";
import { useNavigate } from "react-router-dom";

const Header = () => {

    const navigate = useNavigate();

    return (
        <header>
            <h1 style={{cursor:'pointer'}} onClick={() => navigate('/')}>TheBookCooper</h1>
            {
                //got rid of sign up button in place for just log in where user can
                //sign up for an account if they don't have one already. 
                //
                //makes it easier for us so we don't have to check if the user is
                //signed in already and thus have to remove the sign up button. Although
                //it probably wouldn't take long to implement it is just a lot of extra work
                //
                //<div className = "signup-button"> <Link to="/create-account">Sign up</Link> </div>
            }
            <div className = "header-buttons">
                <button className="login-button" onClick={() => navigate('/about')}>About Us</button>
                <button className="login-button" onClick={() => navigate('/contact')}>Contact Us</button>
            </div>
            <Logout />
        </header>
    );
};


export default Header;

