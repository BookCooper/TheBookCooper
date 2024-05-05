import React from 'react';
import useUser from "../hooks/useUser";
import '../styles/LandingPage.css';

const ProfileEdit = () => {
    const { user, isLoading } = useUser();

    if (isLoading) return <p>Loading...</p>;
    if (!user) return <p>Please log in.</p>;

    return (
        <div>
            <h1> Profile Edit </h1>
        </div>
    );
}
export default ProfileEdit;

