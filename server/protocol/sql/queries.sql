-- Validate User:
-- The returned value is expected 1.
-- Some queries don't need this validation.
select count(*) from Prisoners where id=? and pass=?;


-- Log In (1)
select * from Prisoners where username=? and pass=?;

-- Log In (2)
select * from Contacts where prisoner=?;

-- Sign Up (1):
-- Note: Unique constraint guarantees that username is not duplicated.
insert into Prisoners(`username`, `pass`, `name`, `info`)
values(?, ?, ?, ?);

-- Update (1):
-- Note: in JPA, the executeUpdate method returns the number of rows affected.
-- If the number is 0, it means id of password hash is invalid.
update Prisoners
set name=?, info=?
where id=?;

-- Update (2):
delete from Contacts where prisoner=?;

-- Update (3):
insert into Contacts(prisoner, type, data)
values (?, ?, ?)


-- Get Arests (1):
select * from Arests;

-- Get Arests (2):
-- Select all Jail IDs for each Arest:
select distinct jail from Periods where arest=?;

-- Create Arest (1)
-- Ensure arests do not intersect:
-- Parameter 1: 'start' field of the Arest being created.
-- Parameter 2: 'end' field of the Arest being created.
select id from Arests
where not (`end` < ? or `start` > ?);

-- Create Arest (2):
-- After validation:
insert into Arests(`prisoner`, `start`, `end`)
values(?, ?, ?);

