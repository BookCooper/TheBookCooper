// src/components/ContentRow.js
import React from 'react';

const ContentRow = ({ colorClass, title, content, image, name }) => {
  return (
    <div className={`row ${colorClass}`} name={name}>
      <div className="content-container">
        {title && (
          <div className={image ? "text-section" : "slogan"}>
            <h2>{title}</h2>
            <h3>{content}</h3>
          </div>
        )}
        {image && (
          <div className="image-section">
            <img src={image} alt={title} />
          </div>
        )}
      </div>
    </div>
  );
};

export default ContentRow;
