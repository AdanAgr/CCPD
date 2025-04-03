#!/usr/bin/env python
from mpi4py import MPI
import numpy as np

com = MPI.COMM_WORLD
miRango = com.rank
numProcs = com.size

if numProcs%2 != 0:
    if miRango == 0:
        print("ERROR: lanzar el programa con un número par de procesos")
    quit()

randNum =  np.random.randint(low=0, high=100, dtype=np.int32)

print("Proceso %d/%d: %d" % (miRango, numProcs, randNum))


rangosPar = [i for i in range(numProcs) if i%2 == 0]
# TODO: completar para crear comunicador comPar

grupoPar = com.Get_group().Incl(ranks = rangosPar)
comPar = com.Create_group(group = grupoPar)


rangosImpar = [i for i in range(numProcs) if i%2 != 0]
# TODO: completar para crear comunicador comImpar

grupoImpar = com.Get_group().Incl(ranks = rangosImpar)
comImpar = com.Create_group(group = grupoImpar)


# TODO: modificar para crear variable suma en procesos 0 y 1
if miRango <=1:
    suma = np.zeros(1, dtype = int)
else:
    suma = None

# TODO: usar Reduce en los comunicadores comPar y comImpar para calcular las sumas

if miRango in rangosPar:
    comPar.Reduce(randNum, suma, op = MPI.SUM, root = 0)
elif miRango in rangosImpar:
    comImpar.Reduce(randNum, suma, op = MPI.SUM, root = 0)

if miRango == 0:
    print("Suma de los pares: ", suma)
elif miRango == 1:
    print("Suma de los impares: ", suma)