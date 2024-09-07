#include "MD.h"
#include "IteratorMD.h"
#include <exception>
#include <iostream>

using namespace std;

Nod::Nod(TCheie cheie, vector<TElem>& vals) :cheie{ cheie }, vals{ vals } {}

int MD::dPrim(TCheie cheie) const {
	if (cheie < 0)
		cheie = abs(cheie);
	return cheie % m;
}

int MD::d(TCheie cheie, int i) const {
	int disUzuala = dPrim(cheie);
	int dis  = (int)(disUzuala + 0.5 * i + 0.5 * i * i) % m;
	return dis;
}

MD::MD() {
	n = 0;
	m = LEN;
	chei.resize(m);
	for (int i = 0; i < m; i++) {
		chei[i] = NULL;
	}
}


void MD::adauga(TCheie c, TValoare v) {
	int i = 0;
	bool gasit = false;
	int index = -2;
	int pozitie = d(c, i);
	while (i != m && gasit == false) {
		pozitie = d(c, i);
		if (chei[pozitie] == nullptr) {
			vector<TElem> elemente;
			PNod valoare = new Nod(c, elemente);
			chei[pozitie] = valoare;
			n++;
			valoare->vals.push_back(make_pair(c, v));
			return;
		}
		if (chei[pozitie]->cheie == c) {
			chei[pozitie]->vals.push_back(make_pair(c, v));
			n++;
			return;
		}
		i++;
	}
}


bool MD::sterge(TCheie c, TValoare v) {
	int i = 0;
	int pozitie = d(c, i);
	while (i < m) {
		if (chei[pozitie] != nullptr) {
			if (chei[pozitie]->cheie == c) {
				break;
			}
		}
		i++;
		pozitie = d(c, i);
	}
	bool gasit = false;
	if (i == m) {
		return false;
	}
	for (int i = 0; i < chei[pozitie]->vals.size(); i++) {
		if (chei[pozitie]->vals[i].second == v) {
			chei[pozitie]->vals.erase(chei[pozitie]->vals.begin() + i);
			n--;
			gasit = true;
			break;
		}
	}
	if (gasit) {
		if (chei[pozitie]->vals.size() == 0) {
			chei[pozitie]->vals.clear();
			delete chei[pozitie];
			chei[pozitie] = nullptr;
		}
		return true;
	}
	return false;
}


vector<TValoare> MD::cauta(TCheie c) const {
	vector<TValoare> valori;
	int i = 0;
	int pozitie;
	pozitie = d(c, i);
	if (pozitie > n) {
		return valori;
	}
	while (i < m || pozitie > m) {
		if (chei[pozitie] != NULL) {
			if (chei[pozitie]->cheie == c) {
				break;
			}
		}
		i++;
		pozitie = d(c, i);
	}
	if (i == m || pozitie > m) {
		return valori;
	}
	for (auto j : chei[pozitie]->vals) {
		valori.push_back(j.second);
	}
	return valori;
}


int MD::dim() const {
	return n;
}


bool MD::vid() const {
	if (n == 0) {
		return true;
	}
	return false;
}

IteratorMD MD::iterator() const {
	return IteratorMD(*this);
}


MD::~MD() {
	/* de adaugat */
}

