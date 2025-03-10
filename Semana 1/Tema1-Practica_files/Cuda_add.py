from numba import cuda
import numpy as np

blocks_per_grid = 10000
threads_per_block = 1024
n = threads_per_block*blocks_per_grid

def add_cpu(x, y, out):
    out = x + y;

@cuda.jit
def add_gpu(x, y, out):
    idx = cuda.grid(1)
        # 1 = grid unidimensional
        # cuda.grid(1) = cuda.threadIdx.x + cuda.blockIdx.x*cuda.blockDim.x
    out[idx] = x[idx] + y[idx]

h_x = np.ones(n)  # [1...1] 
h_y = np.ones_like(h_x)
h_out = np.ones_like(h_x)

d_x = cuda.to_device(h_x) 
d_y = cuda.to_device(h_y) 
d_out = cuda.device_array_like(d_x) 

add_cpu(h_x, h_y, h_out)

# Un hilo para cada elemento
add_gpu[blocks_per_grid, threads_per_block](d_x, d_y, d_out)

print("Número de hilos:", n)
print(d_out.copy_to_host()) # Resultado: [2...2]