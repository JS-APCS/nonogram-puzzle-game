import java.awt.Color;

import javax.swing.JLabel;
/**
 * @author Jaiden Smith
 * 
 *         References:
 *         Morelli, R., & Walde, R. (2016). Java, Java, Java: Object-Oriented
 *         Problem Solving.
 *         Retrieved from
 *         https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
 * 
 *         Version/date: 4-22-24
 * 
 *         Responsibilities of class:
 *         A RowMarker is a modified JLabel that reads from a 2D array of booleans
 *         (which represent's the Nonogram game board) and determines the list of
 *         numbers that should be listed next to each column and row. It also will
 *         change its background color to indicate that it's associated row is
 *         fulfilled by the player.
 */
public class RowMarker extends JLabel
{
	private String numList; // RowMarker has-a number list
	private int index; // RowMarker has-an index
	private Color bgColor; // RowMarker has-a background color
	private boolean isVertical; // RowMarker knows if it's horizontal or vertical
	
	public RowMarker(boolean[][] solution, int index, Color color, boolean vertical)
	{
		bgColor = color;
		this.index = index;
		isVertical = vertical;
		
		// setting up the list of numbers the marker should display
		numList = getRowNumbers(solution);
		
		this.setText(numList); // set the label's text to the number list
	}
	
	/**
	 * Returns a string of numbers representing how
	 * many boxes should be filled in the marker's
	 * row/column
	 * 
	 * @param solution
	 * @return
	 */
	public String getRowNumbers(boolean[][] solution)
	{
		String list = ""; // this is where we add the numbers
		int boxCounter = 0;	// this will count the consecutive filled boxes
		
		if (!isVertical)
		{	// if not vertical (horizontal), traverse the row of the marker
			for (int i = 0; i < solution[index].length; i++)
			{
				if (solution[index][i])
				{ // if the space here is true (filled)
					boxCounter++; // add 1 to the counter
					
					if (i == solution.length-1)
					{ // if this is the last index, add the counter to the list
						list += Integer.toString(boxCounter);
					}
				}
				else if (!solution[index][i] && boxCounter > 0)
				{ // if the space is false (empty) and boxCounter is over 0
					// add a space to the list
					list += Integer.toString(boxCounter) + " ";
					boxCounter = 0;	// reset boxCounter
				}
			}
		}
		else
		{ // if vertical, traverse the column of the marker
			for (int i = 0; i < solution.length; i++)
			{
				if (solution[i][index])
				{ // if the space is true (filled)
					boxCounter++; // add 1 to the counter
					
					if (i == solution.length-1)
					{ // if this is the last index, add the counter to the list
						list += Integer.toString(boxCounter);
					}
				}
				else if (!solution[index][i] && boxCounter > 0)
				{ // if the space is false (empty) and boxCounter is over 0
					// add a space to the list
					list += Integer.toString(boxCounter) + " ";
					boxCounter = 0; // reset the counter
				}
			}
		}
		
		// remove any extra leading/trailing spaces and return
		return list.trim();
	}
	
	/**
	 * Return the marker's row/column index
	 * 
	 * @return index
	 */
	public int getIndex()
	{
		return index;
	}
	
	/**
	 * Set the background color
	 * 
	 * @param c
	 */
	public void setColor(Color c)
	{
		bgColor = c;
	}
	
	/**
	 * Returns whether or not the marker's corresponding
	 * row/column in the puzzle grid (filled in by the player)
	 * matches the marker's list of numbers
	 * 
	 * @param grid
	 * @return boolean
	 */
	public boolean checkRow(boolean[][] grid)
	{
		// if the row numbers match, return true
		return getRowNumbers(grid) == numList;
	}
}
