USE AplicatieCinematograf
GO

-- Dirty Reads
SELECT * FROM Clienti;

BEGIN TRANSACTION
UPDATE Clienti 
SET dataNasterii='2000-05-20' WHERE cId=8;
WAITFOR DELAY '00:00:10';
ROLLBACK TRANSACTION


-- Non-Repeatable Reads
SELECT * FROM Clienti;

INSERT INTO Clienti(nume, dataNasterii)
VALUES ('Frandes Raul', '2003-05-19');
BEGIN TRAN
WAITFOR DELAY '00:00:10';
UPDATE Clienti SET nume='Frandes Raul-Sorin'
WHERE nume='Frandes Raul';
COMMIT TRAN

DELETE FROM Clienti WHERE nume='Frandes Raul-Sorin';
SELECT * FROM Clienti;


-- Phantom Reads
SELECT * FROM Clienti;

BEGIN TRAN
WAITFOR DELAY '00:00:10'
INSERT INTO Clienti(nume, dataNasterii)
VALUES ('Frandes Raul', '2003-05-19');
COMMIT TRAN

DELETE FROM Clienti WHERE nume='Frandes Raul';
SELECT * FROM Clienti;


-- Deadlock

INSERT INTO Clienti(nume, dataNasterii)
VALUES ('Client Deadlock', '2003-05-19');
INSERT INTO PreferinteClienti(numeActorPreferat, numeDirectorPreferat, genFilmPreferat, cId)
VALUES ('Preferinte Deadlock', 'Preferinte Deadlock', 'Preferinte Deadlock', 1);
SELECT * FROM Clienti;
SELECT * FROM PreferinteClienti;

SET DEADLOCK_PRIORITY HIGH
BEGIN TRAN
UPDATE Clienti SET nume='T1:Deadlock Clienti' WHERE dataNasterii='2003-05-19'
WAITFOR DELAY '00:00:10'
UPDATE PreferinteClienti SET numeActorPreferat='T1:Deadlock Preferinte' WHERE numeDirectorPreferat='Preferinte Deadlock';
COMMIT TRAN