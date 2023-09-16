alter table medicos add activo tinyint;
update medicos set activo = 1;
alter table medicos modify activo tinyint not null;