#!/usr/bin/env python
from mpi4py import MPI
import numpy as np

arraysize = 5

comm = MPI.COMM_WORLD
my_rank = comm.rank
num_processes = comm.size

if my_rank == 0:
   vector = np.random.rand(arraysize)
   print("Vector en %d: " % my_rank, end='')
   for elem in vector:
      print("%.3f " % elem, end='')
   print()
else:
   vector = np.zeros(arraysize)

comm.Bcast(vector, root=0)

if my_rank != 0:
   print("Proceso %d/%d: " % (my_rank,num_processes), end='')
   for elem in vector:
      print("%.3f " % elem, end='')
   print()