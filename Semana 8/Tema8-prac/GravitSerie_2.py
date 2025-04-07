#!/usr/bin/env python
from mpi4py import MPI
import numpy as np
from numba import njit

# Constantes de la simulación
G = 6.674e-11
NUM_ITER = 100001
NUM_ITER_SHOW = 5000

# Inicialización de MPI
comm = MPI.COMM_WORLD
rank = comm.Get_rank()
size = comm.Get_size()

# Selecciona el fichero de datos a usar:
# Para 256 objetos, usa "data256"
# Para 1024 objetos, usa "data1024"
DATAFILE = "data1024"
RESULTFILE = DATAFILE + "_result.txt"

# Crear un tipo derivado que agrupa 5 doubles (x, y, vx, vy, m)
satellite_type = MPI.DOUBLE.Create_contiguous(5)
satellite_type.Commit()

# El proceso 0 lee el fichero y crea la matriz de datos
if rank == 0:
    with open(DATAFILE + ".txt", 'r') as f:
        N = int(f.readline().strip())
        data = np.zeros((N, 5), dtype='d')
        for i in range(N):
            data[i, 0] = float(f.readline().strip())  # x
            data[i, 1] = float(f.readline().strip())  # y
            data[i, 2] = float(f.readline().strip())  # vx
            data[i, 3] = float(f.readline().strip())  # vy
            data[i, 4] = float(f.readline().strip())  # m
else:
    N = None

# Difundir el número total de objetos a todos los procesos
N = comm.bcast(N, root=0)

# Calcular el reparto de objetos para cada proceso
counts = np.array([N // size + (1 if r < (N % size) else 0) for r in range(size)], dtype=int)
displs = np.zeros(size, dtype=int)
displs[0] = 0
for r in range(1, size):
    displs[r] = displs[r-1] + counts[r-1]
local_count = counts[rank]

# Cada proceso reserva un array para sus objetos (forma: [local_count x 5])
local_data = np.empty((local_count, 5), dtype='d')

# Distribuir los datos mediante Scatterv; counts y displs se interpretan en unidades de objetos
if rank == 0:
    comm.Scatterv([data, counts, displs, satellite_type], local_data, root=0)
else:
    comm.Scatterv([None, None, None, satellite_type], local_data, root=0)

# global_offset es el índice global del primer objeto asignado a este proceso
global_offset = displs[rank]

# Buffer para reunir la información global en cada iteración
global_data = np.empty((N, 5), dtype='d')
recvcounts = counts.copy()   # número de objetos de cada proceso
recvdispls = displs.copy()

# Función compilada con Numba para actualizar los objetos
@njit
def update_objects(local_data, global_data, global_offset, N, G):
    local_count = local_data.shape[0]
    for i in range(local_count):
        global_idx = global_offset + i
        xi = local_data[i, 0]
        yi = local_data[i, 1]
        vxi = local_data[i, 2]
        vyi = local_data[i, 3]
        mi = local_data[i, 4]
        ax_total = 0.0
        ay_total = 0.0
        for j in range(N):
            if j == global_idx:
                continue
            xj = global_data[j, 0]
            yj = global_data[j, 1]
            mj = global_data[j, 4]
            dx = xj - xi
            dy = yj - yi
            d = (dx * dx + dy * dy) ** 0.5
            if d == 0:
                continue
            f = G * mi * mj / (d * d)
            fx = f * dx / d
            fy = f * dy / d
            ax_total += fx / mi
            ay_total += fy / mi
        # Actualización de velocidad y posición (integración Euler)
        local_data[i, 2] = vxi + ax_total
        local_data[i, 3] = vyi + ay_total
        local_data[i, 0] = xi + local_data[i, 2]
        local_data[i, 1] = yi + local_data[i, 3]
    return local_data

# Bucle principal de la simulación
for niter in range(NUM_ITER):
    # Reunir los datos de todos los procesos usando Allgatherv con el tipo derivado
    comm.Allgatherv([local_data, satellite_type],
                    [global_data, recvcounts, recvdispls, satellite_type])
    
    # Actualizar los datos locales usando la función compilada por Numba
    local_data = update_objects(local_data, global_data, global_offset, N, G)
    
    # Cada NUM_ITER_SHOW iteraciones, mostrar un snapshot (solo proceso 0 imprime)
    if niter % NUM_ITER_SHOW == 0:
        if rank == 0:
            print("***** ITERATION {} *****".format(niter))
        comm.Allgatherv([local_data, satellite_type],
                        [global_data, recvcounts, recvdispls, satellite_type])
        if rank == 0:
            for idx in range(N):
                print("New position of object {}: {:.2f}, {:.2f}"
                      .format(idx, global_data[idx, 0], global_data[idx, 1]))

# Reunir los datos finales
comm.Allgatherv([local_data, satellite_type],
                [global_data, recvcounts, recvdispls, satellite_type])
if rank == 0:
    print("Final positions:")
    with open(RESULTFILE, "w") as out_file:
        for idx in range(N):
            line = "Object {}: {:.2f}, {:.2f}\n".format(idx, global_data[idx, 0], global_data[idx, 1])
            print(line.strip())
            out_file.write(line)

# Liberar el tipo derivado
satellite_type.Free()

# ------------------------------------------------------------------
# Para el fichero con 256 objetos:
#   mpirun -np 16 --oversubscribe ./GravitSerie_2.py
#
# Para el fichero con 1024 objetos:
#   mpirun -np 32 --oversubscribe ./GravitSerie_2.py
# ------------------------------------------------------------------
