#pragma once
#include "Matrice.h"

typedef int TElem;

class IteratorMatrice
{
	friend class Matrice;

private:
	IteratorMatrice(const Matrice& m);

	const Matrice& mat;

	int curent;

public:
	void prim();

	void urmator();

	bool valid() const;

	TElem element() const;

};

