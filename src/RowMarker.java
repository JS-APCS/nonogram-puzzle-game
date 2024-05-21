import java.awt.Color;
import java.awt.Font;

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
 *         Newline in JLabel
 *         Retrieved from
 *         https://stackoverflow.com/questions/1090098/newline-in-jlabel
 * 
 *         Version/date: 5-20-24
 * 
 *         Responsibilities of class:
 *         A RowMarker is a modified JLabel that reads from a 2D array of
 *         booleans (which represent's the Nonogram game board) and
 *         determines the list of numbers that should be listed next to 
 *         each column and row. It also will change its background color
 *         to indicate that it's associated row is fulfilled by the player.
 */
public class RowMarker extends JLabel
{
	private String numList; // RowMarker has-a number list
	private int index; // RowMarker has-an index
	private Color bgColor; // RowMarker has-a background color
	private boolean isVertical; // RowMarker knows if it's horizontal or vertical

	public RowMarker(boolean[][] solution, int index, boolean vertical)
	{
		// set the color to alternating colors based on the index
		bgColor = ((index + 1) % 2 == 0) ? NonogramGame.markerColor1
				: NonogramGame.markerColor2;

		this.index = index;
		isVertical = vertical;

		// setting up the list of numbers the marker should display
		numList = getRowNumbers(solution);

		// set the label's text to the number list
		if (!vertical)
		{ // if horizontal, use the normal list
			this.setText(numList+" ");
		}
		else
		{ // otherwise, we'll replace the spaces with line breaks
			// so that the numbers will line up vertically for column markers
			this.setText(
					"<html>" + getRowNumbers(solution).replaceAll(" ", "<br>")
							+ "</html>");
		}

		this.setFont(new Font("Arial", Font.BOLD, 16));
		this.setOpaque(true);
		this.setBackground(bgColor);
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
		int boxCounter = 0; // this will count the consecutive filled boxes

		if (!isVertical)
		{ // if not vertical (horizontal), traverse the row of the marker
			for (int i = 0; i < solution[index].length; i++)
			{
				if (solution[index][i] == true)
				{ // if the spot is filled, add to the counter
					boxCounter++;
				}
				else
				{ // if the spot is empty
					if (boxCounter > 0)
					{ // when the counter > 0, add it to the list and reset it
						list += String.valueOf(boxCounter) + " ";
						boxCounter = 0;
					}
				}
			}
		}
		else
		{ // if vertical, traverse the column of the marker
			for (int i = 0; i < solution.length; i++)
			{
				if (solution[i][index] == true)
				{// if the spot is filled, add to the counter
					boxCounter++;
				}
				else
				{// if the spot is empty
					if (boxCounter > 0)
					{// when the counter > 0, add it to the list and reset it
						list += String.valueOf(boxCounter) + " ";
						boxCounter = 0;
					}
				}
			}
		}
		// by the end, if counter is still over 0, add it to the list
		if (boxCounter > 0) list += String.valueOf(boxCounter);

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
	 * Get the background color
	 * 
	 * @return bgColor
	 */
	public Color getColor()
	{
		return bgColor;
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
		return getRowNumbers(grid).equals(numList);
	}
}
