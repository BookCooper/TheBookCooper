import React from 'react';
import '../styles/Profile.css';

const ProfileGeneral = () => {
    return (
        <div className = "general-profile-container">
            <div className = "general-profile-white-box">
                <h1> Welcome to your profile! </h1>
                <h4> Click on the left sidebar to navigate around. </h4>
                <img className = "profile-img" src="/new_logo.png" alt="New Logo" />
            </div>
        </div>
    );
}

export default ProfileGeneral;
