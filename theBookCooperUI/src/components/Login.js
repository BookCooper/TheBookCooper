// src/components/Login.js
import React, { useState } from 'react';
import { auth } from '../firebase-config';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const login = async (e) => {
    e.preventDefault();
    try {
      const userCredential = await auth.signInWithEmailAndPassword(email, password);
      console.log("Logged in as:", userCredential.user);
    } catch (error) {
      console.error("Error logging in:", error.message);
    }
  };
  const handleLogin = async (e) => {
    e.preventDefault();
    try {
        await auth.signInWithEmailAndPassword(email, password);
        alert("Logged in successfully!");
    } catch (error) {
        alert(error.message);
    }
    };

  return (
    <div className="container">
        <div className="login-text">Login</div>
        <form onSubmit={handleLogin}>
            <div className="form-group">
                <label htmlFor="email">Email:</label>
                <input type="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Enter your email" required autoComplete="off" />
            </div>
            <div className="form-group">
                <label htmlFor="password">Password:</label>
                <input type="password" id="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Enter your password" required autoComplete="off" />
            </div>
            <div className="btn-container">
                <button className="btn" type="submit">Login</button>
            </div>
        </form>
        {/* Additional options */}
        <div className="additional-options">
            {/* Adjust these links as per your routing */}
            <a href="/password-reset" className="forgot-password">Forgot password?</a>
            <a href="/signup" className="dont-have-an-account">Don't have an account?</a>
        </div>
    </div>
  );
}

export default Login;
