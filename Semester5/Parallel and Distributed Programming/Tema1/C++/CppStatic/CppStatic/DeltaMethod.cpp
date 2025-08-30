#include "DeltaMethod.h"
#include "Utils.h"
#include <thread>
#include <vector>

void DeltaMethod::task(int result[N][M], int start, int end)
{
	for (int index = start; index < end; index++)
	{
		int i = index / M;
		int j = index % M;
		result[i][j] = Utils::convolveAt(matrix, kernel, i, j);
	}
}

void DeltaMethod::start(int result[N][M])
{
	std::vector<std::thread> threads(p);

	int totalElements = N * M;

	int start = 0;
	int end = totalElements / p;
	int rest = totalElements % p;

	for (int i = 0; i < p; i++)
	{
		if (rest > 0)
		{
			end++;
			rest--;
		}

		threads[i] = std::thread(&DeltaMethod::task, this, std::ref(result), start, end);

		start = end;
		end = start + totalElements / p;
	}

	for (int i = 0; i < p; i++)
	{
		threads[i].join();
	}
}
