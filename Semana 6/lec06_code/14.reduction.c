// 14.reduction.c
#include <omp.h>
#include <stdio.h>

int main() {
    int N = 8;
    int arr[8] = {4, 5, 1, 3, 9, 2, 7, 6};
    int total = 0;

    #pragma omp parallel for reduction(+:total)
    for (int i = 0; i < N; i++) {
        total += arr[i];
    }

    printf("La suma total es: %d\n", total);
    return 0;
}