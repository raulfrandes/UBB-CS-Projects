#include "Var1.h"
#include "Utils.h"

Var1::Var1(int numprocs, int myid, MPI_Status status): numprocs(numprocs), myid(myid), status(status) {}

void Var1::run(std::string fileName)
{
	if (myid == 0) {
		std::ifstream file1("Numbers/" + fileName + "/Number1.txt");
		std::ifstream file2("Numbers/" + fileName + "/Number2.txt");

		if (!file1 || !file2) {
			std::cerr << "Error opening file " << fileName << std::endl;
			return;
		}

		int size1, size2;
		file1 >> size1;
		file2 >> size2;

		int maxSize = (size1 > size2) ? size1 : size2;
		std::vector<int> result(maxSize + 1, 0);

		int p = numprocs - 1;
		int chunck = maxSize / p;
		int rest = maxSize % p;

		int start = 0;
		int end = chunck;

		for (int i = 1; i < numprocs; i++) {
			if (rest) {
				end++;
				rest--;
			}

			std::vector<int> chunk1(end - start, 0);
			std::vector<int> chunk2(end - start, 0);

			for (int j = 0; j < end - start && start + j < size1; j++) {
				char digit;
				file1 >> digit;
				chunk1[j] = digit - '0';
			}

			for (int j = 0; j < end - start && start + j < size2; j++) {
				char digit;
				file2 >> digit;
				chunk2[j] = digit - '0';
			}

			MPI_Send(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
			MPI_Send(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
			MPI_Send(chunk1.data(), end - start, MPI_INT, i, 0, MPI_COMM_WORLD);
			MPI_Send(chunk2.data(), end - start, MPI_INT, i, 0, MPI_COMM_WORLD);

			start = end;
			end += chunck;
		}

		int carry = 0;
		for (int i = 1; i < numprocs; i++) {
			int start, end;
			MPI_Recv(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
			MPI_Recv(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);

			std::vector<int> partialResult(end - start);
			MPI_Recv(partialResult.data(), end - start, MPI_INT, i, 0, MPI_COMM_WORLD, &status);

			if (i == numprocs - 1) {
				MPI_Recv(&carry, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
			}

			for (int j = start; j < end; j++) {
				result[j] = partialResult[j - start];
			}
		}

		if (carry) {
			result[maxSize] = carry;
		}
		else {
			result.pop_back();
		}

		std::string resultFileName = "Numbers/" + fileName + "/Number3Var1.txt";
		Utils::writeNumberToFile(resultFileName, result);
	}
	else {
		int start, end;
		MPI_Recv(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
		MPI_Recv(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);

		std::vector<int> chunk1(end - start);
		std::vector<int> chunk2(end - start);
		std::vector<int> partialResult(end - start, 0);
		int carry = 0;
		int carryReceived = 0;

		MPI_Recv(chunk1.data(), end - start, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
		MPI_Recv(chunk2.data(), end - start, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);

		for (int i = 0; i < end - start; i++) {
			int sum = chunk1[i] + chunk2[i] + carry;
			carry = sum / 10;
			partialResult[i] = sum % 10;
		}

		if (myid != 1) {
			MPI_Recv(&carryReceived, 1, MPI_INT, myid - 1, 0, MPI_COMM_WORLD, &status);
		}

		for (int i = 0; i < end - start && carryReceived != 0; i++) {
			int sum = partialResult[i] + carryReceived;
			carryReceived = sum / 10;
			partialResult[i] = sum % 10;
		}

		carry += carryReceived;

		if (myid != numprocs - 1) {
			MPI_Send(&carry, 1, MPI_INT, myid + 1, 0, MPI_COMM_WORLD);
		}

		MPI_Send(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
		MPI_Send(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
		MPI_Send(partialResult.data(), end - start, MPI_INT, 0, 0, MPI_COMM_WORLD);

		if (myid == numprocs - 1) {
			MPI_Send(&carry, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
		}
	}
}
