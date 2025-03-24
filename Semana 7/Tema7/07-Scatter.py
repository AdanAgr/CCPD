#!/usr/bin/env python

from mpi4py import MPI
import numpy as np

LENGTH = 24

comm = MPI.COMM_WORLD
miRango = comm.rank
numProc = comm.size

if numProc%2 != 0:
   if miRango == 0:
      print("ERROR: Tienes que usar un número par de procesos")
   quit()

if miRango == 0:
   x = np.arange(LENGTH, dtype=np.float64)
else:
   x = None
y = np.empty(int(LENGTH/numProc), dtype=np.float64)

if miRango == 0:
   print("Proceso %d/%d distribuyendo %d elementos" % (miRango, numProc, LENGTH))

comm.Scatter(x, y, root=0)
print("Proceso %d/%d: " % (miRango, numProc), end='')
for elem in y:
   print("%d " % elem, end='')
print()