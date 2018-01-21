REM Correct Caliber field limit
ALTER TABLE system.ammo MODIFY caliber varchar2(16);
ALTER TABLE system.guns MODIFY caliber varchar2(16);

REM Correct role rights for sequences
GRANT select
	ON system.CUSTOMER_CUSTOMER#_SEQ
	TO manager;
GRANT select
	ON system.RANGE_VISIT_ID#_SEQ
	TO manager;
GRANT select
	ON system.CUSTOMER_CUSTOMER#_SEQ
	TO retail;
GRANT select
	ON system.RANGE_VISIT_ID#_SEQ
	TO retail;
	
REM added cost_per_round to ammo
alter table ammo add cost_per_round number(5,2) not null;

REM added trip_cost to range_trip
alter table range_visit add trip_cost number(10,2) not null;

REM change model field to 30 char
alter table ammo modify model varchar2(30);

REM change location in guns to not null
alter table guns modify location varchar2(20) not null;

REM change caliber in guns to not null
alter table caliber modify caliber varchar2(16) not null;

REM change manufacturer in guns to not null
alter table guns modify manufacturer varchar2(20) not null;

REM change model in guns to not null and 30 char
alter table guns modify model varchar2(30) not null;