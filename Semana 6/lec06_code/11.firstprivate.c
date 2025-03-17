// 11.firstprivate.c

#include <omp.h>
#include <stdio.h>
int main (int argc, char *argv[])
{
   int x = 5;
   int y = 7;
   #pragma omp parallel private(x) firstprivate(y)
   {
      // x no está inicializado (indefinido en cada hilo)
      // y empieza en 7 en cada hilo
      printf("Hilo %d (x=%d, y=%d)\n", 
           omp_get_thread_num(), x, y);
   }
}
---