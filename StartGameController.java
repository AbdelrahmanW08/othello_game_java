// StartGameController Class
// Description: 
// Created By: Abdelrahman Moustafa
// Last Modified: 01/20/23
import javax.swing.*;
import java.awt.event.*;

public class StartGameController extends Object implements ActionListener
{
  // Instance Variables
  private GameModel game;              // model
  private GameView view;               // view
  private JTextField name;             // name of user
  private JTextField numRoundsText;    // number of rounds
  private int numRounds;               // int for numRounds to check if its actually a number
  
  
  // constructor
  public StartGameController(GameModel aGame, GameView aView, JTextField nameField, JTextField numRoundField) {
    this.game = aGame;
    this.view = aView;
    this.name = nameField;
    this.numRoundsText = numRoundField;
  }
  
  // called when begin button pressed
  public void actionPerformed(ActionEvent e) {
    
    // try to catch a number format exception
    try {
      
      // throws number format exception if not actually an int
      numRounds = Integer.parseInt(this.numRoundsText.getText());
      
      // if numRounds less than 1 or name is nothing, show error message
      if (numRounds < 1 || this.name.getText().equals("") || this.name.getText().indexOf("/") != -1) {
        this.view.showRoundSelectError(true);
        
      // if name and numRounds valid, start the game
      } else {
        this.view.showRoundSelectError(false);
        this.game.startGame(this.numRounds, this.name.getText());
      }
      
    // if not number, bad
    } catch (NumberFormatException a) {
      this.view.showRoundSelectError(true);
    }
  }
}