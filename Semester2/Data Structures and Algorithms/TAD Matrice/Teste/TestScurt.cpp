#include "TestScurt.h"
#include <assert.h>
#include "../Proiect C++/Matrice.h"
#include "../Proiect C++/IteratorMatrice.h"
#include <iostream>

using namespace std;

void testAll() { //apelam fiecare functie sa vedem daca exista
	Matrice m(4,4);
	assert(m.nrLinii() == 4);
	assert(m.nrColoane() == 4);
	//adaug niste elemente
	m.modifica(1,1,5);
	assert(m.element(1,1) == 5);
	m.modifica(1,1,6);
	assert(m.element(1,2) == NULL_TELEMENT);
}

void testIterator() {
	int vverif[5];
	int iverif;
	TElem e;

	Matrice m(4, 4);
	m.modifica(1, 1, 5);
	m.modifica(3, 3, 1);
	m.modifica(2, 3, 10);
	IteratorMatrice im = m.iterator();
	im.prim();
	iverif = 0;
	e = im.element();
	vverif[iverif++] = e;
	im.urmator();
	while (im.valid()) {
		e = im.element();
		vverif[iverif++] = e;
		im.urmator();
	}
	assert((vverif[0] == 5) && (vverif[1] == 10) && (vverif[2] == 1));
}