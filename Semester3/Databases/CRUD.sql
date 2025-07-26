USE AplicatieCinematograf
GO

------------------------ Sali ------------------------ 

CREATE FUNCTION dbo.TestNumarLocuri(@nl INT)
RETURNS BIT
AS
BEGIN
	DECLARE @ret BIT = 0;
	IF @nl BETWEEN 1 AND 219 SET @ret = 1; 
	RETURN @ret;
END;
GO


CREATE FUNCTION dbo.TestNumarSala(@ns INT)
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


CREATE OR ALTER PROCEDURE SaliCRUD
	@numarLocuri INT,
	@numarSala INT
AS
BEGIN
	SET NOCOUNT ON;

	IF (dbo.TestNumarLocuri(@numarLocuri) = 1 AND dbo.TestNumarSala(@numarSala) = 1)
	BEGIN
		INSERT INTO Sali (numarLocuri, numarSala)
		VALUES (@numarLocuri, @numarSala);

		SELECT * FROM Sali;

		UPDATE Sali SET numarLocuri = 50
		WHERE numarSala = @numarSala;

		DELETE FROM Sali
		WHERE numarSala = @numarSala;

		PRINT 'Operatiile CRUD pentru tabelul Sali';
	END
	ELSE
	BEGIN
		PRINT 'Eroare la efectuarea operatiilor CRUD pentru tabelul Sali';
		RETURN;
	END;
END;
GO


------------------------ Filme ------------------------

CREATE FUNCTION dbo.TestString(@s VARCHAR(50))
RETURNS BIT
AS
BEGIN
	DECLARE @ret BIT = 0;
	IF LEN(@s) > 0 SET @ret = 1;
	RETURN @ret;
END;
GO


CREATE FUNCTION dbo.TestDurata(@d INT)
RETURNS BIT
BEGIN
	DECLARE @ret BIT = 0;
	IF @d > 0 SET @ret = 1;
	RETURN @ret;
END;
GO


CREATE FUNCTION dbo.TestCatId(@c INT)
RETURNS BIT
BEGIN
	DECLARE @ret BIT = 0;
	SET @ret = 
		(SELECT COUNT(catId) FROM CategoriiFilme
		WHERE catId = @c);
	RETURN @ret;
END;
GO


CREATE OR ALTER PROCEDURE FilmeCRUD
	@titlu VARCHAR(50),
	@numeDirector VARCHAR(50),
	@durata INT,
	@descriere VARCHAR(255),
	@pret INT,
	@catId INT
AS
BEGIN
	SET NOCOUNT ON;

	IF (dbo.TestString(@titlu) = 1 AND dbo.TestString(@numeDirector) = 1 AND
		dbo.TestDurata(@durata) = 1 AND dbo.TestCatId(@catId) = 1)
	BEGIN
		INSERT INTO Filme(titlu, numeDirector, durata, descriere, pret, catId)
		VALUES (@titlu, @numeDirector, @durata, @descriere, @pret, @catId);

		SELECT * FROM Filme;

		UPDATE Filme SET descriere = 'Fara descriere'
		WHERE titlu = @titlu;

		DELETE FROM Filme
		WHERE titlu = @titlu;

		PRINT 'Operatiile CRUD pentru tabelul Filme';
	END
	ELSE
	BEGIN
		PRINT 'Eroare la efectuarea operatiilor CRUD pentru tabelul Filme';
		RETURN;
	END;
END;
GO


------------------------ Ecranizari ------------------------

CREATE FUNCTION dbo.TestFId(@f INT)
RETURNS BIT
BEGIN
	DECLARE @ret BIT = 0;
	SET @ret = 
		(SELECT COUNT(fId) FROM Filme
		WHERE fId = @f);
	RETURN @ret;
END;
GO


CREATE FUNCTION dbo.TestSId(@s INT)
RETURNS BIT
BEGIN
	DECLARE @ret BIT = 0;
	SET @ret = 
		(SELECT COUNT(sId) FROM Sali
		WHERE sId = @s);
	RETURN @ret;
END;
GO


CREATE OR ALTER PROCEDURE EcranicariCRUD
	@fId INT,
	@sId INT,
	@oraStartFilm TIME(7)
AS
BEGIN
	SET NOCOUNT ON;

	IF (dbo.TestFId(@fId) = 1 AND dbo.TestSId(@sId) = 1)
	BEGIN
		INSERT INTO Ecranizari(fId, sId, oraStartFilm)
		VALUES (@fId, @sId, @oraStartFilm);

		SELECT * FROM Ecranizari;

		UPDATE Ecranizari SET oraStartFilm = '12:30'
		WHERE fId = @fId AND sId = @sID;

		DELETE FROM Ecranizari
		WHERE fId = @fId AND sId = @sId;

		PRINT 'Operatiile CRUD pentru tabelul Ecranizari';
	END
	ELSE
	BEGIN
		PRINT 'Eroare la efectuarea operatiilor CRUD pentru tabelul Ecranizari';
		RETURN;
	END;
END;
GO


EXEC SaliCRUD 110, 12;
EXEC FilmeCRUD 'Wonka', 'Paul King', 116, 'Bazat pe una dintre cele mai bine vândute cărți pentru copii din toate timpurile, „Wonka”', 40, 4609;
EXEC EcranicariCRUD 3111, 1, '13:45';

GO

------------------------ Views ------------------------

CREATE OR ALTER VIEW SaliView
AS
	SELECT sId, numarLocuri FROM Sali
	WHERE numarLocuri > 100;
GO

IF EXISTS (SELECT NAME FROM sys.indexes WHERE name = 'N_idx_sali_numarLocuri')
DROP INDEX N_idx_sali_numarLocuri ON Sali
CREATE NONCLUSTERED INDEX N_idx_sali_numarLocuri ON Sali(numarLocuri);

SELECT * FROM SaliView;
GO


CREATE OR ALTER VIEW FilmeEcranizariView
AS
	SELECT f.fId, s.sId, e.oraStartFilm FROM Filme f
	INNER JOIN Ecranizari e ON e.fId = f.fId
	INNER JOIN Sali s ON s.sId = e.sId
	WHERE oraStartFilm > '14:00';
GO

IF EXISTS (SELECT NAME FROM sys.indexes	WHERE name = 'N_idx_ecranizari_fId')
DROP INDEX N_idx_ecranizari_fId ON Ecranizari
CREATE NONCLUSTERED INDEX N_idx_ecranizari_fId ON Ecranizari(fId);

IF EXISTS (SELECT NAME FROM sys.indexes WHERE name = 'N_idx_ecranizari_sId')
DROP INDEX N_idx_ecranizari_sId ON Ecranizari
CREATE NONCLUSTERED INDEX N_idx_ecranizari_sId ON Ecranizari(sId);

IF EXISTS (SELECT NAME FROM sys.indexes WHERE name = 'N_idx_ecranizari_oraStartFilm')
DROP INDEX N_idx_ecranizari_oraStartFilm ON Ecranizari
CREATE NONCLUSTERED INDEX N_idx_ecranizari_oraStartFilm ON Ecranizari(oraStartFilm);

SELECT * FROM FilmeEcranizariView