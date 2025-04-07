#! /usr/bin/env python3

from mpi4py import MPI
import sys

com = MPI.COMM_WORLD
numProcs = com.Get_size()
miRango = com.Get_rank()

nombreServicio, port, info = None,None,MPI.INFO_NULL

if miRango == 0:
    port = MPI.Open_port(MPI.INFO_NULL)
    nombreServicio = "servicio"
    print("Puerto abierto: ", port)
    MPI.Publish_name(nombreServicio,MPI.INFO_NULL,port)

intercom = com.Accept(port,info,root=0)
numCli = intercom.Get_remote_size()

print("Soy el servidor %d y hay %d clientes" % \
    (miRango,numCli))

for i in range(numCli):
    message = intercom.recv(source=i)
    print("Soy el servidor %d, y recibo: %s " % \
        (miRango, message))
 
if (miRango==0):
    MPI.Close_port(port)