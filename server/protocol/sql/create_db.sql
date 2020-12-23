create table `Prisoners` (
  `id` int not null primary key auto_increment,
  `username` varchar(10) not null,
  `pass` blob not null,
  `name` text not null,
  `info` text not null,
  unique (`username`)
);

create table `Contacts` (
  `prisoner` int not null,
  `type`smallint not null,
  `data`text not null,
  primary key(`prisoner`, `type`)
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

create table `Cells` (
  `jail` int not null,
  `number` smallint not null,
  `seats` smallint not null,
  primary key (`jail`, `number`)
);

create table `Jails` (
  `id` int not null primary key auto_increment,
  `name` text not null
);

alter table `Contacts` add foreign key (`prisoner`) references `Prisoners` (`id`);

alter table `Arests` add foreign key (`prisoner`) references `Prisoners` (`id`);

alter table `Periods` add foreign key (`arest`) references `Arests` (`id`);

alter table `Periods` add foreign key (`jail`, `cell`) references `Cells` (`jail`, `number`);

alter table `Cells` add foreign key (`jail`) references `Jails` (`id`);