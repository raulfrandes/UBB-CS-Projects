#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <math.h>

#define MAX_NUM 70000
#define MAX_THR 10

int N;
int numbers[MAX_NUM];
int occurrences[MAX_THR];
int sum = 0;

pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;
pthread_barrier_t barr;

void* do_work(void* arg) {

	int id = *(int*)arg;
	free(arg);

	for (int i = 0; i < N; i++) {

		if (numbers[i] % 10 == id) {

			occurrences[id]++;
		}
	}

	pthread_mutex_lock(&mtx);

	sum += occurrences[id];

	pthread_mutex_unlock(&mtx);
	pthread_barrier_wait(&barr);

	double avg = sum / MAX_THR;
	if (fabs(avg - occurrences[id]) < 500000) {

		printf("The digit %d appears %d times being the closest to the average %.2lf\n", id, occurrences[id], avg);
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
	}

	close(fd);

	pthread_barrier_init(&barr, NULL, MAX_THR);

	pthread_t tid[MAX_THR];
	for (int i = 0; i < MAX_THR; i++) {

		int* arg = (int*)malloc(sizeof(int));
		*arg = i;
		pthread_create(&tid[i], NULL, do_work, arg);
	}

	for (int i = 0; i < MAX_THR; i++) {

		pthread_join(tid[i], NULL);
	}

	pthread_barrier_destroy(&barr);

	return 0;
}
