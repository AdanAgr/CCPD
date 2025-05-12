# IMPORTS
from ej2_SumaRiemann_Cython import cython_riemann_sum, cython_riemann_sum_noloops
import numpy as np
from timeit import timeit

# GLOBALES
numbers = 10
TRUE_VALUE = 1.0  # Valor exacto de la integral
A = 0  # límite inferior
B = np.pi  # límite superior

# FUNCIÓN DE SUMA DE RIEMANN
def riemann_sum(n):
    dx = (B - A) / n # ancho de los intervalos (de 0 a pi/2 entre el nº de intervalos)
    riemann_sum = 0 # inicializa la suma de Riemann

    for i in range(n): # para cada intervalo
        x = (i+0.5)*dx  # punto medio
        riemann_sum += np.sin(x) # suma de Riemann --> acumulando

    return riemann_sum * dx

# SUMA DE RIEMANN
print("\nSuma de Riemann Cython, con bucles:")
print("\n", cython_riemann_sum(numbers))
print("\nSuma de Riemann Cython, sin bucles:")
print("\n", cython_riemann_sum_noloops(numbers))

print("\n----------------------")

# ASSERT
#np.testing.assert_almost_equal(cython_riemann_sum(numbers), TRUE_VALUE)
#np.testing.assert_almost_equal(cython_riemann_sum_noloops(numbers), TRUE_VALUE)

# TIMING
print("\nSuma de Riemann Cython, con bucles:") # similar a python
print(timeit(lambda: cython_riemann_sum(numbers), number=2000))

print("\nSuma de Riemann Cython, sin bucles") # similar a numba (+ rápido)
print(timeit(lambda: cython_riemann_sum_noloops(numbers), number=2000))