import React from 'react';
import '../styles/LandingPage.css';

const Header = () => {
  return (
    <header>
        <h1>TheBookCooper</h1>
        <div className="header-buttons">
            <h3 href="/signup" className="signup-button">Sign Up</h3>
            <h3 href="/login" className="login-button">Login</h3>
        </div>
    </header>
  );
};

export default Header;