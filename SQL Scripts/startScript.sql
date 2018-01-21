REM Create Tables

CREATE TABLE customers
(customer#  NUMBER(6),
lastName VARCHAR2(12) NOT NULL,
firstName VARCHAR2(12) NOT NULL,
address VARCHAR2(30),
city VARCHAR2(20),
state VARCHAR2(2),
zip VARCHAR2(5),
phone NUMBER(10),
email VARCHAR2(30),
membership NUMBER(1) ,
membership_exp DATE,
last_visit NUMBER(6),
last_gun_used VARCHAR2(20),
	CONSTRAINT customers_customer#_pk PRIMARY KEY(customer#));
	
CREATE TABLE Guns
(serial VARCHAR2(20),
manufacturer VARCHAR2(20) NOT NULL,
model VARCHAR2(30)  NOT NULL,
caliber VARCHAR2(16)  NOT NULL,
location VARCHAR2(20)  NOT NULL,
last_visit_used NUMBER(6),
	CONSTRAINT guns_serial_pk PRIMARY KEY(serial));
	
CREATE TABLE manufacturer
(company_name VARCHAR2(20),
contact_phone NUMBER(10),
address VARCHAR2(30),
city VARCHAR2(20),
state VARCHAR2(2),
zip VARCHAR2(5),
	CONSTRAINT manufacturer_company_name_pk PRIMARY KEY(company_name));
	
CREATE TABLE caliber
(caliber VARCHAR2(16),
	CONSTRAINT caliber_caliber_pk PRIMARY KEY(caliber));
	
CREATE TABLE ammo
(manufacturer VARCHAR2(20),
caliber VARCHAR2(16) NOT NULL,
model VARCHAR2(30),
qty_stocked NUMBER(6),
	CONSTRAINT ammo_model_pk PRIMARY KEY(model));
	
CREATE TABLE range_visit
(id# NUMBER(6),
date_of_visit DATE NOT NULL,
	CONSTRAINT range_visit_id#_pk PRIMARY KEY(id#));
	
REM Create Relationships

ALTER TABLE customers
	ADD CONSTRAINT customers_last_visit_fk FOREIGN KEY(last_visit)
	REFERENCES range_visit(id#);

ALTER TABLE customers
	ADD CONSTRAINT customers_last_gun_used_fk FOREIGN KEY(last_gun_used)
	REFERENCES guns(serial);
	
ALTER TABLE guns
	ADD CONSTRAINT guns_last_visit_used_fk FOREIGN KEY(last_visit_used)
	REFERENCES range_visit(id#);
	
ALTER TABLE guns
	ADD CONSTRAINT guns_caliber_fk FOREIGN KEY(caliber)
	REFERENCES caliber(caliber);
	
ALTER TABLE guns
	ADD CONSTRAINT guns_manufacturer_fk FOREIGN KEY(manufacturer)
	REFERENCES manufacturer(company_name);
	
ALTER TABLE ammo
	ADD CONSTRAINT ammo_manufacturer_fk FOREIGN KEY(manufacturer)
	REFERENCES manufacturer(company_name);
	
ALTER TABLE ammo
	ADD CONSTRAINT ammo_caliber_fk FOREIGN KEY(caliber)
	REFERENCES caliber(caliber);
	
REM Create Sequences

CREATE SEQUENCE customer_customer#_seq
INCREMENT BY 1
START WITH 1;

CREATE SEQUENCE range_visit_id#_seq
INCREMENT BY 1
START WITH 1;

REM Create Roles

CREATE ROLE owner;
GRANT CREATE SESSION TO owner;
GRANT dba to owner; 

CREATE ROLE manager;
GRANT CREATE SESSION TO manager;
GRANT select, insert, update, delete
	ON ammo
	TO manager;
GRANT select, insert, update, delete
	ON caliber
	TO manager;
GRANT select, insert, update, delete
	ON customers
	TO manager;
GRANT select, insert, update, delete
	ON guns
	TO manager;
GRANT select, insert, update, delete
	ON manufacturer
	TO manager;
GRANT select, insert, update, delete
	ON range_visit
	TO manager;
GRANT select
	ON system.CUSTOMER_CUSTOMER#_SEQ
	TO manager;
GRANT select
	ON system.RANGE_VISIT_ID#_SEQ
	TO manager;

	
CREATE ROLE retail;
GRANT CREATE SESSION TO retail;
GRANT select, update
	ON ammo
	TO retail;
GRANT select
	ON caliber
	TO retail;
GRANT select, insert, update
	ON customers
	TO retail;
GRANT select, update
	ON guns
	TO retail;
GRANT select
	ON manufacturer
	TO retail;
GRANT select, insert, update
	ON range_visit
	TO retail;
GRANT select
	ON system.CUSTOMER_CUSTOMER#_SEQ
	TO retail;
GRANT select
	ON system.RANGE_VISIT_ID#_SEQ
	TO retail;


