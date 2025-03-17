// 06.sections.c

#include <windows.h>
#include <omp.h>
#include <stdio.h>
#include <time.h>


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
            Sleep(500);
         }

         #pragma omp section
         {
            printf("Sección 2  -  Hilo %d\n",
            omp_get_thread_num());
            Sleep(400);
         }

         #pragma omp section
         {
            printf("Sección 3  -  Hilo %d\n",
            omp_get_thread_num());
            Sleep(500);
         }
      }
   }
}
