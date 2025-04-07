#!/usr/bin/env python
from mpi4py import MPI
import numpy as np
TAMBUFFER=4

com = MPI.COMM_WORLD
numProcs = com.Get_size()
miRango = com.Get_rank()

if numProcs!=3:
    if miRango==0:
        print("ERROR: el programa debe ser ejecutado con 3 procesos")
    quit()

data = np.array([miRango*TAMBUFFER+i for i in range(TAMBUFFER)], dtype=np.int16)

mpi_file = MPI.File.Open(com, "Parallel2.data", MPI.MODE_WRONLY + MPI.MODE_CREATE)
mpi_file.Set_view(miRango*data.nbytes, MPI.SHORT)
mpi_file.Write(data)
mpi_file.Close()

print("Proceso %d/%d escribe:"%(miRango, numProcs), data)