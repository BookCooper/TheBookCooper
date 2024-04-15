import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getAuth, createUserWithEmailAndPassword } from 'firebase/auth';
import axios from 'axios'; 
import '../styles/SignUp.css';

const Signup = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [username, setUsername] = useState('');
    const [error, setError] = useState('');

    const navigate = useNavigate();

    const signUp = async (e) => {
        e.preventDefault();
        if (password !== confirmPassword) {
            setError(<span style={{ color: 'red' }}>Passwords do not match.</span>);
            return;
        }
        try {
            const userCredential = await createUserWithEmailAndPassword(getAuth(), email, password);
            const token = await userCredential.user.getIdToken();
            const headers = { Authorization: `Bearer ${token}` };

            await axios.post('/users/create', {
                userName: username,
                email: email,
                bBucksBalance: 0.0  // Optional, include if needed
            }, {headers});

            navigate('/login'); // Navigate to homepage or dashboard upon successful signup
        } catch (error) {
            console.error('Signup error:', error);
            setError(error.response?.data?.message || 'Failed to sign up')
        }
    };


    return (
        <div className="signup-container">
            <div className="signup-white-box">
                <form onSubmit={signUp} className="form-container">
                    <h1 className="signup-label">Create Account</h1>
                    <div>
                        <label htmlFor="email" className="label-text">Email</label>
                        <input
                            id="email"
                            className="input-field"
                            type="email"
                            placeholder="Your email address"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="username" className="label-text">Username</label>
                        <input
                            id="username"
                            className="input-field"
                            type="text"
                            placeholder="Your username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="password" className="label-text">Password</label>
                        <input
                            id="password"
                            className="input-field"
                            type="password"
                            placeholder="Your password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="confirm-password" className="label-text">Confirm Password</label>
                        <input
                            id="confirm-password"
                            className="input-field"
                            type="password"
                            placeholder="Confirm your password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </div>
                    {error && <p className="error-message">{error}</p>}
                    <button type="submit" className="signup-page-button">Sign Up</button>
                    <div className="footer-links">
                        <Link to="/login" className="login-text">Already have an account? Log in here.</Link>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default Signup;
