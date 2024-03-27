import React from 'react';
import './App.css';
import LandingPage from './components/LandingPage';
import { Link } from "react-router-dom";
import useUser from "./hooks/useUser";
/*
function Home() {
    return (
        <div>
            <h1>My Website</h1>
        </div>
    );
}*/

function links() {
    //const {user, isLoading} = useUser();

    return (
        <nav>
            <Link to="/">Home</Link>
            <Link to="/about">About</Link>
            <Link to="/contact">Contact</Link>
            <Link to="/login">Login</Link>
        </nav>
        /*{user
            ? <pre>{JSON.stringify(data, null, 2)}</pre>
            : <p>Log in to view sensitive info!</p>
        }*/
    );
}

export function About() {
    return (
        <div>
            {links()}
            <h1>About Us</h1>
        </div>
    );
}

export function Contact() {
    return (
        <div>
            {links()}
            <h1>Contact Us</h1>
        </div>
    );
}

export function App() {
    return (
        <div className="App">
            {links()}
            <LandingPage />
        </div>
    );
}

//export default App;
