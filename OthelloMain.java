// Startup Class
// Description: 
// Created By: Abdelrahman Moustafa
// Last Modified: 01/20/23
import javax.swing.*;

public class OthelloMain
{
  public static void main (String [] args)
  {
    // Declaring and Initializing Frame, Model and View
    JFrame f = new JFrame("Othello");
    GameModel game = new GameModel(f);         //The game model
    GameView mainScreen = new GameView(game);  //The game view
    
    //Setting up the Frame
    f.setSize(1200,800);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setContentPane(mainScreen);
    f.setUndecorated(false);
    f.setVisible(true);
  }
}//end of Main class