// 07.subroutines.c
#include <omp.h>
#include <stdio.h>
int main (int argc, char *argv[])
{
   void CuentaAtras() {
      int cont;
      printf("**** Hilo %d  - Invocando cuenta atrás ****\n",
             omp_get_thread_num());
      for (cont = 10; cont >= 0; cont--) {
         printf("**** %d ****\n", cont);
         usleep(500000);
      }
      printf("**** Cuenta atrás finalizada ****\n");
   }

   void CuentaAdelante() {
      int cont;
      printf("#### Hilo %d  - Invocando cuenta adelante ####\n",
             omp_get_thread_num());
      for (cont = 0; cont <= 10; cont++) {
         printf("#### %d ####\n", cont);
         usleep(1000000);
      }
      printf("#### Cuenta adelante finalizada ####\n");
   }

   #pragma omp parallel
   {
      if (omp_get_thread_num() == 0)
         printf("Num. hilos: %d\n",
                omp_get_num_threads());
      #pragma omp sections
      {
         #pragma omp section
         {
            CuentaAdelante();
         }

         #pragma omp section
         {
            CuentaAtras();
         }
      }
   }
}
