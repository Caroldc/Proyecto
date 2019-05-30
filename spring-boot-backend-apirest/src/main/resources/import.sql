/*Spring siempre mira si hay un archivo import para ejecutarlo*/
INSERT INTO regiones (id,nombre) VALUES (1,'Triana');
INSERT INTO regiones (id,nombre) VALUES (2,'Sevilla Este');
INSERT INTO regiones (id,nombre) VALUES (3,'Los Remedios');
INSERT INTO regiones (id,nombre) VALUES (4,'Cerro-Amate');
INSERT INTO regiones (id,nombre) VALUES (5,'Mairena');
INSERT INTO regiones (id,nombre) VALUES (6,'República Argentina');
INSERT INTO regiones (id,nombre) VALUES (7,'Ciudad Jardín');
INSERT INTO regiones (id,nombre) VALUES (8,'Viapol Center');

INSERT INTO clientes (region_id, nombre, apellido,email,create_at) VALUES(1,'Carolina','Daza','carolina@gmail.com','2018-10-01');
INSERT INTO clientes (region_id, nombre, apellido,email,create_at) VALUES(2,'Carlos','Sánchez','carlos@gmail.com','2018-10-21');
INSERT INTO clientes (region_id, nombre, apellido,email,create_at) VALUES(3,'Jose María','Agüero','josepa@gmail.com','2018-10-10');
INSERT INTO clientes (region_id, nombre, apellido,email,create_at) VALUES(4,'Jesus','Pajares','chus@gmail.com','2018-10-11');
INSERT INTO clientes (region_id, nombre, apellido,email,create_at) VALUES(5,'Monica','González','monica@gmail.com','2018-10-12');
INSERT INTO clientes (region_id, nombre, apellido,email,create_at) VALUES(6,'Jose María','Hernández','chema@gmail.com','2018-10-13');
INSERT INTO clientes (region_id, nombre, apellido,email,create_at) VALUES(7,'Laura','Damigo','laura@gmail.com','2018-10-12');
INSERT INTO clientes (region_id, nombre, apellido,email,create_at) VALUES(8,'Manuel','Torres','manuel@gmail.com','2018-10-20');
INSERT INTO clientes (region_id, nombre, apellido,email,create_at) VALUES(1,'Antonio','Paez','antoniop@gmailr.com','2018-10-18');
INSERT INTO clientes (region_id, nombre, apellido,email,create_at) VALUES(2,'Alberto','Cobo','alberto@gmail.com','2018-10-15');
INSERT INTO clientes (region_id, nombre, apellido,email,create_at) VALUES(3,'Andrés','Guillen','andres@gmail.com','2018-10-11');
/*Creamos algunos usuarios con sus roles*/
INSERT INTO usuarios (username,password, enabled,nombre,apellido,email) values ('carolina','$2a$10$mzHQM5UR5pOo1C/8a8u2NOywsG6oEW5UQQweUISqagzQXA57wYgx6',1,'Carolina','Daza','carolina@gmail.com');
INSERT INTO usuarios (username,password, enabled,nombre,apellido,email) values ('carlos','$2a$10$.u8jd6nMf7.Czq1ogvAFJeyk3wPNs5xNpMEpDPxmGWDkgTLLnohse',1,'Carlos','Sanchez','carlos@gmail.com');

INSERT INTO roles (nombre) values ('ROLE_USER');
INSERT INTO roles (nombre) values ('ROLE_ADMIN');

INSERT INTO usuarios_roles (usuario_id, role_id) values (1,2);
INSERT INTO usuarios_roles (usuario_id, role_id) values (1,1);
INSERT INTO usuarios_roles (usuario_id, role_id) values (2,1);

/*Tabla productos*/
INSERT INTO productos (nombre, precio, create_at) values ("Affinity Advance Feline Pollo",3.95,NOW());
INSERT INTO productos (nombre, precio, create_at) values ("Latas Gourmet Gold 24 x 85g",10.95,NOW());
INSERT INTO productos (nombre, precio, create_at) values ("Whiskas Multipack Carne",6.90,NOW());
INSERT INTO productos (nombre, precio, create_at) values ("Tapas herméticas Trixie",1.99,NOW());
INSERT INTO productos (nombre, precio, create_at) values ("True Origins Wild Kitten ",3.95,NOW());
INSERT INTO productos (nombre, precio, create_at) values ("True Origins Pure Kitten Pollo ",16.95,NOW());
INSERT INTO productos (nombre, precio, create_at) values ("True Origins Snacks salmon y arándanos ",1.95,NOW());
INSERT INTO productos (nombre, precio, create_at) values ("Leche en polvo San Dimas 250g ",7.25,NOW());
INSERT INTO productos (nombre, precio, create_at) values ("Sterilised sobre comida húmeda ",0.95,NOW());
INSERT INTO productos (nombre, precio, create_at) values ("Anti Hairball sobre comida húmeda ",0.95,NOW());
INSERT INTO productos (nombre, precio, create_at) values ("Arden Grange Feline Light  ",31.95,NOW());
INSERT INTO productos (nombre, precio, create_at) values ("Rascador Catshion Basic House ",20.99,NOW());
INSERT INTO productos (nombre, precio, create_at) values ("Bandeja Higienica Nova ",39.99,NOW());
INSERT INTO productos (nombre, precio, create_at) values ("Cuna Atelier Chess",22.95,NOW());
INSERT INTO productos (nombre, precio, create_at) values ("Arenero Moderna Top Cat ",26.99,NOW());
INSERT INTO productos (nombre, precio, create_at) values ("Transportín Outech ",34.95,NOW());

/*Facturas*/
INSERT INTO facturas (descripcion,observacion,cliente_id,create_at) values ("Factura",null,1,NOW());
INSERT INTO lineas_facturas (cantidad,factura_id,producto_id) values (1,1,1);
INSERT INTO lineas_facturas (cantidad,factura_id,producto_id) values (2,1,4);
INSERT INTO lineas_facturas (cantidad,factura_id,producto_id) values (1,1,2);
INSERT INTO lineas_facturas (cantidad,factura_id,producto_id) values (1,1,3);


INSERT INTO facturas (descripcion,observacion,cliente_id,create_at) values ("factura de prueba","probando los inserts desde el backend ",1,NOW());
INSERT INTO lineas_facturas (cantidad,factura_id,producto_id) values (4,2,1);