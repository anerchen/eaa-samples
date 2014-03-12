DROP TABLE people IF EXISTS;

CREATE TABLE people (
  id int primary key,
first_name varchar(255),
  last_name varchar(255),
  address varchar(255),
  city varchar(255),
  state varchar(2),
  zip_code varchar(10)
);

insert into people (id,first_name,last_name,address,city,state,zip_code)
  VALUES (1,'Albert','Smith','123 A Street','Cincinnati','OH','45202');
insert into people (id,first_name,last_name,address,city,state,zip_code)
  VALUES (2,'Bill','Smith','224 B Street','Cincinnati','OH','45207');
  