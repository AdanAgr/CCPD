#!/usr/bin/env python

from mpi4py import MPI
import numpy as np

com = MPI.COMM_WORLD
numProcs = com.size
miRango = com.rank
miRango_vec = np.array([com.rank])

if numProcs != 8:
    if miRango == 0:
        print("ERROR: lanzar el programa con 8 procesos")
    quit()

grupo = miRango/4
comSub = com.Split(color = grupo)

if grupo == 0:
    local_leader = 0
    remote_leader = 4
else:
    local_leader = 0
    remote_leader = 0

comInt = MPI.Intracomm.Create_intercomm(comSub, local_leader, com, remote_leader)

miRangoInt = comInt.rank

print("Rango %d  -  RangoInt %d  -  Grupo %d" %(miRango, miRangoInt, grupo))

if miRango <= 3:
    print("Hola, soy %d (%d) y envío mi rango a mi colega de la derecha" % (miRangoInt, miRango))
    comInt.Send(miRango_vec, dest = miRango)

if miRango >= 4:
    valor = np.zeros(1)
    comInt.Recv(valor)
    print("Hola, soy %d (%d) y recibo un: %d de mi colega de la izqda." %(miRangoInt, miRango, valor[0]))