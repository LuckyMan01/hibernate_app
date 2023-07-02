create table People(
	id serial primary key,
	full_name varchar(255) not null,
	age int check(age>0),
	email varchar(255) unique,
	date_of_birts date,
	created_at timestamp

);
create table books(
	id serial primary key,
	title varchar(255) not null,
	years int check(years>0),
	author varchar(255),
	person_id bigint,
	time_on_rent timestamp,
	foreign key (person_id) references people (id)
);

drop table books