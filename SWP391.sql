DROP DATABASE swp391;
CREATE database swp391;
Use swp391;

CREATE TABLE users(
id int auto_increment primary key,
user_name varchar(50),
full_name nvarchar(100),
email varchar(50),
password varchar(255),
phone varchar(11),
address varchar(255),
image varchar(255),
description varchar(255),
otp varchar(255),
	
id_role int
);

CREATE TABLE role(
id int auto_increment primary key,
name varchar(50),
description varchar(255)
);

CREATE TABLE blog(

id int auto_increment primary key,
title varchar(100),
content text,
image varchar(255),
price double,
reason nvarchar(255),
status bit,
create_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
confirm bit,

id_user_created int,
id_blog_type int,
id_pet_category int,
id_pet_type int 
	
);

CREATE TABLE pet_type(
	id int auto_increment,
	type varchar(10),
	
	primary key(id)

);

CREATE TABLE blog_type(
id int auto_increment primary key,
name varchar(50)
	
);

CREATE TABLE booking(
id int auto_increment primary key,
total_price double,
paying_method varchar(10),
status bit,
create_date Date,
booking_date Date,
booking_time Time,	
id_user int,
id_blog int
	
);


CREATE TABLE comment(
id int auto_increment primary key,
description text,
create_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
rating int,
id_blog int,
id_user int
);

CREATE TABLE service_category(
    id int auto_increment primary key,
    name nvarchar(255)
);

CREATE TABLE service(
    id int auto_increment primary key,
    schedule nvarchar(255), 
    date_start Date,
    date_end Date,
    id_blog int,
    id_service_cate int
);

CREATE TABLE pet_category (
    id int auto_increment,
    name varchar(50),
    breed varchar(100),
    age int,
    color varchar(50),
    weight double,
    
    primary key(id)
);


CREATE TABLE request(
	id int auto_increment primary key,
	create_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	status bit,
	
	id_blog int,
	id_user int	
);


CREATE TABLE invoice(
    id int auto_increment primary key,
    invoice_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    total_amount double,
    id_user int,
    id_blog int,
    FOREIGN KEY (id_user) REFERENCES users(id),
    FOREIGN KEY (id_blog) REFERENCES blog(id)
);

CREATE TABLE users_notification(
	id int auto_increment primary key,
	id_receiver int,
	message nvarchar(255),
	create_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	
	
	id_sender int,
	id_request int
	);


ALTER TABLE users ADD CONSTRAINT FK_id_role_user FOREIGN KEY (id_role) REFERENCES role(id);
ALTER TABLE blog ADD CONSTRAINT FK_id_user_blog FOREIGN KEY (id_user_created) REFERENCES users(id);
ALTER TABLE blog ADD CONSTRAINT FK_id_pet_category_blog FOREIGN KEY (id_pet_category) REFERENCES pet_category(id);
ALTER TABLE blog ADD CONSTRAINT FK_id_pet_type_blog FOREIGN KEY (id_pet_type) REFERENCES pet_type(id);
ALTER TABLE blog ADD CONSTRAINT FK_blog_type_blog FOREIGN KEY (id_blog_type) REFERENCES blog_type(id);
ALTER TABLE booking ADD CONSTRAINT FK_id_blog_booking FOREIGN KEY (id_blog) REFERENCES blog(id);
ALTER TABLE booking ADD CONSTRAINT FK_id_user_booking FOREIGN KEY (id_user) REFERENCES users(id);
ALTER TABLE comment ADD CONSTRAINT FK_id_user_comment FOREIGN KEY (id_user) REFERENCES users(id);
ALTER TABLE comment ADD CONSTRAINT FK_id_blog_comment FOREIGN KEY (id_blog) REFERENCES blog(id);
ALTER TABLE service ADD CONSTRAINT FK_id_blog_service FOREIGN KEY (id_blog) REFERENCES blog(id);
ALTER TABLE service ADD CONSTRAINT FK_id_service_cate_service_category FOREIGN KEY (id_service_cate) REFERENCES service_category(id);
ALTER TABLE request ADD CONSTRAINT FK_id_blog_request FOREIGN KEY (id_blog) REFERENCES blog(id);
ALTER TABLE request ADD CONSTRAINT FK_id_user_request FOREIGN KEY (id_user) REFERENCES users(id);
ALTER TABLE users_notification ADD CONSTRAINT FK_id_user_users_notification FOREIGN KEY (id_sender) REFERENCES users(id);
ALTER TABLE users_notification ADD CONSTRAINT FK_id_request_users_notification FOREIGN KEY (id_request) REFERENCES request(id);
