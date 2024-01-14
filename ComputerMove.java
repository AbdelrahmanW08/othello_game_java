// ComputerMove Class
// Description: 
// Created By: Abdelrahman Moustafa
// Last Modified: 01/20/23
public class ComputerMove implements Runnable
{
  // Model
  private GameModel game;
  
  // constructor
  public ComputerMove (GameModel aGame) {
    this.game = aGame;
  }
  
  // run method, this class made so that it runs the computer move on a different thread to add a delay between
  // player and computer moves
  public void run () {
    this.game.computerMove();
  }
}