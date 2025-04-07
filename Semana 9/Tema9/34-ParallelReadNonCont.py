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

data = np.zeros(4, dtype=np.int16)
data_aux = np.zeros(1, dtype=np.int16)


mpi_file = MPI.File.Open(com, "Parallel2.data",MPI.MODE_RDONLY)
data_type = MPI.SHORT.Create_vector(4, 1, 3)
data_type.Commit()
mpi_file.Set_view(miRango*data_aux.nbytes, data_type)
mpi_file.Read(data)
mpi_file.Close()

print("Proceso %d/%d lee:"%(miRango, numProcs), data)