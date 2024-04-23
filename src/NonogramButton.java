import java.awt.Color;

import javax.swing.JButton;

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
 *         Represents the boxes that are filled in on the game's grid
 */
public class NonogramButton extends JButton	// NonogramButton is-a JButton
{
	private int row, column; // NonogramButton has-a row and column
	private boolean isFilled = false; // NonogramButton has-a fill state

	/**
	 * Constructor
	 * 
	 * @param r
	 * @param c
	 */
	public NonogramButton(int r, int c)
	{
		row = r;
		column = c;
		
		setBackground(Color.white);
	}

	/**
	 * Gets the button's row
	 * 
	 * @return row
	 */
	public int getRow()
	{
		return row;
	}

	/**
	 * Gets the button's column
	 * 
	 * @return column
	 */
	public int getColumn()
	{
		return column;
	}

	/**
	 * Gets the fill state of the button
	 * 
	 * @return
	 */
	public boolean getIsFilled()
	{
		return isFilled;
	}

	/**
	 * Modifies the fill state of the button
	 * 
	 * @param filled
	 */
	public void setIsFilled(boolean filled)
	{
		isFilled = filled;
	}

}
