-- MySQL script to create the server database.


-- -----------------------------------------------

drop table if exists `Contacts`;
drop table if exists `Periods`;
drop table if exists `CellScheduleEntries`;
drop table if exists `Arests`;
drop table if exists `Prisoners`;
drop table if exists `Cells`;
drop table if exists `Jails`;


-- -----------------------------------------------

create table `Prisoners` (
  `id` int not null primary key,
  `username` varchar(48) not null,
  `pass` blob not null,
  `name` varchar(48) not null,
  `info` text not null
);

create table `Contacts` (
  `id` int not null primary key,
  `prisoner` int not null,
  `type` smallint,
  `data` text
);

create table `Arests` (
  `id` int not null primary key,
  `prisoner` int not null,
  `start` bigint not null,
  `end` bigint not null
);

create table `Periods` (
  `arest` int not null,
  `start` bigint not null,
  `end` bigint not null,
  `jail` int not null,
  `cell` smallint not null,
  primary key (`arest`, `start`, `end`)
);

create table `Cells` (
  `jail` int not null,
  `number` smallint not null,
  `seats` smallint not null,
  primary key (`jail`, `number`)
);

create table `CellScheduleEntries` (
  `arest` int not null,
  `jail` int not null,
  `cell` smallint not null,
  primary key (`arest`, `jail`, `cell`)
);

create table `Jails` (
  `id` int not null primary key,
  `name` varchar(64)
);


alter table `Contacts` add foreign key (`prisoner`) references `Prisoners` (`id`);

alter table `Arests` add foreign key (`prisoner`) references `Prisoners` (`id`);

alter table `Periods` add foreign key (`arest`) references `Arests` (`id`);

alter table `Periods` add foreign key (`jail`, `cell`) references `Cells` (`jail`, `number`);

alter table `Cells` add foreign key (`jail`) references `Jails` (`id`);

alter table `CellScheduleEntries` add foreign key (`arest`) references `Arests` (`id`);

alter table `CellScheduleEntries` add foreign key (`jail`, `cell`) references `Cells` (`jail`, `number`);