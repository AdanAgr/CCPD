#! /usr/bin/env python
from mpi4py import MPI
import numpy as np
import sys

nHijos = 8

if len(sys.argv)==1:
    com = MPI.COMM_WORLD
    numProcs = com.Get_size()
    miRango = com.Get_rank()

    if numProcs != 1:
        if miRango == 0:
            print("ERROR: lanzar el programa con un proceso")
        quit()

    # Genera la matriz y el vector
    vector=np.arange(start=1, stop=nHijos+1, dtype=int)
    matriz=np.tile(vector, (nHijos,1))
    for i in range(nHijos):
        matriz[i] = matriz[i] + i*nHijos
    print("Vector:\n",vector)
    print("Matriz:\n",matriz)
    
    intercom = com.Spawn(sys.executable, args=[sys.argv[0], "hijo"], maxprocs=nHijos)

    num = np.zeros(1, dtype=int)
    result_vector = np.zeros(nHijos, dtype=int)
    for i in range(nHijos):
        intercom.Send(matriz[i], dest=i)
        intercom.Send(vector, dest=i)
        intercom.Recv(num, source=i)
        result_vector[i]=num[0]
    print("Resultado:\n",result_vector)

elif sys.argv[1] == "hijo":
    intercom = MPI.Comm.Get_parent()
    miRangoHijo = intercom.Get_rank()
    fila = np.zeros(nHijos, dtype=int)
    intercom.Recv(fila, source=0)
    vector = np.zeros(nHijos, dtype=int)
    intercom.Recv(vector, source=0)
    result = np.dot(vector,fila)
    intercom.Send(result,dest=0)