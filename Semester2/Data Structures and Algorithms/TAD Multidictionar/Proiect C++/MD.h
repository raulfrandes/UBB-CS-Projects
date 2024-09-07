#pragma once
#include<vector>
#include<utility>

#define LEN 262144;

using namespace std;

typedef int TCheie;
typedef int TValoare;

typedef std::pair<TCheie, TValoare> TElem;

class IteratorMD;

class Nod {
	friend class MD;
	friend class IteratorMD;
	Nod(TCheie cheie, vector<TElem>& val);
private:
	TCheie cheie;
	vector<TElem> vals;
};

typedef Nod* PNod;

class MD
{
	friend class IteratorMD;
	friend class Nod;

private:
	int n;
	int m;
	vector<PNod> chei;

	int dPrim(TCheie cheie) const;
	int d(TCheie cheie, int i) const;

public:
	// constructorul implicit al MultiDictionarului
	MD();

	// adauga o pereche (cheie, valoare) in MD	
	void adauga(TCheie c, TValoare v);

	//cauta o cheie si returneaza vectorul de valori asociate
	vector<TValoare> cauta(TCheie c) const;

	//sterge o cheie si o valoare 
	//returneaza adevarat daca s-a gasit cheia si valoarea de sters
	bool sterge(TCheie c, TValoare v);

	//returneaza numarul de perechi (cheie, valoare) din MD 
	int dim() const;

	//verifica daca MultiDictionarul e vid 
	bool vid() const;

	// se returneaza iterator pe MD
	IteratorMD iterator() const;

	// destructorul MultiDictionarului	
	~MD();



};

