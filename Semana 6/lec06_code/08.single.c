// 08.single.c

#include <omp.h>
#include <stdio.h>

int main (int argc, char *argv[])
{
    int i;
    int a = 10;
    int b[10];

    #pragma omp parallel shared(a,b) private(i)
    {
        // Sólo un hilo ejecuta esta sección
        #pragma omp single
        {
            a = a + 1; // Se ejecuta una vez
            printf("Hay %d hilos\n", omp_get_num_threads());
            printf("Sección single ejecutada por hilo: %d\n",
                   omp_get_thread_num());
        }
        // Barrera implícita aquí 
        // (después del single, a menos que 'nowait')

        #pragma omp for
        for (i = 0; i < 10; i++)
            b[i] = a;  // Todos los hilos participan en el for
    }

    printf("Después de la región paralela\n");
    printf("Valores: ");
    for (i = 0; i < 10; i++)
        printf("%d ", b[i]);
    printf("\n");
    return 0;
}