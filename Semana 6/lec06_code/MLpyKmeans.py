#!/usr/bin/env python3
import numpy as np
import math
import pyomp  # <-- Hypothetical Python wrapper for your OpenMP code

# Constants
N = 100000  # number of data points
K = 4       # number of clusters
D = 2       # dimension of data

# Allocate arrays (global, for simplicity)
data = np.zeros((N, D), dtype=np.float32)
centroids = np.zeros((K, D), dtype=np.float32)
labels = np.zeros(N,       dtype=np.int32)

def main():
    # 1) Initialize data + centroids (similarly to the C code)
    #    Here we use NumPy random; in practice you might load real data from files.
    np.random.seed(42)   # for reproducibility
    data[:] = np.random.rand(N, D).astype(np.float32)
    centroids[:] = np.random.rand(K, D).astype(np.float32)

    # 2) Repeat a few iterations of K-means
    iterations = 10
    for iter_num in range(iterations):
        # (a) Assign points to nearest centroid
        #     We call a hypothetical PyOMP function that runs in parallel
        pyomp.assign_points_omp(data, centroids, labels, N, K, D)
        # --------------------------------------------------------------
        #    The C/C++ code that this might wrap looks like:
        #
        #    #pragma omp parallel for
        #    for (int i = 0; i < N; i++) {
        #       float bestDist = distance2D(data[i], centroids[0]);
        #       int bestC = 0;
        #       for (int c = 1; c < K; c++) {
        #           float dist = distance2D(data[i], centroids[c]);
        #           if (dist < bestDist) {
        #               bestDist = dist;
        #               bestC = c;
        #           }
        #       }
        #       labels[i] = bestC;
        #    }
        # --------------------------------------------------------------

        # (b) Compute new centroids by summing data points per cluster
        #     We'll sum up local partials in parallel. Another hypothetical function:
        sum_buffer = np.zeros((K, D), dtype=np.float32)
        count_buffer = np.zeros(K,     dtype=np.int32)

        pyomp.accumulate_omp(data, labels, sum_buffer, count_buffer, N, K, D)
        # --------------------------------------------------------------
        #   The underlying C/OpenMP code might do something like:
        #
        #   #pragma omp parallel
        #   {
        #       float private_sum[K][D] = {0};
        #       int   private_count[K]  = {0};
        #
        #       #pragma omp for
        #       for (int i = 0; i < N; i++) {
        #           int c = labels[i];
        #           private_sum[c][0] += data[i][0];
        #           private_sum[c][1] += data[i][1];
        #           private_count[c]++;
        #       }
        #       #pragma omp critical
        #       {
        #           for (int c = 0; c < K; c++) {
        #               sum_buffer[c][0] += private_sum[c][0];
        #               sum_buffer[c][1] += private_sum[c][1];
        #               count_buffer[c]  += private_count[c];
        #           }
        #       }
        #   }
        # --------------------------------------------------------------

        # (c) Update the centroids
        for c in range(K):
            if count_buffer[c] > 0:
                centroids[c][0] = sum_buffer[c][0] / count_buffer[c]
                centroids[c][1] = sum_buffer[c][1] / count_buffer[c]

    # 3) Print final centroids
    print("Final centroids after", iterations, "iterations:")
    for c in range(K):
        print(f"  C{c}: ({centroids[c][0]:.3f}, {centroids[c][1]:.3f})")

if __name__ == "__main__":
    main()
