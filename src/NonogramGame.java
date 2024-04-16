import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

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
 *         Version/date: 4-12-24
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

	/**
	 * Constructor
	 * 
	 * @param fileName
	 */
	public NonogramGame(String fileName)
	{
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
		game.printSolution();
	}

}
