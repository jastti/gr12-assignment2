import java.io.*;
import java.util.Scanner;
import java.io.*;
import java.util.Scanner;

/**
 * Mar 13, 2021
 * Author @Jasmine Tian
 * Assignment 2 - CheapestRoutes
 * Ms. Wong
 * ICS4U
 * Class Name: CheapestRoutes
 * Description: this class reads all 2D toll grids from a specific format data file and find the cheapest route 
 *             from bottom-left to top-right corner of each grid. For each grid, this class will print the cheapest route, directions,
 *             and the total cost of the route. 
 */

public class CheapestRoutes {
	// used to hold all the directions for the cheapest route
	static String cheapestRoute;
	// used to hold all the toll information for the cheapest route
	static String cheapestTolls;
	// used to hold the total cost of the cheapest route
	static int cheapestCost;

	/*
	* Description: this is the main method of the class. It reads grids information from an input file and call findPath to find the cheapest route 
	*              of each grid.
	*/
	public static void main(String[] args) {

		// define variables of number of grids, rows, columns, and the data issue status
		int numOfGrids;
		int numOfRows;
		int numOfColumns;
		boolean DataIssue = false;

		// create a 2D array
		int[][] myGrid;

		// read route data from a file
		try {
			// create a text document called "input.txt"
			// the input file path and file name need to be changed for your testing.
			BufferedReader inFile = new BufferedReader(new FileReader("input.txt"));
			String line = " ";

			// read first line to find out the total number of grids
			line = inFile.readLine();
			// covert the string to integer for number of Grids
			numOfGrids = Integer.parseInt(line);
			// print some information before finding the routes
			System.out.println("Finding the cheapest routes:");
			
			// Now we need to process one grid at a time by using the loop.
			for (int i = 1; i <= numOfGrids; i++) {
				// Total number of loops depending on the value of numOfGrids
				System.out.printf("\nGird #%d:\n", i);// print the current grid name
				line = inFile.readLine();// read number of rows of the current grid
				
				if (line != null) { // determine if the file is null
					numOfRows = Integer.parseInt(line);// if not null, get the number of rows
				} else {
					// if null, print out the warning
					System.out.printf("Grid #%d: Missing the number of rows in data file!\n", i);
					DataIssue = true;// and set the data issue as true
					break; //break the loop
				}
				
				line = inFile.readLine();// read number of columns of the current grid
				if (line != null) {	// determine if the file is null
					numOfColumns = Integer.parseInt(line);// if not null, get the number of columns
				} else {
					// if null, print out the warning
					System.out.printf("Grid #%d: Missing the number of columns in data file!\n", i);
					DataIssue = true;// and set the data issue as true
					break; //break the loop
				}
				
				
				// initialize a 2D array to hold the toll information of the current grid
				myGrid = new int[numOfRows][numOfColumns];
				// define an array to hold the tolls in one row
				String[] toll;
				
				// Use a loop to read rows of tolls
				for (int j = 0; j < numOfRows; j++) {
					line = inFile.readLine(); // read one row of the grid from the file
					if (line != null) {
						// check if the line of the number of rows is null 
						System.out.println(line); // print the current row in one line
						toll = line.split("\\s"); // split the row into columns
						
						if (toll.length != numOfColumns) {
							// check if the number of columns from split result matches pre-defined number
							// of columns of the grid
							
							// print out the warning
							System.out.printf(
									"Gid #%d, row #%d: number of values does not match the number of columns!\n", i,
									j + 1);
							DataIssue = true; // and set the data issue as true
							break; //break the current for loop
						}
						// start processing the value in each column
						for (int k = 0; k < numOfColumns; k++) {
							myGrid[j][k] = Integer.parseInt(toll[k]);// get the toll value, convert it to integer, and store it.
						}
					} else {
						// if the line is null, print out the warning
						System.out.printf("Gid #%d, row #%d: missing data for grid!\n", i, j + 1);
						DataIssue = true; // and set the data issue as true
						break; //break the current for loop
					}
				}
				
				if (DataIssue == true) {
					// check if there is any data issue when process the current grid
					break;	// data issue, break the outter loop
				} else { //no data issue
					// reset the cheapest variables: cost, routes, and tolls for the current grid
					cheapestCost = Integer.MAX_VALUE; //initialize the cheapestCost to a MAX integer
					cheapestRoute = ""; //initialize the direction string
					cheapestTolls = ""; //initialize the route string
					
					// No data issue, find the cheapest route string from the bottom-left corner
					// The initial cost is 0
					findPath(myGrid, myGrid.length - 1, 0, "", 0, "");
					
					if (cheapestRoute.length() > 0) { //find the cheapest route
						// print the cheapest tolls for the current grid
						System.out.printf("Cheapest Route: %s\n", cheapestTolls);
						// print the direction of the cheapest route for the current grid
						System.out.printf("Direction: %s\n", cheapestRoute);
						// print the cheapest cost for the current grid
						System.out.printf("Cheapest Cost: $%d\n", cheapestCost);
						
					} else {
						// Cannot find the cheapest route. Print out the warning
						System.out.printf("Grid #%d: Cannot find cheapest route!", i);
					}

				}
			}
		} catch (IOException e) {
			// try to catch any IO exceptions
			System.out.println(e.getMessage()); // print out the message got
		} catch (NumberFormatException e) {
			// try to catch any NumberFormat exceptions
			System.out.println(e.getMessage());	// print out the message got
		}
		System.out.println("Program is complete");
	}

	
	/*
	* Description:
	*    findPath is a method to find and print the cheapest route of the input grid.
	* @param 
	*    grid: 2D array contains the toll data for each grid
	*    x   : row number of start point
	*    y   : column number of start point
	*    currentRoute: the directions string before entering the current location
	*    currentCost: the total cost before entering the current location
	*    currentTolls: the route string (toll values) before entering the current location
	* @return:
	*    no return.
	*/
	public static void findPath(int[][] grid, int x, int y, String currentRoute, int currentCost, String currentTolls) {
		int totalRows = grid.length;// create an integer variable of the number of total rows
		int totalColumns = grid[0].length;// create an integer variable of the number of total columns
		int newCost;// create an integer variable of the new cost
		String newRoute;// create a string variable of the number of the new route
		String newTolls;// create a string variable of the number of the new tolls

		
		// calculate new total cost
		newCost = currentCost + grid[x][y];
		// append the toll of current point to the route string
		newTolls = currentTolls + " " + Integer.toString(grid[x][y]);
		
		if (x == 0 && y == (totalColumns - 1)) {
			// base case: reach the top-right corner
			// we have one completed route
			if (newCost < cheapestCost) {
				// check if the current completed route cost less than cheapest cost or not
				cheapestCost = newCost;// the cheapest cost remains the same
				cheapestRoute = currentRoute;// the direction of the cheapest route remains the same
				cheapestTolls = newTolls; // the cheapest tolls remains the same
				return;	// return to the calling function
			}
			
		} else {

			if (x > 0) {
				// first, move North and find the path to top-right corner
				newRoute = currentRoute + " North"; // update direction string
				

				// call findPath to get the path for the new point in the North
				findPath(grid, x - 1, y, newRoute, newCost, newTolls);

				if (y < totalColumns - 1) { // check if it is able to move East
					newRoute = currentRoute + " East"; 
					// call findPath to get the path for the new point in the East
					findPath(grid, x, y + 1, newRoute, newCost, newTolls);
				}
			} else {
				// if reach to the top row, then move East to the top-right corner
				newRoute = currentRoute + " East";
				// call findPath to get the path for the new point in the East
				findPath(grid, x, y + 1, newRoute, newCost, newTolls);
			}
		}

	}

}
