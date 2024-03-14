// src/components/Testimonials.js
import React from 'react';

const Testimonials = ({ testimonials }) => {
  return (
    <div className="testimonial-section">
      {testimonials.map((testimonial, index) => (
        <div className="testimonial" key={index}>
          <p>{testimonial.quote}</p>
          <h4>– {testimonial.author}</h4>
        </div>
      ))}
    </div>
  );
};

export default Testimonials;
