import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getAuth, signInWithEmailAndPassword } from 'firebase/auth';
import '../styles/Login.css';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const navigate = useNavigate();

    const logIn = async () => {
        try {
            await signInWithEmailAndPassword(getAuth(), email, password);
            navigate('/');
        } catch (e) {
            setError(e.message);
        }
    };

    return (
        <div className="login-container">
            <div className="white-box">
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
                    />
                </div>
                {error && <p className="error-message">{error}</p>}
                <button className="login-page-button" onClick={logIn}>Log In</button>
                <div className="footer-links">
                    <Link to="/forgot-password" className="forgot-password">Forgot password?</Link>
                    <Link to="/signup" className="sign-up-text">Sign Up</Link>
                </div>
            </div>
        </div>
    );
}

export default Login;
