create table People(
	id serial primary key,
	full_name varchar(255) not null,
	age int check(age>0),
	email varchar(255) unique,
	date_of_birts date,
	created_at timestamp

);
create table Books(
	id serial primary key,
	title varchar(255) not null,
	years int check(age>0),
	person_id bigint,
	foreign key (person_id) references Person (id)
);
