// 06.sections.c

#include <omp.h>
#include <stdio.h>
int main (int argc, char *argv[])
{
   #pragma omp parallel
   {
      if (omp_get_thread_num() == 0)
         printf("Num. hilos: %d\n",
         omp_get_num_threads());
      #pragma omp sections
      {
         #pragma omp section
         {
            printf("Sección 1  -  Hilo %d\n",
            omp_get_thread_num());
            usleep(500000);
         }

         #pragma omp section
         {
            printf("Sección 2  -  Hilo %d\n",
            omp_get_thread_num());
            usleep(400000);
         }

         #pragma omp section
         {
            printf("Sección 3  -  Hilo %d\n",
            omp_get_thread_num());
            usleep(50000);
         }
      }
   }
}
