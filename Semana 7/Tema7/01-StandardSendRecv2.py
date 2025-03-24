#!/usr/bin/env python

from mpi4py import MPI
import numpy as np # Al usar arrays de numpy usamos Recv y Send con Mayusculas

num_data = 4

comm = MPI.COMM_WORLD
my_rank = comm.rank
num_processes = comm.size

receiver_rank = num_processes - 1

if my_rank != receiver_rank:
   data = np.arange(my_rank*100, my_rank*100+num_data, dtype=np.float64)
   comm.Send(data, dest=receiver_rank, tag=500)
else:
   print("Hola, soy el proceso %d (hay %d procesos) y recibo:" % (my_rank, num_processes))
   for source_rank in range(0,receiver_rank):
      data = np.empty(num_data)
      comm.Recv(data, source=source_rank, tag=500) # Si quitamos el sourceRank dejan de ir en orden
      
      '''
      status = MPI.Status()
      comm.Recv(data, status = status)
      source_rank = status.Get_source()
      '''
    
      print("Del proceso %d: " % source_rank, data)

