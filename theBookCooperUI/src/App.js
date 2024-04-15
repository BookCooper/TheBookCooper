import React from 'react';
import './App.css';
import LandingPage from './pages/LandingPage';

/*
function Home() {
    return (
        <div>
            <h1>My Website</h1>
        </div>
    );
}*/

export function About() {
    return (
        <div>
            <h1>About Us</h1>
        </div>
    );
}

export function Contact() {
    return (
        <div>
            <h1>Contact Us</h1>
        </div>
    );
}

export function App() {
    return (
        <div className="App">
            <LandingPage />
        </div>
    );
}

//export default App;
