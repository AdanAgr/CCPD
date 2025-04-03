#!/usr/bin/env python
from mpi4py import MPI
SIZE_X = 4
SIZE_Y = 3

def CompruebaTopo(cad,communic):
   topo = communic.Get_topology()
   if topo == MPI.CART:
      print("%s: topología cartesiana" % cad)
   elif topo == MPI.GRAPH:
      print("%s: topología en grafo" % cad)
   elif topo == MPI.UNDEFINED:
      print("%s: topología sin definir" % cad)

com = MPI.COMM_WORLD
numProcs = com.size
miRango = com.rank
if (miRango == 0):
   CompruebaTopo("Comunicador original",com)

if numProcs != 12:
   if miRango == 0:
      print("ERROR: lanzar el programa con 12 procesos")
   quit()

comCartesiano = com.Create_cart((SIZE_X,SIZE_Y),(1,0))

if (miRango == 0):
   CompruebaTopo("Comunicador nuevo",comCartesiano)

if miRango==0:
   for i in range(SIZE_X):
      for j in range(SIZE_Y):
         r = comCartesiano.Get_cart_rank([i,j])
         print("El proceso de coordenadas (%d,%d) tiene rango %d" %(i,j,r))

if (miRango == 0):
   data1,data2 = comCartesiano.Shift(0,1)
   print("Dirección 0, distancia 1, nodo1 %d, nodo2 %d" % (data1,data2))
   data1,data2 = comCartesiano.Shift(1,1)
   print("Dirección 1, distancia 1, nodo1 %d, nodo2 %d" % (data1,data2))
   data1,data2 = comCartesiano.Shift(0,5)
   print("Dirección 0, distancia 5, nodo1 %d, nodo2 %d" % (data1,data2))
   data1,data2 = comCartesiano.Shift(1,5)
   print("Dirección 1, distancia 5, nodo1 %d, nodo2 %d" % (data1,data2))