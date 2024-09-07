#include <assert.h>
#include <exception>

#include "TestScurt.h"
#include "../Proiect C++/LI.h"
#include "../Proiect C++/IteratorLI.h"




void testAll() {
    LI lista = LI();
    assert(lista.vida());
    lista.adaugaSfarsit(1);
    assert(lista.dim() == 1);
    lista.adauga(0,2);
    assert(lista.dim() == 2);
    IteratorLI it = lista.iterator();
    assert(it.valid());
    it.urmator();
    assert(it.element() == 1);
    it.prim();
    assert(it.element() == 2);
    assert(lista.cauta(1) == 1);
    assert(lista.modifica(1,3) == 1);
    assert(lista.element(1) == 3);
    assert(lista.sterge(0) == 2);
    assert(lista.dim() == 1);
}

void testFunctionalitateNoua() {
    LI lista = LI();
    lista.adaugaSfarsit(1);
    lista.adaugaSfarsit(2);
    lista.adaugaSfarsit(3);
    lista.adaugaSfarsit(4);
    lista.adaugaSfarsit(5);
    lista.adaugaSfarsit(6);
    assert(lista.eliminaDinKInK(2) == 3);
    assert(lista.dim() == 3);
    assert(lista.element(0) == 1);
    assert(lista.element(1) == 3);
    assert(lista.element(2) == 5);
}