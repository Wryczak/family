INSERT INTO family values(1, 'Wrycza',1);
INSERT INTO family values(2, 'Paluszewscy',2);

INSERT INTO user values(1, 'Krzys','12345',1,1);
INSERT INTO user values(2, 'Ryszard','12345',2,1);

INSERT INTO member values(1,'Krzysztof','Wrycza','1988-06-26',null, 2, 3,11,1,1);
INSERT INTO member values(2,'Ryszard','Wrycza','1988-06-26',null, 4, 3,11,1,1);
INSERT INTO member values(3,'Elżbieta','Wrycza','1988-06-26',null, 2, 3,11,1,1);
INSERT INTO member values(4,'Jan','Wrycza','1988-06-26',null, 2, 3,11,1,1);
INSERT INTO member values(5,'Magdalena','Wrycza','1988-06-26',null, 2, 3,11,1,1);
INSERT INTO member values(6,'Agata','Wrycza','1988-06-26',null, 2, 3,11,1,1);
INSERT INTO member values(7,'Małgorzata','Wrycza','1988-06-26',null, 2, 3,11,1,1);
INSERT INTO member values(8,'Michał','Wrycza','1988-06-26',null, 2, 3,11,1,1);
INSERT INTO member values(9,'Wiesław','Wrycza','1988-06-26',null, 2, 3,11,1,1);
INSERT INTO member values(10,'Bartosz','Wrycza','1988-06-26',null, 2, 3,11,1,1);

INSERT INTO member values(11,'Dobrosława','Paluszewska','1992-06-26',16 ,13 ,2);
INSERT INTO member values(12,'Bogusława','Paluszewska','2004-06-26', 16, 13,2);
INSERT INTO member values(13,'Katarzyna','Paluszewska','1969-06-26',null , null,2);
INSERT INTO member values(14,'Radosław','Paluszewski','1997-06-26',16, 13,2);
INSERT INTO member values(15,'Edyta','Paluszewska','1990-06-26', null,null ,2);
INSERT INTO member values(16,'Stojgniew','Paluszewski','1962-06-26', null,null ,2);

INSERT INTO family_members values(1,1);
INSERT INTO family_members values(1,2);
INSERT INTO family_members values(1,3);
INSERT INTO family_members values(1,4);
INSERT INTO family_members values(1,5);
INSERT INTO family_members values(1,6);
INSERT INTO family_members values(1,7);
INSERT INTO family_members values(1,8);
INSERT INTO family_members values(1,9);
INSERT INTO family_members values(1,10);

INSERT INTO family_members values(2,10);
INSERT INTO family_members values(2,11);
INSERT INTO family_members values(2,12);
INSERT INTO family_members values(2,13);
INSERT INTO family_members values(2,14);
INSERT INTO family_members values(2,15);
INSERT INTO family_members values(2,16);