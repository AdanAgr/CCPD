#!/usr/bin/env python

from mpi4py import MPI
# import numpy as np
# Al no usar arrays de numpy y usar arrays de python directamente 
# usamos tanto send como recv con minusculas

comm = MPI.COMM_WORLD
my_rank = comm.rank
num_processes = comm.size

if my_rank != 0:
   data = "Saludos del proceso {}".format(my_rank)
   comm.send(data, dest=0, tag=500)
else:
   print("Hola, soy el proceso %d (hay %d procesos) y recibo:" % (my_rank, num_processes))
   for source_rank in range(1,num_processes):   
      data = comm.recv(source=source_rank, tag=500)
      print(data)