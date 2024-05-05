import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import '../styles/ProfileEdit.css';

import axios from 'axios';
import { getAuth, updateEmail, updatePassword } from "firebase/auth";
import { reauthenticateWithCredential, EmailAuthProvider } from 'firebase/auth';
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

    const [newPassword, setNewPassword] = useState("");
    const [confirmNewPassword, setConfirmNewPassword] = useState("");

    const [email, setEmail] = useState("");
    const [reauthSuccess, setReauthSuccess] = useState("");

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
        if (isLoading) {
            return; //dont proceed if already loading
        }

        //reauthenticate user
        const auth = getAuth();
        const credential = EmailAuthProvider.credential(email, password);

        try {
            await reauthenticateWithCredential(auth.currentUser, credential);
            setReauthSuccess(true);
        } catch (err) {
            setError("Re-authentication failed: " + err.message);
        }


        if(reauthSuccess) {
            try {
                //shows the little loading message
                setLoadingDetails(true);

                const token = await user.getIdToken();
                const headers = { Authorization: `Bearer ${token}` };

                
                // update user in firebase
                if (user.email !== email) {
                    await updateEmail(user, email);
                }
                if (newPassword === confirmNewPassword) {
                    await updatePassword(user, newPassword);
                }

                //update user in back end
                const updateResponse = await axios.put(`/users/update/${userId}`, {
                    userName: username,
                    email: email,
                    bBucksBalance: loggedUser.bbucksBalance
                }, { headers });

                console.log(updateResponse.data);
                if (updateResponse.status === 200) {
                    console.log("Profile updated successfully.");
                    window.location.reload(true); //reload page on success
                } else {
                    console.error("Failed to update the profile. Status code:", updateResponse.status);
                }
            } catch (error) {
                console.error("Error updating profile:", error);
                setError("Error updating profile: " + (error.message || "Unknown error"));
            } finally {
                setLoadingDetails(false);
            }
        }
    }


    if (isLoading || loadingDetails) return <p>Loading...</p>;
    if (error) return <p>Error: {error}</p>;
    if (!user || !loggedUser) return <p>Please log in.</p>;

    return (
        <div className="create-edit-container">
            <div className="create-edit-white-box">
                <h1> Edit Your Profile </h1>

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
                    type="password"
                    value={newPassword}
                    onChange={e => setNewPassword(e.target.value)}
                    placeholder="Change your new password"
                    className="input-field"
                /><br/>
                <a className = "label-text"> Confirm New Password: </a> <input
                    className="input-field"
                    type="password"
                    placeholder="Confirm your new password"
                    value={confirmNewPassword}
                    onChange={(e) => setConfirmNewPassword(e.target.value)}
                    required
                /><br/><br/><br/>


                <a className = "label-text"> Current Password: </a> <input
                    type="password"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    placeholder="Change your password"
                    className="input-field"
                /><br/>

                <br/> <button className = "update-profile-button" onClick={updateUser} disabled={isLoading || !user || (newPassword !== confirmNewPassword)}>Update Account</button>
            </div>
        </div>
    );
}
export default ProfileEdit;
