const { Client } = require('pg'); // PostgreSQL client
const { faker } = require('@faker-js/faker');// Faker package for generating random data

// Configure your PostgreSQL connection
const client = new Client({
  host: 'localhost', // Replace with your PostgreSQL host
  port: 5432,        // Default PostgreSQL port
  user: 'batchuser', // Replace with your PostgreSQL username
  password: 'batchpassword', // Replace with your PostgreSQL password
  database: 'batchdb', // Replace with your database name
});

client.connect();

const insertRandomReviews = async () => {
  const totalRecords = 100000;
  const userCount = 1000; // Adjust according to your user base size

  for (let i = 0; i < totalRecords; i++) {
    const productId = faker.number.int({ min: 1, max: 100 }); // Simulating 100 products
    const userId = faker.number.int({ min: 1, max: userCount }); // Random user
    const rating = faker.number.int({ min: 1, max: 5 });
    const reviewText = faker.lorem.sentence();

    const query = `
      INSERT INTO product_review (product_id, user_id, rating, review_text, created_at, is_processed)
      VALUES ($1, $2, $3, $4, CURRENT_TIMESTAMP, FALSE)
    `;

    const values = [productId, userId, rating, reviewText];

    try {
      await client.query(query, values);
    } catch (err) {
      console.error('Error inserting data: ', err);
    }
  }

  console.log(`Inserted ${totalRecords} reviews successfully!`);
  client.end();
};

insertRandomReviews();