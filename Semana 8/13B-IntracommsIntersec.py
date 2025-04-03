#!/usr/bin/env python
from mpi4py import MPI

com = MPI.COMM_WORLD
miRango = com.Get_rank()
numProcs = com.Get_size()

if numProcs < 7:
    if miRango == 0:
        print("ERROR: lanzar el programa con 7 procesos o más")
    quit()

# Grupo1: pares
if miRango % 2 == 0:
    grupo1 = com.Get_group().Incl(range(0, numProcs, 2))  
else:
    grupo1 = com.Get_group().Incl([]) # Grupo vacío

# Grupo2: algunos procesos
if miRango in [2, 3, 4, 5]:
    grupo2 = com.Get_group().Incl([2, 3, 4, 5])  
else:
    grupo2 = com.Get_group().Incl([]) # Grupo vacío

# Grupo intersección
grupoIntersec = MPI.Group.Intersection(grupo1, grupo2)

# Comunicador intersección
comIntersec = com.Create(grupoIntersec)

if comIntersec != MPI.COMM_NULL:
    miRangoIntersec = comIntersec.Get_rank()
    numProcsIntersec = comIntersec.Get_size()
    print(f"Proceso {miRango} en intersección ({miRangoIntersec}/{numProcsIntersec})")

else:
    print(f"Proceso {miRango} no en intersección")