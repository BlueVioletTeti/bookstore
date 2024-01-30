# **BookStore Application**

## Intro
This project was created to make the life of a bookstore owners easier 
via development of well-thought-out management system.
Simultaneously, clients of the bookstore would enjoy online shopping 
using easy navigation tools through the variety of books represented in the store.

## Technologies and tools used 

During development of the project were used such technologies and tools as: 
* Spring Boot, 
* Spring Boot Security, 
* Spring Data JPA, 
* Liquibase,
* Soft deleting,
* GlobalExceptionHandler,
* Data Validation,
* Pagination and sorting,
* Swagger,
* Docker, 
* Spring Boot Testing,
* Mockito,
* Postman.

## Controller functionalities

### _For User class_:
#### [POST]: register endpoint
Allows unauthorised users to register on the site.

#### [POST]: login endpoint
Allows registered users to log into the site.

### _For Book class_:
#### [GET]: get all book
Shows all books from the database. 
Available to users with the role USER.
#### [GET]: get a book by id
Shows a book by id if it is present in the database. 
Available to users with the role USER.
#### [POST]: create a book
Allows to create a new book in the database. 
Available to users with the role ADMIN.
#### [PUT]: update a book
Allows to update information about a particular book in the database.
Available to users with the role ADMIN.
#### [DELETE]: delete a book
Allows to delete a book from the database.
Available to users with the role ADMIN.

### _For Category class_:
#### [GET]: get all categories
Shows all book categories from the database.
Available to users with the role USER.
#### [GET]: get a category by id
Shows a book category by id if it is present in the database.
Available to users with the role USER.
#### [GET]: get all books by category id
Shows all books by book category id if it is present in the database.
Available to users with the role USER.
#### [POST]: create a category
Allows to create a new book category in the database.
Available to users with the role ADMIN.
#### [PUT]: update a category
Allows to update information about a particular book category in the database.
Available to users with the role ADMIN.
#### [DELETE]: delete a category
Allows to delete a book category from the database.
Available to users with the role ADMIN.

### _For ShoppingCart class_:
#### [GET]: get a shopping cart
Shows a shopping cart of the logged-in user.
Available to users with the role USER.
#### [POST]: add an item to the shopping cart
Allows to add an item to the shopping cart of the logged-in user.
Available to users with the role USER.
#### [PUT]: update quantity of an item
Allows to update quantity a particular item in the shopping cart of the logged-in user.
Available to users with the role USER.
#### [DELETE]: delete an item from the shopping cart
Allows to delete a particular item in the shopping cart of the logged-in user.
Available to users with the role USER.

### _For Order class_:
#### [GET]: get all orders
Shows history of all orders made by logged-in user.
Available to users with the role USER.
#### [POST]: place an order
Allows to place an order with the shopping cart content of the logged-in user.
Available to users with the role USER.
#### [GET]: get all order items
Allows to view all items in the particular order of the logged-in user.
Available to users with the role USER.
#### [GET]: get specific order item
Allows to view a particular order item in the particular order of the logged-in user.
Available to users with the role USER.
#### [PUT]: update order status
Allows to update status of the order.
Available to users with the role ADMIN.

## How to set up and use this project
- prerequisites: install the Docker, JDK, Maven,
- clone project repository,
- build the project by using `mvn clean package`,
- start the application by running `docker-compose up`,
- work with application using REST API via the tool of your choice (ex. Postman).

## Challenges and space for improvement
- extra functionality could be added, for instance, search for a book by its name 
- test coverage could be improved by adding test cases for repository, service, and controller layers
- the project needs to be deployed to the AWS

## Contributions
You are welcome to contribute to this project by commenting code at hands and sharing your ideas for improvement 
