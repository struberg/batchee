---
--- automatically executed by OpenEJB at started when creating jdbc/orderDB
---
CREATE TABLE Numbers (item  	INT, quantity  INT);
CREATE TABLE Orders (orderID	INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY, itemID	INT, quantity  INT);
CREATE TABLE Inventory(itemID	INT NOT NULL PRIMARY KEY, quantity	INT NOT NULL );

INSERT INTO Inventory VALUES (1, 100);
INSERT INTO Numbers VALUES (1, 10);
INSERT INTO Numbers VALUES (2, 10);
INSERT INTO Numbers VALUES (3, 10);
INSERT INTO Numbers VALUES (4, 10);
INSERT INTO Numbers VALUES (5, 10);
INSERT INTO Numbers VALUES (6, 10);
INSERT INTO Numbers VALUES (7, 10);
INSERT INTO Numbers VALUES (8, 10);
INSERT INTO Numbers VALUES (9, 10);
INSERT INTO Numbers VALUES (10, 10);
INSERT INTO Numbers VALUES (11, 10);
INSERT INTO Numbers VALUES (12, 10);
INSERT INTO Numbers VALUES (13, 10);
INSERT INTO Numbers VALUES (14, 10);
INSERT INTO Numbers VALUES (15, 10);
INSERT INTO Numbers VALUES (16, 10);
INSERT INTO Numbers VALUES (17, 10);
INSERT INTO Numbers VALUES (18, 10);
INSERT INTO Numbers VALUES (19, 10);
INSERT INTO Numbers VALUES (20, 10);
