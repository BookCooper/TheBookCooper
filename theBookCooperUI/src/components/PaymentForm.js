import React, {useState, useEffect} from "react";
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

    const [ success, setSuccess ] = useState(false); 
    const stripe = useStripe(); 
    const elements = useElements(); 
    const { storeId } = useParams();
    const [ storeItem, setStoreItem ] = useState(null); 
    const [ userInfo, setUserInfo ] = useState(null); 
    const { user, isLoading } = useUser();
    const { userId } = useDetails();
    const [loading, setLoading] = useState(true);
    const host = window.location.hostname;

    useEffect(() => {
        const loadStoreItem = async () => {
            if (!user || !userId) {
                setStoreItem('No user logged in.');
                alert('Please log in to continue');
                setLoading(false); // Stop loading as no user is logged in
                return;
            }
            try {
                const token = await user.getIdToken();
                const headers = token ? { Authorization: `Bearer ${token}` } : {};

                //get store item data
                const storeItemResponse = await axios.get(`http://` + host + `:8080/store-items/${storeId}`, { headers });
                setStoreItem(storeItemResponse.data);

                //get user data
                const userResponse = await axios.get(`http://` + host + `:8080/users/${userId}`, { headers });
                setUserInfo(userResponse.data);
            } catch (e) {
                setStoreItem(e.message);
            } finally {
                setLoading(false); // Data has been fetched or failed
            }
        };

        if (user) {
            setLoading(true); // Start loading when fetching starts
            loadStoreItem();
        }
    }, [storeId, userId, user]);

    useEffect(() => {
        console.log("Current userID:", userId);  // Check the value of userId whenever the effect runs
    }, [user, userId, storeId]);  // Adjust dependencies as needed

    
    /* Stripe Payment stuff */
    const handleSubmit = async(e) => {

        e.preventDefault();
        const {error, paymentMethod} = await stripe.createPaymentMethod({
            type: "card",
            card: elements.getElement(CardElement)
        })


        if(!error) {
            try {
                const id = paymentMethod.id; 

                const token = await user.getIdToken();
                const headers = { Authorization: `Bearer ${token}` };
                const pointAmount = parseFloat(storeItem.item);

                console.log("Price is " + storeItem.itemPrice * 100);
                console.log("Payment id is " + id);

                const response = await axios.post(`http://` + host + `:8080/payment-request`, {
                    amount: storeItem.itemPrice * 100,
                    id: id,
                    userId: userId,
                    pointAmount: pointAmount
                }, { headers });

                if(response.data.success) {
                    console.log("Successful payment"); 
                    setSuccess(true); 
                }

                console.log(response.data);
            }
            catch (error) {
                console.log("Error", error); 
            }
        }
        else {
            console.log(error.message)
        }
    }


   return (
        <div className="create-store-container">
            <div className="create-store-white-box">
                {loading ? (
                    <p>Loading...</p>
                ) : (
                    !success ? (
                        <>
                            <h2> Purchasing {storeItem.item} for ${storeItem.itemPrice} </h2>
                            <form className="payment-form" onSubmit={handleSubmit}>
                                <fieldset className = "FormGroup">
                                    <div className = "FormRow">
                                        <CardElement options={CARD_OPTIONS} />
                                    </div>
                                </fieldset>
                                <br/><button className = "store-page-button" disabled={!storeItem}>Pay</button>
                            </form>
                        </>
                    ) : (
                        <div className = "successful-payment">
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