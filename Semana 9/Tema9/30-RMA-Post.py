#! /usr/bin/env python

import numpy
from mpi4py import MPI

comm = MPI.COMM_WORLD
numProcs = comm.Get_size()
miRango = comm.Get_rank()

if numProcs!=2:
    if miRango==0:
        print("ERROR: el programa debe ser ejecutado con 2 procesos")
    quit()

if miRango == 1:
    a = numpy.zeros(9, dtype=numpy.int16)
    print("Proceso",miRango,"comparto", a)
    window = MPI.Win.Create(a, comm=comm)
elif miRango == 0:
    window = MPI.Win.Create(None, comm=comm)

if miRango == 1:
    g1 = comm.group.Incl([0])
    window.Post(group=g1)
    window.Wait()
elif miRango == 0:
    g2 = comm.group.Incl([1])
    b = numpy.full(9, 1, dtype=numpy.int16)
    print("Proceso",miRango,"escribo", b)
    window.Start(group=g2)
    window.Put(b, target_rank=1)
    window.Complete()

if miRango == 1:
    print("Proceso",miRango,"resultado", a)