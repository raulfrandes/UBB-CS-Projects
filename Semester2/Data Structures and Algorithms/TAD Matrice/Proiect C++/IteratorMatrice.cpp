#include "IteratorMatrice.h"
#include <exception>

IteratorMatrice::IteratorMatrice(const Matrice& mat) :mat{ mat } {
	prim();
}

void IteratorMatrice::prim() {
	curent = mat.root;
	if (curent == -1) {
		return;
	}
	while (mat.st[curent] != -1) {
		curent = mat.st[curent];
	}
}
void IteratorMatrice::urmator() {
	if (!valid()) {
		throw std::exception();
	}
	if (mat.dr[curent] != -1) {
		curent = mat.dr[curent];
		while (mat.st[curent] != -1) {
			curent = mat.st[curent];
		}
	}
	else {
		int p = mat.pi[curent];
		while (p != -1 && mat.dr[p] == curent) {
			curent = p;
			p = mat.pi[p];
		}
		curent = p;
	}
}

bool IteratorMatrice::valid() const {
	return curent != -1;
}

TElem IteratorMatrice::element() const {
	if (!valid()) {
		throw std::exception();
	}
	return mat.el[curent].elem;
}