create table `Prisoners` (
  `id` int primary key,
  `username` varchar(10),
  `pass` blob,
  `name` text,
  `info` text
);

create table `Contacts` (
  `id` int primary key,
  `prisoner` Int,
  `type` smallint,
  `data` text
);

create table `Arests` (
  `id` Int primary key,
  `prisoner` int,
  `start` bigint,
  `end` bigint
);

create table `Periods` (
  `arest` int,
  `start` bigint,
  `end` bigint,
  `jail` int,
  `cell` smallint,
  primary key (`arest`, `start`, `end`)
);

create table `Cells` (
  `jail` int,
  `number` smallint,
  `seats` smallint,
  primary key (`jail`, `number`)
);

create table `Jails` (
  `id` int primary key,
  `name` text
);

alter table `Contacts` add foreign key (`prisoner`) references `Prisoners` (`id`);

alter table `Arests` add foreign key (`prisoner`) references `Prisoners` (`id`);

alter table `Periods` add foreign key (`arest`) references `Arests` (`id`);

alter table `Periods` add foreign key (`jail`, `cell`) references `Cells` (`jail`, `number`);

alter table `Cells` add foreign key (`jail`) references `Jails` (`id`);