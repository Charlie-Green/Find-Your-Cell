-- MySQL script to create the server database.


-- -----------------------------------------------

drop table if exists `Contacts`;
drop table if exists `Periods`;
drop table if exists `ScheduleCellEntries`;
drop table if exists `Arests`;
drop table if exists `Prisoners`;
drop table if exists `Cells`;
drop table if exists `Jails`;


-- -----------------------------------------------

create table `Prisoners` (
  `id` int not null primary key auto_increment,
  `username` varchar(48) not null,
  `pass` blob not null,
  `name` varchar(48) not null,
  `info` text not null,

  unique(`username`)
);

create table `Contacts` (
  `prisoner` int not null,
  `type` smallint not null,
  `data` text not null,

  primary key(`prisoner`, `type`)
);

create table `Jails` (
  `id` int not null primary key auto_increment,
  `name` varchar(64) not null,
  `cells` smallint not null,

  unique (`name`)
);

create table `Cells` (
  `jail` int not null,
  `number` smallint not null,
  `seats` smallint not null,
  primary key (`jail`, `number`)
);

create table `Arests` (
  `id` int not null primary key auto_increment,
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

create table `ScheduleCellEntries` (
  `arest` int not null,
  `jail` int not null,
  `cell` smallint not null,
  primary key (`arest`, `jail`, `cell`)
);


alter table `Contacts` add foreign key (`prisoner`) references `Prisoners` (`id`);

alter table `Arests` add foreign key (`prisoner`) references `Prisoners` (`id`);

alter table `Cells` add foreign key (`jail`) references `Jails` (`id`);

alter table `ScheduleCellEntries` add foreign key (`arest`) references `Arests` (`id`);

alter table `ScheduleCellEntries` add foreign key (`jail`, `cell`) references `Cells` (`jail`, `number`);

alter table `Periods` add foreign key (`arest`, `jail`, `cell`) references `ScheduleCellEntries` (`arest`, `jail`, `cell`);