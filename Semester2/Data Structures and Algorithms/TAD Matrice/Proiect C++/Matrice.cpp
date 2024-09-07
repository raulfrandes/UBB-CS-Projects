#include "Matrice.h"
#include "IteratorMatrice.h"

#include <exception>

using namespace std;

// theta(1)
bool Matrice::rel(const Triplet& t1, const Triplet& t2) const {
	if (t1.lin < t2.lin) {
		return true;
	}
	if (t1.lin == t2.lin && t1.col < t2.col) {
		return true;
	}
	return false;
}

// O(h)
int Matrice::adaugaRec(int p, Triplet e) {
	if (p == -1) {
		p = creeazaNod(e);
	}
	else {
		if (rel(e, el[p])) {
			st[p] = adaugaRec(st[p], e);
			pi[st[p]] = p;
		}
		else {
			dr[p] = adaugaRec(dr[p], e);
			pi[dr[p]] = p;
		}
	}
	return p;
}

// O(cap)
int Matrice::creeazaNod(Triplet e) {
	int i = primLiber;
	el[i] = e;
	while (el[primLiber] != NULL_TRIPLET) {
		primLiber++;
	}
	if (primLiber >= cap) {
		throw exception();
	}
	return i;
}

// O(h)
int Matrice::minim(int p) {
	while (st[p] != -1) {
		p = st[p];
	}
	return p;
}

// O(h)
int Matrice::stergeRec(int p, Triplet e) {
	if (p == -1) {
		return -1;
	}
	if (rel(e, el[p])) {
		st[p] = stergeRec(st[p], e);
		return p;
	}
	if (e == el[p]) {
		if (st[p] != -1 && dr[p] != -1) {
			int temp = minim(dr[p]);
			el[p] = el[temp];
			dr[p] = stergeRec(dr[p], el[p]);
			return p;
		}
		else {
			int temp = p, repl;
			if (st[p] == -1) {
				repl = dr[p];
			}
			else {
				repl = st[p];
			}
			pi[temp] = -1;
			st[temp] = -1;
			dr[temp] = -1;
			el[temp] = NULL_TRIPLET;

			if (p < primLiber) {
				primLiber = p;
			}

			return repl;
		}
	}
	dr[p] = stergeRec(dr[p], e);
	return p;
}

// theta(cap)
Matrice::Matrice(int m, int n) {
	if (m <= 0 || n <= 0) {
		throw std::exception();
	}

	nrLin = m;
	nrCol = n;
	cap = m * n;
	
	el = new Triplet[cap];
	st = new int[cap];
	dr = new int[cap];
	pi = new int[cap];

	nrElem = 0;
	root = -1;
	primLiber = 0;

	for (int i = 0; i < cap; i++) {
		el[i] = NULL_TRIPLET;
		st[i] = -1;
		dr[i] = -1;
		pi[i] = -1;
	}
}

// theta(1)
int Matrice::nrLinii() const{
	return nrLin;
}

// theta(1)
int Matrice::nrColoane() const{
	return nrCol;
}

// O(h)
TElem Matrice::element(int i, int j) const{
	if (i < 0 || j < 0 || i >= nrLin || j >= nrCol) {
		throw exception();
	}
	Triplet toFind = { i, j, NULL_TELEMENT };
	int curent = root;
	while (curent != -1) {
		if (rel(toFind, el[curent])) {
			curent = st[curent];
			continue;
		}
		if (toFind == el[curent]) {
			return el[curent].elem;
		}
		curent = dr[curent];
	}
	return NULL_TELEMENT;
}

// O(cap)
TElem Matrice::modifica(int i, int j, TElem e) {
	if (i < 0 || j < 0 || i >= nrLin || j >= nrCol) {
		throw std::exception();
	}

	Triplet toFind = { i, j, e };
	int curent = root;
	while (curent != -1) {
		if (rel(toFind, el[curent])) {
			curent = st[curent];
			continue;
		}
		if (toFind == el[curent]) {
			break;
		}
		curent = dr[curent];
	}
	if (curent == -1) {
		root = adaugaRec(root, toFind);
		nrElem++;
		return NULL_TELEMENT;
	}
	if (e == NULL_TELEMENT) {
		TElem old = el[curent].elem;
		root = stergeRec(root, el[curent]);
		nrElem--;
		return old;
	}
	TElem old = el[curent].elem;
	root = stergeRec(root, el[curent]);
	root = adaugaRec(root, toFind);

	return old;
}

IteratorMatrice Matrice::iterator() const {
	return IteratorMatrice(*this);
}

