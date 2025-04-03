#!/usr/bin/env python
from mpi4py import MPI
import numpy as np

com = MPI.COMM_WORLD
numProcs = com.size
miRango = com.rank

if numProcs != 9:
   if miRango == 0:
      print("ERROR: lanzar el programa con 9 procesos")
   quit()

miValor=np.array([miRango],dtype=float)

if miRango==0:
   matrizOld = np.zeros(shape=(3,3))
else:
   matrizOld = None

com.Gather(miValor, matrizOld, root=0)

if miRango==0:
   print("Valores viejos:")
   print(matrizOld)

comSub = com.Split(color = miRango/3)
comSub.Bcast(miValor,root=0)

if miRango==0:
   matrizNew = np.zeros(shape=(3,3))
else:
   matrizNew = None

com.Gather(miValor, matrizNew, root=0)

if miRango==0:
   print("Valores nuevos:")
   print(matrizNew)