import React from 'react';
import '../styles/LandingPage.css';
import StoreItem from '../components/StoreItem';
import Header from '../components/Header'

const StorePage = () => {
    return (
        <div>
            <Header/ >
            <h1>B-Buck Shop!</h1>

            <StoreItem bbucks={1000} dollars={10.00} storeId={1}/> 
            <StoreItem bbucks={2500} dollars={20.00} storeId={2}/> 
            <StoreItem bbucks={5000} dollars={35.00} storeId={3}/> 
        </div>
    );
}

export default StorePage;
