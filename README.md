# movie-search

The Java Project, which is a skeleton of all the features you need to comfortably watch movies alone and with friends. 
* Like and watch only top movies
* Find your friends
* Watch movies you and your friends like

![Filmorate drawio](https://user-images.githubusercontent.com/108021314/206771862-b7e8d819-8f5a-4ef2-80d6-bd9aca40fbea.png)

Examples of database accesses:

DELETE FROM films WHERE id = 2 CASCADE;

INSERT INTO friends_list (user_id, friend_id, friendship_status)
VALUES(2, 1, 'approved');
