USE AplicatieCinematograf;
GO

INSERT INTO Tables (Name)
VALUES  ('CategoriiFilme'),
		('Filme'),
		('Ecranizari');

SELECT * FROM Tables;
GO

CREATE VIEW FilmeView AS
	SELECT titlu, durata, pret
	FROM Filme
GO

SELECT * FROM FilmeView;
GO

CREATE VIEW CategoriiView AS
	SELECT titlu, categorie, pret
	FROM CategoriiFilme cf INNER JOIN Filme f ON cf.catId = f.catId
GO

SELECT * FROM CategoriiView;
GO

CREATE VIEW EcranizariView AS
	SELECT titlu, COUNT(oraStartFilm) AS numarEcranizari
	FROM Filme f INNER JOIN Ecranizari e ON f.fId = e.fId
	GROUP BY f.titlu;
GO

SELECT * FROM EcranizariView;
GO


INSERT INTO Views (Name)
VALUES	('CategoriiView'),
		('FilmeView'),
		('EcranizariView');

SELECT * FROM Views;
GO 


INSERT INTO Tests (Name)
VALUES	('DI_CategoriiFilme_CategoriiView'),
		('DI_Filme_FilmeView'),
		('DI_Ecranizari_EcranizariView');
SELECT * FROM Tests;
GO

INSERT INTO TestTables (TestID, TableID, NoOfRows, Position)
VALUES	(1, 1, 100, 1),
		(2, 1, 50, 1),
		(2, 2, 50, 2),
		(3, 1, 100, 1),
		(3, 2, 100, 2),
		(3, 3, 100, 3);

INSERT INTO TestTables (TestID, TableID, NoOfRows, Position)
VALUES	(1, 2, 100, 2),
		(2, 3, 50, 3);

INSERT INTO TestTables (TestID, TableID, NoOfRows, Position)
VALUES	(1, 3, 100, 3);


SELECT * FROM TestTables;
GO

INSERT INTO TestViews (TestID, ViewID)
VALUES	(1, 1),
		(2, 2),
		(3, 3);
		
SELECT * FROM TestViews;
GO

CREATE PROCEDURE insertCategoriiFilme @NoOfRows INT
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @categorie VARCHAR(50);
	DECLARE @n INT = 1;
	DECLARE @current_id INT = 1;
			--(SELECT MAX(cf.catId) FROM CategoriiFilme cf) + 1;

	WHILE @n <= @NoOfRows
	BEGIN
		SET @categorie = 'Categorie' + CONVERT(VARCHAR(10), @current_id);
		INSERT INTO CategoriiFilme (categorie)
		VALUES (@categorie);
		SET @current_id = @current_id + 1;
		SET @n = @n + 1;
	END

	PRINT 'S-au inserat ' + CONVERT(VARCHAR(10), @NoOfRows) + ' categorii'; 
END
GO
DROP PROCEDURE insertCategoriiFilme;
GO
EXEC insertCategoriiFilme 100;
GO

CREATE PROCEDURE insertFilme @NoOfRows INT
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @fk INT = 
			(SELECT MAX(cf.catId) FROM CategoriiFilme cf);
	DECLARE @titlu VARCHAR(50);
	DECLARE @numeDirector VARCHAR(50);
	DECLARE @durata INT;
	DECLARE @descriere VARCHAR(255);
	DECLARE @pret INT;
	DECLARE @n INT = 1;
	DECLARE @current_id INT = 1;
			--(SELECT MAX(f.fId) FROM Filme f) + 1;

	WHILE @n <= @NoOfRows
	BEGIN
		SET @titlu = 'titlu' + CONVERT(VARCHAR(10), @current_id);
		SET @numeDirector = 'director' + CONVERT(VARCHAR(10), @current_id);
		SET @durata = 120;
		SET @descriere = 'descriere' + CONVERT(VARCHAR(10), @current_id);
		SET @pret = 40;
		INSERT INTO Filme (titlu, numeDirector, durata, descriere, pret, catId)
		VALUES (@titlu, @numeDirector, @durata, @descriere, @pret, @fk);
		SET @current_id = @current_id + 1;
		SET @n = @n + 1;
	END

	PRINT 'S-au inserat ' + CONVERT(VARCHAR(10), @NoOfRows) + ' filme';
END
GO
DROP PROCEDURE insertFilme;
GO

CREATE PROCEDURE insertEcranizari @NoOfRows INT 
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @n INT = 0;
	DECLARE @fk1 INT;
	INSERT INTO Sali (numarLocuri, numarSala)
	VALUES (219, 9999);
	DECLARE @fk2 INT = 
			(SELECT s.sId FROM Sali s WHERE s.numarSala = 9999);
	DECLARE	@oraStartFilm TIME(7);
	
	DECLARE cursorFilme CURSOR FAST_FORWARD FOR
	SELECT f.fId FROM Filme f WHERE f.titlu LIKE 'titlu%';
	
	OPEN cursorFilme;
	
	FETCH NEXT FROM cursorFilme INTO @fk1;
	WHILE (@n < @NoOfRows) AND (@@FETCH_STATUS = 0)
	BEGIN
		SET @oraStartFilm = '01:23';
		INSERT INTO Ecranizari (fId, sId, oraStartFilm)
		VALUES (@fk1, @fk2, @oraStartFilm);
		SET @n = @n + 1;
		FETCH NEXT FROM cursorFIlme INTO @fk1;
	END

	CLOSE cursorFilme;
	DEALLOCATE cursorFilme;

	PRINT 'S-au inserat ' + CONVERT(VARCHAR(10), @n) + ' ecranizari';
END
GO
DROP PROCEDURE insertEcranizari;
GO

CREATE PROCEDURE insertTable @idTest INT
AS
BEGIN
	DECLARE @numeTest NVARCHAR(50) =
			(SELECT t.Name FROM Tests t WHERE t.TestID = @idTest);
	DECLARE @numeTabel NVARCHAR(50);
	DECLARE @NoOfRows INT;
	DECLARE @procedura VARCHAR(50);

	DECLARE cursorTabele CURSOR FORWARD_ONLY FOR
	SELECT ta.Name, te.NoOfRows 
	FROM TestTables te INNER JOIN Tables ta ON te.TableID = ta.TableID
	WHERE te.TestID = @idTest
	ORDER BY te.Position;

	OPEN cursorTabele;

	FETCH NEXT FROM cursorTabele INTO @numeTabel, @NoOfRows;
	WHILE @@FETCH_STATUS = 0
	BEGIN
		SET @procedura = 'insert' + @numeTabel;
		EXEC @procedura @NoOfRows;
		FETCH NEXT FROM cursorTabele INTO @numeTabel, @NoOfRows;
	END

	CLOSE cursorTabele;
	DEALLOCATE cursorTabele;
END
GO


CREATE PROCEDURE deleteCategoriiFilme
AS
BEGIN
	SET NOCOUNT ON;

	DELETE FROM CategoriiFilme;

	PRINT 'S-au sters ' + CONVERT(VARCHAR(10), @@ROWCOUNT) + ' categorii';
END
GO
DROP PROCEDURE deleteCategoriiFilme;
GO
EXEC deleteCategoriiFilme;
GO

CREATE PROCEDURE deleteFilme
AS
BEGIN
	SET NOCOUNT ON;

	DELETE FROM Filme;

	PRINT 'S-au sters ' + CONVERT(VARCHAR(10), @@ROWCOUNT) + ' filme';
END
GO
DROP PROCEDURE deleteFilme;
GO
EXEC deleteFilme;
GO

CREATE PROCEDURE deleteEcranizari
AS
BEGIN
	SET NOCOUNT ON;
	
	DELETE FROM Ecranizari;
	DELETE FROM Sali
	WHERE numarSala = 9999;

	PRINT 'S-au sters ' + CONVERT(VARCHAR(10), @@ROWCOUNT) + ' ecranizari';
END
GO
DROP PROCEDURE deleteEcranizari;
GO
EXEC deleteEcranizari;
GO

CREATE PROCEDURE deleteTable @idTest INT
AS
BEGIN
	DECLARE @numeTest NVARCHAR(50) =
			(SELECT t.Name FROM Tests t WHERE t.TestID = @idTest);
	DECLARE @numeTabel NVARCHAR(50);
	DECLARE @procedura VARCHAR(50);

	DECLARE cursorTabele CURSOR FORWARD_ONLY FOR
	SELECT ta.Name 
	FROM TestTables te INNER JOIN Tables ta ON te.TableID = ta.TableID
	WHERE te.TestID = @idTest
	ORDER BY te.Position DESC;

	OPEN cursorTabele;

	FETCH NEXT FROM cursorTabele INTO @numeTabel;
	WHILE @@FETCH_STATUS = 0
	BEGIN
		SET @procedura = 'delete' + @numeTabel;
		EXEC @procedura;
		FETCH NEXT FROM cursorTabele INTO @numeTabel;
	END

	CLOSE cursorTabele;
	DEALLOCATE cursorTabele;
END
GO

CREATE PROCEDURE selectView @idTest INT
AS
BEGIN
	DECLARE @viewName NVARCHAR(50) =
			(SELECT v.Name FROM Views v INNER JOIN TestViews tv on tv.ViewID = v.ViewID
			 WHERE tv.TestID = @idTest);
	DECLARE @select VARCHAR(50) = 'SELECT * FROM ' + @viewName;

	EXEC (@select);
END
GO


CREATE PROCEDURE runTest @idTest INT
AS
BEGIN
	DECLARE @ds DATETIME;
	DECLARE @di DATETIME;
	DECLARE @de DATETIME;

	SET @ds = GETDATE();

	EXEC deleteTable @idTest;
	EXEC insertTable @idTest;

	SET @di = GETDATE();

	EXEC selectView @idTest;

	SET @de = GETDATE();

	DECLARE @testName NVARCHAR(50) = 
			(SELECT t.Name FROM Tests t WHERE t.TestID = @idTest);
	INSERT INTO TestRuns (Description, StartAt, EndAt)
	VALUES (@testName, @ds, @de);

	DECLARE @viewID INT =
			(SELECT v.ViewID FROM Views v
			INNER JOIN TestViews tv ON tv.ViewID = v.ViewID
			WHERE tv.TestID = @idTest);
	DECLARE @tableID INT =
			(SELECT ta.TableID FROM Tests te
			INNER JOIN TestTables tt ON tt.TestID = te.TestID
			INNER JOIN Tables ta ON ta.TableID = tt.TableID
			WHERE tt.TestID = @idTest AND
			te.Name LIKE 'DI_' + ta.Name + '_%');
	DECLARE @testRunID INT =
			(SELECT MAX(tr.TestRunID) FROM TestRuns tr
			WHERE tr.Description = @testName);

	INSERT INTO TestRunTables (TestRunID, TableID, StartAt, EndAt)
	VALUES (@testRunID, @tableID, @ds, @di);
	INSERT INTO TestRunViews (TestRunID, ViewID, StartAt, EndAt)
	VALUES (@testRunID, @viewID, @di, @de);

	PRINT 'Testul a rulat pentru ' + CONVERT(VARCHAR(10), DATEDIFF(millisecond, @de, @ds)) + ' milisecunde';
END
GO
DROP PROCEDURE runTest;
GO

EXEC runTest 8;
GO

SELECT * FROM TestRuns;
SELECT * FROM TestRunTables;
SELECT * FROM TestRunViews;
DELETE FROM TestRuns;
GO

CREATE or ALTER PROCEDURE runTest2
AS
BEGIN
	DECLARE @ds DATETIME;
	DECLARE @de DATETIME;
	DECLARE @dsAll DATETIME = NULL;
	DECLARE @idTest INT = 1;
	DECLARE @tableID INT;
	DECLARE @viewID INT;

	INSERT INTO TestRuns (Description, StartAt, EndAt)
	VALUES ('Test Database', null, null);

	DECLARE @testRunID INT =
			(SELECT MAX(tr.TestRunID) FROM TestRuns tr);

	WHILE @idTest < 4
	BEGIN
		SET @ds = GETDATE();
		IF(@dsAll is NULL)
		BEGIN
			SET @dsAll = @ds;
		END;

		EXEC deleteTable @idTest;
		EXEC insertTable @idTest;

		SET @de = GETDATE();

		SET @tableID =
			(SELECT ta.TableID FROM Tests te
			INNER JOIN TestTables tt ON tt.TestID = te.TestID
			INNER JOIN Tables ta ON ta.TableID = tt.TableID
			WHERE tt.TestID = @idTest AND
			te.Name LIKE 'DI_' + ta.Name + '_%');
	
		INSERT INTO TestRunTables (TestRunID, TableID, StartAt, EndAt)
		VALUES (@testRunID, @tableID, @ds, @de);

		SET @idTest = @idTest + 1;
	END

	UPDATE TestRuns SET StartAt = @dsAll WHERE TestRunID = @testRunID;

	SET @idTest = 1;
	WHILE @idTest < 4
	BEGIN
		SET @ds = GETDATE();

		EXEC selectView @idTest;

		SET @de = GETDATE();

		SET @viewID =
			(SELECT v.ViewID FROM Views v
			INNER JOIN TestViews tv ON tv.ViewID = v.ViewID
			WHERE tv.TestID = @idTest);
	
		INSERT INTO TestRunViews (TestRunID, ViewID, StartAt, EndAt)
		VALUES (@testRunID, @viewID, @ds, @de);

		SET @idTest = @idTest + 1;
	END

	UPDATE TestRuns SET EndAt = @de WHERE TestRunID = @testRunID;	

	PRINT 'Testul a rulat pentru ' + CONVERT(VARCHAR(10), DATEDIFF(millisecond, @de, @dsAll)) + ' milisecunde';
END
GO

EXEC runTest2;
GO