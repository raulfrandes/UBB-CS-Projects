#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <time.h>

int digits_sum(int n) {

	int sum = 0;
	while (n > 0) {

		sum += n % 10;
		n /= 10;
	}
	return sum;
}

int digits_product(int n) {

	int prod = 1;
	while (n > 0) {

		prod *= n % 10;
		n /= 10;
	}
	return prod;
}

int main(int argc, char** argv) {

	int a2b[2];
	int res = pipe(a2b);
	if (res == -1) {

		perror("pipe()");
		exit(EXIT_FAILURE);
	}

	int pid = fork();
	if (pid == -1) {

		perror("fork()");
		exit(EXIT_FAILURE);
	}

	if (pid == 0) {

		close(a2b[1]);

		FILE* fout = fopen("file_ex1.txt", "w");
		if (fout == NULL) {

			perror("fopen()");
			exit(EXIT_FAILURE);
		}

		while (1) {

			int k, N, rez;

			read(a2b[0], &k, sizeof(int));

			if (k == 0) break;

			read(a2b[0], &N, sizeof(int));

			if (k % 2 == 0) {

				rez = digits_sum(N);
			}
			else {

				rez = digits_product(N);
			}

			fprintf(fout, "%d %d %d\n", k, N, rez);
		}

		close(a2b[0]);
		fclose(fout);
		exit(EXIT_SUCCESS);
	}

	close(a2b[0]);

	while (1) {

		int k;
		printf("k: ");
		scanf("%d", &k);

		write(a2b[1], &k, sizeof(int));

		if (k == 0) break;

		srand(time(NULL));
		int N = rand() % 1000;

		write(a2b[1], &N, sizeof(int));

		sleep(1);
	}

	int status;
	wait(&status);

	close(a2b[1]);

	return 0;
}
