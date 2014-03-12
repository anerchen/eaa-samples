DROP TABLE people IF EXISTS;

CREATE TABLE people ( 
  id int not null primary key,
  name varchar not null,
  city varchar not null,
  state varchar not null );
  
insert into people (id,name,city,state) VALUES ( 1, 'Test Person' ,'Oxford','Ohio');

DROP TABLE bookmarks IF EXISTS;

CREATE TABLE bookmarks( 
  id int not null primary key,
  person_id int not null,
  url varchar not null );
  
insert into bookmarks(id, person_id, url) VALUES( 1, 1, 'http://www.google.com' );
insert into bookmarks(id, person_id, url) VALUES( 2, 1, 'http://www.yahoo.com' );
insert into bookmarks(id, person_id, url) VALUES( 3, 1, 'http://www.msnbc.com' );


DROP TABLE app_keys IF EXISTS;
CREATE TABLE app_keys (
  table_name varchar primary key,
  next_id int not null 
 );
 
INSERT INTO app_keys ( table_name, next_id ) VALUES ( 'people', 2 );
INSERT INTO app_keys ( table_name, next_id ) VALUES ( 'bookmarks', 4 );