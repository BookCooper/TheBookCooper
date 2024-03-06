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

## Example workflow

### Create a user that just finished OS and wants to sell textbook

#### 0. Admin Creates a Store Item

- **POST** `http://localhost:8080/store-items/create`

```json
{
  "item": "1000 B-Bucks",
  "item_price": 10.00,
  "special_offer": "Bonus B-Bucks with every purchase!",
  "item_description": "Get 1000 B-Bucks for only $10. Use B-Bucks to buy books!"
}
```


#### 1. Create a User

- **POST** `http://localhost:8080/users/create`

```json
{
  "userName": "Finished OS",
  "password": "password",
  "email": "donewithos@gmail.com",
  "bBucksBalance": 10.0
}
```

#### 2. Create a book with an unknown price

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

#### 3. Search for an Operating Systems Textbook

simulate searching for the Operating Systems textbook by its title.

- **GET** `http://localhost:8080/books/search?title=Advanced Programming in the UNIX Environment`

This GET request will return all listings that match the book title "Advanced Programming in the UNIX Environment". Make sure to encode the URL parameters correctly if the title includes spaces or special characters.


#### 4. Create a listing for the Operating Systems Textbook

- **POST** `http://localhost:8080/listings/create`
```json
{
  "userId": 1,
  "bookId": 1,
  "price": 5100.00, // in B-Bucks
  "bookCondition": "new",
  "listingStatus": "listed"
}
```


### Scenario: Upcoming Junior needs OS textbook

#### 1. Create a User

- **POST** `http://localhost:8080/users/create`

```json
{
  "userName": "IneedOS",
  "password": "password",
  "email": "inneedforos@gmail.com",
  "bBucksBalance": 10.0
}

```

#### 2. User Purchases B-Bucks

To buy B-Bucks, the user makes a purchase transaction, which is automatically reflected in their account balance.

- **POST** `http://localhost:8080/point-transactions/create`

```json
{
  "userId": 2, 
  "transactionType": "Deposit", // Deposit or Purchase
  "amount": 5000.0 // B-Bucks to add/subtract
}
```
This transaction credits 5000 B-Bucks to the user's account, automatically updating their current balance to reflect the purchase. The balance is calculated based on the most recent balance available in the user's transaction history.

#### 3. User Searches for OS Textbook

- **GET** `http://localhost:8080/listings/search?title=Advanced Programming in the UNIX`

#### 4. User Tries to buy the OS Textbook

- **POST** `http://localhost:8080/book-transactions/create`

```json
{
  "buyerId": 2,
  "sellerId": 1,
  "listingId": 1,
  "transactionPrice": 5100.00,
  "transactionStatus": "pending"
}
```

If the user doesn't have enough B-Bucks, they will have to buy more (refer to step 2)!

#### 5. User checks B-Buck Balance

- **GET** `http://localhost:8080/users/{userId = 2}`

If the user has enough B-Bucks to purchase the book, go back to step 4 to purchase the book again. 


#### 6. Update a transaction

Once the transaction is made, its status can be set to "complete"

- **PUT** `http://localhost:8080/book-transactions/update/{transactionId = 1}`
```json
{
    "buyerId": 2,
    "sellerId": 1,
    "listingId": 1,
    "transactionPrice": 5100.00,
    "transactionStatus": "completed"
}
```

### Book with an unknown price and ISBN

Using the users already created,

#### 1. Create a Book

- **POST** `http://localhost:8080/books/create`

```json
{
  "title": "The Lemonade War",
  "publishDate": "2013-05-24",
  "author": "Davies",
  "genre": "Fiction",
  "bookCondition": "new",
  "isbn": 0,
  "price": 0 
}
```

#### 2. Create a Listing or Two

Send one or two (or three) HTTP requests using the information below to create listings for your new book.

- **POST** `http://localhost:8080/listings/create`

```json
{
  "userId": 2,
  "bookId": 2,
  "price": 1000.00, // in B-Bucks
  "bookCondition": "new",
  "listingStatus": "listed"
}
```

#### 3. View All of the Listings for Your Book Title

- **GET** `http://localhost:8080/listings/search?title=The Lemonade War`

#### 4. Remove a specific listing

- **DELETE** `http://localhost:8080/listings/delete/{listingId = 2}`


Functionality for all database tables such as create, update, count, delete, and viewing a specific ID is implemented though not explicitly featured in this demo to save time. 
