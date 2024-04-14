import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getAuth, createUserWithEmailAndPassword } from 'firebase/auth';
import '../styles/SignUp.css'; // Assuming you have similar styles as Login.css

const Signup = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [user, setUser] = useState(null); // You may not need this if you're using a custom hook like useUser 

    const navigate = useNavigate();

    const signUp = async (e) => {
        e.preventDefault();
        if(password !== confirmPassword){
            setError(<span style={{ color: 'red' }}>Passwords do not match.</span>);
            return;
        }
        try {
            await createUserWithEmailAndPassword(getAuth(), email, password);
            navigate('/'); // Navigate to homepage or dashboard upon successful signup
        } catch (e) {
            setError(e.message);
        }
    };

    return (
        <div className="signup-container"> {/* Consider reusing login-container styles */}
            <div className="white-box">
                <form onSubmit={signUp} className="form-container"> {/* Reuse form-container styles */}
                <h1 className="signup-label">Create Account</h1> {/* Reuse login-label styles, just change class if necessary */}
                <div>
                <label htmlFor="email" className="label-text">Email</label>
                <input
                    id="email"
                    className="input-field" // Reuse email-box styles
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
                        className="input-field" // Consider adding a new class if you need
                        type="text"
                        placeholder="Your username"
                        value={user}
                        onChange={(e) => setUser(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="password" className="label-text">Password</label>
                    <input
                        id="password"
                        className="input-field" // Reuse password-box styles
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
                        className="input-field" // Consider adding a new class if you need different styles
                        type="password"
                        placeholder="Confirm your password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                />
                </div>
                    {error && <p className="error-message">{error}</p>}
                    <button type="submit" className="signup-page-button">Sign Up</button> {/* Consider reusing login-page-button styles */}
                    <div className="footer-links">
                    <Link to="/login" className="login-text">Already have an account? Log in here.</Link>
                </div>
            </form>
        </div>
    </div>
  );
}

export default Signup;
