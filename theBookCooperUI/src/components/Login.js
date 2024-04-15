import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getAuth, signInWithEmailAndPassword } from 'firebase/auth';
import '../styles/Login.css';
import { useDetails } from '../hooks/useDetails';
import axios from 'axios';
import useUser from "../hooks/useUser";

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const { userId, setUserId } = useDetails();
    const {user, isLoading} = useUser();
    const navigate = useNavigate();

    // Reset userId when component mounts
    useEffect(() => {
        setUserId(null);
        console.log(userId)
    }, [setUserId, userId]);
    
    const logIn = async (e) => {
        e.preventDefault();
        setError('');
        if (!email || !password) {
            setError('Please provide both email and password');
            return;
        }
        try {
            await signInWithEmailAndPassword(getAuth(), email, password);
            // If this is successful, the user will be set by useUser and caught by the useEffect below
        } catch (e) {
            setError(e.message);
        }
    };

    // Fetch user data after successful login
    useEffect(() => {
        const fetchUserData = async () => {
            if (user) {
                try {
                    const token = await user.getIdToken();
                    const headers = { Authorization: `Bearer ${token}` };
                    const userResponse = await axios.get(`/users/email/${email}`, { headers });
                    console.log("API Response:", userResponse.data);
                    
                    if (userResponse.data && userResponse.data.userId) {
                        console.log("Setting userID:", userResponse.data.userId);
                        setUserId(userResponse.data.userId);
                    }
                } catch (e) {
                    console.error("Error fetching user data:", e);
                    setError(e.toString());
                }
            }
        };
        if (user) {
            fetchUserData();
        }
    }, [email]);

    useEffect(() => {
        if (userId) {
            console.log("User ID is now set to:", userId);
            navigate('/'); // Navigate only after userId is set
        }
    }, [userId, navigate]);


    /*
    useEffect(() => {
        const fetchUserData = async () => {
            if (user) {
                try {
                    const token = await user.getIdToken();
                    const headers = { Authorization: `Bearer ${token}` };
                    const userResponse = await axios.get(`/users/email/${email}`, { headers });
                    console.log("API Response:", userResponse.data);

                    if (userResponse.data && userResponse.data.userId) {
                        console.log("Setting userID:", userResponse.data.userId);
                        setUserId(userResponse.data.userId);
                    };
                    
                    navigate('/');
                } catch (e) {
                    console.error("Error fetching user data:", e);
                }
            }
        };
        fetchUserData();
    }, [user, email, navigate]);

    // Effect to log userInfo when it updates
    
    const logIn = async (e) => {
        e.preventDefault();
        setError('');
        if (!email || !password) {
            setError('Please provide both email and password');
            return;
        }
        try {
            await signInWithEmailAndPassword(getAuth(), email, password);
            console.log("Again user id is: " + userId)
        } catch (e) {
            setError(e.message);
        }
    };
    
    
    const logIn = async (e) => {
        e.preventDefault(); // Prevent the form from causing a page reload
        try {
            await signInWithEmailAndPassword(getAuth(), email, password);

            const token = await user.getIdToken();
            const headers = token ? { Authorization: `Bearer ${token}` } : {};
            
            //get listing data
            const userResponse = await axios.get(`/users/email/${email}`, { headers });
            setUserInfo(userResponse.data);

            console.log(userInfo.userId)

            navigate('/');
        } catch (e) {
            setError(e.message);
        }
    };*/


    return (
        <div className="login-container">
            <div className="white-box">
                <form onSubmit={logIn} className="form-container">
                <h1 className="login-label">Login</h1>
            <div>
                <label htmlFor="email" className="email-label">Email</label>
                <input
                    id="email"
                    className="email-box"
                    type="email"
                    placeholder="Your email address"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                </div>
            <div>
                <label htmlFor="password" className="password-label">Password</label>
                <input
                    id="password"
                    className="password-box"
                    type="password"
                    placeholder="Your password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
            </div>
                {error && <p className="error-message">{error}</p>}
                <button type="submit" className="login-page-button">Log In</button>
                <div className="footer-links">
                    <Link to="/forgot-password" className="forgot-password">Forgot password?</Link>
                    <Link to="/signup" className="sign-up-text">Sign Up</Link>
                </div>
                </form>
            </div>
        </div>
    );
}

export default Login;
