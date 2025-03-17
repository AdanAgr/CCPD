// 12.barrier.c


#include <omp.h>
#include <stdio.h>
#include <time.h>

int main (int argc, char *argv[])
{
   int TID;
   #pragma omp parallel private(TID)
   {
      char buff[100];
      time_t now;
      TID = omp_get_thread_num();
      if (TID < omp_get_num_threads() / 2) {
         usleep(2000000);

      }
      now = time (0);
      strftime (buff, 100, "%H:%M:%S.0", localtime (&now));
      printf ("Thread %d before barrier: %s\n", TID, buff);

      #pragma omp barrier

      now = time (0);
      strftime (buff, 100, "%H:%M:%S.0", localtime (&now));
      printf ("Thread %d after barrier: %s\n", TID, buff);
   }
}