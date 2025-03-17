#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <omp.h>

#define N  100000  // number of data points
#define K  4       // number of clusters
#define D  2       // dimension of data

float data[N][D];
float centroids[K][D];
int   labels[N];    // cluster assignments for each data point

// Euclidean distance
float distance2D(const float *a, const float *b) {
    float dx = a[0] - b[0];
    float dy = a[1] - b[1];
    return sqrtf(dx*dx + dy*dy);
}

int main() {
    // 1) Initialize data + centroids (random or some init).
    for (int i=0; i<N; i++) {
        data[i][0] = rand()/(float)RAND_MAX; // in [0,1]
        data[i][1] = rand()/(float)RAND_MAX;
    }
    for (int c=0; c<K; c++) {
        centroids[c][0] = rand()/(float)RAND_MAX;
        centroids[c][1] = rand()/(float)RAND_MAX;
    }

    // 2) Repeat a few iterations of K-means:
    for (int iter=0; iter<10; iter++) {

        // (a) Assign points to nearest centroid
        #pragma omp parallel for
        for (int i=0; i<N; i++) {
            float bestDist = distance2D(data[i], centroids[0]);
            int bestC = 0;
            for (int c=1; c<K; c++) {
                float dist = distance2D(data[i], centroids[c]);
                if (dist < bestDist) {
                    bestDist = dist;
                    bestC = c;
                }
            }
            labels[i] = bestC;
        }

        // (b) Compute new centroids
        //     We'll sum up the data and then divide to get average
        float sum[K][D];
        int   count[K];
        // Initialize local accumulators
        for (int c=0; c<K; c++) {
            sum[c][0] = 0.0f; sum[c][1] = 0.0f;
            count[c]  = 0;
        }

        // Accumulate in parallel:
        #pragma omp parallel
        {
            float private_sum[K][D] = {0};
            int   private_count[K]  = {0};

            #pragma omp for
            for (int i=0; i<N; i++) {
                int c = labels[i];
                private_sum[c][0] += data[i][0];
                private_sum[c][1] += data[i][1];
                private_count[c]++;
            }

            // Combine partial sums with shared arrays (reduce manually)
            #pragma omp critical
            {
                for (int c=0; c<K; c++) {
                    sum[c][0]   += private_sum[c][0];
                    sum[c][1]   += private_sum[c][1];
                    count[c]    += private_count[c];
                }
            }
        } // end of parallel region

        // (c) Update centroids
        for (int c=0; c<K; c++) {
            if (count[c] > 0) {
                centroids[c][0] = sum[c][0] / count[c];
                centroids[c][1] = sum[c][1] / count[c];
            }
        }
    }

    // 3) Print final centroids
    printf("Final centroids:\n");
    for (int c=0; c<K; c++) {
        printf("  C%d: (%.3f, %.3f)\n",
               c, centroids[c][0], centroids[c][1]);
    }

    return 0;
}