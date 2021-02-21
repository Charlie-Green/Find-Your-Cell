-- An additional test script.
-- Can be ran after test.sql to add more users to the database
-- and test interaction between users.


-- -------------------------------------------------------------------
-- Prisoner #2:

-- The password is "222"
insert into `Prisoners`(`username`, `pass`, `name`, `info`)
values(
    "simon",
    0x5f28f24f5520230fd1e66ea6ac649e9f9637515f516b2ef74fc90622b60f165eafca8f34db8471b85b9b4a2cdf72f75099ae0eb8860c4f339252261778d406eb,
    "Саймон",
    "Я второй тестовый пользователь"
);

insert into `Contacts`(`prisoner`, `type`, `data`)
values(2, 0, "+370 2 2334455");

insert into `Contacts`(`prisoner`, `type`, `data`)
values(2, 5, "vk.com/simondebug");


-- 08.11.2020 - 18.11.2020:
insert into `Arests`(`prisoner`, `start`, `end`)
values(2, 1604786400000, 1605650400000);

-- Окрестина ИВС, 3
insert into `ScheduleCellEntries`(`arest`, `jail`, `cell`)
values(3, 1, 3);

-- Окрестина ЦИП, 1
insert into `ScheduleCellEntries`(`arest`, `jail`, `cell`)
values(3, 2, 1);

-- 08.11.2020 - 11.11.2020: Окрестина ИВС, 3
insert into `Periods`(`arest`, `start`, `end`, `jail`, `cell`)
values(3, 1604786400000, 1605045600000, 1, 3);

-- 11.11.2020 - 18.11.2020: Окрестина ЦИП, 1
-- Intersects with Prisoner 1.
insert into `Periods`(`arest`, `start`, `end`, `jail`, `cell`)
values(3, 1605045600000, 1605650400000, 2, 1);


-- -------------------------------------------------------------------
-- Prisoner #3:

-- The password is "333"
insert into `Prisoners`(`username`, `pass`, `name`, `info`)
values(
    "john",
    0x5e3155774d39d97c5f9e17c108c2b3e0485a43ae34ebd196f61a6f8bf732ef71a49e5710594cfc7391db114edf99f5da3ed96ef1d6ca5e598e85f91bd41e7eeb,
    "Džonas",
    "Я никакой не Йонас, а тестовый пользователь Джонас!"
);

insert into `Contacts`(`prisoner`, `type`, `data`)
values(3, 2, "+370 60 555443");

insert into `Contacts`(`prisoner`, `type`, `data`)
values(3, 6, "fb.com/jonaspage");


-- 15.07.2020 - 30.07.2020:
insert into `Arests`(`prisoner`, `start`, `end`)
values(3, 1594760400000, 1596056400000);

-- Окрестина ЦИП, 2:
insert into `ScheduleCellEntries`(`arest`, `jail`, `cell`)
values(4, 2, 2);

-- Жодино, 4:
insert into `ScheduleCellEntries`(`arest`, `jail`, `cell`)
values(4, 3, 4);

-- 15.07.2020 - 21.07.2020: Жодино, 4
-- Intersects with Prisoner 1.
insert into `Periods`(`arest`, `start`, `end`, `jail`, `cell`)
values(4, 1594760400000, 1595278800000, 3, 4);

-- 21.07.2020 - 30.07.2020: Окрестина ЦИП, 2
insert into `Periods`(`arest`, `start`, `end`, `jail`, `cell`)
values(4, 1595278800000, 1596056400000, 2, 2);


-- 10.10.2020 - 22.10.2020:
insert into `Arests`(`prisoner`, `start`, `end`)
values(3, 1602277200000, 1603314000000);

-- Окрестина ИВС, 2
insert into `ScheduleCellEntries`(`arest`, `jail`, `cell`)
values(5, 1, 2);

-- 10.10.2020 - 22.10.2020: Окрестина ИВС, 2
insert into `Periods`(`arest`, `start`, `end`, `jail`, `cell`)
values(5, 1602277200000, 1603314000000, 1, 2);


-- -------------------------------------------------------------------
-- Prisoner #4:

-- The password is "444"
insert into `Prisoners`(`username`, `pass`, `name`, `info`)
values(
    "kate",
    0xa5e4209e841321ae706ee84b94b38088a18acc7643250e4bb0af543c9d7599a0854c8e08c2283ec0ee338806cca171206340a510c5c406beb6ec3b6f18150c4b,
    "Кацярына",
    "Бацькаўна"
);

insert into `Contacts`(`prisoner`, `type`, `data`)
values(4, 7, "instagram.com/kateofbatska");


-- 16.10.2020 - 26.10.2020
insert into `Arests`(`prisoner`, `start`, `end`)
values(4, 1602795600000, 1603663200000);

-- Окрестина ЦИП, 2
insert into `ScheduleCellEntries`(`arest`, `jail`, `cell`)
values(6, 2, 2);

-- 16.10.2020 - 20.10.2020: Окрестина ЦИП, 2
insert into `Periods`(`arest`, `start`, `end`, `jail`, `cell`)
values(6, 1602795600000, 1603141200000, 2, 2);