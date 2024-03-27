import React from 'react';
import '../styles/LandingPage.css';
import Landing1 from '../assets/images/Landing1.png';
import Landing2 from '../assets/images/Landing2.png';
import Landing3 from '../assets/images/Landing3.png';
import Landing4 from '../assets/images/Landing4.png';

const ContentRow = () => {
    return (
        <div>
            <section className="row color1">
                <div className="content-container">
                        <div className="slogan">
                                <h2>A NEW WAY TO OPTIMIZE YOUR ACADEMIC ARSENAL</h2>
                        </div>
                        <div className="image-section">
                                <img src={Landing1} alt="Optimize Your Academic Schedule"/>
                        </div>
                </div>
            </section>

            <section className="row color2">
                <div className="content-container">
                        <div className="image-section">
                                <img src={Landing2} alt="Trade Posts"/>
                        </div>
                        <div className="text-section">
                                <h2>MAKE TRADE POSTS</h2>
                                <h3>Missed out on the textbook you wanted? TheBookCooper lets you get textbooks from fellow cooper students...</h3>
                        </div>
                </div>
            </section>

            <section className="row color1">
                <div className="content-container">
                        <div className="text-section">
                                <h2>SEARCH FOR YOUR NEEDS</h2>
                                <h3>Search for the listings that are most relevant to you. TheBookCooper lets you know which textbook you need for the desired class. You can also filter listings by other options such as departments, professors, etc</h3>
                        </div>
                        <div className="image-section">
                                <img src={Landing3} alt="Search for your needs"/>
                        </div>
                </div>
            </section>

            <section className="row color2">
                <div className="content-container">
                    <div className="image-section">
                            <img src={Landing4} alt="Get Notified Instantly"/>
                    </div>
                    <div className="text-section">
                            <h2>GET NOTIFIED INSTANTLY</h2>
                            <h3>Never miss an opportunity with TheBookCooper's real-time notifications. Whether a new listing drops or your listing gets sold</h3>
                    </div>
                </div>
            </section>
        </div>
    );
};

export default ContentRow;
