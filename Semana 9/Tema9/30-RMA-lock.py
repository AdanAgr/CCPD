#! /usr/bin/env python

import numpy as np
from mpi4py import MPI
import time

com = MPI.COMM_WORLD
numProcs = com.Get_size()
miRango = com.Get_rank()

if numProcs!=3:
    if miRango==0:
        print("ERROR: el programa debe ser ejecutado con 3 procesos")
    quit()

if miRango == 0:
    a = np.arange(9, dtype=np.int16)
    print("Proceso",miRango,"comparto", a)
    window = MPI.Win.Create(a, comm=com)
else:
    window = MPI.Win.Create(None, comm=com)

if miRango == 1:
    b = 10 + np.arange(9, dtype=np.int16)
    window.Lock(0)
    time.sleep(3)
    window.Put(b, target_rank=0)
    window.Unlock(0)


window.Free()
com.Barrier()

if miRango == 0:
    print("Proceso",miRango,"resultado", a)
