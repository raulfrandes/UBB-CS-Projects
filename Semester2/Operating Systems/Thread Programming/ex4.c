#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <fcntl.h>
#include <float.h>
#include <math.h>

#define MAX_NUM 90000
#define MIN_NUM 30000
#define MIN_THR 10

int N;
int numbers[MAX_NUM];
int num_threads;
double diff_min = DBL_MAX;

pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;

void* read_numbers(void* arg) {

	int fd = open("random-file.bin", O_RDONLY);
	if (fd == -1) {

		perror("open()");
		exit(EXIT_FAILURE);
	}

	for (int i = 0; i < N; i++) {

		read(fd, &numbers[i], sizeof(int));
	}

	close(fd);

	return NULL;
}

void* do_work(void* arg) {

	int id = *(int*)arg;
	free(arg);

	int left = id * (N / num_threads);
	int right = (id + 1) * (N / num_threads);
	int sum = 0;
	for (int i = left; i < right; i++) {

		sum += numbers[i];
	}
	double avg = sum / (N / num_threads);

	int M = rand() % 256;

	double diff = fabs(avg - M);

	printf("The difference between the number %d and the average %.2lf of the interval %d is %.2lf\n", M, avg, id, diff);

	pthread_mutex_lock(&mtx);
	if (diff <= diff_min) {

		diff_min = diff;
	}
	pthread_mutex_unlock(&mtx);

	return NULL;
}

int main(int argc, char** argv) {

	scanf("%d", &N);

	pthread_t reader;
	pthread_create(&reader, NULL, read_numbers, NULL);
	pthread_join(reader, NULL);

	srand(time(NULL));

	num_threads = MIN_THR * (N / MIN_NUM);
	pthread_t tid[num_threads];
	for (int i = 0; i < num_threads; i++) {

		int* arg = (int*)malloc(sizeof(int));
		*arg = i;
		pthread_create(&tid[i], NULL, do_work, arg);
	}

	for (int i = 0; i < num_threads; i++) {

		pthread_join(tid[i], NULL);
	}

	printf("The minimum difference is: %.2lf\n", diff_min);

	return 0;
}
