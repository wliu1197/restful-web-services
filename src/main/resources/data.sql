/*
create table user_details(
	id bigint auto_increment primary key,
	name varchar(255) not null,
	password varchar(255) not null,
	birth_date timestamp,
	age bigint,
	cancelled timestamp
);
*/

insert into 
user_details (name,password,birth_date,age,cancelled) 
values('wen','comein22','1988-02-25',38,null);

insert into 
user_details (name,password,birth_date,age,cancelled) 
values('anna','comein22','1990-02-25',36,null);

insert into
post_details (description,user_details_id)
values('wen first message',1);
insert into
post_details (description,user_details_id)
values('wen second message',1);
insert into
post_details (description,user_details_id)
values('anna f message',2);
insert into
post_details (description,user_details_id)
values('anna second message',2);
