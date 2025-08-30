#pragma once
#include <mpi.h>
#include <string>
#include <vector>
#include <fstream>
#include <iostream>


class Var2
{
private:
	int numprocs, myid;
	MPI_Status status;

public:
	Var2(int numprocs, int myid, MPI_Status status);

	void run(std::string fileName);
};

