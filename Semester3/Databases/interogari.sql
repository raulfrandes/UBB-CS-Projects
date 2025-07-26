USE AplicatieCinematograf
GO

INSERT INTO CategoriiFilme(categorie) VALUES 
	('aventura'),
	('actiune'),
	('crima'),
	('horror'),
	('animatie'),
	('comedie'),
	('drama'),
	('documentar');

SELECT * FROM CategoriiFilme;
DELETE FROM CategoriiFilme;

INSERT INTO Filme(titlu, numeDirector, durata, descriere, pret, catId) VALUES	
	('Argonuts', 'David Alaux', 95, 'Viața liniștită din Yolcos, un frumos și prosper oraș port din Grecia antică, este tulburată într-o zi de furia lui Poseidon.', 20, 4609),
	('Freelance', 'Pierre Morel', 109, 'Un fost agent al forțelor speciale acceptă să se ocupe de siguranța unei jurnaliste în timp ce intervievează un dictator, dar, sunt nevoiți să se adăpostească în junglă și să lupte pentru supraviețuire.', 30, 4610),
	('The Creator', 'Gareth Edwards', 135, 'The Creator este un thriller post-apocaliptic care se desfășoară într-un viitor afectat de războiul dintre oameni și inteligența artificială.', 40, 4610),
	('Killers of the Flower Moon', 'Martin Sorsese', 206, 'Mai mulți membri ai tribului amerindian Osage sunt uciși în împrejurări misterioase în anii 20, iar FBI-ul, sub coordonarea directă a lui J. Edgar Hoover, începe o amplă investigație.', 40, 4611),
	('The Exorcist: Believer', 'David Gordon Green', 111, 'Continuare a filmului din 1973 despre o fetiță de 12 ani care este posedată de o entitate demonică misterioasă și mama ei care se vede obligată să ceară ajutorul a doi preoți pentru a o salva.', 20, 4612),
	('The Canterville Ghost', 'Kim Burdon', 89, 'De 300 de ani, biata fantomă a lui Sir Simon de Canterville își bântuie în zadar conacul în așteptarea unui descendent curajos care să-l elibereze de un blestem prin săvârșirea unei fapte curajoase.', 30, 4613),
	('Five Nights at Freeddys', 'Emma Tammi', 109, 'Un agent de pază se angajează la Freddy Fazbears Pizza. Pe măsură ce orele primei sale ture de noapte trec, bărbatul realizează că s-a angajat pentru mai mult decât poate duce.', 40, 4612),
	('Inca doua lozuri', 'Paul Negoescu', 86, 'Sile iese din închisoare cu ideea să se îmbogățească minând cryptomonede.', 20, 4614);

SELECT * FROM Filme;
DELETE FROM Filme;

INSERT INTO Sali(numarLocuri, numarSala) VALUES
	(66, 5),
	(174, 1),
	(124, 8),
	(174, 2),
	(217, 7),
	(207, 6),
	(200, 9),
	(66, 10);	   
				   
SELECT * FROM Sali;
DELETE FROM Sali;

INSERT INTO Ecranizari(fId, sId, oraStartFilm) VALUES 
	(1, 1, '11:50'),
	(1, 1, '14:10'),
	(2, 2, '19:30'),
	(3, 3, '17:20'),
	(4, 4, '13:20'),
	(4, 4, '17:20'),
	(5, 3, '20:10'),
	(6, 2, '17:30'),
	(7, 5, '20:20');

SELECT * FROM Ecranizari;
DELETE FROM Ecranizari;

INSERT INTO Locuri(rand, tipScaun, numarLoc, sId) VALUES
	(8, 'normal', 9, 2),
	(7, 'normal', 10, 7),
	(1, 'VIP', 6, 5),
	(8, 'premium', 5, 6),
	(3, 'VIP', 5, 5),
	(8, 'normal', 1, 1),
	(7, 'normal', 16, 5),
	(5, 'VIP', 15, 4),
	(9, 'VIP', 16, 2),
	(10, 'premium', 12, 2);

SELECT * FROM Locuri; 
DELETE FROM Locuri;

INSERT INTO Clienti(nume, dataNasterii) VALUES 
	('Cosmin Lupu', '2004-01-23'),
	('Ovidiu Moldoveanu', '1987-04-03'),
	('Stefania Calin', '1996-02-28'),
	('Diana Todica', '2004-08-31'),
	('Narcisa Nita', '1997-04-03'),
	('Eugen Popa', '2000-05-22'),
	('Laurentiu Stanescu', '2005-03-13'),
	('Alberto Cristian', '1985-05-20');

SELECT * FROM Clienti;
DELETE FROM Clienti;

INSERT INTO PreferinteClienti(numeActorPreferat, numeDirectorPreferat, genFilmPreferat, cId) VALUES
	('Elizabeth Olsen', 'Pierre Morel', 'horror', 8),
	('Tom Hanks', 'Emma Tammi', 'comedie', 5),
	('Daniel Craig', 'Kim Burdon', 'actiune', 4),
	('Jennifer Lawrence', 'Emma Tammi', 'drama', 8),
	('Dwayne Johnson', 'Paul Negoescu', 'comedie', 3),
	('Patrick Wilson', 'Sam Raimi', 'horror', 5),
	('Tom Cruise', 'Anthony Russo', 'actiune', 6),
	('Anna Kendrick', 'Joseph Russo', 'aventura', 1);

SELECT * FROM PreferinteClienti;
DELETE FROM PreferinteClienti;

INSERT INTO ConturiClienti(username, parola, email, cId) VALUES
	('lupusor', 'lupu9mmg', 'cosminlupu@gmail.com', 1),
	('stefi28', 'stefceyh', 'stefaniamail@yahoo.com', 3),
	('narci1997', 'narcisa22', 'eusuntnarcisa@yahoo.com', 5),
	('cosmilupu', 'lupu1234', 'cosmin.lupu2@yahoo.com', 1),
	('narcisa', 'parola10', 'narcisanita@gmail.com', 5),
	('diadia', 'computer', 'diadia2004@gmail.com', 4);

SELECT * FROM ConturiClienti;
DELETE FROM ConturiClienti;

INSERT INTO Rezervari(achitata, activa, contId) VALUES 
	(1, 1, 3),
	(0, 1, 2),
	(0, 0, 2),
	(1, 1, 1),
	(1, 1, 6),
	(1, 1, 3),
	(0, 0, 3);

SELECT * FROM Rezervari;
DELETE FROM Rezervari;

INSERT INTO LocuriRezervate(rId, lId, oraRezervare) VALUES
	(2, 5, '20:20'),
	(1, 6, '11:50'),
	(5, 7, '20:20'),
	(1, 10, '19:30'),
	(1, 6, '14:10'),
	(1, 3, '20:20'),
	(4, 10, '17:30'),
	(4, 9, '17:30');

SELECT * FROM LocuriRezervate;
DELETE FROM LocuriRezervate;

/* 1. toate filmele si salile in care se vor difuza acestea si vor incepe dupa ora 17:00 */
SELECT f.titlu, s.numarSala, e.oraStartFilm FROM Filme f INNER JOIN Ecranizari e ON f.fId = e.fId INNER JOIN Sali s ON s.sId = e.sId 
	WHERE e.oraStartFilm >= '17:00';

/* 2. toate locurile care apartin unei rezervari active */
SELECT l.numarLoc, l.rand, l.tipScaun, lr.oraRezervare FROM Locuri l INNER JOIN LocuriRezervate lr ON l.lId = lr.lId INNER JOIN Rezervari r ON r.rId = lr.rId
	WHERE r.activa = 1;

/* 3. toate email-urile clientilor care prefera filmele horror */
SELECT A.nume, cc.email FROM (SELECT DISTINCT c.cId, c.nume FROM Clienti c INNER JOIN PreferinteClienti pc ON pc.cId = c.cId
	WHERE pc.genFilmPreferat = 'horror') A INNER JOIN ConturiClienti cc ON cc.cId = A.cId;

/* 4. recomanda unui client filme din genul sau preferat */
SELECT c.nume, f.titlu, f.descriere FROM Filme f INNER JOIN CategoriiFilme cf ON f.catId = cf.catId INNER JOIN 
	PreferinteClienti pc ON pc.genFilmPreferat = cf.categorie INNER JOIN Clienti c ON c.cId = pc.cId;

/* 5. recomanda unui client filme regizate de regizorul sau preferat */
SELECT c.nume, f.titlu, f.descriere FROM Filme f INNER JOIN PreferinteClienti pc ON pc.numeDirectorPreferat = f.numeDirector INNER JOIN Clienti c ON c.cId = pc.cId
	WHERE durata > 100;

/* 6. la ce ora are fiecare client rezervarea locului si daca aceasta este achitata */
SELECT cc.email, lr.oraRezervare, r.achitata FROM ConturiClienti cc INNER JOIN Rezervari r ON cc.contId = r.contId INNER JOIN LocuriRezervate lr ON lr.rId = r.rId;
	
/* 7. salile in care se difuzeaza filme de actiune */
SELECT DISTINCT s.numarSala, f.titlu, e.oraStartFilm FROM Sali s INNER JOIN Ecranizari e ON s.sId = e.sId INNER JOIN Filme f ON f.fId = e.fId
	INNER JOIN CategoriiFilme cf ON cf.catId = f.catId WHERE cf.categorie = 'actiune';

/* 8. rezervarile care au pretul total mai mare de 100 */
SELECT r.rId, SUM(f.pret) AS pretRezervare FROM Filme f INNER JOIN Ecranizari e ON e.fId = f.fId INNER JOIN Sali s ON s.sId = e.sId
	INNER JOIN Locuri l ON l.sId = s.sId INNER JOIN LocuriRezervate lr ON lr.lId = l.lId 
	INNER JOIN Rezervari r ON r.rId = lr.rId GROUP BY r.rId HAVING SUM(f.pret) > 100;

/* 9. cate locuri rezervate sunt intr-o sala */
SELECT s.numarSala, COUNT(l.lId) AS locuriRezervate FROM Rezervari r INNER JOIN LocuriRezervate lr ON lr.rId = r.rId INNER JOIN Locuri l ON l.lId = lr.lId
	INNER JOIN Sali s ON s.sId = l.sId GROUP BY s.numarSala;

/* 10. filmele care se difuzeaza in salile cele mai mari */
SELECT f.titlu, s.numarSala, MAX(s.numarLocuri) AS numarLocuri FROM Filme f INNER JOIN Ecranizari e ON e.fId = f.fId INNER JOIN Sali s ON s.sId = e.sId
	GROUP BY f.titlu, s.numarSala HAVING MAX(s.numarLocuri) = 217;