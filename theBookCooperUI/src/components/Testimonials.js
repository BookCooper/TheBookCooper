// src/components/Testimonials.js
import React from 'react';
import '../styles/LandingPage.css';

const Testimonial = ({ quote, author }) => (
    <div className="testimonial">
      <h3 style={{ color: 'white' }}>"{quote}"</h3>
      <h4>– {author}</h4>
    </div>
  );
  
  const testimonials = [
    {
      quote: "TheBookCooper transformed my college experience! I was able to get the textbook I needed without the usual stress.",
      author: "Alex Liu, Junior",
    },
    {
      quote: "Thanks to TheBookCooper, I traded a textbook with a peer. Like a boss.",
      author: "Aidan Cusa, Sophmore",
    },
    {
      quote: "I can't recommend TheBookCooper enough. Their search feature is so clean! I love the white and gray boxes so much!",
      author: "Chris Hong, Alumni",
    },
  ];

const Testimonials = () => {
  return (
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
    );
}

export default Testimonials;
