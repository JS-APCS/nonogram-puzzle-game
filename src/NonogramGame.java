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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author Jaiden Smith
 * 
 *         References:
 *         Morelli, R., & Walde, R. (2016). Java, Java, Java: Object-Oriented
 *         Problem Solving.
 *         Retrieved from
 *         https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
 * 
 *         Java GridBagLayout
 *         Retrieved from
 *         https://www.javatpoint.com/java-gridbaglayout
 * 
 *         Version/date: 4-29-24
 * 
 *         Responsibilities of class:
 *         Holds the main functions of the game and displays the game's GUI
 *         elements;
 */
public class NonogramGame extends JFrame // NonogramGame is-a JFrame
{
	
	private boolean fillMode = true; // NonogramGame has-a fill mode
	private boolean[][] grid; // NonogramGame has-a grid
	private NonogramLevel level; // NonogramGame has-a level

	// NonogramGame has-a list of row markers and column markers
	private ArrayList<RowMarker> rowMarkerList;

	private int FRAME_SIZE = 700; // NonogramGame knows its frame size
	
	// These are the colors of the row markers
	public static Color markerColor1 = new Color(204, 221, 252);
	public static Color markerColor2 = new Color(153, 172, 207);
	public static Color solvedColor = new Color(98, 240, 105);

	/**
	 * Constructor
	 * 
	 * @param fileName
	 */
	public NonogramGame(String name)
	{
		this.setTitle("Nonogram"); // set the frame's title
		this.setLayout(new GridBagLayout());

		// this constraint will allow us to position GUI elements
		GridBagConstraints GBC = new GridBagConstraints();

		level = new NonogramLevel(name); // setup the level with the name

		// create the arrays with the image dimensions
		grid = new boolean[level.getWidth()][level.getHeight()];

		// JPanels for organization
		// buttonPanel holds the buttons in a grid
		JPanel buttonPanel = new JPanel(new GridLayout(level.getWidth(), level.getHeight()));
		// rowMarkerPanel holds the row markers on the side of the grid
		JPanel rowMarkerPanel = new JPanel(new GridBagLayout());
		// columnMarkerPanel holds the row markers on the top of the grid
		JPanel columnMarkerPanel = new JPanel(new GridBagLayout());

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
		rowMarkerList = new ArrayList<RowMarker>();
		
		// constraint configurations for the row markers
		GBC.gridx = 0;
		GBC.weightx = 1.0;
		GBC.weighty = 1.0;
		GBC.fill = GridBagConstraints.BOTH;

		for (int i = 0; i < level.getHeight(); i++)
		{// create and add a marker to the list of markers and to the panel
			RowMarker rm = new RowMarker(level.getSolution(), i, false);
			rm.setHorizontalAlignment(SwingConstants.RIGHT);
			rowMarkerList.add(rm);
			
			GBC.gridy = i;
			rowMarkerPanel.add(rm, GBC);
		}

		// Adding the column markers

		GBC.gridy = 0;
		
		for (int i = 0; i < level.getWidth(); i++)
		{// create and add a marker to the list of markers and to the panel
			RowMarker rm = new RowMarker(level.getSolution(), i, true);
			rm.setHorizontalAlignment(SwingConstants.CENTER);
			rm.setVerticalAlignment(SwingConstants.BOTTOM);
			rowMarkerList.add(rm);
			
			GBC.gridx = i;
			columnMarkerPanel.add(rm, GBC);
		}
		

		// Adding the panels
		// edit the GBC as we go
		// the toggle button will be at 0,0
		GBC.weightx = 0;
		GBC.weighty = 0;
		GBC.gridx = 0;
		GBC.gridy = 0;
		this.add(toggleButton, GBC);
		
		// the row marker panel will be at 0,1
		GBC.gridx = 0;
		GBC.gridy = 1;
		GBC.fill = GridBagConstraints.BOTH;
		this.add(rowMarkerPanel, GBC);
		
		// the column marker panel will be at 1,0
		GBC.gridx = 1;
		GBC.gridy = 0;
		this.add(columnMarkerPanel, GBC);

		// the button panel will be at 1,1
		GBC.gridx = 1;
		GBC.gridy = 1;
		GBC.fill = GridBagConstraints.NONE;
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
	 * Get the current level
	 * @return level
	 */
	public NonogramLevel getLevel()
	{
		return level;
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
		for (int x = 0; x < level.getSolution().length; x++)
		{
			for (int y = 0; y < level.getSolution()[x].length; y++)
			{
				// check that each box of the game grid matches each box of the
				// solution grid
				if (grid[x][y] != level.getSolution()[x][y])
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
		for (RowMarker marker : rowMarkerList)
		{ // for every marker, check if the row matches the solution
			if (marker.checkRow(grid))
			{// if so, change the color to green
				marker.setBackground(solvedColor);
			}
			else
			{// otherwise, reset the color
				marker.setBackground(marker.getColor());
			}
		}
	}
	
	/**
	 * Starts a new game and disposes of the current one
	 * (This method will change once text IO is implemented)
	 */
	public void restart()
	{
		String[] levels = {"smile", "heart", "spiral", "donut"};
		
		// create a game with a random puzzle
		NonogramGame game = new NonogramGame(levels[new Random().nextInt(levels.length)]);
		this.dispose();
	}

	/**
	 * Debug/Testing: Prints the solution with stars
	 * representing filled boxes and
	 * spaces representing empty boxes
	 */
	public void printSolution()
	{
		for (int x = 0; x < level.getWidth(); x++)
		{
			for (int y = 0; y < level.getHeight(); y++)
			{
				if (level.getSolution()[x][y] == true)
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
		/* For testing purposes: this will be used to start
		 * a random puzzle on each run. I will eventually
		 * implement text file IO for reading and saving
		 * level data.
		 */
		String[] levels = {"smile", "heart", "spiral", "donut"};
		
		// create a game with a random puzzle
		NonogramGame game = new NonogramGame(levels[new Random().nextInt(levels.length)]);

		// testing that the image was read by printing the solution
		//game.printSolution();

	}

}
