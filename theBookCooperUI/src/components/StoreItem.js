import React from 'react';
import '../styles/LandingPage.css';
import { useNavigate } from "react-router-dom";
import useUser from "../hooks/useUser";

const StoreItem = ({ bbucks, dollars, storeId }) => {

    const navigate = useNavigate();
    const { user, isLoading } = useUser();

    return (
        <button onClick={() => navigate(`/payment/${storeId}`)}> {bbucks} B-Bucks - ${dollars}</button>
    );
};


export default StoreItem;

