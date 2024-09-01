#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <math.h>
#include <string.h>

#define MAX_THR 25

int N;
int numbers[100000];
int occurrences[101];
int sum = 0;

pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;
pthread_barrier_t barr;

int is_prime(int n) {

	for (int i = 2; i * i <= n; i++) {

		if (n % i == 0) {

			return 0;
		}
	}
	return 1;
}

void* do_work(void* arg) {

	int id = *(int*)arg;
	free(arg);

	for (int i = 0; i < N; i++) {

		if (numbers[i] == id) {

			occurrences[id]++;
		}
	}

	pthread_mutex_lock(&mtx);

	sum += occurrences[id];

	pthread_mutex_unlock(&mtx);
	pthread_barrier_wait(&barr);

	float avg = sum / MAX_THR;

	if (fabs(avg - occurrences[id]) < 2) {

		printf("The number %d appears %d times being the closest to the mean %.2f\n", id, occurrences[id], avg);
	}

	return NULL;
}

int main(int argc, char** argv) {

	scanf("%d", &N);

	int fd = open("random-file.bin", O_RDONLY);
	if (fd == -1) {

		perror("open()");
		exit(EXIT_FAILURE);
	}

	for (int i = 0; i < N; i++) {

		read(fd, &numbers[i], sizeof(int));
		numbers[i] = numbers[i] % 10001;
	}

	close(fd);

	memset(occurrences, 0, sizeof(int) * 100);

	pthread_barrier_init(&barr, NULL, MAX_THR);
	pthread_t tid[MAX_THR];

	int current = 0;
	for (int i = 2; i < 100; i++) {

		if (is_prime(i)) {
			int* arg = (int*)malloc(sizeof(int));
			*arg = i;
			pthread_create(&tid[current], NULL, do_work, arg);
			current++;
		}
	}

	for (int i = 0; i < MAX_THR; i++) {

		pthread_join(tid[i], NULL);
	}

	pthread_barrier_destroy(&barr);

	return 0;
}
