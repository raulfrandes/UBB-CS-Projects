#include "Var2.h"
#include "Utils.h"

Var2::Var2(int numprocs, int myid, MPI_Status status): numprocs(numprocs), myid(myid), status(status) {}

void Var2::run(std::string fileName)
{
	std::vector<int> number1;
	std::vector<int> number2;
	int maxSize = 0;
	int addedSize = 0;

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

		maxSize = (size1 > size2) ? size1 : size2;
		number1.resize(maxSize, 0);
		number2.resize(maxSize, 0);

		std::string number1Str, number2Str;
		file1 >> number1Str;
		file2 >> number2Str;

		while (number1Str.size() < maxSize) {
			number1Str.push_back('0');
		}
		while (number2Str.size() < maxSize) {
			number2Str.push_back('0');
		}

		for (int i = 0; i < maxSize; i++) {
			number1[i] = number1Str[i] - '0';
			number2[i] = number2Str[i] - '0';
		}

		if (maxSize % numprocs != 0) {
			int newSize = ((maxSize + numprocs - 1) / numprocs) * numprocs;
			number1.resize(newSize, 0);
			number2.resize(newSize, 0);
			addedSize = newSize - maxSize;
			maxSize = newSize;
		}
	}

	MPI_Bcast(&maxSize, 1, MPI_INT, 0, MPI_COMM_WORLD);

	int sizeChunk = maxSize / numprocs;
	std::vector<int> auxNumber1(sizeChunk, 0);
	std::vector<int> auxNumber2(sizeChunk, 0);
	std::vector<int> auxResult(sizeChunk, 0);

	MPI_Scatter(number1.data(), sizeChunk, MPI_INT, auxNumber1.data(), sizeChunk, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Scatter(number2.data(), sizeChunk, MPI_INT, auxNumber2.data(), sizeChunk, MPI_INT, 0, MPI_COMM_WORLD);

	int carry = 0;
	int carryReceived = 0;

	for (int i = 0; i < sizeChunk; i++) {
		int sum = auxNumber1[i] + auxNumber2[i] + carry;
		auxResult[i] = sum % 10;
		carry = sum / 10;
	}

	if (myid > 0) {
		MPI_Recv(&carryReceived, 1, MPI_INT, myid - 1, 0, MPI_COMM_WORLD, &status);

		for (int i = 0; i < sizeChunk && carryReceived != 0; i++) {
			int sum = auxResult[i] + carryReceived;
			carryReceived = sum / 10;
			auxResult[i] = sum % 10;
		}

		carry += carryReceived;
	}

	if (myid < numprocs - 1) {
		MPI_Send(&carry, 1, MPI_INT, myid + 1, 0, MPI_COMM_WORLD);
	}

	std::vector<int> result(maxSize);
	MPI_Gather(auxResult.data(), sizeChunk, MPI_INT, result.data(), sizeChunk, MPI_INT, 0, MPI_COMM_WORLD);

	if (myid == numprocs - 1) {
		MPI_Send(&carry, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
	}

	if (myid == 0) {
		MPI_Recv(&carry, 1, MPI_INT, numprocs - 1, 0, MPI_COMM_WORLD, &status);

		if (addedSize) {
			carry = result[maxSize - addedSize];
		}

		result.resize(maxSize - addedSize);
		if (carry) {
			result.push_back(carry);
		}

		std::string resultFileName = "Numbers/" + fileName + "/Number3Var2.txt";
		Utils::writeNumberToFile(resultFileName, result);
	}
}
