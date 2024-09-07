#pragma once
using namespace std;

typedef int TElem;

class Coada
{
private:
	// capacitatea maxima de memorare
	int cp;

	// dimeniunea
	int n;

	// indicii fata si spate
	int fata;
	int spate;

	// elementele memorate
	TElem* e;

	// functia de redimensionare
	void redim(); // theta(n), unde n este lungimea cozii

public:
	//constructorul implicit
	Coada( ); // theta(1)

	//adauga un element in coada
	void adauga(TElem e); // theta(1)

	//acceseaza elementul cel mai devreme introdus in coada 
	//arunca exceptie daca coada e vida
	TElem element() const; // theta(1)

	//sterge elementul cel mai devreme introdus in coada si returneaza elementul sters (principiul FIFO)
	//arunca exceptie daca coada e vida
	TElem sterge(); // theta(1)

	//verifica daca coada e vida;
	bool vida() const; // theta(1)


	// destructorul cozii
	~Coada(); // theta(1)
};
