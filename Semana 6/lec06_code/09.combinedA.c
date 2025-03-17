// 09.combinedA.c

#include <omp.h>
#include <stdio.h>

int main (int argc, char *argv[])
{
    int i;
    int n = 12;

    // parallel + for en una sola directiva
    #pragma omp parallel for shared(n) private(i)
    for (i = 0; i < n; i++)
        printf("Hilo %d - Iteración %d\n", 
               omp_get_thread_num(), i);

    return 0;
}