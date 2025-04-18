/**
 * Recursive Sudoku Solver
 * Name: Lydia Palmer
 * email: lapalmer@bu.edu
 * 
 * Java implementation of a Sudoku puzzle solver using recursive backtracking.
 * The program reads a 9x9 puzzle from a text file and fills in missing values
 * while enforcing Sudoku constraints.
 *
 * Developed as part of an academic project in CS112 at Boston University
 */

 import java.io.*;   // allows us to read from a file
 import java.util.*;
 
 public class Sudoku {    
     // The current contents of the cells of the puzzle. 
     private int[][] grid;
     
     /*
      * Indicates whether the value in a given cell is fixed 
      * (i.e., part of the initial configuration).
      * valIsFixed[r][c] is true if the value in the cell 
      * at row r, column c is fixed, and false otherwise.
      */
     private boolean[][] valIsFixed;
     
     /*
      * This 3-D array allows us to determine if a given subgrid (i.e.,
      * a given 3x3 region of the puzzle) already contains a given
      * value.  We use 2 indices to identify a given subgrid:
      *
      *    (0,0)   (0,1)   (0,2)
      *
      *    (1,0)   (1,1)   (1,2)
      * 
      *    (2,0)   (2,1)   (2,2)
      * 
      * For example, subgridHasVal[0][2][5] will be true if the subgrid
      * in the upper right-hand corner already has a 5 in it, and false
      * otherwise.
      */
     private boolean[][][] subgridHasVal;
     
     private boolean[][] rowHasVal;  // Track values in each row.
     private boolean[][] colHasVal;  // Track values in each column.
     
     /* 
      * Constructs a new Puzzle object, which initially
      * has all empty cells.
      */
     public Sudoku() {
         this.grid = new int[9][9];
         this.valIsFixed = new boolean[9][9];     
         
         /* 
          * Note that the third dimension of the following array is 10,
          * because we need to be able to use the possible values 
          * (1 through 9) as indices.
          */
         this.subgridHasVal = new boolean[3][3][10];        
 
         this.rowHasVal = new boolean[9][10];
         this.colHasVal = new boolean[9][10];
     }
     
     /*
      * Place the specified value in the cell with the specified
      * coordinates, and update the state of the puzzle accordingly.
      */
     public void placeVal(int val, int row, int col) {
         if (val < 1 || val > 9) {
             throw new IllegalArgumentException("Value must be between 1 and 9.");
         }
         if (row < 0 || row >= 9 || col < 0 || col >= 9) {
             throw new IllegalArgumentException("Row and column indices must be between 0 and 8.");
         }
         
         this.grid[row][col] = val;
         this.subgridHasVal[row/3][col/3][val] = true;
         
         
         this.rowHasVal[row][val] = true;
         this.colHasVal[col][val] = true;
         
     }
         
     /*
      * remove the specified value from the cell with the specified
      * coordinates, and update the state of the puzzle accordingly.
      */
     public void removeVal(int val, int row, int col) {
         this.grid[row][col] = 0;
         this.subgridHasVal[row/3][col/3][val] = false;
         
         rowHasVal[row][val] = false;
         colHasVal[col][val] = false;
     }  
         
     /*
      * read in the initial configuration of the puzzle from the specified 
      * Scanner, and use that config to initialize the state of the puzzle.  
      * The configuration consists of one line for each row, with the
      * values in the row specified as integers separated by spaces.
      * A value of 0 is used to indicate an empty cell.
      * 
      * 
      */
     public void readConfig(Scanner input) {
         for (int r = 0; r < 9; r++) {
             for (int c = 0; c < 9; c++) {
                 int val = input.nextInt();
                 if (val != 0) {
                     this.placeVal(val, r, c);
                     this.valIsFixed[r][c] = true;
                 }
             }
         }
     }
                 
     /*
      * Displays the current state of the puzzle.
      * 
      */        
     public void printGrid() {
         for (int r = 0; r < 9; r++) {
             printRowSeparator();
             for (int c = 0; c < 9; c++) {
                 System.out.print("|");
                 if (this.grid[r][c] == 0) {
                     System.out.print("   ");
                 } else {
                     System.out.print(" " + this.grid[r][c] + " ");
                 }
             }
             System.out.println("|");
         }
         printRowSeparator();
     }
         
     // A private helper method used by display() 
     // to print a line separating two rows of the puzzle.
     private static void printRowSeparator() {
         for (int i = 0; i < 9; i++) {
             System.out.print("----");
         }
         System.out.println("-");
     }
     
 
 
          
     /*
      * This is the key recursive-backtracking method.  Returns true if
      * a solution has already been found, and false otherwise.
      * 
      * Each invocation of the method is responsible for finding the
      * value of a single cell of the puzzle. The parameter n
      * is the number of the cell that a given invocation of the method
      * is responsible for. We recommend that you consider the cells
      * one row at a time, from top to bottom and left to right,
      * which means that they would be numbered as follows:
      *
      *     0  1  2  3  4  5  6  7  8
      *     9 10 11 12 13 14 15 16 17
      *    18 ...
      */
     private boolean solveRB(int n) {
         if (n == 81) return true; // All cells are processed.
 
         int row = n / 9;
         int col = n % 9;
 
         if (this.valIsFixed[row][col]) {
             return solveRB(n + 1);
         }
 
         for (int val = 1; val <= 9; val++) {
         // Check if placing 'val' in grid[row][col] is valid.
         if (!this.rowHasVal[row][val] && !this.colHasVal[col][val] && !this.subgridHasVal[row/3][col/3][val]) {
                placeVal(val, row, col); // Place the value temporarily.
                if (solveRB(n + 1)) {
                     return true;
                 }
             // If not then backtrack
             removeVal(val, row, col);
         }
     }
       return false;
 
      } 
     
     /*
      * public "wrapper" method for solveRB().
      * Makes the initial call to solveRB, and returns whatever it returns.
      */
     public boolean solve() { 
         boolean foundSol = this.solveRB(0);
         return foundSol;
     }
     
     public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Sudoku puzzle = new Sudoku();
        
        System.out.print("Enter the name of the puzzle file: ");
        String filename = scan.nextLine();
        
        try {
            Scanner input = new Scanner(new File(filename));
            puzzle.readConfig(input);
        } catch (IOException e) {
            System.out.println("error accessing file " + filename);
            System.out.println(e);
            System.exit(1);
        }
        
        System.out.println();
        System.out.println("Here is the initial puzzle: ");
        puzzle.printGrid();
        System.out.println();
        
        if (puzzle.solve()) {
            System.out.println("Here is the solution: ");
        } else {
            System.out.println("No solution could be found.");
            System.out.println("Here is the current state of the puzzle:");
        }
        puzzle.printGrid();  
    }    

 }
 
 
 