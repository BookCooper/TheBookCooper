import React from 'react';
import { useNavigate } from "react-router-dom";
import useUser from "../hooks/useUser";
import '../styles/Store.css';

const StoreItem = ({ bbucks, dollars, storeId }) => {

    const navigate = useNavigate();
    const { user, isLoading } = useUser();

    return (
        <button className = "store-page-button" onClick={() => navigate(`/payment/${storeId}`)}> {bbucks} B-Bucks - ${dollars}</button>
    );
};


export default StoreItem;

