cimport numpy
import numpy as np

def cython_riemann_sum(int n):
    cdef:
        double dx = (np.pi/2 - 0) / n
        double riemann_sum = 0
        int i
        double x

    for i in range(n):
        x = (i+0.5)*dx  # punto medio
        riemann_sum += np.sin(x)
    return riemann_sum * dx


def cython_riemann_sum_noloops(int n):
    cdef:
        double dx = (np.pi/2 - 0) / n
        double riemann_sum = 0
        points = np.arange(dx/2, np.pi/2, dx)

    riemann_sum = np.sum(np.sin(points)) * dx

    return riemann_sum
