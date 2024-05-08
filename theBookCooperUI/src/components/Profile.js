import '../styles/Profile.css';
import React, { useState, useEffect } from 'react';
import { Routes, Route, Outlet, Link } from 'react-router-dom';
import useUser from "../hooks/useUser";
import { useDetails } from "../hooks/useDetails";
import axios from 'axios';

import ProfileGeneral from './ProfileGeneral';
import ProfileEdit from './ProfileEdit';
import ProfileListings from './ProfileListings';
import ProfileHistory from './ProfileHistory';

const Profile = () => {
    const { user, isLoading } = useUser();
    const { userId } = useDetails();

    const [loggedUser, setLoggedUser] = useState(null);
    const [loadingDetails, setLoadingDetails] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        const loadUserDetails = async () => {
            if (!user) {
                setLoggedUser(null);
                return;
            }
            setLoadingDetails(true);
            try {
                const token = await user.getIdToken();
                const headers = token ? { Authorization: `Bearer ${token}` } : {};
                const userResponse = await axios.get(`/users/${userId}`, { headers });
                setLoggedUser(userResponse.data);
            } catch (error) {
                setError("UNAUTHORIZED " + error.message);
            } finally {
                setLoadingDetails(false);
            }
        };

        if (user) {
            loadUserDetails();
        }
    }, [user, userId]);

    if (isLoading || loadingDetails) return <p>Loading...</p>;
    if (error) return <p>Error: {error}</p>;
    if (!user || !loggedUser) return <p>Please log in.</p>;

    return (
        <div className="profile-container">
            <div className="sidebar">
                <Link to="">Home</Link>
                <Link to="edit">Edit Profile</Link>
                <Link to="listings">My Listings</Link>
                <Link to="history">My History</Link>
                <div className = "sidebar-text"> You have <b>{loggedUser.bbucksBalance}</b> B-Bucks! </div>
            </div>
            <div className="profile-content">
                <h1 className = "profile-for">{loggedUser.userName}'s Profile</h1>
                <Routes>
                    <Route path="/" element={<ProfileGeneral />} />
                    <Route path="edit" element={<ProfileEdit />} />
                    <Route path="listings" element={<ProfileListings />} />
                    <Route path="history" element={<ProfileHistory />} />
                </Routes>
                <Outlet />
            </div>
        </div>
    );
}

export default Profile;
