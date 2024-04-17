import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';


import { App } from './App';
import DashboardPage from "./pages/DashboardPage";
import AboutPage from "./pages/AboutPage";
import ContactPage from "./pages/ContactPage";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import PaymentPage from "./pages/PaymentPage";
import SearchPage from "./pages/SearchPage";
import ListingDetailPage from "./pages/ListingDetailPage"
import CreateBookPage from "./pages/CreateBookPage"
import StorePage from "./pages/StorePage"
import CreateListingPage from "./pages/CreateListingPage"
import { UserDetailsProvider } from './hooks/useDetails'

import { BrowserRouter, Routes, Route } from "react-router-dom";
import { auth } from './firebase-config';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <UserDetailsProvider>
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<App />} />
            <Route path="/about" element={<AboutPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />
            <Route path="/contact" element={<ContactPage />} />
            <Route path="/dashboard" element={<DashboardPage />} />
            <Route path="/payment/:storeId" element={<PaymentPage />} />
            <Route path="/search/" element={<SearchPage />} />
            <Route path="/listings/:listingId" element={<ListingDetailPage />} />
            <Route path="/create-book" element={<CreateBookPage />} />
            <Route path="/create-listing" element={<CreateListingPage />} />
            <Route path="/store" element={<StorePage />} />
        </Routes>
    </BrowserRouter>
    </UserDetailsProvider>,
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();


