#!/usr/bin/env python

from mpi4py import MPI

comm = MPI.COMM_WORLD
my_rank = comm.rank
num_processes = comm.size

if num_processes != 2:
    if my_rank==0:
        print("*** Error: ejecutar programa con dos procesos ***")
    quit()

if my_rank==0:
    a = float(input("Introduce a (float): "))
    b = float(input("Introduce b (float): "))
    n = int(input("Introduce n (int): "))
    data = {'a':a, 'b':b, 'n':n}
    print("Proceso 0: mandando datos a proceso 1")
    comm.send(data, dest=1)

else:
    datarecv = comm.recv(source=0)
    print("Proceso 1: recibiendo datos de proceso 0")
    print("Recibidos valores",datarecv) 
    