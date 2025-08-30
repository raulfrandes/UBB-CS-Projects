#pragma once
#include <mpi.h>
#include <string>
#include <vector>
#include <fstream>
#include <iostream>

class Var3
{
private:
	int numprocs, myid;
	MPI_Status status;

public:
	Var3(int numprocs, int myid, MPI_Status status);

	void run(std::string fileName);
};

