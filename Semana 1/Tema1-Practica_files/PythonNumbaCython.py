# Cython Function
import pyximport; pyximport.install() 
from sum_series import sum_series_cython


#Python Function
def sum_series_python(x):
    y = 0
    for i in range(x):
        y += i
    return y

# Numba Function
from numba import njit
@njit(cache = True)
def sum_series_numba(x):
    y = 0
    for i in range(x):
        y += i
    return y
 
     
