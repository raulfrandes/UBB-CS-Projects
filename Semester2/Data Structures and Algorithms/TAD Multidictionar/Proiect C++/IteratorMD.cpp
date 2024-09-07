#include "IteratorMD.h"
#include "MD.h"

using namespace std;

IteratorMD::IteratorMD(const MD& _md): md(_md) {
	index = 0;
	lungime = md.dim();
	if (md.vid())
		return;
	IndexChei = 0;
	val = 0;
	while (md.chei[IndexChei] == nullptr || md.chei[IndexChei] == nullptr) {
		IndexChei++;
	}
	primIndexChei = IndexChei;
}

TElem IteratorMD::element() const{
	return md.chei[IndexChei]->vals[val];
}

bool IteratorMD::valid() const {
	return index < lungime;
}

void IteratorMD::urmator() {
	if (!valid())
		throw std::exception();
	index++;
	if (md.chei[IndexChei]->vals.size() == val + 1) {
		IndexChei++;
		while (md.chei[IndexChei] == nullptr) {
			IndexChei++;
			if (IndexChei == md.m) {
				break;
			}
		}
		if (IndexChei == md.m) {
			return;
		}
		val = 0;
	}
	else {
		val++;
	}
}

void IteratorMD::prim() {
	index = 0;
	IndexChei = primIndexChei;
	val = 0;
}

void IteratorMD::revinoKPasi(int k) {
	while (k > 0) {
		if (!valid() || k > md.dim()) {
			throw std::exception();
		}
		k--;
		index--;
		if (val == 0) {
			IndexChei--;
			while (md.chei[IndexChei] == nullptr) {
				IndexChei--;
				if (IndexChei == 0) {
					break;
				}
			}
			if (IndexChei == 0) {
				return;
			}
			val = md.chei[IndexChei]->vals.size() - 1;
		}
		else {
			val--;
		}
	}
}

