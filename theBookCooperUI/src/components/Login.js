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

    const host = window.location.host;

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
            const userCredential = await signInWithEmailAndPassword(getAuth(), email, password);
            const firebaseUser = userCredential.user;

            // Get the token from the signed-in user
            const token = await firebaseUser.getIdToken();
            const headers = { Authorization: `Bearer ${token}` };
            console.log("token is: " + token); 

            // Fetch user data using the token
            const userResponse = await axios.get(`${host}/users/email/${email}`, { headers });
            console.log("API Response:", userResponse.data);

            // If the user data includes the userId, set it in the state
            if (userResponse.data && userResponse.data.userId) {
                console.log("Setting userID:", userResponse.data.userId);
                setUserId(userResponse.data.userId);
                navigate('/'); // Navigate only after userId is set
            }

        } catch (e) {
            console.error("Login error:", e);
            setError(e.message);
        }
    };

    useEffect(() => {
        if (userId) {
            console.log("User ID is now set to:", userId);
            navigate('/'); // Navigate only after userId is set
        }
    }, [userId, navigate]);


    return (
        <div className="login-container">
            <div className="login-white-box">
                <form onSubmit={logIn} className="form-container">
                <h1 className="login-label">Login</h1>
            <div>
                <label htmlFor="email" className="email-label">Email</label> <br/>
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
                <label htmlFor="password" className="password-label">Password</label><br/>
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
                    <Link to="/signup" className="sign-up-text">Don't have an account? Sign Up</Link>
                </div>
                </form>
            </div>
        </div>
    );
}

export default Login;
