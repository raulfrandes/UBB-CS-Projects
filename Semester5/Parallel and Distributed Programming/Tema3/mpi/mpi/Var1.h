#pragma once
#include <vector>
#include <string>
#include <mpi.h>
#include <fstream>
#include <iostream>

class Var1
{
private:
	int numprocs, myid;
	MPI_Status status;

public:
	Var1(int numprocs, int myid, MPI_Status status);

	void run(std::string fileName);
};

