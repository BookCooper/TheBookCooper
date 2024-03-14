import React from 'react';
import '../styles/LandingPage.css';

import Header from './Header';
import ContentRow from './ContentRow';
import Testimonials from './Testimonials';
import GetStarted from './GetStarted';

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
