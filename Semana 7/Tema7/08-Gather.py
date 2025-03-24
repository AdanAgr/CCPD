#!/usr/bin/env python

from mpi4py import MPI
import numpy as np

LENGTH = 16

comm = MPI.COMM_WORLD
miRango = comm.rank
numProc = comm.size

if (LENGTH%numProc != 0):
   if miRango == 0:
      print("Tienes que usar un número de procesos divisor de",LENGTH)
   quit()

x = np.arange(LENGTH/numProc*miRango, LENGTH/numProc*(miRango+1))
print("Proceso %d/%d: " % (miRango, numProc), end='')
print(x)

if miRango==0:
   y = np.zeros(LENGTH)
else:
   y = None	

comm.Gather(x, y, root=0)

if miRango == 0:
   print("El proceso 0 reunió los elementos:", y)