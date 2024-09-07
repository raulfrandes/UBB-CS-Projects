#include "IteratorMDO.h"
#include "MDO.h"
#include <iostream>
#include <vector>

#include <exception>
using namespace std;

int MDO::aloca() { //theta(1)
	int i = primLiber;
	primLiber = urm[primLiber];
	return i;
}

void MDO::dealoca(int i) { //theta(1)
	urm[i] = primLiber;
	prec[primLiber] = i;
	primLiber = i;
}

int MDO::creeazaNod(TElem el) { //theta(1) amortizat
	if (primLiber == -1) {
		int cp_nou = cp * 2;
		e.resize(cp_nou);
		urm.resize(cp_nou);
		prec.resize(cp_nou);
		for (int i = cp; i < cp_nou - 1; i++) {
			urm[i] = i + 1;
			prec[i] = -1;
		}
		urm[cp_nou - 1] = -1;
		prec[cp_nou - 1] = -1;
		primLiber = cp;
		cp = cp_nou;
	}
	int i = aloca();
	LDI<TElem> lista(el.first);
	e[i] = lista;
	e[i].creeazaNod(el);
	return i;
}

TValoare MDO::ceaMaiFrecventaValoare() const {
	if (vid()) {
		throw exception();
	}
	vector<TValoare> val;
	int curent = prim;
	while (curent != -1) {
		int i = e[curent].prim;
		while (i != -1) {
			val.push_back(e[curent].e[i].second);
			i = e[curent].urm[i];
		}
		curent = urm[curent];
	}
	for (int i = 0; i < val.size() - 1; i++) {
		for (int j = 0; j < val.size() - i - 1; j++) {
			if (val[j] > val[j + 1]) {
				int aux = val[j];
				val[j] = val[j + 1];
				val[j + 1] = aux;
			}
		}
	}
	int count = 1;
	int max = 0;
	TValoare v = 0;
	for (int i = 0; i < val.size() - 1; i++) {
		if (val[i] == val[i + 1]) {
			count++;
		}
		else {
			if (count > max) {
				max = count;
				v = val[i];
			}
			count = 1;
		}
	}
	if (count > max) {
		v = val[val.size() - 1];
	}
	return v;
}

MDO::MDO(Relatie r) { //theta(1)
	len = 0;
	rel = r;
	prim = -1;
	primLiber = 0;
	cp = 2;
	e.resize(cp);
	urm.resize(cp);
	prec.resize(cp);
	for (int i = 0; i < cp - 1; i++) {
		urm[i] = i + 1;
		prec[i] = -1;
	}
	urm[cp - 1] = -1;
	prec[cp - 1] = -1;
	prim = -1;
	primLiber = 0;
}


void MDO::adauga(TCheie c, TValoare v) { //O(n), unde n este numarul de chei
	if (prim == -1) {
		TElem aux;
		aux.first = c;
		aux.second = v;
		int i = creeazaNod(aux);
		urm[i] = prim;
		prim = i;
		prec[i] = -1;
		len = 1;
		return;
	}

	int anterior = -1;
	int curent = prim;
	while (curent != -1 && rel(e[curent].cheie, c)) {
		if (e[curent].cheie == c) {
			TElem aux;
			aux.first = c;
			aux.second = v;
			e[curent].creeazaNod(aux);
			len++;
			return;
		}
		anterior = curent;
		curent = urm[curent];
	}
	if (curent == -1) {
		TElem aux;
		aux.first = c;
		aux.second = v;
		int i = creeazaNod(aux);
		len++;
		urm[anterior] = i;
		prec[i] = anterior;
		urm[i] = -1;
		return;
	}
	else if (!rel(e[curent].cheie, c)) {
		TElem aux;
		aux.first = c;
		aux.second = v;
		int i = creeazaNod(aux);
		if (anterior > -1) {
			urm[anterior] = i;
		}
		prec[i] = anterior;
		urm[i] = curent;
		prec[curent] = i;
		if (anterior == -1)
			prim = i;
		len++;
		return;
	}
}

vector<TValoare> MDO::cauta(TCheie c) const { //O(n), unde n este numarul de perechi (cheie, valoare) din multidictionar
	vector<TValoare> v;
	int curent = prim;
	while (curent != -1 && rel(e[curent].cheie, c)) {
		if (e[curent].cheie == c) {
			int i = e[curent].prim;
			while (i != -1) {
				v.push_back(e[curent].e[i].second);
				i = e[curent].urm[i];
			}
			return v;
		}
		curent = urm[curent];
	}
	return v;
}

bool MDO::sterge(TCheie c, TValoare v) { //O(n), unde n este numarul de perechi (cheie, valoare) din multidictionar
	if (prim == -1) {
		return false;
	}
	int curent = prim;
	if (c == e[curent].cheie) {
		int i = e[curent].prim;
		while (i != -1) {
			if (e[curent].e[i].second == v) {
				if (e[curent].prec[i] > -1) {
					e[curent].urm[e[curent].prec[i]] = e[curent].urm[i];
				}
				if (e[curent].urm[i] > -1) {
					e[curent].prec[e[curent].urm[i]] = e[curent].prec[i];
				}
				if (e[curent].prim == i) {
					e[curent].prim = e[curent].urm[i];
				}
				e[curent].dealoca(i);
				if (e[curent].prim == -1) {
					if (prec[curent] > -1) {
						urm[prec[curent]] = urm[curent];
					}
					if (urm[curent] > -1) {
						prec[urm[curent]] = prec[curent];
					}
					prim = urm[curent];
					dealoca(curent);
				}
				len--;
				return true;
			}
			i = e[curent].urm[i];
		}
		return false;
	}

	curent = urm[curent];
	while (curent != -1 && rel(e[curent].cheie, c)) {
		if (e[curent].cheie == c) {
			int i = e[curent].prim;
			while (i != -1) {
				if (e[curent].e[i].second == v) {
					if (e[curent].prec[i] > -1) {
						e[curent].urm[e[curent].prec[i]] = e[curent].urm[i];
					}
					if (e[curent].urm[i] > -1) {
						e[curent].prec[e[curent].urm[i]] = e[curent].prec[i];
					}
					if (e[curent].prim == i) {
						e[curent].prim = e[curent].urm[i];
					}
					e[curent].dealoca(i);
					if (e[curent].prim == -1) {
						if (prec[curent] > -1) {
							urm[prec[curent]] = urm[curent];
						}
						if (urm[curent] > -1) {
							prec[urm[curent]] = prec[curent];
						}
						dealoca(curent);
					}
					len--;
					return true;
				}
				i = e[curent].urm[i];
			}
			return false;
		}
		curent = urm[curent];
	}
	return false;
}

int MDO::dim() const { //theta(1)
	return len;
}

bool MDO::vid() const { //theta(1)
	return len == 0;
}

IteratorMDO MDO::iterator() const { //theta(1)
	return IteratorMDO(*this);
}

MDO::~MDO() = default;
