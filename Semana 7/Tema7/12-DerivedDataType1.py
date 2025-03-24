#!/usr/bin/env python

from mpi4py import MPI
import numpy as np

comm = MPI.COMM_WORLD
my_rank = comm.rank
num_processes = comm.size

if num_processes != 2:
   if my_rank==0:
      print("*** Error: ejecutar programa con dos procesos ***")
   quit()

if my_rank==0:
   a = float(input("Introduce a (float): "))
   b = float(input("Introduce b (float): "))
   n = int(input("Introduce n (int): "))	

npydt = np.dtype('f8, f8, i4')
npydt.names=['a', 'b', 'n']

if my_rank == 0:
   print("Proceso 0: mandando datos a proceso 1")
   x_send = np.array((a,b,n), dtype=npydt)
   comm.send(x_send, dest=1)

else:
   y_recv = np.zeros(3, dtype=npydt)
   y_recv = comm.recv(source=0)
   print("Proceso 1: recibiendo datos de proceso 0")
   print("Recibidos valores %3.1f, %3.1f, %d" % (y_recv['a'],y_recv['b'],y_recv['n']))
   