import React from 'react';
import '../styles/LandingPage.css';
import Landing1 from '../assets/images/Landing1.png';
import Landing2 from '../assets/images/Landing2.png';
import Landing3 from '../assets/images/Landing3.png';
import Landing4 from '../assets/images/Landing4.png';

const Testimonial = ({ quote, author }) => (
    <div className="testimonial">
      <h3>"{quote}"</h3>
      <h4>– {author}</h4>
    </div>
  );
  
  const testimonials = [
    {
      quote: "TheBookCooper transformed my college experience! I was able to get the textbook I needed without the usual stress.",
      author: "Jane Doe, Freshman",
    },
    {
      quote: "Thanks to TheBookCooper, I traded a textbook with a peer",
      author: "John Smith, Junior",
    },
    {
      quote: "I can't recommend TheBookCooper enough. The real-time notifications are a game-changer. I always stay ahead now!",
      author: "Alice Johnson, Sophomore",
    },
  ];



const LandingPage = () => {
    return (
        <div>
            <header>
                <h1>TheBookCooper</h1>
                <div className="header-buttons">
                    <a href="/signup" className="signup-button">Sign Up</a>
                    <a href="/login" className="login-button">Login</a>
                </div>
            </header>

            <section className="row color1">
                <div className="content-container">
                    <div className="slogan">
                        <h2>A NEW WAY TO OPTIMIZE YOUR ACADEMIC</h2>
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

            <section className="row color1">
        <div className="content-container">
          <div className="testimonial-header">
            <h2>HERE'S WHAT OUR USERS SAY</h2>
          </div>
        </div>
        <div className="testimonial-section">
          {testimonials.map((testimonial, index) => (
            <Testimonial key={index} quote={testimonial.quote} author={testimonial.author} />
          ))}
        </div>
      </section>

      <div className="row">
        <div className="content-container">
          <div className="get-started-container">
            <h2 className="get-started-text">GET STARTED NOW</h2>
            <i className="fas fa-arrow-right arrow-icon"></i>
            {/* Update link as necessary */}
            <a href="/signup" className="signup-button">Sign Up</a>
          </div>
        </div>
      </div>
    </div>
    );};
export default LandingPage;
