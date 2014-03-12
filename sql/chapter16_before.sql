
DROP TABLE TRANSACTIONS IF EXISTS;
DROP TABLE accounts IF EXISTS;

create table accounts (
  id int not null primary key,
  description varchar,
  balance float not null,
  version_number int not null,
  modified_by varchar
  );
  
  insert into accounts (id,description,balance,version_number,modified_by) VALUES (1, 'Checking', 100.00, 1, 'unknown' );
  
DROP TABLE locks IF EXISTS;
create table locks(
	locked_table varchar,
	locked_id int not null,
	owner_id varchar,
	primary key ( locked_table, locked_id )
);
