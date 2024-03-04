## The BookCooper Repository

Our project is titled _The BookCooper - Virtual Book Exchange Platform_. Our team consists of Alex Liu (EE’25), Aidan Cusa (EE’26), and ~~Isaac Schertz (EE’26)~~. 

The project goal is to create a simple marketplace for book lovers, while not having to purchase a new book everytime and get no value for their old purchases. Here users have the ability to earn points for sending in undesired books, and then use points to purchase new ones. These points, called Book Bucks (B-Bucks for short) can also be purchased with real money. This Facebook-like online marketplace exchange is tailored made for all types of books. _The BookCooper_ will start small, being an exclusive marketplace for Cooper affiliated individuals, though will eventually reach to every book lover in the world!

Creating a comprehensive README for your demo will guide users through the process of setting up and running the demo, ensuring they understand how to interact with your application. Below is a template for your README that you can customize as needed:

---

# The Book Cooper Demo

Welcome to the Book Cooper demo! This demo showcases the process of creating books and users, searching for textbooks, and simulating the purchase of textbooks using a virtual currency called B-Bucks. Follow the steps below to get started.

## Application Set Up

   ```bash
   docker compose up --build
   ```

### Prefill the Database

- **POST** `http://localhost:8080/books/create`

```json
{
  "title": "Advanced Programming in the UNIX Environment, 3rd Edition",
  "publishDate": "2013-05-24",
  "author": "Stevens, W. Richard & Rago, Stephen A.",
  "genre": "Computer Science",
  "bookCondition": "new",
  "isbn": 9780321637734,
  "price": 0 
}
```

- **POST** `http://localhost:8080/users/create`

```json
{
  "userName": "SuperDev",
  "password": "password",
  "email": "thebookcooper@gmail.com",
  "bBucksBalance": 10000000.0
}

```

### Scenario: User Buys a Textbook

#### 1. Create a User

- **POST** `http://localhost:8080/users/create`

```json
{
  "userName": "IneedOS",
  "password": "password",
  "email": "test@gmail.com",
  "bBucksBalance": 10.0
}

```
To reflect the updated functionality of automatically updating the current balance when a user buys B-Bucks, you can update the README section for buying B-Bucks as follows:

---

#### 2. Create a Store Item for B-Bucks

- **POST** `http://localhost:8080/store-items/create`

```json
{
  "item": "5000 B-Bucks",
  "item_price": 40.00, // in USD
  "special_offer": "Bonus B-Bucks with every purchase!",
  "item_description": "Get 1000 B-Bucks extra for buying this pack! Use B-Bucks to buy books!"
}
```

#### 3. Create a listing for the Operating Systems Textbook

- **POST** `http://localhost:8080/listings/create`

```json
{
  "sellerId": 2,
  "bookId": 1,
  "price": 5000.00, // in B-Bucks
  "condition": "new"
}
```

#### 4. Search for an Operating Systems Textbook

simulate searching for the Operating Systems textbook by its title.

- **GET** `http://localhost:8080/books/search?title=Advanced Programming in the UNIX Environment`

This GET request will return all listings that match the book title "Advanced Programming in the UNIX Environment". Make sure to encode the URL parameters correctly if the title includes spaces or special characters.

#### 5. User Purchases B-Bucks

To buy B-Bucks, the user makes a purchase transaction, which is automatically reflected in their account balance.

- **POST** `http://localhost:8080/point-transactions/create`

```json
{
  "userId": 1, 
  "transactionType": "Deposit", // Deposit or Purchase
  "amount": 5000.0 // B-Bucks to add/subtract
}
```
This transaction credits 6000 B-Bucks to the user's account, automatically updating their current balance to reflect the purchase. The balance is calculated based on the most recent balance available in the user's transaction history.

### 6. Buy Textbook

Finally, the user purchases the textbook. You can simulate a pending purchase followed by a completed purchase to demonstrate handling different transaction states.

#### Purchase Pending

- **POST** `http://localhost:8080/book-transactions/create`

```json
{
  "buyerId": 1,
  "sellerId": 2,
  "listingId": 1,
  "transactionPrice": 5000.00,
  "transactionStatus": "pending"
}
```

#### Purchase Completed

After the purchase is confirmed (e.g., the seller delivers the book), update the transaction to "completed".

- **PUT** `http://localhost:8080/book-transactions/update/{transactionId}`

Replace `{transactionId}` with the ID of the transaction you've created.

```json
{
  "transactionStatus": "completed"
}
```

