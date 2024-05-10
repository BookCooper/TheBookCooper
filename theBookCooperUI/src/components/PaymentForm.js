import React, { useState, useEffect } from "react";
import { CardElement, useElements, useStripe } from "@stripe/react-stripe-js";
import axios from "axios";
import { useParams } from 'react-router-dom';
import useUser from "../hooks/useUser";
import { useDetails } from '../hooks/useDetails';
import '../styles/Store.css';

const CARD_OPTIONS = {
    iconStyle: "solid",
    style: {
        base: {
            iconColor: "#000",
            color: "#000",
            fontWeight: 500,
            fontFamily: "Roboto, Open Sans, Segoe UI, sans-serif",
            fontSize: "16px",
            fontSmoothing: "antialiased",
            ":-webkit-autofill": { color: "#fce883" },
            "::placeholder": { color: "#87bbfd" }
        },
        invalid: {
            iconColor: "#ffc7ee",
            color: "#ffc7ee"
        }
    }
}

function PaymentForm() {
    const [success, setSuccess] = useState(false); 
    const stripe = useStripe(); 
    const elements = useElements(); 
    const { storeId } = useParams();
    const [storeItem, setStoreItem] = useState(null); 
    const [userInfo, setUserInfo] = useState(null); 
    const { user, isLoading } = useUser();
    const { userId } = useDetails();
    const [loading, setLoading] = useState(false);
    const [confirming, setConfirming] = useState(false);
    const host = window.location.hostname;

    useEffect(() => {
        if (user) {
            setLoading(true);
            loadStoreItem();
        }
    }, [storeId, userId, user]);

    const loadStoreItem = async () => {
        // Loading store item logic
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Show confirmation dialog
        if (!confirming) {
            setConfirming(true);
            return;
        }

        setConfirming(false);
        setLoading(true); // Start loading

        const {error, paymentMethod} = await stripe.createPaymentMethod({
            type: "card",
            card: elements.getElement(CardElement)
        });

        if (!error) {
            const id = paymentMethod.id;
            const token = await user.getIdToken();
            const headers = { Authorization: `Bearer ${token}` };

            const response = await axios.post(`http://${host}:8080/payment-request`, {
                amount: storeItem.itemPrice * 100,
                id,
                userId
            }, { headers });

            if (response.data.success) {
                setSuccess(true);
            } else {
                console.error("Payment failed: ", response.data.message);
            }
        } else {
            console.error("Stripe error: ", error.message);
        }
        setLoading(false);
    };

    return (
        <div className="create-store-container">
            <div className="create-store-white-box">
                {loading ? (
                    <p>Loading...</p>
                ) : (
                    !success ? (
                        <>
                            <h2>Purchasing {storeItem && storeItem.item} for ${storeItem && storeItem.itemPrice}</h2>
                            {confirming && (
                                <div>
                                    <p>Are you sure you want to make this payment?</p>
                                    <button onClick={handleSubmit}>Yes, Confirm</button>
                                    <button onClick={() => setConfirming(false)}>Cancel</button>
                                </div>
                            )}
                            {!confirming && (
                                <form className="payment-form" onSubmit={handleSubmit}>
                                    <fieldset className="FormGroup">
                                        <div className="FormRow">
                                            <CardElement options={CARD_OPTIONS} />
                                        </div>
                                    </fieldset>
                                    <br/><button className="store-page-button" disabled={!storeItem || loading}>Pay</button>
                                </form>
                            )}
                        </>
                    ) : (
                        <div className="successful-payment">
                            <h2>You have just purchased {storeItem.item}</h2>
                            <h3>New B-Bucks Balance is {userInfo.bbucksBalance + parseFloat(storeItem.item)}</h3>
                        </div>
                    )
                )}
            </div>
        </div>
    );
}

export default PaymentForm;
