// 10.lastprivate.c

#include <omp.h>
#include <stdio.h>

int main() {
   int i, a;
   int n = 5;

   #pragma omp parallel for shared(n) private(i) lastprivate(a)
   for (i = 0; i < n; i++) {
      a = i + 1;
      printf("Hilo %d: a=%d (iteración %d)\n",
             omp_get_thread_num(), a, i);
   }
   // Aquí 'a' tendrá el valor de la última iteración (i=4) 
   printf("Valor final a=%d\n", a);
   return 0;
}
