#!/usr/bin/env python

from mpi4py import MPI

comm = MPI.COMM_WORLD
my_rank = comm.rank
num_processes = comm.size

if my_rank != 0:
	data = "Saludos del proceso {}".format(my_rank)
	comm.send(data, dest=0)

else:
	print("Hola, soy el proceso %d/%d y recibo:" % (my_rank, num_processes))
	for source_rank in range(1,num_processes):		
		data_in = comm.recv(source = source_rank)
		print("  "+data_in)