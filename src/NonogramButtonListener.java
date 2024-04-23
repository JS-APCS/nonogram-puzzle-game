import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
 *         Version/date: 4-22-24
 * 
 *         Responsibilities of class:
 *         A modified ActionListener that listens to the NonogramButton class
 *         and updates the NonogramGame's board of buttons to reflect whether
 *         or not they are filled in or crossed out. It also checks the 
 *         player's progress after every move.
 */
public class NonogramButtonListener implements ActionListener
{
	private NonogramGame game; // NonogramButtonListener has-a NonogramGame
	private NonogramButton button; // NonogramButtonListener has-a NonogramButton

	// NonogramButtonListener has image icons for setting the button's icon
	private ImageIcon fillIcon = new ImageIcon("images/fill-in.gif");
	private ImageIcon crossIcon = new ImageIcon("images/cross-out.gif");

	/**
	 * Constructor
	 * 
	 * @param game
	 * @param button
	 */
	public NonogramButtonListener(NonogramGame game, NonogramButton button)
	{
		this.game = game;
		this.button = button;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// on a button click, check this structure
		if (game.getFillMode())
		{ // if the fill mode is true (fill)
			if (!button.getIsFilled() && button.getIcon() == null)
			{ // and if the button is not filled
				// then we can set it to filled and change its appearance
				button.setIsFilled(true);
				button.setIcon(fillIcon);
			}
			else if (button.getIsFilled() && button.getIcon() != null)
			{ // if the button is filled
				// then we can un-fill it and update the appearance
				button.setIsFilled(false);
				button.setIcon(null);
			}
			// update the puzzle grid based on the changes we made
			game.updateGridAtIndex(button.getRow(), button.getColumn(),
					button.getIsFilled());

			// after any move has been made with the fill mode set to true
			// check if the player's progress matches the solution
			if (game.checkSolution())
			{
				System.out.println("Puzzle Complete!");
			}
		}
		else
		{ // if the fill mode is not true (empty)
			if (!button.getIsFilled() && button.getIcon() == null)
			{ // if the button is empty and it has no icon
				// we can cross it out
				button.setIcon(crossIcon);
			}
			else if (!button.getIsFilled() && button.getIcon() != null)
			{ // if the button is empty, but it has an icon
				// we can assume it's crossed out, then remove the icon
				button.setIcon(null);
			}
		}

	}

}
