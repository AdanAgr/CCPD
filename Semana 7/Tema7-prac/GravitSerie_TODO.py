#!/usr/bin/env python
import numpy as np

DATAFILE = "data4sat.txt"
G = 6.674e-11
NUM_ITER  = 100001
NUM_ITER_SHOW = 5000
verbose = False

with open(DATAFILE, 'r') as file:
    noOfObjects = int(file.readline().strip())
    print("Number of objects:",noOfObjects)
    print()

    x = np.zeros(noOfObjects) # x coordinate (m)
    y = np.zeros(noOfObjects) # y coordinate  (m)
    vx = np.zeros(noOfObjects) # x velocity (m/s)
    vy = np.zeros(noOfObjects) # y velocity (m/s)
    m = np.zeros(noOfObjects) # mass (kg)

    for i in range(noOfObjects):
        x[i] = float(file.readline().strip())
        y[i] = float(file.readline().strip())
        vx[i] = float(file.readline().strip())
        vy[i] = float(file.readline().strip())
        m[i] = float(file.readline().strip())

for niter in range(NUM_ITER):

    x_new = np.copy(x)
    y_new = np.copy(y)
    vx_new = np.copy(vx)
    vy_new = np.copy(vy)

    showData = False

    if (niter % NUM_ITER_SHOW == 0):
        showData = True

    if (showData):
        print("***** ITERATION {} *****".format(niter))

    for i in range(noOfObjects):
        ax_total = 0.0
        ay_total = 0.0

        for j in range(noOfObjects):
            if (i==j):
                continue
            d = 0 # TODO
            f = 0 # TODO
            fx = 0 # TODO
            ax = 0 # TODO
            fy = 0 # TODO
            ay = 0 # TODO

            if(showData and verbose):
                print("  Distance between objects {} and {}: {:.1f} m".format(i+1,j+1,d))
                print("  Force between objects {} and {}: {:.3f} N*m²/kg²".format(i+1,j+1,f))
                print("  Force along x axis on object {} made by object {}: {:.3f} N*m²/kg²".format(i+1,j+1,fx))
                print("  Acceleration along x axis on object {} made by object {}: {:.3f} m/s²".format(i+1,j+1,ax))
                print("  Force along y axis on object {} made by object {}: {:.3f} N*m²/kg²".format(i+1,j+1,fy))
                print("  Acceleration along y axis on object {} made by object {}: {:.3f} m/s²".format(i+1,j+1,ay))
            
            ax_total += ax
            ay_total += ay

        # for j

        vx_new[i] += ax_total
        vy_new[i] += ay_total

        x_new[i] += vx_new[i]
        y_new[i] += vy_new[i]

        if (showData):
            print("New position of object {}: {:.2f}, {:.2f}".format(i,x_new[i],y_new[i]))

    # for i
            
    x = np.copy(x_new)
    y = np.copy(y_new)
    vx = np.copy(vx_new)
    vy = np.copy(vy_new)

# for niter