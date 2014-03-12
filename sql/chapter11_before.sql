
DROP TABLE favorites IF EXISTS;

CREATE TABLE favorites (
  id int not null primary key,
  url varchar not null,
   visits int not null
);

insert into favorites (id,url,visits) VALUES ( 1, 'http://www.google.com', 1 );

DROP TABLE people IF EXISTS;

CREATE TABLE people ( 
  id int not null primary key,
  name varchar not null );
  
insert into people (id,name) VALUES ( 1, 'Test Person' );

DROP TABLE bookmarks IF EXISTS;

CREATE TABLE bookmarks( 
  id int not null primary key,
  person_id int not null,
  url varchar not null );
  
insert into bookmarks(id,person_id,url) VALUES( 1, 1, 'http://www.google.com' );
insert into bookmarks(id,person_id,url) VALUES( 2, 1, 'http://www.yahoo.com' );
insert into bookmarks(id,person_id,url) VALUES( 3, 1, 'http://www.ask.com' );
insert into bookmarks(id,person_id,url) VALUES( 4, 1, 'http://www.cnn.com' );
insert into bookmarks(id,person_id,url) VALUES( 5, 1, 'http://www.msnbc.com' );
insert into bookmarks(id,person_id,url) VALUES( 6, 1, 'http://www.usatoday.com' );