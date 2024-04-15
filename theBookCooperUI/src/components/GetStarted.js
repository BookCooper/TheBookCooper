import React from 'react';
import '../styles/LandingPage.css';

const GetStarted = () => {
    return (
        <div className="row">
            <div className="content-container">
            <div className="get-started-container">
                <h2 className="get-started-text">GET STARTED NOW</h2>
                <i className="fas fa-arrow-right arrow-icon"></i>
                <a href="/signup" className="signup-button">Sign Up</a>
            </div>
            </div>
        </div>
    );
};
export default GetStarted;