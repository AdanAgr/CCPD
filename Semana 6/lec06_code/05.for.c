// 05.for.c
#include <omp.h>
#include <stdio.h>
int main (int argc, char *argv[])
{
    int n = 12;
    #pragma omp parallel for shared(n)
    for (int i = 0; i < n; i++)
        printf("Hilo %d  -  Iteración %d\n",
               omp_get_thread_num(), i);
    printf("Fuera del for\n");
}