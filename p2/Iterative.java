/* ==========================================================
Dai Vuong (Paul)
CS3010.01
Sept 22th, 2020
Jacobi interative method and Gaussian-Seidel method
=============================================================*/


import java.util.Arrays;
import java.util.Scanner;
import java.io.File;

public class Iterative {

    

    // Function enter equations.
    private static double [][] enterMatrix() {
        Scanner kb = new Scanner(System.in);
        int row = 0;

        try {
            System.out.print("Enter the number of equations: ");
            row = kb.nextInt();
            System.out.println();
        }
        catch (Exception e) {
            System.out.println("Enter the invalid value.");
            System.exit(0);
        }

        double [][] matrix = new double [row][row+1]; // declare matrix

        int option;
        
        System.out.println("Do you want to enter the equations in the console or enter a file name? Choose:\n\t1 - enter coefficients equations.\n\t2 - enter file name.");
        System.out.print("Enter: ");
        option = kb.nextInt();
        while (option < 1 || option > 2) {
            System.out.print("Enter 1 or 2 again to choose options: ");
            option = kb.nextInt();
            System.out.println();
        }

        if (option == 1) { //import equations from console
            try {
                System.out.println("Enter the coefficients row by row ( includes the b value):  ");

                for (int r=0; r<row; r++) {
                    for (int c=0; c<row+1; c++) {
                        matrix[r][c] = kb.nextInt();
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Invalid value.");
                System.exit(0);
            }
        }

        
        if (option == 2) { // import equations from text file
            
            System.out.print("Enter the file name: ");
            Scanner read = new Scanner("");
            try {
                File file = new File(kb.next());

                while (!file.exists()) {
                    System.out.print("Enter the file name again: ");
                    file = new File(kb.next());
                }
                
                read = new Scanner(file);
                for(int r = 0; r < row; r++) {
                    for(int c = 0; c < row+1; c++) {
                        matrix[r][c] = read.nextDouble();
                    }
                }
            } 
            catch (Exception e) {
                System.out.println("An error occurred. Either the file is not found or invalid values.");
                System.exit(0);
            }
            read.close();
        }
        return matrix;
    };

    // Jacobi method
    private static void jacobi(double [][] matrix, double [] start, double err) {
        int REPEAT_MAX = 50;
        int count = 0;
        int n = matrix.length;
        double [] curr = new double[n]; // current iteration k
        double [] prev = (double[])start.clone(); // previous k-1
        
        System.out.println("\nJacobi method.");

        while(true) {
            for (int r=0; r<n; r++) {
                double add = matrix[r][n];
                for (int c=0; c<n; c++) {
                    if (c != r)
                        add -= matrix[r][c] * prev[c]; // change side
                }
                curr[r] = add/matrix[r][r]; // current iteration for each variable
            }
            // print out current iteration
            System.out.printf("x%-2d = [", count+1);
            for (int i=0; i<n; i++) {
                System.out.printf("%8.4f  ", curr[i]);
            }
            System.out.println("]T");

            count++;

            boolean stopSign = false;

            // check for error by using norm l_2
            double numerator = 0;
            double  denominator = 0;
            for (int i=0; i<n; i++) {
                numerator += Math.pow((curr[i]-prev[i]), 2);
                denominator += Math.pow(curr[i], 2);
            }
            if ((Math.sqrt(numerator)/Math.sqrt(denominator)) < err)
                stopSign = true;

            // Check the current hit the 50 iterations or over the error
            if (stopSign || (count >= REPEAT_MAX))
                break;
            
            prev = (double[])curr.clone(); // update new current for next iteration
        }
    }

    // Gauss-Seidel method
    private static void gauss_seidel(double [][] matrix, double [] start, double err) {
        int REPEAT_MAX = 50;
        int count = 0;
        int n = matrix.length;
        double [] curr = new double[n]; // current iteration k
        double [] prev = (double[])start.clone(); // previous k-1

        System.out.println("Gauss-Seidel method.");

        while(true) {
            for (int r=0; r<n; r++) {
                double add = matrix[r][n];
                for (int c=0; c<n; c++) {
                    if (c != r) {
                        if (c<r)
                            add -= matrix[r][c] * curr[c]; // xj(k1)
                        else
                            add -= matrix[r][c] * prev[c]; // xj(k-1)
                    }
                }
                curr[r] = add/matrix[r][r]; // current iteration for each variable
            }
            // print out current iteration
            System.out.printf("x%-2d = [", count+1);
            for (int i=0; i<n; i++) {
                System.out.printf("%8.4f  ", curr[i]);
            }
            System.out.println("]T");

            count++;

            boolean stopSign = false;

            // check for error by using norm l_2
            double numerator = 0;
            double  denominator = 0;

            for (int i=0; i<n; i++) {
                numerator += Math.pow((curr[i]-prev[i]), 2);
                denominator += Math.pow(curr[i], 2);
            }
            if ((Math.sqrt(numerator)/Math.sqrt(denominator)) < err)
                stopSign = true;
            
            // Check the current hit the 50 iterations or over the error
            if (stopSign || (count >= REPEAT_MAX))
                break;
            
            prev = (double[])curr.clone(); // update new current for next iteration
        }
    }

    public static void main(String args [] ) {

        Scanner kb = new Scanner(System.in);

        double [][] matrix = enterMatrix(); // Enter the number of equations and input the coefficients. 
        double [] start = new double[matrix.length];
        double err = 1e-15;

        try {
            System.out.print("Enter the desired stopping error: ");
            err = kb.nextDouble();
            System.out.print("Enter the starting solution: ");
            for (int i=0; i<start.length; i++)
                start[i] = kb.nextDouble();
        } catch (Exception e) {
            System.out.println("Invalid error value or start points value.");
        }
        
        jacobi(matrix, start, err);
        System.out.println("\n");
        gauss_seidel(matrix, start, err);
    }
}

// Test cases
        /*double [][]m = {{2,3,0,8},
                          {-1,2,-1,0},
                          {3,0,2,9}};*/

        // No unique solution
        /*double [][]m = {{-3,-5,36,10},
                          {-1,0,7,5},
                          {1,1,-10,-4}};*/
        
        // No solution
        /*double [][]m = {{1,1,1,2},
                        {0,1,-3,1},
                        {2,1,5,0}};*/


        /*double [][]m = {{3,-13,9,3,-19},
                        {-6,4,1,18,-34},
                        {6,-2,2,4,16},
                        {12,-8,6,10,26}};*/

        /*double [][]m = {{1,0,3,0,0},
                        {0,1,3,-1,0},
                        {3,-3,0,6,0},
                        {0,2,4,6,0}};*/