#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <math.h>
#include <limits.h>

#define MAX_NUM 80000
#define MAX_THR 50

int N;
int numbers[MAX_NUM];
int occurrences[MAX_THR * 2];
int sum = 0;

pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;
pthread_barrier_t barr;

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

	double avg = sum / MAX_THR;
	double diff_min = INT_MAX;
	for (int i = 2; i <= 100; i += 2) {

		if (fabs(occurrences[i] - avg) <= diff_min) {

			diff_min = fabs(occurrences[i] - avg);
		}
	}

	printf("The number %d with the number of occurrences %d and the average %.2lf\n", id, occurrences[id], avg);

	if (fabs(occurrences[id] - avg) == diff_min) {

		printf("The number %d with the number of occurrences %d is the closest to the average %.2lf\n", id, occurrences[id], avg);
	}

	return NULL;
}

int main(int argc, char** argv) {

	scanf("%d", &N);

	int read_number;
	int fd = open("random-file.bin", O_RDONLY);
	for (int i = 0; i < N; i++) {

		read(fd, &read_number, sizeof(int));
		numbers[i] = read_number % 101;
	}

	close(fd);

	pthread_barrier_init(&barr, NULL, MAX_THR);

	pthread_t tid[MAX_THR];
	for (int i = 0; i < MAX_THR; i++) {

		int* arg = (int*)malloc(sizeof(int));
		*arg = 2 * (i + 1);
		pthread_create(&tid[i], NULL, do_work, arg);
	}

	for (int i = 0; i < MAX_THR; i++) {

		pthread_join(tid[i], NULL);
	}

	pthread_barrier_destroy(&barr);

	return 0;
}
