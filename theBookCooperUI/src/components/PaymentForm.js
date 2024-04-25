import React, {useState, useEffect} from "react";
import { CardElement, useElements, useStripe } from "@stripe/react-stripe-js";
import axios from "axios";
import { useParams } from 'react-router-dom';
import useUser from "../hooks/useUser";
import { useDetails } from '../hooks/useDetails';


const CARD_OPTIONS = {
	iconStyle: "solid",
	style: {
		base: {
			iconColor: "#c4f0ff",
			color: "#fff",
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

    /* get store item stuff*/
    useEffect(() => {
        const loadStoreItem = async () => {
            if (!user || !userId) {
                setStoreItem('No user logged in.');
                return;
            }
            try {
                const token = await user.getIdToken();
                const headers = token ? { Authorization: `Bearer ${token}` } : {};
                console.log(token)

                //get store item data
                const storeItemResponse = await axios.get(`/store-items/${storeId}`, { headers });
                setStoreItem(storeItemResponse.data)

                console.log("store item is: " + storeItem)
                console.log(storeItemResponse.data)
                console.log("store item id is: " + storeId)

                //get user data
                const userResponse = await axios.get(`/users/${userId}`, { headers });
                console.log("user id is: " + userId)

                setUserInfo(userResponse.data);
                console.log("User response " + userResponse.data);

                console.log(userInfo)
                
            } catch (e) {
                setStoreItem(e.message);
            }
        };

        if (user) {
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

                console.log("Price is " + storeItem.itemPrice * 100);
                console.log("Payment id is " + id);

                const response = await axios.post("/payment-request", {
                    amount: storeItem.itemPrice * 100,
                    id: id
                }, { headers });

                if(response.data.success) {
                    console.log("Successful payment"); 
                    setSuccess(true); 

                    const newBalance = userInfo.bbucksBalance + parseFloat(storeItem.item);
                    console.log("New Balance is:" + newBalance);

                    const updateResponse = await axios.put(`/users/update/${userId}`, {
                        userName: userInfo.userName,
                        email: userInfo.email,
                        bBucksBalance: newBalance
                    }, { headers });
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
        <>
            {!success ?
                <form onSubmit={handleSubmit}>
                    <fieldset className = "FormGroup">
                        <div className = "FormRow">
                            <CardElement options={CARD_OPTIONS} />
                        </div>
                    </fieldset>

                    <button disabled={!storeItem}>Pay</button>
                </form>
            :
                <div>
                    <h2> You have just purchased {storeItem.item} </h2>   
                    <h3> New B-Bucks Balance is {userInfo.bbucksBalance + parseFloat(storeItem.item)} </h3>
                </div>
            }
        </>

    );
}

export default PaymentForm; 
