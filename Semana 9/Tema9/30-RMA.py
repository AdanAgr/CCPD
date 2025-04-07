#! /usr/bin/env python
from mpi4py import MPI
import numpy as np

com = MPI.COMM_WORLD
numProcs = com.Get_size()
miRango = com.Get_rank()

if miRango==0:
    expo = 3
    cad = "Vamos a calcular: "
    for i in range(1,numProcs+1):
        cad = cad + "(%d^%d)" % (i,expo)
        if i!=numProcs:
            cad = cad +"+"
    print(cad)

if miRango==0:
    expo_mem = np.array([expo])
    result_mem = np.array([0])
else:
    expo_mem = None
    result_mem = None

expo_win = MPI.Win.Create(expo_mem,comm=com)
result_win = MPI.Win.Create(result_mem,comm=com)

expo_leido = np.array([0])
expo_win.Fence()
expo_win.Get(expo_leido, target_rank=0)
expo_win.Fence()

mi_numero = pow((miRango+1),expo_leido[0])
print("Proceso %d/%d: exponente=%d - número=%d" % (miRango, numProcs, expo_leido[0], mi_numero))

result_win.Fence()
result_win.Accumulate(mi_numero, target_rank=0, op=MPI.SUM)
result_win.Fence()

if miRango == 0:
    print("El resultado es:",result_mem[0])

expo_win.Free()
result_win.Free()