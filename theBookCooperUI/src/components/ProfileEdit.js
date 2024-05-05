import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import '../styles/LandingPage.css';

import axios from 'axios';
import { useDetails } from "../hooks/useDetails";

const ProfileEdit = () => {

    const { user, isLoading } = useUser();
    const { userId } = useDetails();

    const [listings, setListings] = useState([]);
    const [loggedUser, setLoggedUser] = useState(null);
    const [loadingDetails, setLoadingDetails] = useState(false);
    const [error, setError] = useState('');

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [email, setEmail] = useState("");

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
                
                //can remove this, but can also use it to display B-Buck balance, etc.
                const userResponse = await axios.get(`/users/${userId}`, { headers });

                setLoggedUser(userResponse.data);
                setUsername(userResponse.data.userName);
                setEmail(userResponse.data.email);
                console.log(userResponse.data);

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


    //update user when they click button
    const updateUser = async () => {
        try {
            const token = await user.getIdToken();
            const headers = token ? { Authorization: `Bearer ${token}` } : {};

            //console.log("Username is: " + username + " | email is: " + email + " | b-bucks are: " + loggedUser.bBucksBalance) 
            
            const updateResponse = await axios.put(`/users/update/${userId}`, {
                userName: username,
                email: email,
                bBucksBalance: loggedUser.bbucksBalance
            }, { headers });

            console.log(updateResponse.data);

            if (updateResponse.status === 200) {
                console.log("Profile updated successfully.");
                window.location.reload(true);
            } else {
                console.error("Failed to update the profile. Status code:", updateResponse.status);
            }

        } catch (error) {
            setError("UNAUTHORIZED " + error.message);
        } 
    }


    if (isLoading || loadingDetails) return <p>Loading...</p>;
    if (error) return <p>Error: {error}</p>;
    if (!user || !loggedUser) return <p>Please log in.</p>;

    return (
        <div>
            <h1> Profile Edit </h1>

            <a className = "label-text"> Username: </a> <input
                type="text"
                value={username}
                onChange={e => setUsername(e.target.value)}
                placeholder="Change your username"
                className="input-field"
            /><br/>
            <a className = "label-text"> Email: </a> <input
                type="text"
                value={email}
                onChange={e => setEmail(e.target.value)}
                placeholder="Change your email"
                className="input-field"
            /><br/>
            <a className = "label-text"> Password: </a> <input
                type="text"
                value={password}
                onChange={e => setPassword(e.target.value)}
                placeholder="Change your password"
                className="input-field"
            /><br/>
            <a className = "label-text"> Confirm Password: </a> <input
                id="confirm-password"
                className="input-field"
                type="password"
                placeholder="Confirm your password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
            /><br/>

            <br/> <button onClick={updateUser} disabled={isLoading || !user}>Update Account</button>

        </div>
    );
}
export default ProfileEdit;
