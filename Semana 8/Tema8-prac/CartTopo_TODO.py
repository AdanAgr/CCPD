#!/usr/bin/env python
from mpi4py import MPI
import numpy as np

NFilas = 2
NCols = 3
com = MPI.COMM_WORLD
numProcs = com.size
miRango = com.rank

if numProcs != (NFilas*NCols):
    if miRango == 0:
        print("** ERROR: lanzar el programa con %d procesos **" % (NFilas*NCols))
    quit()

# TODO : crear comunicador cartesiano

comCartesiano = com.Create_cart((NFilas,NCols),(1,0))

if comCartesiano.Get_topology() != MPI.CART:
    if miRango==0:
        print("** Error creando topología cartesiana **")
    quit()

miNum = np.random.randint(low=0, high=10, size=1, dtype=np.int32)

def mostrarDatos() :
    # TODO: reunir los valores en el array 'matriz' del proceso 0
    if miRango == 0:
        matriz = np.empty((NFilas,NCols), dtype = np.int32)
    else:
        matriz = None

    comCartesiano.Gather(miNum,matriz, root=0)
    if miRango == 0:
        print(matriz)



if miRango==0:
    print("Datos iniciales:")
mostrarDatos()

col1=np.empty(1)
fil1=np.empty(1)
col2=np.empty(1)
fil2=np.empty(1)
if miRango==0:
    fil1[0] = int(input("Fila elemento 1: "))
    col1[0] = int(input("Columna elemento 1: "))
    fil2[0] = int(input("Fila elemento 2: "))
    col2[0] = int(input("Columna elemento 2: "))

# TODO: difundir los valores fil1, fil2, col1, col2

com.Bcast(fil1, root = 0)
com.Bcast(col1, root = 0)
com.Bcast(fil2, root = 0)
com.Bcast(col2, root = 0)

# TODO: obtener los rangos de los procesos que van a intercambiar sus datos
proc1 = comCartesiano.Get_cart_rank([fil1[0],col1[0]])
proc2 = comCartesiano.Get_cart_rank([fil2[0],col2[0]])



if miRango == 0:
    print("Intercambiando datos entre procesos con rangos %d, %d" % (proc1, proc2))

# TODO proc1 y proc2 intercambian sus datos. Usar una variable 'aux' donde almacenar
# el valor recibido en ambos casos
if miRango == proc1:
    aux = np.zeros(1, dtype = np.int32)
    com.Send(miNum, dest = proc2)
    com.Recv(aux)
if miRango == proc2:
    aux = np.zeros(1, dtype = np.int32)
    com.Send(miNum, dest = proc1)
    com.Recv(aux)



if miRango == proc1 or miRango == proc2:
    miNum = aux

if miRango == 0:
    print("Tras el intercambio: ")
mostrarDatos()