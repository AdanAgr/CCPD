// 13.synchronization.c

#include <omp.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
    int N = 10;
    int arr[10] = {8, 3, 7, 4, 9, 1, 2, 6, 5, 0};
    int global_sum = 0;
    int global_max = arr[0];

    #pragma omp parallel shared(arr, global_sum, global_max)
    {
        // 1) Suma atómica
        #pragma omp for
        for (int i = 0; i < N; i++) {
            #pragma omp atomic
            global_sum += arr[i];
        }

        // 2) Barrera: todos esperan antes de continuar
        #pragma omp barrier

        // 3) Zona crítica: cálculo de máximo
        #pragma omp for
        for (int i = 0; i < N; i++) {
            #pragma omp critical
            {
                if (arr[i] > global_max) {
                    global_max = arr[i];
                }
            }
        }

        // 4) Sección 'single': un solo hilo imprime la suma
        #pragma omp single
        {
            printf("global_sum = %d\n", global_sum);
        }

        // 5) Sección 'master': sólo el hilo maestro imprime el máximo
        #pragma omp master
        {
            printf("global_max = %d (impreso por hilo maestro)\n", global_max);
        }
    }
    return 0;
}
