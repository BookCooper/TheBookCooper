import React, { createContext, useContext, useState, useEffect } from 'react';

const UserDetailsContext = createContext(null);

export const UserDetailsProvider = ({ children }) => {
  const [userId, setUserId] = useState(() => {
    // Get userId from local storage if available
    const savedUserId = localStorage.getItem('userId');
    return savedUserId ? JSON.parse(savedUserId) : null;
  });

  useEffect(() => {
    // Save userId to local storage whenever it changes
    if (userId !== null) {
      localStorage.setItem('userId', JSON.stringify(userId));
    }
  }, [userId]);

  return (
    <UserDetailsContext.Provider value={{ userId, setUserId }}>
      {children}
    </UserDetailsContext.Provider>
  );
};

export const useDetails = () => useContext(UserDetailsContext);
