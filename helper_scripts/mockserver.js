// Import required modules
const express = require('express');

// Create an Express application
const app = express();

// Port where the mock server will run
const PORT = 3031;

// Define the endpoint
app.get('/endpoint', (req, res) => {
    const productId = req.query.id; // Extract the 'id' query parameter

    if (!productId) {
        return res.status(400).send('Missing product ID');
    }

    // Respond with a mock string response
    res.send(`Mock response for product ID: ${productId}`);
});

// Start the server
app.listen(PORT, () => {
    console.log(`Mock server is running on http://localhost:${PORT}`);
});