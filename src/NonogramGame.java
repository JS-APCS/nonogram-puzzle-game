import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

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
 *         Java Default Icons
 *         Retrieved from
 *         https://en-human-begin.blogspot.com/2007/11/javas-icons-by-default.html
 * 
 *         Java JLayeredPane
 *         Retrieved from
 *         https://www.geeksforgeeks.org/java-jlayeredpane/
 * 
 *         Java JProgressBar
 *         Retrieved from
 *         https://www.javatpoint.com/java-jprogressbar
 * 
 *         Version/date: 5-25-24
 * 
 *         Responsibilities of class:
 *         Holds the main functions of the game and displays the game's GUI
 *         elements; Also manages the saving and editing of level data, and
 *         the level's timer.
 */
public class NonogramGame extends JFrame // NonogramGame is-a JFrame
{

	private boolean fillMode = true; // NonogramGame has-a fill mode
	private boolean[][] grid; // NonogramGame has-a grid
	private NonogramLevel level; // NonogramGame has-a level
	private JProgressBar progressBar; // NonogramGame has-a progress bar
	private Timer gameTimer; // NonogramGame has-a timer
	private int timeCounter = 0; // NonogramGame keeps track of the time

	// NonogramGame has-a list of row markers and column markers
	private ArrayList<RowMarker> rowMarkerList;

	private int FRAME_SIZE = 700; // NonogramGame knows its frame size

	// This will track the level completion data
	private static ArrayList<String> levelData;

	// These are the colors of the row markers
	public static Color markerColor1 = new Color(204, 221, 252);
	public static Color markerColor2 = new Color(153, 172, 207);
	public static Color solvedColor = new Color(98, 240, 105);

	// The how to play message
	private static String helpMessage = "";

	// This message appears when all levels are complete
	private static String noticeMessage = "";

	/**
	 * Constructor
	 * 
	 * @param fileName
	 */
	public NonogramGame()
	{
		this.setTitle("Nonogram"); // set the frame's title
		this.setLayout(new GridBagLayout());

		// this constraint will allow us to position GUI elements
		GridBagConstraints constraints = new GridBagConstraints();

		// try to open and read from the level data file
		// and the message text file
		Scanner reader = null;

		// setting up the help and warning messages
		try
		{
			reader = new Scanner(new File("messages.txt"));
			String line;

			// this flag will tell us when to start reading the second message
			boolean delimiterReached = false;

			while (reader.hasNext())
			{ // while the file isn't empty, read each line
				line = reader.nextLine();

				// if the line is the message delimiter, then set the flag to
				// true and skip to the next line
				if (line.equals("[--DELIMITER--]"))
				{
					delimiterReached = true;
					line = reader.nextLine();
				}

				if (delimiterReached == false)
				{ // save all the lines that come BEFORE the delimiter here
					helpMessage += line + "\n";
				} // all lines read AFTER the delimiter go here
				else noticeMessage += line + "\n";

			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{// close the file at the end
			if (reader != null) reader.close();
		}

		// now we'll read from the level file
		try
		{
			reader = new Scanner(new File("level_data.txt"));
			levelData = new ArrayList<String>();

			// read from the file until it is empty
			while (reader.hasNext()) levelData.add(reader.nextLine());
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		finally
		{// close the file at the end
			if (reader != null) reader.close();
		}

		// after establishing the array of level data
		// we can create a new level
		level = createNewLevel();

		// What if all the levels are complete and the level
		// object does not get instantiated?
		if (level == null)
		{
			// open a prompt informing the user that they can
			// either reset the data, or stop here
			int response = JOptionPane.showOptionDialog(this, noticeMessage,
					"Hey!", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null,
					new Object[] { "Yes", "No" }, JOptionPane.YES_OPTION);

			if (response == JOptionPane.YES_OPTION)
			{// if yes is chosen, reset the data and start a new game
				resetLevelData();
				level = createNewLevel();
			} // otherwise, close the program
			else System.exit(0);
		}

		// create the arrays with the image dimensions
		grid = new boolean[level.getWidth()][level.getHeight()];

		// JPanels for organization
		// buttonPanel holds the buttons in a grid
		JPanel buttonPanel = new JPanel(
				new GridLayout(level.getWidth(), level.getHeight()));

		// rowMarkerPanel holds the row markers on the side of the grid
		JPanel rowMarkerPanel = new JPanel(new GridBagLayout());

		// columnMarkerPanel holds the row markers on the top of the grid
		JPanel columnMarkerPanel = new JPanel(new GridBagLayout());

		// infoPanel holds the information buttons
		JPanel infoPanel = new JPanel(new GridBagLayout());

		// progressPane holds the progress bar and image
		JLayeredPane progressPane = new JLayeredPane();

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
		{ // this button needs an actionListener
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
		constraints.gridx = 0;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;

		for (int i = 0; i < level.getHeight(); i++)
		{// create and add a marker to the list of markers and to the panel
			RowMarker rm = new RowMarker(level.getSolution(), i, false);
			rm.setHorizontalAlignment(SwingConstants.RIGHT);
			rowMarkerList.add(rm);

			constraints.gridy = i;
			rowMarkerPanel.add(rm, constraints);
		}

		// Adding the column markers
		constraints.gridy = 0;

		for (int i = 0; i < level.getWidth(); i++)
		{// create and add a marker to the list of markers and to the panel
			RowMarker rm = new RowMarker(level.getSolution(), i, true);
			rm.setHorizontalAlignment(SwingConstants.CENTER);
			rm.setVerticalAlignment(SwingConstants.BOTTOM);
			rowMarkerList.add(rm);

			constraints.gridx = i;
			columnMarkerPanel.add(rm, constraints);
		}

		// progress bar setup
		progressBar = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
		progressBar.setBorderPainted(false); // hide border
		progressBar.setValue(getProgress()); // set progress
		progressBar.setBounds(24, 12, 16, 320); // set bounds
		progressBar.setForeground(new Color(250, 32, 32)); // make it red

		progressBar.setString("");
		progressBar.setStringPainted(true);
		progressBar.setVisible(false);

		// this will display an image over the progress bar
		JLabel thermometer = new JLabel(
				new ImageIcon("images/thermometer.png"));
		thermometer.setSize(64, 384);
		thermometer.setVisible(false);
		// add the bar and the image to the layered pane
		progressPane.setPreferredSize(new Dimension(64, 384));
		progressPane.add(thermometer, 2);
		progressPane.add(progressBar, 1);

		// This is a help button that tells you how to play
		JButton helpButton = new JButton();
		helpButton.setPreferredSize(new Dimension(48, 48));
		helpButton.setIcon(UIManager.getIcon("OptionPane.questionIcon"));

		helpButton.addActionListener(new ActionListener()
		{ // this button needs an actionListener
			@Override
			public void actionPerformed(ActionEvent e)
			{// display the "How to play" message
				JOptionPane.showMessageDialog(progressPane.getParent(),
						helpMessage, "How to play",
						JOptionPane.QUESTION_MESSAGE, null);
			}
		});
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		infoPanel.add(helpButton, constraints); // add the button to the panel

		// This button will show/hide the progress bar
		JButton infoButton = new JButton();
		infoButton.setPreferredSize(new Dimension(48, 48));
		infoButton.setIcon(UIManager.getIcon("OptionPane.informationIcon"));

		infoButton.addActionListener(new ActionListener()
		{ // this button needs an actionListener
			@Override
			public void actionPerformed(ActionEvent e)
			{// toggle the visibility of the progress bar and image
				progressBar.setVisible(!progressBar.isVisible());
				thermometer.setVisible(!thermometer.isVisible());
			}
		});
		constraints.gridx = 1;
		constraints.gridy = 0;
		infoPanel.add(infoButton, constraints); // add the button to the panel

		// Adding the panels
		// edit the constraints as we go
		// the toggle button will be at 0,0
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(10, 10, 10, 10);
		this.add(toggleButton, constraints);

		// the row marker panel will be at 0,1
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(2, 2, 2, 2);
		this.add(rowMarkerPanel, constraints);

		// the column marker panel will be at 1,0
		constraints.gridx = 1;
		constraints.gridy = 0;
		this.add(columnMarkerPanel, constraints);

		// the button panel will be at 1,1
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.NONE;
		this.add(buttonPanel, constraints);

		// the info panel will be at 2,0
		constraints.gridx = 2;
		constraints.gridy = 0;
		this.add(infoPanel);

		// the progress pane will be at 2,1
		constraints.gridx = 2;
		constraints.gridy = 1;
		this.add(progressPane, constraints);

		// this label will display the level's elapsed time
		JLabel timerLabel = new JLabel("--:--");
		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timerLabel.setFont(new Font("Courier New", Font.ITALIC, 30));
		timerLabel.setOpaque(true);
		timerLabel.setBackground(NonogramGame.markerColor1);

		// adding the level's timer label
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.ipadx = 10;
		constraints.ipady = 10;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		infoPanel.add(timerLabel, constraints);

		// Timer set up
		gameTimer = new Timer(1000, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{ // update time and timer label
				timeCounter++;
				timerLabel.setText(getTime());
			}
		});

		this.setMinimumSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * Creates a NonogramLevel that will either be
	 * a level or null based on whether an incomplete
	 * level is available in the level data array
	 * 
	 * @return NonogramLevel or null
	 */
	public NonogramLevel createNewLevel()
	{
		NonogramLevel newLevel = null;
		// iterate through each level and instantiate a level object
		// once the next incomplete level is found
		for (int i = 0; i < levelData.size(); i++)
		{
			if (levelData.get(i).contains("incomplete"))
			{
				// setup the level with the name
				newLevel = new NonogramLevel(levelData.get(i), i);
				break; // a level was found, so we can stop here
			}
		}
		// this should return a new level or null if
		// an incomplete level isn't found
		return newLevel;
	}

	/**
	 * Get the current fill mode
	 * 
	 * @return fillMode
	 */
	public boolean getFillMode()
	{
		return fillMode;
	}

	/**
	 * Get the elapsed time of the level
	 * 
	 * @return the current level's time
	 */
	public String getTime()
	{
		String minutes = timeCounter / 60 > 9 ? "" + timeCounter / 60
				: "0" + timeCounter / 60;
		String seconds = timeCounter % 60 > 9 ? "" + timeCounter % 60
				: "0" + timeCounter % 60;
		return minutes + ":" + seconds;
	}

	/**
	 * Get the game's timer
	 * 
	 * @return gameTimer
	 */
	public Timer getTimer()
	{
		return gameTimer;
	}

	/**
	 * Get the current level
	 * 
	 * @return level
	 */
	public NonogramLevel getLevel()
	{
		return level;
	}

	/**
	 * Returns the progress of the level's
	 * completion as a percent
	 * 
	 * @return the current level's completion as a percent
	 */
	public int getProgress()
	{
		int progress = 0; // this will track progress
		// total number of spaces in the grid
		int total = level.getWidth() * level.getHeight();

		for (int x = 0; x < level.getSolution().length; x++)
		{
			for (int y = 0; y < level.getSolution()[x].length; y++)
			{
				// if the player's grid matches the solution
				if (grid[x][y] == level.getSolution()[x][y])
				{
					progress++; // add to the progress
				}
			}
		}
		// return a percentage of the progress / total
		return (progress * 100) / total;
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
		// update progress bar
		if (progressBar.getValue() < getProgress())
		{// if the progress should increase, turn the bar red
			progressBar.setForeground(new Color(250, 32, 32));
			progressBar.setString("Getting Warmer...");
		}
		else
		{ // otherwise, turn it blue
			progressBar.setForeground(new Color(33, 87, 235));
			progressBar.setString("Getting Colder...");
		}
		// update progress bar value
		progressBar.setValue(getProgress());
	}

	/**
	 * Writes the completion status of a level
	 * to the level data file
	 */
	public void saveDataToFile()
	{
		// create a file writer
		BufferedWriter writer = null;
		try
		{
			// overwrite the level data file
			writer = new BufferedWriter(
					new FileWriter("level_data.txt", false));

			for (String data : levelData)
			{ // write each line of data to the file
				writer.write(data);
				writer.newLine();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{// finally, close the file
			if (writer != null) try
			{
				writer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Modifies the level data array at the
	 * index of the current level and writes
	 * the contents to the level data file
	 * 
	 * @param completionStatus
	 */
	public void editLevelData(String completionStatus)
	{
		// edit the data of the current level
		levelData.set(level.getID(),
				level.getName() + " - " + completionStatus + " - " + getTime());
		// save the data to the file
		saveDataToFile();
	}

	public void resetLevelData()
	{
		// to reset level data, set all
		// completion statuses to incomplete
		// and write to the file
		for (int i = 0; i < levelData.size(); i++)
		{
			// get the current string and alter it
			String data = levelData.get(i).replace("complete", "incomplete");
			data = data.replaceAll("\\d", "-"); // reset level times
			levelData.set(i, data); // set the current index to the altered data
		}
		// after altering the data, save it to the file
		saveDataToFile();
	}

	/**
	 * Starts a new game and disposes of the current one
	 * (This method will change once text IO is implemented)
	 */
	public void restart()
	{
		NonogramGame game = new NonogramGame();
		this.dispose();
	}

	public static void main(String[] args)
	{
		NonogramGame game = new NonogramGame();
	}

}
