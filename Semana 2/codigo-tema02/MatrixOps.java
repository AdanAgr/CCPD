import java.util.Random;

public class MatrixOps {

    private static final int MATRIX_SIZE = 4; // Example size, can be adjusted
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        int[][] matrixA = new int[MATRIX_SIZE][MATRIX_SIZE];
        int[][] matrixB = new int[MATRIX_SIZE][MATRIX_SIZE];
        int[][] resultMatrix = new int[MATRIX_SIZE][MATRIX_SIZE];

        // Initialize matrices with random integers
        initializeMatrix(matrixA);
        initializeMatrix(matrixB);

        // Perform matrix addition
        addMatrices(matrixA, matrixB, resultMatrix);

        // Print the result matrix
        printMatrix(resultMatrix);
    }

    private static void initializeMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = random.nextInt(10); // Assign random integers (0-9)
            }
        }
    }

    private static void addMatrices(int[][] matrixA, int[][] matrixB, int[][] resultMatrix) {
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA[i].length; j++) {
                resultMatrix[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }
}