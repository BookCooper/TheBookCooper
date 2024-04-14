import React from 'react';
import '../styles/LandingPage.css';

import Header from '../components/Header';
import ContentRow from '../components/ContentRow';
import Testimonials from '../components/Testimonials';
import GetStarted from '../components/GetStarted';

const LandingPage = () => {
    return (
        <div>
            <Header />
            <ContentRow />
            <Testimonials />
            <GetStarted />
        </div>
    );
};

export default LandingPage;
