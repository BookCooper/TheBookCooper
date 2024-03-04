## Steps
1. **Clone the Repository**
   ```bash
   git clone <repository-url>
    ```
2. Start the PostgreSQL server

    Make sure docker is installed and then run the following command:
   ```bash
   docker compose up --build
   ```
3. Connect to the PostgreSQL server using dbeaver or any other SQL client
    Information to connect to the database:
    ```bash
    Host: localhost
    Port: 5555
    Database: thebookcooper
    User: BCdev
    Password: password
    ```
    If it doesnt work double check that database is created:
    ```bash
    docker exec -it thebookcooper-db psql -U BCdev -l
    psql -h localhost -p 5555 -U BCdev -d thebookcooper


4. Go to postman and test the following API endpoints:

    # API Documentation

    - [User Operations](#user-operations)
    - [Book Operations](#book-operations)
    - [Listing Operations](#listing-operations)
    - [Book Tag Operations](#book-tag-operations)
    - [Book Transaction Operations](#book-transaction-operations)
    - [Store Item Operations](#store-item-operations)
    - [Point Transaction Operations](#point-transaction-operations)
    - [Book Search Operations](#book-search-operations)

    ## User Operations

    ### Creating a New User

    - **POST** `http://localhost:8080/users/create`

    ```json
    {
        "userName": "test",
        "password": "password",
        "email": "test@gmail.com",
        "bBucksBalance": 1000.0
    }
    ```

    ### Updating a User

    - **PUT** `http://localhost:8080/users/update/{id}`

    ```json
    {
        "userName": "tester",
        "password": "passwords",
        "email": "tester@gmail.com",
        "bBucksBalance": 100000.0
    }
    ```

    ### Counting Users

    - **GET** `http://localhost:8080/users/count`

    ### Getting a User by ID

    - **GET** `http://localhost:8080/users/{id}`

    ### Deleting a User by ID

    - **DELETE** `http://localhost:8080/users/delete/{id}`

    Make sure that the `userId` is not in the `book_listings` table before deletion.

    ## Book Operations

    ### Creating a New Book

    - **POST** `http://localhost:8080/books/create`

    ```json
    {
        "title": "Everything you need to know about software engineering",
        "isbn": 12345678901,
        "publishDate": "2024-02-22",
        "author": "Chris Hong",
        "genre": "Amaaaazing",
        "bookCondition": "New",
        "price": 999.99
    }
    ```
    
    `note: A book with a valid ISBN number and price can be left blank and will be autofilled` 

    ### Updating a Book

    - **PUT** `http://localhost:8080/books/update/{id}`

    ```json
    {
        "title": "Everything you dont need to know about software engineering",
        "isbn": 11111111,
        "publishDate": "2020-02-20",
        "author": "Christopher Hong",
        "genre": "Cool",
        "bookCondition": "Old",
        "price": 999999.99
    }
    ```

    ### Counting Books

    - **GET** `http://localhost:8080/books/count`

    ### Getting a Book by ID

    - **GET** `http://localhost:8080/books/{id}`

    ### Deleting a Book by ID

    - **DELETE** `http://localhost:8080/books/delete/{id}`

    Make sure that the `bookId` is not in the `book_listings` table before deletion.

    ## Listing Operations

    ### Creating a New Listing

    - **POST** `http://localhost:8080/listings/create`

    ```json
    {
        "bookId": 1,
        "userId": 1,
        "listingStatus": "new"
    }
    ```

    ### Updating a Listing

    - **PUT** `http://localhost:8080/listings/update/{id}`

    Make sure the `bookId` and `userId` refer to existing records.

    ```json
    {
        "bookId": 2,
        "userId": 2,
        "listingStatus": "old"
    }
    ```

    ### Counting Listings

    - **GET** `http://localhost:8080/listings/count`

    ### Getting a Listing by ID

    - **GET** `http://localhost:8080/listings/{id}`

    ### Deleting a Listing by ID

    - **DELETE** `http://localhost:8080/listings/delete/{id}`

    ## Book Tag Operations

    ### Creating a New Book Tag

    - **POST** `http://localhost:8080/booktags/create`

    ```json
    {
        "tagName": "Science Fiction",
        "bookId": 1
    }
    ```

    ### Getting a Book Tag by ID

    - **GET** `http://localhost:8080/booktags/{id}`

    ### Getting Book Tags Count

    - **GET** `http://localhost:8080/booktags/count`

    ### Updating a Book Tag

    - **PUT** `http://localhost:8080/booktags/update/{id}`

    ```json
    {
        "tagName": "Fantasy",
        "bookId": 2
    }
    ```

    ### Deleting a Book Tag by ID

    - **DELETE** `http://localhost:8080/booktags/delete/{id}`

    ## Book Transaction Operations

    Make sure that the referenced `buyerId` and `sellerId` exist in the `users` table (userIds), and the `listingId` exists in the `book_listings` table.

    ### Creating a New Book Transaction

    - **POST** `http://localhost:8080/book-transactions/create`

    Make sure the `buyerId`, `sellerId`, and `listingId` refer to existing records.

    ```json
    {
        "buyerId": 1,
        "sellerId": 2,
        "listingId": 1,
        "transactionPrice": 250.00,
        "transactionStatus": "completed"
    }
    ```

    ### Getting a Book Transaction by ID

    - **GET** `http://localhost:8080/book-transactions/{id}`

    ### Getting Book Transactions Count

    - **GET** `http://localhost:8080/book-transactions/count`

    ### Updating a Book Transaction

    - **PUT** `http://localhost:8080/book-transactions/update/{id}`

    Make sure the `buyerId`, `sellerId`, and `listingId` refer to existing records.

    ```json
    {
        "buyerId": 1,
        "sellerId": 2,
        "listingId": 1,
        "transactionPrice": 300.00,
        "transactionStatus": "completed"
    }
    ```

    ### Deleting a Book Transaction by ID

    - **DELETE** `http://localhost:8080/book-transactions/delete/{id}`

    ## Store Item Operations

    ### Creating a New Store Item

    - **POST** `http://localhost:8080/store-items/create`

    ```json
    {
        "item": "CooperCombo",
        "item_price": 79.99,
        "special_offer": "Get 10% more B-Bucks when you purchase this item!",
        "item_description": "A special combo pack for Cooper students."
    }
    ```

    ### Getting a Store Item by ID

    - **GET** `http://localhost:8080/store-items/{id}`

    ### Getting Store Items Count

    - **GET** `http://localhost:8080/store-items/count`

    ### Updating a Store Item

    - **PUT** `http://localhost:8080/store-items/update/{id}`

    ```json
    {
        "item": "SuperCooperCombo",
        "item_price": 89.99,
        "special_offer": "Get 20% more B-Bucks when you purchase this item!",
        "item_description": "A super combo pack for Cooper students."
    }
    ```

    ### Deleting a Store Item by ID

    - **DELETE** `http://localhost:8080/store-items/delete/{id}`

    ## Point Transaction Operations

    Ensure that the `userId` referenced exists in the `users` table for all operations.

    ### Creating a New Point Transaction

    - **POST** `http://localhost:8080/point-transactions/create`

    ```json
    {
        "userId": 1,
        "transactionType": "Purchase",
        "amount": 100.00,
        "currentBalance": 900.00
    }
    ```

    ### Getting a Point Transaction by ID

    - **GET** `http://localhost:8080/point-transactions/{id}`

    ### Counting Point Transactions

    - **GET** `http://localhost:8080/point-transactions/count`

    ### Updating a Point Transaction

    - **PUT** `http://localhost:8080/point-transactions/update/{id}`

    Make sure the `userId` refers to an existing record.

    ```json
    {
        "userId": 1,
        "transactionType": "Refund",
        "amount": 50.00,
        "currentBalance": 950.00
    }
    ```

    ### Deleting a Point Transaction by ID

    - **DELETE** `http://localhost:8080/point-transactions/delete/{id}`

    ## Book Search Operations

    Before performing book search operations, make sure the `userId` referenced exists in the `users` table.

    ### Creating a New Book Search

    - **POST** `http://localhost:8080/book-searches/create`

    ```json
    {
        "userId": 1,
        "searchQuery": "Data Structures and Algorithms"
    }
    ```

    ### Getting a Book Search by ID

    - **GET** `http://localhost:8080/book-searches/{id}`

    ### Counting Book Searches

    - **GET** `http://localhost:8080/book-searches/count`

    ### Updating a Book Search

    - **PUT** `http://localhost:8080/book-searches/update/{id}`

    Ensure the `userId` refers to an existing record.

    ```json
    {
        "userId": 1,
        "searchQuery": "Introduction to Software Engineering"
    }
    ```

    ### Deleting a Book Search by ID

    - **DELETE** `http://localhost:8080/book-searches/delete/{id}`
