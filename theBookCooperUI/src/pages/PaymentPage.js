import React from "react";
import { loadStripe } from "@stripe/stripe-js";
import { Elements } from "@stripe/react-stripe-js";

import PaymentForm from "../components/PaymentForm";
import Header from "../components/Header";

import '../styles/LandingPage.css';

const PUBLIC_KEY = "pk_test_51OuJjmRsi5TzF2bOMww0g5enzTILgrXfQEqrjP6ILzI5I8apuzVhjJOX3m5aAX48wcCwWIkdDPVRhSL3VNBUfTtL00NO1UxJNX"; 
const stripeTestPromise = loadStripe(PUBLIC_KEY); 


export default function PaymentPage() {
    return (

        <div>
            <Header />
            <br/> <br/>
            <Elements stripe={stripeTestPromise}>
                <PaymentForm />
            </Elements>
        </div>
    );
}
