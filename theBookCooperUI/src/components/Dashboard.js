import '../styles/LandingPage.css';
import React, { useState, useEffect } from 'react';
import axios from 'axios';

function ShowListings() {
    const [data, setData] = useState(null);
    useEffect(() => {

        const loadUsers = async () => {
            const response = await axios.get(`http://localhost:8080/users/count`);
            setData(response.data)
        }
        loadUsers();
    }, []);
    if (data)
        return (
            <>
                <pre>{JSON.stringify(data, null, 2)}</pre>
            </>
        );
    return <h1>Dataaaa</h1>;
}


const Dashboard = () => {
    return (
        <div>
            {ShowListings()}
            <h2> Dashboard </h2>
        </div>
    );
};

export default Dashboard;
