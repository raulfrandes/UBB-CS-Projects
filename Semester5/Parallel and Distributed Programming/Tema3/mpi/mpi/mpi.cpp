#include <iostream>
#include <vector>
#include "Utils.h"
#include "Var0.h"
#include <chrono>
#include <mpi.h>
#include "Var1.h"
#include "Var2.h"
#include "Var3.h"

int main(int argc, char* argv[])
{
	// Utils::generateNumbers();

	int numprocs, myid;

	MPI_Init(&argc, &argv);

	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
	MPI_Comm_rank(MPI_COMM_WORLD, &myid);
	MPI_Status status{};

	std::string option = argv[1];
	std::string fileName = argv[2];

	if (option == "Var0") {
		Var0 var0;
		auto startTime = std::chrono::high_resolution_clock::now();
		var0.run(fileName);
		auto endTime = std::chrono::high_resolution_clock::now();
		double elapsed_time_ms = std::chrono::duration<double, std::milli>(endTime - startTime).count();
		if (myid == 0) {
			std::cout << elapsed_time_ms << std::endl;
			std::cout << std::endl;
		}
	}
	else if (option == "Var1") {
		Var1 var1(numprocs, myid, status);
		auto startTime = std::chrono::high_resolution_clock::now();
		var1.run(fileName);
		auto endTime = std::chrono::high_resolution_clock::now();
		double elapsed_time_ms = std::chrono::duration<double, std::milli>(endTime - startTime).count();
		if (myid == 0) {
			std::cout << elapsed_time_ms << std::endl;
			
			bool identical = Utils::areResultFilesIdentical("Numbers/" + fileName + "/Number3Var1.txt");
			if (identical) {
				std::cout << "Identical" << std::endl;
			}
			else {
				std::cout << "Not identical" << std::endl;
			}
		}
	}
	else if (option == "Var2") {
		Var2 var2(numprocs, myid, status);
		auto startTime = std::chrono::high_resolution_clock::now();
		var2.run(fileName);
		auto endTime = std::chrono::high_resolution_clock::now();
		double elapsed_time_ms = std::chrono::duration<double, std::milli>(endTime - startTime).count();
		if (myid == 0) {
			std::cout << elapsed_time_ms << std::endl;

			bool identical = Utils::areResultFilesIdentical("Numbers/" + fileName + "/Number3Var2.txt");
			if (identical) {
				std::cout << "Identical" << std::endl;
			}
			else {
				std::cout << "Not identical" << std::endl;
			}
		}
	}
	else if (option == "Var3") {
		Var3 var3(numprocs, myid, status);
		auto startTime = std::chrono::high_resolution_clock::now();
		var3.run(fileName);
		auto endTime = std::chrono::high_resolution_clock::now();
		double elapsed_time_ms = std::chrono::duration<double, std::milli>(endTime - startTime).count();
		if (myid == 0) {
			std::cout << elapsed_time_ms << std::endl;

			bool identical = Utils::areResultFilesIdentical("Numbers/" + fileName + "/Number3Var3.txt");
			if (identical) {
				std::cout << "Identical" << std::endl;
			}
			else {
				std::cout << "Not identical" << std::endl;
			}
		}
	}

	MPI_Finalize();

	return 0;
}