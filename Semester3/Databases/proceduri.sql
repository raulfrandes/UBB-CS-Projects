USE AplicatieCinematograf;
GO

CREATE TABLE Versiune(
	numarVersiune INT
);

INSERT INTO Versiune (numarVersiune)
VALUES (0);

GO

CREATE PROCEDURE ModificaTipColoana
AS
BEGIN
ALTER TABLE Filme
ALTER COLUMN descriere NVARCHAR(300);

PRINT 'Tipul coloanei descriere a fost modificat'
END;
GO

CREATE PROCEDURE UndoModificaTipColoana
AS
BEGIN
ALTER TABLE Filme
ALTER COLUMN descriere VARCHAR(255);

PRINT 'Tipul coloanei descriere a fost modificat'
END;
GO

EXEC ModificaTipColoana;

EXEC UndoModificaTipColoana;
GO

CREATE PROCEDURE AdaugaConstrangereDefault
AS
BEGIN
ALTER TABLE Filme
ADD CONSTRAINT df_descriere DEFAULT 'Fara descriere.'
FOR descriere

PRINT 'Constrangerea default a fost adaugata coloanei descriere'
END;
GO

CREATE PROCEDURE UndoAdaugaConstrangereDefault
AS
BEGIN
ALTER TABLE Filme
DROP CONSTRAINT df_descriere

PRINT 'Constrangerea default a coloanei descriere a fost stearsa'
END;
GO

EXEC AdaugaConstrangereDefault;

EXEC UndoAdaugaConstrangereDefault;
GO

CREATE PROCEDURE CreeazaTabel
AS
BEGIN
CREATE TABLE Reduceri(
	redId INT PRIMARY KEY IDENTITY,
	codPromotie VARCHAR(50) UNIQUE,
	procentReducere INT NOT NULL
);

PRINT 'Tabelul Reduceri a fost creat'
END;
GO

CREATE PROCEDURE UndoCreeazaTabel
AS
BEGIN
DROP TABLE Reduceri;

PRINT 'Tabelul Reduceri a fost sters'
END;
GO

EXEC CreeazaTabel;

EXEC UndoCreeazaTabel;
GO

CREATE PROCEDURE AdaugaCamp
AS
BEGIN
ALTER TABLE Reduceri
ADD contId INT;

PRINT 'Campul contId a fost adaugat'
END;
GO

CREATE PROCEDURE UndoAdaugaCamp
AS
BEGIN
ALTER TABLE Reduceri
DROP COLUMN contId;

PRINT 'Campul contId a fost sters'
END;
GO

EXEC AdaugaCamp;

EXEC UndoAdaugaCamp;
GO

CREATE PROCEDURE CreeazaCheieStraina
AS
BEGIN
ALTER TABLE Reduceri
ADD CONSTRAINT fk_Reduceri FOREIGN KEY(contId) REFERENCES ConturiClienti(contId);

PRINT 'Constrangerea de cheie straina a fost adaugata coloanei contId'
END;
GO

CREATE PROCEDURE UndoCreeazaCheieStraina
AS
BEGIN
ALTER TABLE Reduceri
DROP CONSTRAINT fk_Reduceri;

PRINT 'Constragerea de cheie straina a coloanei contId a fost stearsa'
END;
GO

EXEC CreeazaCheieStraina;

EXEC UndoCreeazaCheieStraina;
GO

CREATE PROCEDURE ActualizeazaVersiunea @versiune VARCHAR(2)
AS
BEGIN

IF(@versiune <> '0' AND @versiune <> '1' AND @versiune <> '2' AND @versiune <> '3' AND @versiune <> '4' AND @versiune <> '5')
BEGIN
	RAISERROR('Versiune invalida: introduceti un numar de versiune de la 0 la 5!', 16, 1);
	RETURN;
END;

DECLARE @versiuneNoua AS INT;
SET @versiuneNoua = CAST(@versiune AS INT);

IF(@versiuneNoua < 0 OR @versiuneNoua > 5)
BEGIN
	RAISERROR('Versiune invalida: introduceti un numar de versiune de la 0 la 5!', 16, 1);
	RETURN
END;

DECLARE @versiuneCurenta AS INT;
SET @versiuneCurenta = (SELECT numarVersiune FROM Versiune);

IF(@versiuneCurenta = @versiuneNoua)
BEGIN
	PRINT 'Baza de date se afla deja la versiunea aceeasta';
	RETURN
END;

WHILE(@versiuneCurenta <> @versiuneNoua)
BEGIN
	IF(@versiuneCurenta < @versiuneNoua)
	BEGIN
		IF(@versiuneCurenta = 0)
			EXEC ModificaTipColoana;
		IF(@versiuneCurenta = 1)
			EXEC AdaugaConstrangereDefault;
		IF(@versiuneCurenta = 2)
			EXEC CreeazaTabel;
		IF(@versiuneCurenta = 3)
			EXEC AdaugaCamp;
		IF(@versiuneCurenta = 4)
			EXEC CreeazaCheieStraina;
		SET @versiuneCurenta = @versiuneCurenta + 1;
	END;
	IF(@versiuneCurenta > @versiuneNoua)
	BEGIN
		IF(@versiuneCurenta = 5)
			EXEC UndoCreeazaCheieStraina;
		IF(@versiuneCurenta = 4)
			EXEC UndoAdaugaCamp;
		IF(@versiuneCurenta = 3)
			EXEC UndoCreeazaTabel;
		IF(@versiuneCurenta = 2)
			EXEC UndoAdaugaConstrangereDefault;
		IF(@versiuneCurenta = 1)
			EXEC UndoModificaTipColoana;
		SET @versiuneCurenta = @versiuneCurenta - 1;
	END;
END;

UPDATE Versiune
SET numarVersiune = @versiuneNoua;

PRINT 'Baza de date a fost actualizata la versiunea ' + CAST(@versiuneNoua AS VARCHAR);

END;
GO

EXEC ActualizeazaVersiunea 5;

SELECT * FROM Versiune;

drop procedure ActualizeazaVersiunea
