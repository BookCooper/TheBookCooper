import '../styles/LandingPage.css';
import React, { useState, useEffect } from 'react';
import useUser from "../hooks/useUser";
import axios from 'axios';


function ShowListings() {
    const [data, setData] = useState(null);
    const {user, isLoading} = useUser();
    useEffect(() => {
        
        const loadUsers = async () => {
        
            try {
                //const response = await axios.get(`users/count`);
                const token = user && await user.getIdToken();
                console.log(token);
                const headers = token ? {Authorization: `Bearer ${token}`} : {};
                const response = await axios.get(`/users/count`, {headers});
                setData(response.data)
            }
            catch (error) {
                setData("UNAUTHORIZED " + error.message);
            }
        }
        loadUsers();
    }, [isLoading, user]);
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
