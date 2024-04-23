import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Jaiden Smith
 * 
 *         References:
 *         Morelli, R., & Walde, R. (2016). Java, Java, Java: Object-Oriented
 *         Problem Solving.
 *         Retrieved from
 *         https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
 * 
 *         Understanding BufferedImage.getRGB output values
 *         Retrieved from
 *         https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
 *         
 *         Java GridBagLayout
 *         Retrieved from
 *         https://www.javatpoint.com/java-gridbaglayout
 * 
 *         Version/date: 4-22-24
 * 
 *         Responsibilities of class:
 *         Holds the main functions of the game and displays the game's GUI
 *         elements;
 *         establishes the game board by reading an image and creating a grid of
 *         buttons based on the image dimensions.
 */
public class NonogramGame extends JFrame // NonogramGame is-a JFrame
{
	private int width, height; // NonogramGame has-a width and height
	private boolean fillMode = true; // NonogramGame has-a fill mode
	private boolean[][] grid; // NonogramGame has-a grid
	private boolean[][] solution; // NonogramGame has-a solution
	private BufferedImage image; // NonogramGame has-an image

	// NonogramGame has-a list of row markers and column markers
	private ArrayList<RowMarker> rowMarkerList;
	private ArrayList<RowMarker> columnMarkerList;

	private int FRAME_SIZE = 700; // NonogramGame knows its frame size

	/**
	 * Constructor
	 * 
	 * @param fileName
	 */
	public NonogramGame(String fileName)
	{
		this.setTitle("Nonogram"); // set the frame's title
		this.setLayout(new GridBagLayout());

		// this constraint will allow us to position GUI elements
		GridBagConstraints GBC = new GridBagConstraints();

		try
		{ // try to read the image with the file name
			image = ImageIO.read(new File("images/" + fileName));
		}
		catch (IOException e)
		{ // if the image fails to be read
			e.printStackTrace();
			System.out.println("Couldn't read image!");
		}

		// set image width and height
		width = image.getWidth();
		height = image.getHeight();

		// create the arrays with the image dimensions
		grid = new boolean[width][height];
		solution = new boolean[width][height];

		// Setting up the solution grid based on the image
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				// set the solution grid's corresponding coordinate
				// to a boolean based on whether this pixel is black
				solution[x][y] = (image.getRGB(y, x) == Color.black.getRGB());
			}
		}

		// JPanels for organization
		// buttonPanel holds the buttons in a grid
		JPanel buttonPanel = new JPanel(new GridLayout(width, height));

		// buttonPanel setup
		buttonPanel.setPreferredSize(
				new Dimension(3 * FRAME_SIZE / 4, 3 * FRAME_SIZE / 4));
		for (int x = 0; x < grid.length; x++)
		{
			for (int y = 0; y < grid[x].length; y++)
			{ // for every spot on the grid, make a button with a listener
				NonogramButton button = new NonogramButton(x, y);
				button.setBackground(Color.white);

				button.addActionListener(
						new NonogramButtonListener(this, button));
				buttonPanel.add(button); // add the button to the panel
			}
		}

		// This the button that toggles the fill mode
		JButton toggleButton = new JButton("Fill");
		toggleButton.setPreferredSize(new Dimension(80, 80));

		toggleButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (fillMode == true) // if fill mode is on
				{
					fillMode = false; // turn it off and relabel the button
					toggleButton.setText("Cross");
				}
				else
				{ // if fill mode is off
					fillMode = true; // turn it on and relabel the button
					toggleButton.setText("Fill");
				}
			}
		});

		// Setting up the row markers
		rowMarkerList = new ArrayList<RowMarker>(height); // horizontal

		for (int i = 0; i < height; i++)
		{
			RowMarker rm = new RowMarker(solution, i, Color.white, false);
			rowMarkerList.add(rm);
		}

		columnMarkerList = new ArrayList<RowMarker>(width); // vertical

		for (int i = 0; i < height; i++)
		{
			RowMarker rm = new RowMarker(solution, i, Color.white, true);
			columnMarkerList.add(rm);
		}

		// Testing: test that the RowMarker class knows the number(s)
		// of filled boxes in its row/column
		for (int i = 0; i < width; i++)
		{
			System.out.println(String.format("row %d: %s |column %d: %s", i,
					rowMarkerList.get(i).getText(), i, columnMarkerList.get(i).getText()));
		}

		// Adding the panels
		// edit the GBC as we go
		// the toggle button will be at 0,0
		GBC.gridx = 0;
		GBC.gridy = 0;
		this.add(toggleButton, GBC);

		// the button panel will be at 1,1
		GBC.gridx = 1;
		GBC.gridy = 1;
		this.add(buttonPanel, GBC);

		this.setMinimumSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * Get the current fill mode
	 */
	public boolean getFillMode()
	{
		return fillMode;
	}

	/**
	 * Updates the puzzle grid at the given row and column
	 * with the provided boolean value
	 * 
	 * @param row
	 * @param column
	 * @param filled
	 */
	public void updateGridAtIndex(int row, int column, boolean filled)
	{
		grid[row][column] = filled;
	}

	/**
	 * Compares the game grid to the solution grid and returns
	 * a boolean based on whether or not they match
	 */
	public boolean checkSolution()
	{
		for (int x = 0; x < solution.length; x++)
		{
			for (int y = 0; y < solution[x].length; y++)
			{
				// check that each box of the game grid matches each box of the
				// solution grid
				if (grid[x][y] != solution[x][y])
				{ // return false if the boxes don't match
					return false;
				}
			}
		}
		// return true if everything matches up to this point
		return true;
	}

	/**
	 * Updates the UI based on the player's progress
	 */
	public void updateUI()
	{
		// TODO: finish this method once GUI elements are all in place
	}

	/**
	 * Debug/Testing: Prints the solution with stars
	 * representing filled boxes and
	 * spaces representing empty boxes
	 */
	public void printSolution()
	{
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				if (solution[x][y] == true)
				{ // if true, print a star
					System.out.print("*");
				}
				else
				{ // else print a space
					System.out.print(" ");
				}
			}
			// end each row with a newline
			System.out.print("\n");
		}
	}

	public static void main(String[] args)
	{
		NonogramGame game = new NonogramGame("smile.png");

		// testing that the image was read by printing the solution
		// game.printSolution();

		// compare the grid to the solution grid.
		// should be false since the grid is empty
		// System.out.println(
		// "Has the player completed the puzzle? " + game.checkSolution());

	}

}
