#!/usr/bin/env python

from mpi4py import MPI
import numpy as np

comm = MPI.COMM_WORLD
my_rank = comm.rank
num_processes = comm.size

num = np.zeros(1)
num[0] = my_rank+1

if my_rank == 0:
    fact = np.zeros(1)
else:
    fact = None

comm.Reduce(num, fact, op=MPI.PROD, root=0)

if my_rank == 0:
    print("Proceso %d/%d: fact(%d)=%d" % (
        my_rank, num_processes, num_processes ,fact[0]))