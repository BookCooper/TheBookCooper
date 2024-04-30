import React from 'react';
import '../styles/Store.css';
import StoreItem from './StoreItem';

const Store = () => {
    return (
        <>
            <div className = "create-store-container">
                <div className = "create-store-white-box">
                    <h2 className="create-store-label">B-Buck Shop</h2>
                    <br/>

                    <div className = "store-items-container">
                        
                        <div className = "store-item-grey-box">
                            <img src="/One-B-Buck-small.png" alt="One-B-Buck" className = "store-item-image"/>
                            <br/> <br/>
                            <StoreItem bbucks={1000} dollars={10.00} storeId={1}/> 
                        </div>
                        <div className = "store-item-grey-box">
                            <img src="/Three-B-Bucks.png" alt="One-B-Buck" className = "store-item-image"/>
                            <br/> <br/>
                            <StoreItem bbucks={2500} dollars={20.00} storeId={2}/> 
                        </div>
                        <div className = "store-item-grey-box">
                            <img src="/Six-B-Bucks.png" alt="One-B-Buck" className = "store-item-image"/>
                            <br/> <br/>
                            <StoreItem bbucks={5000} dollars={35.00} storeId={3}/> 
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default Store;
