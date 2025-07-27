USE AplicatieCinematograf
GO

CREATE FUNCTION dbo.ValidateString(@s VARCHAR(50))
RETURNS BIT
AS
BEGIN
	DECLARE @ret BIT = 0;
	IF LEN(@s) > 0 SET @ret = 1;
	RETURN @ret;
END;
GO

CREATE FUNCTION dbo.ValidateDurata(@d INT)
RETURNS BIT
BEGIN
	DECLARE @ret BIT = 0;
	IF @d > 0 SET @ret = 1;
	RETURN @ret;
END;
GO

CREATE FUNCTION dbo.ValidatePret(@pret INT)
RETURNS BIT
BEGIN
	DECLARE @ret BIT = 0;
	IF @pret > 0 SET @ret = 1;
	RETURN @ret;
END;
GO

CREATE FUNCTION dbo.ValidateNumarLocuri(@nl INT)
RETURNS BIT
AS
BEGIN
	DECLARE @ret BIT = 0;
	IF @nl BETWEEN 1 AND 219 SET @ret = 1; 
	RETURN @ret;
END;
GO

CREATE FUNCTION dbo.ValidateNumarSala(@ns INT)
RETURNS BIT
AS
BEGIN
	DECLARE @ret BIT = 0;
	SET @ret = 
		(SELECT COUNT(sId) FROM Sali
		WHERE numarSala = @ns);
	RETURN 1 - @ret;
END;
GO

CREATE OR ALTER PROCEDURE AddEcranizare @title VARCHAR(50), @numeDirector VARCHAR(50), @durata INT, @descriere VARCHAR(255), @pret INT, @numarLocuri INT, @numarSala INT, @oraStartFilm TIME(7) AS
BEGIN
	BEGIN TRAN
	BEGIN TRY

		IF (dbo.ValidateString(@title) <> 1 and dbo.ValidateString(@numeDirector) <> 1 and dbo.ValidateDurata(@durata) <> 1 and dbo.ValidateString(@descriere) <> 1 and dbo.ValidatePret(@pret) <> 1 and dbo.ValidateNumarLocuri(@numarLocuri) <> 1 and dbo.ValidateNumarSala(@numarSala) <> 1)
		BEGIN
			RAISERROR('Invalid parameters', 14, 1);
		END

		DECLARE @cId INT;
		SET @cId = (SELECT MAX(catId) FROM CategoriiFilme);

		INSERT INTO Filme(titlu, numeDirector, durata, descriere, pret, catId)
		VALUES (@title, @numeDirector, @durata, @descriere, @pret, @cId);

		INSERT INTO Sali(numarLocuri, numarSala)
		VALUES (@numarLocuri, @numarSala);

		DECLARE @filmId INT;
		DECLARE @salaId INT;
		SET @filmId = (SELECT MAX(fId) FROM Filme);
		SET @salaId = (SELECT MAX(sId) FROM Sali);

		INSERT INTO Ecranizari(fId, sId, oraStartFilm)
		VALUES (@filmId, @salaId, @oraStartFilm);

		COMMIT TRAN
		SELECT 'Transaction commited'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		SELECT 'Transaction rollback'
	END CATCH
END
GO

-- commit
SELECT * FROM Filme;
SELECT * FROM Sali;
SELECT * FROM Ecranizari;
EXEC AddEcranizare 'Harry Potter', 'Russo', 120, 'Un taram magic', 35, 100, 15, '12:30:00'
SELECT * FROM Filme;
SELECT * FROM Sali;
SELECT * FROM Ecranizari;

-- rollback
SELECT * FROM Filme;
SELECT * FROM Sali;
SELECT * FROM Ecranizari;
EXEC AddEcranizare 'Harry Potter', 'Russo', -10, '', 35, 400, 7, '12:30:00'
SELECT * FROM Filme;
SELECT * FROM Sali;
SELECT * FROM Ecranizari;
GO


CREATE OR ALTER PROCEDURE AddFilmSalaEcranizare @title VARCHAR(50), @numeDirector VARCHAR(50), @durata INT, @descriere VARCHAR(255), @pret INT, @numarLocuri INT, @numarSala INT, @oraStartFilm TIME(7) AS
BEGIN
	BEGIN TRAN
	BEGIN TRY

		IF (dbo.ValidateString(@title) <> 1 and dbo.ValidateString(@numeDirector) <> 1 and dbo.ValidateDurata(@durata) <> 1 and dbo.ValidateString(@descriere) <> 1 and dbo.ValidatePret(@pret) <> 1)
		BEGIN
			RAISERROR('Invalid parameters for movie', 14, 1);
		END

		DECLARE @cId INT;
		SET @cId = (SELECT MAX(catId) FROM CategoriiFilme);

		INSERT INTO Filme(titlu, numeDirector, durata, descriere, pret, catId)
		VALUES (@title, @numeDirector, @durata, @descriere, @pret, @cId);

		COMMIT TRAN
		SELECT 'Transaction commited film'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		SELECT 'Transaction rollback film'
	END CATCH

	BEGIN TRAN
	BEGIN TRY

		IF (dbo.ValidateNumarLocuri(@numarLocuri) <> 1 and dbo.ValidateNumarSala(@numarSala) <> 1)
		BEGIN
			RAISERROR('Invalid parameters for sala', 14, 1);
		END

		INSERT INTO Sali(numarLocuri, numarSala)
		VALUES (@numarLocuri, @numarSala);

		COMMIT TRAN
		SELECT 'Transaction commited sala'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		SELECT 'Transaction rollback sala'
	END CATCH

	BEGIN TRAN
	BEGIN TRY
		DECLARE @filmId INT;
		DECLARE @salaId INT;
		SET @filmId = (SELECT MAX(fId) FROM Filme);
		SET @salaId = (SELECT MAX(sId) FROM Sali);

		INSERT INTO Ecranizari(fId, sId, oraStartFilm)
		VALUES (@filmId, @salaId, @oraStartFilm);

		COMMIT TRAN
		SELECT 'Transaction commited ecranizare'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		SELECT 'Transaction rollback ecranizare'
	END CATCH
END
GO

-- commit
SELECT * FROM Filme;
SELECT * FROM Sali;
SELECT * FROM Ecranizari;
EXEC AddFilmSalaEcranizare 'Deadpool', 'Russo', 120, 'Super puteri', 45, 150, 19, '18:45:00'
SELECT * FROM Filme;
SELECT * FROM Sali;
SELECT * FROM Ecranizari;

-- rollback
SELECT * FROM Filme;
SELECT * FROM Sali;
SELECT * FROM Ecranizari;
EXEC AddFilmSalaEcranizare 'Deadpool2', 'Russo', 120, 'Super puteri', 35, 400, 7, '12:30:00'
SELECT * FROM Filme;
SELECT * FROM Sali;
SELECT * FROM Ecranizari;
GO