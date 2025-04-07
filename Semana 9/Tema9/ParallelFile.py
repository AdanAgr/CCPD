#!/usr/bin/env python
from mpi4py import MPI
import numpy as np

com = MPI.COMM_WORLD
numProcs = com.Get_size()
miRango = com.Get_rank()
TAMMBUFFER = 1000
numVectors = 3

if numProcs!=numVectors:
   if miRango==0:
      print("ERROR: el programa debe ser ejecutado con 3 procesos")
   quit()

data = np.zeros(TAMMBUFFER, dtype=np.float64)

mpi_file = MPI.File.Open(com, "data.bin", MPI.MODE_RDONLY)
mpi_file.Set_view(miRango*data.nbytes, MPI.DOUBLE)
mpi_file.Read(data)

print("Proceso %d/%d lee:%d  elementos y la media es= %.3f"% (miRango, numProcs,TAMMBUFFER, np.mean(data)))

mpi_file.Close()