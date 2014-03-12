

DROP TABLE app_keys IF EXISTS;
CREATE TABLE app_keys (
  table_name varchar primary key,
  next_id int not null 
 );
 
INSERT INTO app_keys ( table_name, next_id ) VALUES ( 'people', 2);
INSERT INTO app_keys ( table_name, next_id ) VALUES ( 'bookmarks', 7 );

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


DROP TABLE employees IF EXISTS;
CREATE TABLE employees (  id int primary key, first_name varchar, last_name varchar );
DROP TABLE skills IF EXISTS;
CREATE TABLE skills (id int primary key, name varchar );
DROP TABLE employees_skills IF EXISTS;
CREATE TABLE employees_skills( employee_id int, skill_id int, primary key( employee_id, skill_id ) );

INSERT INTO employees (id,first_name,last_name) VALUES( 1, 'Lead', 'Programmer' );
INSERT INTO skills( id, name ) VALUES( 1, 'Java' );
INSERT INTO skills( id, name ) VALUES( 2, 'C++' );
INSERT INTO skills( id, name ) VALUES( 3, 'Virual Basic' );
INSERT INTO employees_skills( employee_id, skill_id ) VALUES ( 1, 1 );
INSERT INTO employees_skills( employee_id, skill_id ) VALUES ( 1, 2 );

INSERT INTO app_keys ( table_name, next_id ) VALUES ( 'employees', 4 );
INSERT INTO app_keys ( table_name, next_id ) VALUES ( 'skills', 3 );


DROP TABLE lobs IF EXISTS;
CREATE TABLE lobs ( 
  id int primary key,
  data LONGVARBINARY );
  INSERT INTO app_keys ( table_name, next_id ) VALUES ( 'lobs', 1 );
  
  
DROP TABLE players IF EXISTS;
CREATE TABLE players (
  id int primary key,
  class varchar,
  name varchar,
  batting_average int null,
  receptions int null
 );
INSERT INTO app_keys ( table_name, next_id ) VALUES ( 'players', 1 );
 
 
DROP TABLE cti_players IF EXISTS;
CREATE TABLE cti_players ( id int primary key, class varchar, name varchar );
DROP TABLE cti_football_players IF EXISTS;
CREATE TABLE cti_football_players ( id int primary key, receptions int not null );
DROP TABLE cti_baseball_players IF EXISTS;
CREATE TABLE cti_baseball_players ( id int primary key, batting_average int not null );


DROP TABLE con_football_players IF EXISTS;
DROP TABLE con_baseball_players IF EXISTS;
CREATE TABLE con_football_players ( id int primary key, name varchar, receptions int not null );
CREATE TABLE con_baseball_players ( id int primary key, name varchar, batting_average int not null );
 