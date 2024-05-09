import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';


import { App } from './App';
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import PaymentPage from "./pages/PaymentPage";
import SearchPage from "./pages/SearchPage";
import ListingDetailPage from "./pages/ListingDetailPage"
import CreateBookPage from "./pages/CreateBookPage"
import StorePage from "./pages/StorePage"
import CreateListingPage from "./pages/CreateListingPage"
import ProfilePage from "./pages/ProfilePage"

import { UserDetailsProvider } from './hooks/useDetails'

import { BrowserRouter, Routes, Route } from "react-router-dom"
//import { auth } from './firebase-config';
// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
//import { getAnalytics } from "firebase/analytics";
import { getAuth } from "firebase/auth"; // Import Authentication

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyCu5ovkarugZLk8QeXE1oovuASed5zkw0c",
  authDomain: "the-book-cooper.firebaseapp.com",
  projectId: "the-book-cooper",
  storageBucket: "the-book-cooper.appspot.com",
  messagingSenderId: "312128100089",
  appId: "1:312128100089:web:cebe5225ef7040320a7428",
  measurementId: "G-7Y4SZ4EJVE"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

// Initialize Firebase Analytics
//const analytics = getAnalytics(app);

// Initialize Firebase Authentication and export it
export const auth = getAuth(app);
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <UserDetailsProvider>
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<App />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />
            <Route path="/payment/:storeId" element={<PaymentPage />} />
            <Route path="/search/" element={<SearchPage />} />
            <Route path="/listings/:listingId" element={<ListingDetailPage />} />
            <Route path="/create-book" element={<CreateBookPage />} />
            <Route path="/create-listing" element={<CreateListingPage />} />
            <Route path="/store" element={<StorePage />} />
            <Route path="/profile/*" element={<ProfilePage />} />
        </Routes>
    </BrowserRouter>
    </UserDetailsProvider>,
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();


