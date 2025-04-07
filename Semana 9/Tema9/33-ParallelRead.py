#! /usr/bin/env python
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

data = np.zeros(TAMBUFFER, dtype=np.int16)

mpi_file = MPI.File.Open(com, "Parallel2.data",MPI.MODE_RDONLY)
mpi_file.Set_view((2-miRango)*data.nbytes, MPI.SHORT)
mpi_file.Read(data)
mpi_file.Close()

print("Proceso %d/%d lee:"%(miRango, numProcs), data)