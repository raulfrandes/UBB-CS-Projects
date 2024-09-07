#include "IteratorLI.h"
#include "LI.h"
#include <exception>

IteratorLI::IteratorLI(const LI& li): lista(li) { // theta(1)
    curent = li.prim;
}

void IteratorLI::prim(){ // theta(1)
    curent = lista.prim;
}

void IteratorLI::urmator(){ // theta(1)
    if (!valid()) {
        throw std::exception("Iteratorul nu este valid!");
    }
    curent = curent->urmator();
}

bool IteratorLI::valid() const{ // theta(1)
    return curent != nullptr;
}

TElem IteratorLI::element() const{ // theta(1)
    if (!valid()) {
        throw std::exception("Iteratorul nu este valid!");
    }
    return curent->element();
}
