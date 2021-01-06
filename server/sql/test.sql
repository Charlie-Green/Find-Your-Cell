-- This script prepopulates the database with script data.
-- It should be run on a clear database
-- (no data should have been inserted before),
-- otherwise foreign key constraints will likely break.


-- ---------------------------------------------
-- Jails:
-- Cell counts are fake.

insert into Jails(`name`, `cells`)
values("Окрестина ИВС", 3);

insert into Jails(`name`, `cells`)
values("Окрестина ЦИП", 5);

insert into Jails(`name`, `cells`)
values("Жодино", 4);


-- Cells within Окрестина ИВС:

insert into Cells(`jail`, `number`, `seats`)
values(1, 1, 5);

insert into Cells(`jail`, `number`, `seats`)
values(1, 2, 4);

insert into Cells(`jail`, `number`, `seats`)
values(1, 3, 6);


-- Cells within Окрестина ЦИП:

insert into Cells(`jail`, `number`, `seats`)
values(2, 1, 2);

insert into Cells(`jail`, `number`, `seats`)
values(2, 2, 4);

insert into Cells(`jail`, `number`, `seats`)
values(2, 3, 6);

insert into Cells(`jail`, `number`, `seats`)
values(2, 4, 2);

insert into Cells(`jail`, `number`, `seats`)
values(2, 5, 4);


-- Cells within Жодино:

insert into Cells(`jail`, `number`, `seats`)
values(3, 1, 8);

insert into Cells(`jail`, `number`, `seats`)
values(3, 2, 12);

insert into Cells(`jail`, `number`, `seats`)
values(3, 3, 8);

insert into Cells(`jail`, `number`, `seats`)
values(3, 4, 10);


-- ---------------------------------------------
-- Profile:

insert into `Prisoners`(`username`, `pass`, `name`, `info`)
values("charl", "111", "Чарльз", "Я - тестовый пользователь.");

insert into `Contacts`(`prisoner`, `type`, `data`)
values(1, 1, "t.me/mytelega");

insert into `Contacts`(`prisoner`, `type`, `data`)
values(1, 3, "+375 44 2334455");


-- ---------------------------------------------
-- Arest #1:

-- 14.07.2020 - 24.07.2020:
insert into `Arests`(`prisoner`, `start`, `end`)
values(1, 1594674000000, 1595538000000);


-- Окрестина ИВС, 2
insert into `ScheduleCellEntries`(`arest`, `jail`, `cell`)
values(1, 1, 2);

-- Окрестина ЦИП, 1
insert into `ScheduleCellEntries`(`arest`, `jail`, `cell`)
values(1, 2, 1);

-- Жодино, 4
insert into `ScheduleCellEntries`(`arest`, `jail`, `cell`)
values(1, 3, 4);


-- 14.07.2020 - 16.07.2020: Окрестина ИВС, 2
insert into `Periods`(`arest`, `start`, `end`, `jail`, `cell`)
values(1, 1594674000000, 1594846800000, 1, 2);

-- 16.07.2020 - 21.07.2020: Жодино, 4
insert into `Periods`(`arest`, `start`, `end`, `jail`, `cell`)
values(1, 1594846800000, 1595278800000, 3, 4);

-- Note: 21.07.2020 - 24.07.2020 is not specified.


-- ---------------------------------------------
-- Arest #2:

-- 15.11.2020 - 27.11.2020:
insert into `Arests`(`prisoner`, `start`, `end`)
values(1, 1605391200000, 1606428000000);


-- Окрестина ЦИП, 1:
insert into `ScheduleCellEntries`(`arest`, `jail`, `cell`)
values(2, 2, 1);

-- Окрестина ИВС, 2:
insert into `ScheduleCellEntries`(`arest`, `jail`, `cell`)
values(2, 1, 2);

-- Жодино, 3:
insert into `ScheduleCellEntries`(`arest`, `jail`, `cell`)
values(2, 3, 3);


-- Note: 15.11.2020 - 16.11.2020 is not specified.

-- 16.11.2020 - 20.11.2020: Окрестина ЦИП, 1
insert into `Periods`(`arest`, `start`, `end`, `jail`, `cell`)
values(2, 1605477600000, 1605823200000, 2, 1);

-- 20.11.2020 - 27.11.2020: Жодино, 3
insert into `Periods`(`arest`, `start`, `end`, `jail`, `cell`)
values(2, 1605823200000, 1606428000000, 3, 3);