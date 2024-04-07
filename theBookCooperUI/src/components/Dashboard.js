import '../styles/LandingPage.css';
import React, { useState, useEffect } from 'react';

function ShowListings() {
    const [data, setData] = useState(null);
    useEffect(() => {
        fetch(
            `http://localhost:8080/api/getUsers`
        )
            .then((response) => response.json())
            .then(setData);
    }, []);
    if (data)
        return (
            <>
                <pre>{JSON.stringify(data, null, 2)}</pre>
            </>
        );
    return <h1>Data</h1>;
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
