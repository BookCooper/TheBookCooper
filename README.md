## The BookCooper Repository

Our project is titled _The BookCooper - Virtual Book Exchange Platform_. Our team consists of Alex Liu (EE’25), Aidan Cusa (EE’26), and ~~Isaac Schertz (EE’26)~~. 

The project goal is to create a simple marketplace for book lovers, while not having to purchase a new book everytime and get no value for their old purchases. Here users have the ability to earn points for sending in undesired books, and then use points to purchase new ones. These points, called Book Bucks (B-Bucks for short) can also be purchased with real money. This Facebook-like online marketplace exchange is tailored made for all types of books. _The BookCooper_ will start small, being an exclusive marketplace for Cooper affiliated individuals, though will eventually reach to every book lover in the world!

Creating a comprehensive README for your demo will guide users through the process of setting up and running the demo, ensuring they understand how to interact with your application. Below is a template for your README that you can customize as needed:

---

Sure, here's the reorganized list in numerical order according to the ACM Software Engineering Code of Ethics:

---

## Software Engineering Ethics at The BookCooper

### 1. Respect privacy (1.6)
We implemented strict privacy controls to protect user data on our platform. The platform only collects necessary information with explicit user consent. Sensitive data, such as user passwords, are encrypted using industry-standard methods before being stored. This ensures that personal information is safeguarded against unauthorized access.

### 2. Honor confidentiality (1.7)
Our team adhered to confidentiality agreements to protect sensitive project information and user data. For instance, passwords are hashed using advanced cryptographic techniques before being stored in our Firebase database, ensuring that they cannot be reversed to plain text by unauthorized parties.

### 3. Promote no interest adverse to the employer or client (2.09)
In developing _The BookCooper_, we ensured that all platform features and updates aligned with the strategic interests of our stakeholders. We maintained transparency in our development process, regularly updating stakeholders on progress and any significant changes to project scope or objectives.

### 4. Accept and provide appropriate professional review (2.4)
Throughout the development of _The BookCooper_, we engaged in peer reviews and actively sought feedback from other developers to ensure the integrity and quality of our codebase. This collaborative approach facilitated early identification and resolution of potential issues, enhancing the robustness of our platform.

### 5. Maintain high standards of professional competence, conduct, and ethical practice (2.2)
For _The BookCooper_, we worked hard to keep our skills and knowledge up to date. Everyone on the team made sure to learn about the latest in software development to handle the project’s technical and ethical challenges. We also made sure everyone behaved professionally and ethically. This helped us make sure our work was not just good technically, but also right ethically.

### 6. Strive for high quality, acceptable cost, and a reasonable schedule (3.01)
Our development process was characterized by iterative testing and feedback loops with stakeholders, ensuring that the platform met stringent quality standards while adhering to predefined cost and schedule targets. This approach helped balance project demands with available resources, ensuring sustainable development practices.

### 7. Ensure an appropriate method is used for any project (3.05)
For _The BookCooper_, we selected Docker as the primary method for deployment to ensure consistency across different development and production environments. Docker provides a controlled and consistent environment, which helps in reducing discrepancies between different setups and supports our goal of a robust and scalable platform.

### 8. Ensure that specifications for software have been well documented (3.08)
A high priority was placed on comprehensive documentation throughout the development process. We utilized Swagger for API documentation, which helped future developers and users easily understand how to interact with the platform, significantly easing future enhancements and maintenance efforts.

### 9. Ensure realistic quantitative estimates of cost, scheduling, personnel, quality, and outcomes (3.09)
Transparent communication was maintained with all team members regarding project milestones, resource allocation, and budget constraints. This openness ensured that everyone involved was aligned with the project objectives and prepared for any adjustments that might be required.

### 10. Ensure adequate testing, debugging, and review of software (3.10)
We established comprehensive testing protocols, including unit tests, integration tests, and user acceptance testing, to minimize bugs and improve the user experience. These protocols were crucial in ensuring that the final product was both functional and user-friendly, enhancing overall satisfaction.

---

# The Book Cooper Demo

Welcome to the Book Cooper demo! This demo showcases the process of creating books and users, searching for textbooks, and simulating the purchase of textbooks using a virtual currency called B-Bucks. Follow the steps below to get started.

## Application Set Up

   ```bash
   docker compose up --build
   ```

## Example workflow

### Create a user that just finished OS and wants to sell textbook

#### 0. Admin Creates a Store Item (Created in the Backend)

```json
{
  "item": "1000 B-Bucks",
  "item_price": 10.00,
  "special_offer": "Bonus B-Bucks with every purchase!",
  "item_description": "Get 1000 B-Bucks for only $10. Use B-Bucks to buy books!"
}
```


#### 1. Sign up a user

User signs up with the following information:

username: JustFinishedOS
password: password
email: JustFinishedOS@gmail.com

#### 2. Login in the user

User logs into their account using the following information:

email: JustFinishedOS@gmail.com
password: password

#### 3. Create a book with an unknown price

User tries to create a book listing for the Operating Systems textbook, but the book does not exist in the database. So the user creates the book first. 

```json
{
   "title": "Advanced Programming in the UNIX Environment, 3rd Edition"
   "publishDate": "2013-05-24"
   "author": "Stevens, W. Richard & Rago, Stephen A."
   "genre": "Computer Science"
   "bookCondition": "new"
   "isbn": 9780321637734
   "price": 0 (The price will be filled automatically through API calls)
}
```

#### 4. Create a listing for the Operating Systems Textbook

User creates a listing for the Operating Systems textbook with the following information:

Book ID: 1
Book Condition: new
Price: 5100.00 B-Bucks (The price is determined by the user)

#### 5. Search for the Listing

User searches for the Operating Systems textbook by its title and can find their own listing.

Note: The User cannot buy their own listing.

### Scenario: Upcoming Junior needs OS textbook

#### 1. Create another user 

Another user signs up to buy the Operating Systems textbook with the following information:

username: IneedOS
password: password
email: ineedforos@gmail.com

#### 2. User Searches for OS Textbook

User searches for the Operating Systems textbook by its title.
And find the listing created by the user JustFinishedOS.

#### 3. User Buys B-Bucks

User tries to buy the Operating Systems textbook. However, the user does not have enough B-Bucks to purchase the book. So the user buys B-Bucks in the store. 

The user buys 5000 B-Bucks for $35.00 and input the following payment information:

Card Number: 4242 4242 4242 42424
Expiration Date: 12/34
CVV: 567
Zip: 89012

#### 4. User Tries to buy the OS Textbook Again

User search the textbook up again and tries to buy the Operating Systems textbook yet again. This time the user has enough B-Bucks to purchase the book.

#### 5. Transaction complete

If any user tries to search for the OS Textbook that was just bought, it will not show up in the search results.
