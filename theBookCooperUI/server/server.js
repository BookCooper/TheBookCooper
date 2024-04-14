const express = require('express');
const cors = require('cors');

require('dotenv').config(); 

const stripe = require('stripe')(process.env.STRIPE_SECRET_KEY); // Use the correct Stripe secret key

const app = express();

app.use(cors()); // Enable CORS
app.use(express.json()); // Middleware for parsing JSON bodies

// Handle payment request
app.post('/payment', async (req, res) => {
    const { amount, id } = req.body;

    try {
        const payment = await stripe.paymentIntents.create({
            amount,
            currency: 'USD',
            description: 'Your Product Description',
            payment_method: id,
            return_url: 'http://localhost:3001/payment_success',
            confirm: true
        });

        console.log('Payment', payment);
        res.json({ message: 'Payment successful', success: true });
    } catch (error) {
        console.log('Error', error);
        res.json({ message: 'Payment failed', success: false });
    }
});

const PORT = process.env.PORT || 3001;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});

