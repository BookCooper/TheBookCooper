import React from 'react';
import '../styles/LandingPage.css';
import { Link } from "react-router-dom";

const Navbar = () => {
  return (
    <nav>
        <Link to="/">Home</Link>
        <Link to="/about">About</Link>
        <Link to="/contact">Contact</Link>
        <Link to="/login">Login</Link>
    </nav>
  );
};

export default Navbar;
