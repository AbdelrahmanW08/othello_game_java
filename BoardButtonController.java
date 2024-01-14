// BoardButtonController Class
// Description: 
// Created By: Abdelrahman Moustafa
// Last Modified: 01/20/23
import java.awt.event.*;

public class BoardButtonController extends Object implements ActionListener
{
  // Instance Variables
  private GameModel game; // model
  private int column;     // column of the selected move
  private int row;        // row of the selected move
  
  // constructor
  public BoardButtonController(GameModel aGame, int x, int y) {
    this.game = aGame;
    this.column = x;
    this.row = y;
  }
  
  // called when button pressed
  public void actionPerformed(ActionEvent e) {
    
    // tells the model that the user made a move
    this.game.boardButtonPressed(column, row);
  }
}