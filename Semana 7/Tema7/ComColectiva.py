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
        #A_t = np.concatenate([A_t, A[  ...TODO... ].ravel()])
        A_t = np.concatenate([A_t, A[0:L,i*L:(i+1)*L ].ravel()])
        # En este caso hacemos bloques de matrices 3x3
        #Al enviar estructuras de numpy enviamos un vector directamente, no la matriz 3x3
        # Despues hacemos un scatter para conseguir esa matriz 3x3
        # 0 te da 0,1,2
        # 1 te da 3,4 ,5 
        # 2 te da 6,7,8
        # El método .ravel() convierte una matriz en un vector
else:
    A_t = None

bloque=np.empty(shape=(L,L), dtype=np.int32)
#comm.Scatter(  ...TODO... )
comm.Scatter(A_t, recvbuf= bloque, root=0) # Send, recvBuffer, root
bloque_sum = np.sum(bloque, axis=1, dtype=np.int32)

if miRango == 0:
    col = np.zeros(shape=(L,1), dtype=np.int32) 
    # Como recive el 0 debemos inicializarlo en el 0
else:
    col = None

comm.Reduce(bloque_sum, col , op=MPI.SUM, root=0)
if miRango == 0:
    print("Resultado: ")
    print(col)