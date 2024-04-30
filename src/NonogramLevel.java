import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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
 *         Version/date: 4-29-24
 * 
 *         Responsibilities of class:
 *         Represents a level of the game based on the given image name;
 *         establishes the solution array and image width and height by
 *         reading an image file.
 */
public class NonogramLevel
{
	private String name; // NonogramLevel has-a name
	private BufferedImage image; // NonogramLevel has-an image
	private ImageIcon solvedImage; // NonogramLevel has-a solved image
	private int width, height; // NonogramLevel has-a width and height
	private boolean[][] solution; // NonogramLevel has-a solution array
	
	public NonogramLevel(String name)
	{
		this.name = name;
		
		try
		{ // try to read the image based on the level's name
			image = ImageIO.read(new File("images/" + name + ".png"));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		solvedImage = new ImageIcon("images/" + name + "_solved.png");
		
		width = image.getWidth();
		height = image.getHeight();
		
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
	 * Get the level solution
	 * @return solution
	 */
	public boolean[][] getSolution()
	{
		return solution;
	}
	
	/**
	 * Get the level width
	 * @return width
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Get the level height
	 * @return height
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Get the icon of the solved image
	 * @return solvedImage
	 */
	public ImageIcon getSolvedIcon()
	{
		return solvedImage;
	}
	
	/**
	 * Get the name of the puzzle
	 * @return name
	 */
	public String getName()
	{
		return name;
	}
}
