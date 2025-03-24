#!/usr/bin/env python

from mpi4py import MPI
import numpy as np # Al usar arrays de numpy usamos Recv y Send con Mayusculas

num_data = 4

comm = MPI.COMM_WORLD
my_rank = comm.rank
num_processes = comm.size

next_rank = (my_rank + 1) % num_processes
prev_rank = (my_rank - 1) % num_processes

data = np.arange(my_rank*100, my_rank*100+num_data, dtype=np.float64)

comm.Send(data, dest=next_rank, tag=500)



print("Hola, soy el proceso %d (hay %d procesos) y recibo:" % (my_rank, num_processes))

data = np.empty(num_data)

status = MPI.Status()
comm.Recv(data, status = status, source = prev_rank)

print("Del proceso %d: " % prev_rank, data)

