// 09.combinedB.c

#include <omp.h>
#include <stdio.h>

int main (int argc, char *argv[])
{
    int i;
    int n = 12;

    #pragma omp parallel shared(n) private(i)
    {
        #pragma omp for
        for (i = 0; i < n; i++)
            printf("Hilo %d - Iteración %d\n", 
                   omp_get_thread_num(), i);
    }

    return 0;
}