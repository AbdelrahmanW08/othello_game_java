// ButtonPressedController Class
// Description: 
// Created By: Abdelrahman Moustafa
// Last Modified: 01/20/23
import javax.swing.*;
import java.awt.event.*;

public class ButtonPressedController extends Object implements ActionListener
{
  // Instance Variables
  private GameModel game;  // model
  private JButton button;  // the button that was pressed
  
  // constructor
  public ButtonPressedController(GameModel aGame, JButton aButton) { 
    this.game = aGame;
    this.button = aButton;
  }
  
  // called when button pressed
  public void actionPerformed(ActionEvent e) {
    
    // tells method that button was pressed and gives it the button name
    this.game.buttonPressed(this.button.getText());
  }
}