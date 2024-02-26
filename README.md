## The BookCooper Repository

Our project is titled _The BookCooper - Virtual Book Exchange Platform_. Our team consists of Alex Liu (EE’25), Aidan Cusa (EE’26), and ~~Isaac Schertz (EE’26)~~. 

The project goal is to create a simple marketplace for book lovers, while not having to purchase a new book everytime and get no value for their old purchases. Here users have the ability to earn points for sending in undesired books, and then use points to purchase new ones. These points, called Book Bucks (B-Bucks for short) can also be purchased with real money. This Facebook-like online marketplace exchange is tailored made for all types of books. _The BookCooper_ will start small, being an exclusive marketplace for Cooper affiliated individuals, though will eventually reach to every book lover in the world!

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

4. Go to postman and test the following:

    In the Post request, check the following URLs based on your local database:

    - Create a new user:
        - URL: http://localhost:8080/users/create
        - Body (raw JSON):
            ```json
            {
                "userName": "test",
                "password": "password",
                "email": "test@gmail.com",
                "bBucksBalance": 1000.0
            }
            ```
            

    - Create a new book:
        - URL: http://localhost:8080/books/create
        - Body (raw JSON):
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

    - Create a new listing (make sure that the userId and bookId exist in the users and book_listings table though or else you'll get an error):
        - URL: http://localhost:8080/listings/create
        - Body (raw JSON):
            ```json
            {
                "bookId": 1,
                "userId": 1,
                "listingStatus": "new"
            }
            ```

    In the Put request, check the following URLs based on your local database:

    - Update a book:
        - URL: http://localhost:8080/books/update/{id}
        - Body (raw JSON):
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

    - Update a user:
        - URL: http://localhost:8080/users/update/{id}
        - Body (raw JSON):
            ```json
            {
                "userName": "tester",
                "password": "passwords",
                "email": "tester@gmail.com",
                "bBucksBalance": 100000.0
            }
            ```
    - Update a listing:
        - URL: http://localhost:8080/listings/update/{id}
        - Body (raw JSON):
            ```json
            {
                "bookId": 2,
                "userId": 2,
                "listingStatus": "old"
            }
            ```

    In the Get request, check the following URLs based on your local database:

    - Count users:
        - URL: http://localhost:8080/users/count

    - Get user by ID:
        - URL: http://localhost:8080/users/{id}

    - Count books:
        - URL: http://localhost:8080/books/count

    - Get book by ID:
        - URL: http://localhost:8080/books/{id}

    - Count listings:
        - URL: http://localhost:8080/listings/count
    
    - Get listing by ID:
        - URL: http://localhost:8080/listings/{id}

    In the delete request, check the following URLs based on your local database:

    - Delete user by ID (Make sure that the userId is not in the book_listings table):
        - URL: http://localhost:8080/users/delete/{id}

    - Delete book by ID (Make sure that the bookId is not in the book_listings table):
        - URL: http://localhost:8080/books/delete/{id}

    - Delete listing by ID:
        - URL: http://localhost:8080/listings/delete/{id}

    
