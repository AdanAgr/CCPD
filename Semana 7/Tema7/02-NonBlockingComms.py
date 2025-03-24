#!/usr/bin/env python
from mpi4py import MPI
import numpy as np
import time

sizeRand = 500000

comm = MPI.COMM_WORLD
my_rank = comm.rank
num_processes = comm.size

if num_processes!=2:
   if my_rank == 0:
      print("*** ERROR: lanzar con dos procesos ***")

else:
   if my_rank == 0:
      randNum = np.random.random_sample(sizeRand)
      print("Tarea 0: esperando comienzo de envío", flush=True)
      request = comm.Isend(randNum,dest=1)
      print("Tarea 0: envío comenzado (%.3f, %.3f)" % (randNum[0], randNum[1]), flush=True)
      request.Wait()
      print("Tarea 0: envío finalizado", flush=True)

   elif my_rank == 1:
      time.sleep(1)
      randNum2 = np.zeros(sizeRand)
      print("Tarea 1: esperando comienzo de recepción", flush=True)
      request = comm.Irecv(randNum2, source=0)
      print("Tarea 1: recepción comenzada", flush=True)
      request.Wait()
      print("Tarea 1: recepción finalizada (%.3f, %.3f)" % (randNum2[0], randNum2[1]), flush=True)