import React from 'react';
import '../styles/LandingPage.css';
import Logout from './Logout.js';
//import Link from "react-router-dom";

const Header = () => {
  return (
    <header>
        <h1>TheBookCooper</h1>
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
        <Logout />
    </header>
  );
};

export default Header;
