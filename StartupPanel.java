// StartupPanel Class
// Description: 
// Created By: Abdelrahman Moustafa
// Last Modified: 01/20/23
import javax.swing.*;
import java.awt.*;

public class StartupPanel extends JPanel
{
  // overriding paintComponent to draw half black, half white background
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    // white
    g.setColor(Color.WHITE);
    g.fillRect(0,0,this.getSize().width,this.getSize().height);
    
    // black
    g.setColor(Color.BLACK);
    g.fillRect(0,0,(this.getSize().width/2)-2,this.getSize().height);
  }
}