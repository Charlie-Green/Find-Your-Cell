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