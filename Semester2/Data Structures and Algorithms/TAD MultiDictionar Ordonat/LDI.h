#pragma once
#include <vector>

using std::vector;

typedef int TCheie;

template<typename TElem>
class LDI
{
public:
	int cp;

	int prim;

	int ultim;

	int primLiber;

	vector<TElem> e;

	vector<int> urm;

	vector<int> prec;

	TCheie cheie;

	LDI() = default;

	LDI(TCheie c) { //theta(1)
		cheie = c;
		cp = 2;
		e.resize(cp);
		urm.resize(cp);
		prec.resize(cp);
		prim = -1;
		ultim = -1;
		for (int i = 0; i < cp - 1; i++) {
			urm[i] = i + 1;
			prec[i] = -1;
		}
		urm[cp - 1] = -1;
		prec[cp - 1] = -1;
		primLiber = 0;
	}

	int aloca() { //theta(1)
		int i = primLiber;
		primLiber = urm[primLiber];
		return i;
	}

	void dealoca(int i) { //theta(1)
		urm[i] = primLiber;
		primLiber = i;
	}

	int creeazaNod(TElem el) { //theta(1) amortizat
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
		e[i] = el;
		if (prim != -1) {
			prec[prim] = i;
		}
		urm[i] = prim;
		prim = i;
		prec[i] = -1;

		return i;
	}

	~LDI() = default;
};