#! /usr/bin/env python3
from mpi4py import MPI
import sys

com = MPI.COMM_WORLD
numProcs = com.Get_size()
miRango = com.Get_rank()

message = "saludos del proceso %d" % miRango

port = MPI.Lookup_name('servicio')
intercom=com.Connect(port,MPI.INFO_NULL,root=0)
print("Port name %s" % port)

numServ = intercom.Get_remote_size()
print("Soy el cliente %d y hay %d servidores " % \
    (miRango,numServ))

for i in range(numServ):
    intercom.send(message,i)