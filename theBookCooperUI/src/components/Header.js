import React from 'react';

const Header = () => {
  return (
    <header>
      <h1>The Book Cooper</h1>
      <div className="header-buttons">
        {/* Update these links to your React Router Links or a tags for external links */}
        <a href="/signup" className="signup-button">Sign Up</a>
        <a href="/login" className="login-button">Login</a>
      </div>
    </header>
  );
};

export default Header;