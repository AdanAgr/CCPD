#!/usr/bin/env python

from mpi4py import MPI
import numpy as np

L = 3 # tamaño bloque L*L
    
comm = MPI.COMM_WORLD
miRango = comm.rank
numProc = comm.size

if miRango == 0:
    A = np.random.randint(low=0, high=10, size=(L, L*numProc), dtype=np.int32)
    print("Matriz A:")
    print(A)
    A_t = np.empty(0, dtype=np.int32)
    for i in range(numProc):
        A_t = np.concatenate([A_t, A[  ...TODO... ].ravel()])
        # El método .ravel() convierte una matriz en un vector
else:
    A_t = None

bloque=np.empty(shape=(L,L), dtype=np.int32)
comm.Scatter(  ...TODO... )
bloque_sum = np.sum(bloque, axis=1, dtype=np.int32)

if miRango == 0:
    col = np.zeros(shape=(L,1), dtype=np.int32)
else:
    col = None

comm.Reduce( ... TODO ...)
if miRango == 0:
    print("Resultado: ")
    print(col)