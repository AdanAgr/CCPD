// 04.Conspar.c

#include <omp.h>
#include <stdio.h>
int main (int argc, char *argv[])
{
        int tid;
        #pragma omp parallel private(tid)
        {
                tid = omp_get_thread_num();
                printf("Región paralela del hilo %d\n", tid);
                if (tid % 2) {
                        printf("Soy un hilo impar (%d)\n", tid);
                } else {
                        printf("Soy un hilo par (%d)\n", tid);
                }
        }
}

