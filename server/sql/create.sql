-- MySQL script to create the server database.


-- -----------------------------------------------

drop table if exists `prisoners_relations`;
drop table if exists `contacts`;
drop table if exists `periods`;
drop table if exists `schedule_cell_entries`;
drop table if exists `arests`;
drop table if exists `prisoners`;
drop table if exists `cells`;
drop table if exists `jails`;


-- -----------------------------------------------

create table `prisoners` (
  `id` int not null primary key auto_increment,
  `username` varchar(48) not null,
  `pass` blob not null,
  `name` varchar(48) not null,
  `info` text not null,

  unique(`username`)
);

create table `prisoners_relations` (
  `p1` int not null,
  `p2` int not null,
  `rel` smallint not null,
  `jail` int not null,
  `cell` smallint not null,

  primary key(`p1`, `p2`)
);

create table `contacts` (
  `prisoner` int not null,
  `type` smallint not null,
  `data` text not null,

  primary key(`prisoner`, `type`)
);

create table `jails` (
  `id` int not null primary key auto_increment,
  `name` varchar(64) not null,
  `cells` smallint not null,

  unique (`name`)
);

create table `cells` (
  `jail` int not null,
  `number` smallint not null,
  `seats` smallint not null,
  primary key (`jail`, `number`)
);

create table `arests` (
  `id` int not null primary key auto_increment,
  `prisoner` int not null,
  `start` bigint not null,
  `end` bigint not null
);

create table `periods` (
  `arest` int not null,
  `start` bigint not null,
  `end` bigint not null,
  `jail` int not null,
  `cell` smallint not null,
  primary key (`arest`, `start`, `end`)
);

create table `schedule_cell_entries` (
  `arest` int not null,
  `jail` int not null,
  `cell` smallint not null,
  primary key (`arest`, `jail`, `cell`)
);


-- -----------------------------------------------

alter table `prisoners_relations` add foreign key (`p1`)            references `prisoners` (`id`);
alter table `prisoners_Relations` add foreign key (`p2`)            references `prisoners` (`id`);
alter table `prisoners_relations` add foreign key (`jail`, `cell`)  references `cells` (`jail`, `number`);

alter table `contacts`           add foreign key (`prisoner`)      references `prisoners` (`id`);

alter table `arests`             add foreign key (`prisoner`)      references `prisoners` (`id`);

alter table `cells`              add foreign key (`jail`)          references `jails` (`id`);

alter table `schedule_cell_entries` add foreign key (`arest`)        references `arests` (`id`);
alter table `schedule_cell_entries` add foreign key (`jail`, `cell`) references `cells` (`jail`, `number`);

alter table `periods` add foreign key (
    `arest`,
    `jail`,
    `cell`
) references `schedule_cell_entries` (`arest`, `jail`, `cell`);


-- -----------------------------------------------

-- TODO: Jails' cell counts are not real, neither are cells' seat counts.
-- Replace with actual numbers!

insert into `jails`(name, cells)
values("Окрестина ИВС", 4);

insert into `jails`(name, cells)
values("Окрестина ЦИП", 5);

insert into `jails`(name, cells)
values("Жодино", 9);

insert into `jails`(name, cells)
values("Барановичи", 8);

insert into `jails`(name, cells)
values("Могилёв", 6);


-- NOTE: Jail IDs must correspond to those assigned by the server to the Jails inserted before.
-- Here IDs (1, 11, 21, ...) are as typically assigned by Heroku's ClearDB database.

-- "Окрестина ИВС" Jail:
insert into `cells`(jail, number, seats)
values(1, 1, 5);
insert into `cells`(jail, number, seats)
values(1, 2, 5);
insert into `cells`(jail, number, seats)
values(1, 3, 8);
insert into `cells`(jail, number, seats)
values(1, 4, 7);

-- "Окрестина ЦИП" Jail:
insert into `cells`(jail, number, seats)
values(11, 1, 2);
insert into `cells`(jail, number, seats)
values(11, 2, 2);
insert into `cells`(jail, number, seats)
values(11, 3, 4);
insert into `cells`(jail, number, seats)
values(11, 4, 4);
insert into `cells`(jail, number, seats)
values(11, 5, 2);

-- "Жодино" Jail:
insert into `cells`(jail, number, seats)
values(21, 1, 8);
insert into `cells`(jail, number, seats)
values(21, 2, 8);
insert into `cells`(jail, number, seats)
values(21, 3, 6);
insert into `cells`(jail, number, seats)
values(21, 4, 10);
insert into `cells`(jail, number, seats)
values(21, 5, 10);
insert into `cells`(jail, number, seats)
values(21, 6, 8);
insert into `cells`(jail, number, seats)
values(21, 7, 12);
insert into `cells`(jail, number, seats)
values(21, 8, 12);
insert into `cells`(jail, number, seats)
values(21, 9, 10);

-- "Барановичи" Jail:
insert into `cells`(jail, number, seats)
values(31, 1, 4);
insert into `cells`(jail, number, seats)
values(31, 2, 4);
insert into `cells`(jail, number, seats)
values(31, 3, 10);
insert into `cells`(jail, number, seats)
values(31, 4, 10);
insert into `cells`(jail, number, seats)
values(31, 5, 10);
insert into `cells`(jail, number, seats)
values(31, 6, 8);
insert into `cells`(jail, number, seats)
values(31, 7, 8);
insert into `cells`(jail, number, seats)
values(31, 8, 10);

-- "Могилёв" Jail:
insert into `cells`(jail, number, seats)
values(41, 1, 10);
insert into `cells`(jail, number, seats)
values(41, 2, 8);
insert into `cells`(jail, number, seats)
values(41, 3, 7);
insert into `cells`(jail, number, seats)
values(41, 4, 8);
insert into `cells`(jail, number, seats)
values(41, 5, 7);
insert into `cells`(jail, number, seats)
values(41, 6, 8);