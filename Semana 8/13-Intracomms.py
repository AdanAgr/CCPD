#!/usr/bin/env python

from mpi4py import MPI
import numpy as np

num_data = 4

com = MPI.COMM_WORLD
miRango = com.rank
numProcs = com.size
miRangoSub = -9999

vector = np.zeros(num_data, dtype=int)

if numProcs != 5:
   if miRango == 0:
      print("ERROR: lanzar el programa con 5 procesos")
   quit()

grupo = com.Get_group()
Rangos=[2,3,4]
grupoSub = grupo.Incl(Rangos)
comSub = com.Create(grupoSub)

if miRango in Rangos:
   miRangoSub = comSub.rank
   if miRangoSub == 0:
      vector = np.random.randint(0, 10, num_data)
   comSub.Bcast(vector, root=0)

print("Proceso %d/%d:" % (miRango,numProcs), vector)