CREATE DATABASE AplicatieCinematograf
GO
USE AplicatieCinematograf
GO

CREATE TABLE CategoriiFilme
(
	catId INT PRIMARY KEY IDENTITY,
	categorie VARCHAR(50) NOT NULL
);
DROP TABLE CategoriiFilme;

CREATE TABLE Filme
(
	fId INT PRIMARY KEY IDENTITY,
	titlu VARCHAR(50) NOT NULL,
	numeDirector VARCHAR(50) NOT NULL,
	durata INT CHECK(durata > 0),
	descriere VARCHAR(255),
	pret INT NOT NULL,
	catId INT FOREIGN KEY REFERENCES CategoriiFilme(catId)
);
DROP TABLE Filme;

CREATE TABLE Sali
(
	sId INT PRIMARY KEY IDENTITY,
	numarLocuri INT CHECK(numarLocuri > 0 AND numarLocuri < 220) NOT NULL,
	numarSala INT UNIQUE NOT NULL
);
DROP TABLE Sali;

CREATE TABLE Ecranizari
(
	fId INT FOREIGN KEY REFERENCES Filme(fId),
	sId INT FOREIGN KEY REFERENCES Sali(sId),
	oraStartFilm TIME NOT NULL,
	CONSTRAINT pk_Ecranizari PRIMARY KEY (fId, sId, oraStartFilm)
);
DROP TABLE Ecranizari;

CREATE TABLE Locuri
(
	lId INT PRIMARY KEY IDENTITY,
	rand INT CHECK(rand > 0 AND rand < 11) NOT NULL,
	tipScaun VARCHAR(50) CHECK(tipScaun = 'normal' OR tipScaun = 'premium' OR tipScaun = 'vip'),
	numarLoc INT NOT NULL,
	sId INT FOREIGN KEY REFERENCES Sali(sId)
);
DROP TABLE Locuri;

CREATE TABLE Clienti
(
	cId INT PRIMARY KEY IDENTITY,
	nume VARCHAR(50) NOT NULL,
	dataNasterii DATE
);
DROP TABLE Clienti;

CREATE TABLE PreferinteClienti
(
	prefId INT PRIMARY KEY IDENTITY,
	numeActorPreferat VARCHAR(50),
	numeDirectorPreferat VARCHAR(50),
	genFilmPreferat VARCHAR(50),
	cId INT FOREIGN KEY REFERENCES Clienti(cId)
);
DROP TABLE PreferinteClienti;

CREATE TABLE ConturiClienti
(
	contId INT PRIMARY KEY IDENTITY,
	username VARCHAR(50) UNIQUE NOT NULL,
	parola VARCHAR(50) UNIQUE NOT NULL,
	email VARCHAR(50) UNIQUE NOT NULL,
	cId INT FOREIGN KEY REFERENCES Clienti(cId)
);
DROP TABLE ConturiClienti;

CREATE TABLE Rezervari
(
	rId INT PRIMARY KEY IDENTITY,
	achitata BIT NOT NULL,
	activa BIT NOT NULL,
	contId INT FOREIGN KEY REFERENCES ConturiClienti(contId)
);
DROP TABLE Rezervari;

CREATE TABLE LocuriRezervate
(
	rId INT FOREIGN KEY REFERENCES Rezervari(rId),
	lId INT FOREIGN KEY REFERENCES Locuri(lId),
	oraRezervare TIME NOT NULL,
	CONSTRAINT pk_LocuriRezervate PRIMARY KEY (rId, lId, oraRezervare)
);
DROP TABLE LocuriRezervate;