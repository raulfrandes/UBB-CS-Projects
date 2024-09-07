#include <exception>
#include <iostream>
#include "LI.h"
#include "IteratorLI.h"

Nod::Nod(TElem e, PNod urm, PNod prec) {
	this->e = e;
	this->urm = urm;
	this->prec = prec;
}

TElem Nod::element() {
	return e;
}

PNod Nod::urmator() {
	return urm;
}

PNod Nod::precedent() {
	return prec;
}

LI::LI() { // theta(1)
	prim = nullptr;
	ultim = nullptr;
}

int LI::dim() const { // theta(n), unde n este numarul de elemente din lista
	if (vida()) {
		return 0;
	}
	int d = 0;
	PNod aux = prim;
	while (aux != nullptr) {
		d++;
		aux = aux->urm;
	}
	return d;
}


bool LI::vida() const { // theta(1)
	return prim == nullptr;
}

TElem LI::element(int i) const { // O(n), unde n este numarul de elemente din lista
	if (i < 0 || i >= dim()) {
		throw std::exception("Pozitie invalida!");
	}
	PNod aux = prim;
	while (i != 0) {
		aux = aux->urm;
		i--;
	}
	return aux->e;
}

TElem LI::modifica(int i, TElem e) { // O(n), unde n este numarul de elemente din lista
	if (i < 0 || i >= dim()) {
		throw std::exception("Pozitie invalida!");
	}
	PNod aux = prim;
	while (i != 0) {
		aux = aux->urm;
		i--;
	}
	TElem eVechi = aux->e;
	aux->e = e;
	return eVechi;
}

void LI::adaugaSfarsit(TElem e) { // theta(1)
	PNod q = new Nod(e, nullptr, nullptr);
	if (vida()) {
		prim = q;
		ultim = q;
	}
	else {
		ultim->urm = q;
		q->prec = ultim;
		ultim = q;
	}
}

void LI::adauga(int i, TElem e) { // O(n), unde n este numarul de elemente din lista
	if (i < 0 || i >= dim()) {
		throw std::exception("Pozitie invalida!");
	}

	PNod q = new Nod(e, nullptr, nullptr);
	if (i == 0) {
		q->urm = prim;
		prim->prec = q;
		prim = q;
	}
	else if (i == dim() - 1) {
		ultim->urm = q;
		q->prec = ultim;
		ultim = q;
	}
	else {
		PNod aux = prim;
		while (i != 1) {
			aux = aux->urm;
			i--;
		}
		q->urm = aux->urm;
		aux->urm->prec = q;
		aux->urm = q;
		q->prec = aux;
	}
}

TElem LI::sterge(int i) { // O(n), unde n este numarul de elemente din lista
	if (i < 0 || i >= dim()) {
		throw std::exception("Pozitie invalida!");
	}
	PNod q;
	TElem eSters;
	if (dim() == 1) {
		eSters = prim->e;
		q = prim;
		prim = nullptr;
		ultim = nullptr;
		delete q;
		return eSters;
	}
	if (i == 0) {
		q = prim;
		prim = prim->urm;
		prim->prec = nullptr;
	}
	else if (i == dim() - 1) {
		q = ultim;
		ultim = ultim->prec;
		ultim->urm = nullptr;
	}
	else {
		q = prim;
		while (i != 0) {
			q = q->urm;
			i--;
		}
		q->prec->urm = q->urm;
		q->urm->prec = q->prec;
	}
	eSters = q->e;
	delete q;
	return eSters;
}

int LI::cauta(TElem e) const{ // O(n), unde n este numarul de elemente din lista
	if (prim->e == e) {
		return 0;
	}
	PNod aux = prim;
	int poz = 0;
	while (aux != nullptr && aux->e != e) {
		aux = aux->urm;
		poz++;
	}
	if (poz < dim()) {
		return poz;
	}
	return -1;
}

IteratorLI LI::iterator() const {
	return  IteratorLI(*this);
}

LI::~LI() { // theta(n), unde n este numarul de elemente din lista
	PNod nod = prim;
	while (prim != nullptr) {
		nod = prim->urm;
		delete prim;
		prim = nod;
	}
}

int LI::eliminaDinKInK(int k) { // theta(n), unde n este numarul de elemente din lista
	if (k <= 0) {
		throw std::exception("k este negativ sau 0!");
	}
	if (dim() < k) {
		return 0;
	}
	int nrElem = 0;
	int len = dim();
	for (int i = len; i >= 0; i--) {
		if ((i + 1) % k == 0) {
			sterge(i);
			nrElem++;
		}
	}
	return nrElem;
}