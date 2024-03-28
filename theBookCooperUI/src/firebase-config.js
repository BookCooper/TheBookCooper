// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getAuth } from "firebase/auth"; // Import Authentication

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyCu5ovkarugZLk8QeXE1oovuASed5zkw0c",
  authDomain: "the-book-cooper.firebaseapp.com",
  projectId: "the-book-cooper",
  storageBucket: "the-book-cooper.appspot.com",
  messagingSenderId: "312128100089",
  appId: "1:312128100089:web:cebe5225ef7040320a7428",
  measurementId: "G-7Y4SZ4EJVE"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

// Initialize Firebase Analytics
const analytics = getAnalytics(app);

// Initialize Firebase Authentication and export it
export const auth = getAuth(app);
