import React from 'react';
import useUser from "../hooks/useUser";
import '../styles/LandingPage.css';

const ProfileTransactions = () => {
    const { user, isLoading } = useUser();

    if (isLoading) return <p>Loading...</p>;
    if (!user) return <p>Please log in.</p>;

    return (
        <div>
            <h1> Profile Transactions </h1>
        </div>
    );
}
export default ProfileTransactions;
