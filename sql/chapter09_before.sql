DROP TABLE people IF EXISTS;
DROP TABLE purchases IF EXISTS;
DROP TABLE rewards IF EXISTS;
DROP TABLE plans IF EXISTS;

CREATE TABLE people (
  id int primary key,
  plan_id int
);
CREATE TABLE purchases (
  id int primary key,
  person_id int,
  created_at datetime,
  gallons float,
  reward_gallons float
);
CREATE TABLE rewards (
  id int primary key,
  person_id int,
  created_at datetime,
  gallons float
);
CREATE TABLE plans (
  id int primary key,
  purchase_requirement float,
  reward_gallons float
);

INSERT INTO plans(id,purchase_requirement,reward_gallons) VALUES( 1, 10.0, 1.0 );
INSERT INTO plans(id,purchase_requirement,reward_gallons) VALUES( 2, 15.0, 1.0 );

INSERT INTO people(id, plan_id) VALUES( 1, 1 );
INSERT INTO people(id, plan_id) VALUES( 2, 2 );