
DROP TABLE people IF EXISTS;
create table people (
  id int not null primary key,
  name varchar );
  
DROP TABLE bookmarks IF EXISTS;
CREATE TABLE bookmarks( 
  id int not null primary key,
  person_id int not null,
  url varchar not null );
  
 
 insert into people( id, name ) VALUES( 1, 'Test Person');
  
insert into bookmarks(id, person_id, url) VALUES( 1, 1, 'http://www.google.com' );
insert into bookmarks(id, person_id, url) VALUES( 2, 1, 'http://www.yahoo.com' );
insert into bookmarks(id, person_id, url) VALUES( 3, 1, 'http://www.msnbc.com' );


insert into people( id, name ) VALUES( 2, 'Person Number 2');
  
insert into bookmarks(id, person_id, url) VALUES( 4, 2, 'http://www.google.com' );
insert into bookmarks(id, person_id, url) VALUES( 5, 2, 'http://www.yahoo.com' );
insert into bookmarks(id, person_id, url) VALUES( 6, 2, 'http://www.msnbc.com' );