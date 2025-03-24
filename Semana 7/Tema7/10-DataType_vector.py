#!/usr/bin/env python

import numpy as np
from mpi4py import MPI
from time import sleep

comm = MPI.COMM_WORLD
world_size = comm.Get_size()
rank = comm.Get_rank()

height = 5
width  = 5
num_of_data = 1 # No. bloques a enviar/recibir

checkerboard_type = MPI.FLOAT.Create_vector(count = 13, blocklength = 1, stride = 2)
    # Tipo básico float32
checkerboard_type.Commit()

if rank == 0:
    send_array = (10+np.arange(width*height,dtype=np.float32)).reshape(height,width)
    comm.Send([send_array, (num_of_data, None), checkerboard_type], dest = 1)
    print("Array on sender:")
    print(send_array)
    print()

if rank == 1:
    rec_array = np.zeros(width*height, dtype = np.float32).reshape(height, width)
    comm.Recv([rec_array, (num_of_data, None), checkerboard_type])
    sleep(1)
    print("Array on receiver:")
    print(rec_array)
    