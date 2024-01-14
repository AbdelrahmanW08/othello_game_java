// GameSquareIcon Class
// Description: 
// Created By: Abdelrahman Moustafa
// Last Modified: 01/20/23
import javax.swing.*;
import java.awt.*;

public class GameSquareIcon extends JButton
{
  // Instance Variables
  private int currentPiece;
  
  // constructor
  public GameSquareIcon () {
    super();
    
    // sets the square to not have a piece on it by default and sets size
    this.currentPiece = 0;
    this.setPreferredSize(new Dimension(90,90));
    
    // sets default settings for button it be invisible
    this.setOpaque(false);
    this.setContentAreaFilled(false);
    this.setBorderPainted(false);
  }
  
  // setPieceColor method, sets the piece on the square
  public void setPieceColor(int newPiece) {
    this.currentPiece = newPiece;
    this.repaint();
  } // end of setPieceColor method
  
  // getPieceColor method, gets the current piece that is being shown on the square
  public int getPieceColor() {
    return this.currentPiece;
  } // end of getPieceColor method
  
  // overriding paintComponent method to draw the green background and the pieces on it
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    // drawing the green square
    g.setColor(new Color(57, 112, 6));
    g.fillRect(0,0,this.getSize().width,this.getSize().height);
    
    // drawing a black outline
    g.setColor(Color.BLACK);
    g.drawRect(0,0,this.getSize().width,this.getSize().height);
    
    // draws the piece based on the currentPiece variable
    if (currentPiece == 1) {
      g.setColor(Color.WHITE);
      g.fillOval(9, 9, 70, 70);
    } else if (currentPiece == 2) {
      g.setColor(Color.BLACK);
      g.fillOval(9, 9, 70, 70);
    }
  } // end of paintComponent
}