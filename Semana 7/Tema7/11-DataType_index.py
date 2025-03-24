#!/usr/bin/env python

import numpy as np
from mpi4py import MPI
from time import sleep

comm = MPI.COMM_WORLD
world_size = comm.Get_size()
rank = comm.Get_rank()

height = 6 #filas
width  = 6 #columnas

num_of_data = 2 # Nº de bloques a enviar/recibir
pos_of_data = 2


col_type = MPI.INT64_T.Create_indexed(np.full(height ,2, dtype=np.float64),[c*width + pos_of_data for c in range(height)])
    # Tipo básico float64
col_type.Commit()

if rank == 0:
    send_array = (10+np.arange(width*height,dtype=np.float64)).reshape(height,width)
    comm.Send([send_array, (num_of_data, None), col_type], dest = 1)
    print("Array on sender:")
    print(send_array)
    print()

if rank == 1:
    rec_array = np.zeros(width*height, dtype = np.float64).reshape(height, width)
    comm.Recv([rec_array, (num_of_data, None), col_type])
    sleep(1)
    print("Array on receiver:")
    print(rec_array)
    