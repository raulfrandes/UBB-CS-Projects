#include "IteratorMDO.h"
#include "MDO.h"

IteratorMDO::IteratorMDO(const MDO& d) : dict(d){ //theta(1)
	prim();
}

void IteratorMDO::prim(){ //theta(1)
	curentDict = dict.prim;
	if (curentDict > -1) {
		curentLDI = dict.e[curentDict].prim;
	}
}

void IteratorMDO::urmator(){ //theta(1)
	if (!valid()) {
		throw exception();
	}
	curentLDI = dict.e[curentDict].urm[curentLDI];
	if (curentLDI == -1 || curentLDI == dict.e[curentDict].primLiber) {
		curentDict = dict.urm[curentDict];
		if (curentDict != -1) {
			curentLDI = dict.e[curentDict].prim;
		}
	}
}

bool IteratorMDO::valid() const{ //theta(1)
	if (curentDict == -1 || curentLDI == -1) {
		return false;
	}
	if (curentDict >= 0 && curentLDI >= 0 && dict.cp > curentDict && dict.e[curentDict].cp > curentLDI && curentDict != dict.primLiber && curentLDI != dict.e[curentDict].primLiber) {
		return true;
	}
	return false;
}

TElem IteratorMDO::element() const{ //theta(1)
	if (!valid()) {
		throw exception();
	}
	return dict.e[curentDict].e[curentLDI];
}


