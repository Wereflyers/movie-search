# movie-search

The Java Project, which is a skeleton of all the features you need to comfortably watch movies alone and with friends. 
* Like and watch only top movies
* Find your friends
* Watch movies you and your friends like

## Features

* SpringBoot
* Maven
* JDBC
* H2

## How to install

1. Download and install Java 11
2. Download and install Maven
3. Clone this repo

## How to use

1. Run maven-package
2. Run FilmorateApplication
3. Open http://localhost:8080
4. In order to execute the desired method, it is necessary to write the required endpoint.    
   For example, GET http://localhost:8080/users/1 allows you to find a user by id.

## Plans for future

:black_square_button: Connect Docker to save db in container    
:black_square_button: Add JPA and Hibernate    
:black_square_button: Code style    

### Database schema
    

![Filmorate drawio](https://user-images.githubusercontent.com/108021314/206771862-b7e8d819-8f5a-4ef2-80d6-bd9aca40fbea.png)

Examples of database accesses:

DELETE FROM films WHERE id = 2 CASCADE;

INSERT INTO friends_list (user_id, friend_id, friendship_status)
VALUES(2, 1, 'approved');
