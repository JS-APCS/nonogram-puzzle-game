import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * @author Jaiden Smith
 * 
 *         References:
 *         Morelli, R., & Walde, R. (2016). Java, Java, Java: Object-Oriented
 *         Problem Solving.
 *         Retrieved from
 *         https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
 * 
 *         Add Image to JOptionPane
 *         Retrieved from
 *         https://stackoverflow.com/questions/13963392/add-image-to-joptionpane
 * 
 *         Version/date: 5-13-24
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
	private NonogramButton button; // NonogramButtonListener has-a
									// NonogramButton
	private NonogramLevel level; // NonogramButtonListener has-a NonogramLevel

	// NonogramButtonListener has image icons for setting the button's icon
	private static ImageIcon fillIcon = new ImageIcon("images/filled.gif");
	private static ImageIcon crossIcon = new ImageIcon("images/cross-out.gif");

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
		level = game.getLevel();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// the level timer will start once any grid buttons are clicked
		if (!game.getTimer().isRunning())
		{ // if it's not running, then start the timer
			game.getTimer().start();
		}

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
			// check if the player's progress is at 100
			if (game.getProgress() == 100)
			{
				game.getTimer().stop(); // stop the level timer
				System.out.println("Puzzle Complete!");

				game.editLevelData("complete"); // save the level data

				// show the complete image, the player's time, and prompt to continue
				int response = JOptionPane.showOptionDialog(game,
						String.format(
								"It's a %s!\r\n" + "Time: %s\r\n" + "Continue?",
								level.getName(), game.getTime()),
						"Puzzle Complete!", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, level.getSolvedIcon(),
						new Object[] { "Yes", "No" }, JOptionPane.YES_OPTION);

				if (response == JOptionPane.YES_OPTION)
				{ // if yes, start a new game
					game.restart(); // start a new game
				}
				else
				{ // otherwise, we can close the program
					System.exit(0);
				}
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
		game.updateUI(); // update the UI
	}

}
