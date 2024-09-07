
#include "../Coada.h"
#include <exception>
#include <iostream>

using namespace std;

void Coada::redim() {
	// alocam un spatiu de capacitate dubla
	TElem* eNou = new int[2 * cp];

	// copiem vechile valori in noua zona
	for (int i = 0; i < this->n; i++) {
		eNou[i] = this->e[i];
	}

	// eliberam vechea zona
	delete[] this->e;

	// vectorul indica spre noua zona
	this->e = eNou;

	// dublam capacitatea
	this->cp = 2 * this->cp;
}

Coada::Coada() {
	// setam capacitatea
	this->cp = 10;
	this->n = 0;
	
	// setam indicii fata si spate
	this->fata = 0;
	this->spate = 0;

	// alocam spatiu de memorare pentru vector
	e = new TElem[cp];
}


void Coada::adauga(TElem elem) {
	// daca avem o coada plina redimensionam
	if ((this->fata == 0 && this->spate == cp) || this->fata == this->spate + 1) {
		redim();
	}

	// adaugam la sfarsit elementul
	this->e[spate] = elem;

	// crestem lungimea
	this->n++;

	// modificam indicele spate
	if (this->spate == this->cp) {
		this->spate = 0;
	}
	else {
		this->spate = this->spate + 1;
	}
}

//arunca exceptie daca coada e vida
TElem Coada::element() const {
	if (this->vida()) {
		throw exception("Coada este vida!");
	}
	return this->e[this->fata];
}

TElem Coada::sterge() {
	if (this->vida()) {
		throw exception("Coada este vida!");
	}

	// retinem elemetul pe care il vom sterge
	TElem e = this->e[fata];

	if (this->fata == this->cp) { // daca nu mai avem elemente in coada reinitializam indicele fata
		this->fata = 1;
	}
	else { // crestem indicele fata
		this->fata = this->fata + 1;
	}

	// scadem lungimea coadei
	this->n--;

	// returnam elementul sters
	return e;
}

bool Coada::vida() const {
	if (this->n == 0) {
		return true;
	}
	return false;
}


Coada::~Coada() {
	delete[] this->e;
}

