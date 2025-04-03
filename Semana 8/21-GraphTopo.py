#!/usr/bin/env python
from mpi4py import MPI

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

index = [2,5,8,9,10,11,12]
edges = [1,2,0,3,4,0,5,6,1,1,2,2]

if (miRango == 0):
   CompruebaTopo("Comunicador original",com)

if numProcs != 7:
   if miRango == 0:
      print("ERROR: lanzar el programa con 7 procesos")
   quit()

comGraph = com.Create_graph(index, edges)

if (miRango == 0):
   CompruebaTopo("Comunicador nuevo",comGraph)

print("Soy el nodo %d y tengo %d vecinos:" % (miRango,comGraph.nneighbors), \
   comGraph.neighbors)