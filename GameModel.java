// Model Class
// Description: 
// Created By: Abdelrahman Moustafa
// Last Modified: 01/20/23
import javax.swing.*;

public class GameModel extends Object
{
  // hold the view and frame (frame for close button)
  private GameView gameView;
  private JFrame f;
  
  // holds the current status that changes view
  private String status;
  
  // basic variables for game, self explanatory
  private int currentPlayer;
  private boolean validMove;
  private String name;
  private int numRounds;
  
  // algorithm related variables 
  private int [][] boardMap;              // board map to hold info on each square on board
  private int [][] possibleMoves;         // holds possible moves on each player's turn
  private boolean [] possibleDirections;  // finds the possible directions to flip pieces for a move
  private int computerBestMoveCounter;    // a counter that counts how many enemy pieces a move will flip
  
  // information about end game
  private int finishCounter;  // decides when a game is over
  private int winner;         // holds the winner of a round
  
  // constructor
  public GameModel(JFrame f) {
    super();
    
    // sets current status to main menu and declares f with frame
    this.status = "start";
    this.f = f;
  } // end of constructor
  
  // setGUI method, declares the gameView
  public void setGUI(GameView currentGUI) {
    this.gameView = currentGUI;
  } // end of setGUI
  
  // buttonPressed method, called whenever a basic button is pressed and changes view
  public void buttonPressed(String action) {
    if (action.equals("Player Vs. Comp")) {
      this.status = "round select";
    } else if (action.equals("Leaderboard")) {
      this.status = "leaderboard";
    } else if (action.equals("Exit")) {
      f.dispose();
    } else if (action.equals("Back") || action.equals("Back To Main Menu")) {
      this.status = "start";
    } else if (action.equals("Forfeit")) {
      this.endRound(true);
      return;
    } else if (action.equals("Go To Next Round")) {
      this.startGame(this.numRounds, this.name);
    }
    
    this.updateView();
  } // end of buttonPressed method
  
  // boardButtonPressed, gets called when black pressed a square on the board
  public void boardButtonPressed(int column, int row) {
    possibleMoves = new int[1][3];
    
    // checks that it is the players turn and that nothing is in that square
    if (this.currentPlayer == 2 && this.boardMap[row][column] == 0) {
      
      // checks if it is an available move
      this.checkAvailableMove(column, row, 2);
      if (this.validMove) {
        // if so, places it on the board and flips over all appropriate pieces
        this.boardMap[row][column] = 2;
        this.gameView.setPiece(column, row, 2);
        this.flipPieces(column, row, 2);
        
        // computer makes a move
        this.computerMoveThread();
      } else
        this.gameView.setStatusText("Invalid move.");
    }
  } // end of boardButtonPressed
  
  // computerMoveThread, runs computerMove method in a thread
  public void computerMoveThread() {
    
    // this entire method is in a new thread because I want there to be pauses between player and computer moves but
    // when I used Thread.sleep() normally, it would stop any repainting of View, so I had to do it in a separate thread.
    Thread computerMoveThread = new Thread(new ComputerMove(this));
    computerMoveThread.start();
  } // end of computerMoveThread method
  
  // computerMove method, algorithm to decide best move for computer and plays it
  public void computerMove() {
      try {
        // updates to tell user it is computer's turn
        this.currentPlayer = 1;
        this.gameView.setEnabledForfeit(false);
        this.gameView.setStatusText("Computer is thinking...");
        Thread.sleep(1000);
        
        int attempt = -1;
        possibleMoves = new int[1][3];
        possibleMoves[0][0] = -1;
        
        // checks all squares to find possible moves and adds them to possibleMoves array
        for (int x = 0; x < 64; x++) {
          attempt++;
          if (this.boardMap[attempt/8][attempt%8] == 0)
            this.checkAvailableMove(attempt%8, attempt/8, 1);
        }
        
        // checks if there is a possible move
        if (possibleMoves[0][0] == -1) {
          // if not, check if game is over
          if (++finishCounter == 2) {
            // end game if so
            this.gameView.setStatusText("Game Over!");
            Thread.sleep(4000);
            this.gameView.setEnabledForfeit(true);
            this.endRound(false);
            return;
          }
          
          // skips computer's turn
          this.gameView.setStatusText("No Moves For White, Skipping...");
          Thread.sleep(2000);
        } else {
          finishCounter = 0;
          
          int bestX = 0;
          int bestY = 0;
          int bestCount = 0;
          
          // if there is a possible move, find the best one
          for (int x = 0; x < possibleMoves.length; x++) {
            bestCount = Math.max(bestCount,possibleMoves[x][2]);
            if (bestCount == possibleMoves[x][2]) {
              bestX = possibleMoves[x][1];
              bestY = possibleMoves[x][0];
            }
          }
          
          // checks again so that possibleDirections array is accurate to that move
          this.checkAvailableMove(bestY, bestX, 1);
          
          // puts down the piece and flips appropriate discs
          this.boardMap[bestX][bestY] = 1;
          this.gameView.setPiece(bestY, bestX, 1);
          this.flipPieces(bestY, bestX, 1);
        }
        
        // resets status text
        this.gameView.setStatusText("-");
        
        attempt = -1;
        possibleMoves = new int[1][3];
        possibleMoves[0][0] = -1;
        
        // checks all squares to find possible moves and adds them to possibleMoves array
        for (int x = 0; x < 64; x++) {
          attempt++;
          if (this.boardMap[attempt/8][attempt%8] == 0)
            this.checkAvailableMove(attempt%8, attempt/8, 2);
        }
        
        // checks if there are possible moves for user
        if (possibleMoves[0][0] == -1) {
          // if not, check if game is over
          if (++finishCounter == 2) {
            // end game
            this.gameView.setStatusText("Game Over!");
            Thread.sleep(4000);
            this.gameView.setEnabledForfeit(true);
            this.endRound(false);
            return;
          }
          
          // skip turn and execute computerMove() again
          this.gameView.setStatusText("No Moves For Black, Skipping...");
          Thread.sleep(2000);
          this.computerMove();
        } else {
          
          // if there is a possible move, go to user's turn
          finishCounter = 0;
          this.gameView.setEnabledForfeit(true);
          this.currentPlayer = 2;
        }
      } catch (InterruptedException e) {} 
  } // end of computerMove method

  // endRound method, called when a game is finished or when user forfeits
  public void endRound(boolean forfeit) {
    // sets status to update view
    this.status = "end of round";
    
    // decrements rounds and sets up variables to determine winner
    this.numRounds--;
    this.winner = 0;
    int blackCounter = 0;
    int whiteCounter = 0;
    
    // if the user forfeit, the win is automatically given to computer
    if (forfeit)
      winner = 1;
    else {
      // if no forfeit, count the pieces
      for (int x = 0; x < 8; x++) {
        for (int y = 0; y < 8; y++) {
          if (this.boardMap[x][y] == 1)
            whiteCounter++;
          else if (this.boardMap[x][y] == 2)
            blackCounter++;
        }
      }
      
      // determines winner respectively
      if (whiteCounter > blackCounter) {
        winner = 1;
      } else if (whiteCounter < blackCounter) {
        winner = 2;
      }
    }
    this.updateView();
  } // end of endRound method
  
  // getMoreRounds method, accessor method to find if there are any more rounds left
  public boolean getMoreRounds() {
    return this.numRounds > 0;
  } // end of getMoreRounds method
  
  // getName method, accessor method for name
  public String getName() {
    return this.name;
  } // end of getName method
  
  // getEndScreenText method, returns an appropriate end screen message based on winner
  public String getEndScreenText() {
    if (this.winner == 1)
      return "You Lost...";
    else if (this.winner == 2)
      return "You Won!";
    else
      return "The Game was Tied.";
  } // end of getEndScreenText method
  
  // checkAvailableMove method, checks if a given move is legal (where all the magic happens)
  public void checkAvailableMove(int column, int row, int player) {
    
    // resets possible directions and best move counter for this move
    this.possibleDirections = new boolean[8];
    if (player == 1)
      this.computerBestMoveCounter = 0;
    
    // checks if the move can flip discs on each of the 8 sides
    this.checkAvailableMoveLeft(column, row, true, player);
    this.checkAvailableMoveUpLeft(column, row, true, player);
    this.checkAvailableMoveUp(column, row, true, player);
    this.checkAvailableMoveUpRight(column, row, true, player);
    this.checkAvailableMoveRight(column, row, true, player);
    this.checkAvailableMoveDownRight(column, row, true, player);
    this.checkAvailableMoveDown(column, row, true, player);
    this.checkAvailableMoveDownLeft(column, row, true, player);
    
    // checks if at least one side is valid
    this.validMove = false;
    for (int x = 0; x < 8; x++) {
      if (possibleDirections[x])
        this.validMove = true;
    }
    
    // if at least one side is valid, add it to the possibleMoves array
    if (this.validMove)
      this.addToPossibleMovesArray(column, row, this.computerBestMoveCounter);
  } // end of checkAvailableMove method
  
  // ****************************************************************************************************
  //
  // What follows is the methods for checking an available move on each side, I won't comment all of them because
  // they are all the same with very small changes to numbers. I will only comment checkAvailableMoveLeft (the first one)
  //
  //****************************************************************************************************
  
  // checkAvailableMoveLeft method, checks if a disc can be flipped over to the left of the placed piece
  public void checkAvailableMoveLeft(int column, int row, boolean isFirstCheck, int player) {
    
    // try statement in case we try to go off the grid
    try {
      // checks if it is the computers's turn
      if (player == 1) {
        
        // checks if the disc to the left is a user piece
        if (this.boardMap[row][column-1] == 2) {
          
          // if so, check to the left of that one and increment computer best move if it is a legal move
          this.checkAvailableMoveLeft(column-1, row, false, player);
          if (possibleDirections[0])
            this.computerBestMoveCounter++;
          
        // checks if the disc to the left is a computer piece and it is not the first time checking this side, legal move
        } else if (this.boardMap[row][column-1] == 1 && !isFirstCheck)
          possibleDirections[0] = true;
        
        // otherwise, not a legal move
        else
          possibleDirections[0] = false;
        
      // checks if it is the user's turn
      } else if (player == 2) {
        
        // checks if the disc to the left is a computer piece
        if (this.boardMap[row][column-1] == 1)
          
          // if it is, check to the left of that one
          this.checkAvailableMoveLeft(column-1, row, false, player);
        
        // checks if the disc to the left is a user piece and it is not the first time checking this side, legal move
        else if (this.boardMap[row][column-1] == 2 && !isFirstCheck)
          possibleDirections[0] = true;
        
        // otherwise, not a legal move
        else
          possibleDirections[0] = false;
      }
    // if we ever go out of bounds, obviously not a legal move
    } catch (IndexOutOfBoundsException e) {
      possibleDirections[0] = false;
    }
  } // end of checkAvailableMoveLeft method
  
  public void checkAvailableMoveUpLeft(int column, int row, boolean isFirstCheck, int player) {
    try {
      if (player == 1) {
        if (this.boardMap[row-1][column-1] == 2) {
          this.checkAvailableMoveUpLeft(column-1, row-1, false, player);
          if (possibleDirections[1])
            this.computerBestMoveCounter++;
        } else if (this.boardMap[row-1][column-1] == 1 && !isFirstCheck)
          possibleDirections[1] = true;
        else
          possibleDirections[1] = false;
      } else if (player == 2) {
        if (this.boardMap[row-1][column-1] == 1)
          this.checkAvailableMoveUpLeft(column-1, row-1, false, player);
        else if (this.boardMap[row-1][column-1] == 2 && !isFirstCheck)
          possibleDirections[1] = true;
        else
          possibleDirections[1] = false;
      }
    } catch (IndexOutOfBoundsException e) {
      possibleDirections[1] = false;
    }
  }
  
  public void checkAvailableMoveUp(int column, int row, boolean isFirstCheck, int player) {
    try {
      if (player == 1) {
        if (this.boardMap[row-1][column] == 2) {
          this.checkAvailableMoveUp(column, row-1, false, player);
          if (possibleDirections[2])
            this.computerBestMoveCounter++;
        } else if (this.boardMap[row-1][column] == 1 && !isFirstCheck)
          possibleDirections[2] = true;
        else
          possibleDirections[2] = false;
      } else if (player == 2) {
        if (this.boardMap[row-1][column] == 1)
          this.checkAvailableMoveUp(column, row-1, false, player);
        else if (this.boardMap[row-1][column] == 2 && !isFirstCheck)
          possibleDirections[2] = true;
        else
          possibleDirections[2] = false;
      }
    } catch (IndexOutOfBoundsException e) {
      possibleDirections[2] = false;
    }
  }
  
  public void checkAvailableMoveUpRight(int column, int row, boolean isFirstCheck, int player) {
    try {
      if (player == 1) {
        if (this.boardMap[row-1][column+1] == 2) {
          this.checkAvailableMoveUpRight(column+1, row-1, false, player);
          if (possibleDirections[3])
            this.computerBestMoveCounter++;
        } else if (this.boardMap[row-1][column+1] == 1 && !isFirstCheck)
          possibleDirections[3] = true;
        else
          possibleDirections[3] = false;
      } else if (player == 2) {
        if (this.boardMap[row-1][column+1] == 1)
          this.checkAvailableMoveUpRight(column+1, row-1, false, player);
        else if (this.boardMap[row-1][column+1] == 2 && !isFirstCheck)
          possibleDirections[3] = true;
        else
          possibleDirections[3] = false;
      }
    } catch (IndexOutOfBoundsException e) {
      possibleDirections[3] = false;
    }
  }
  
  public void checkAvailableMoveRight(int column, int row, boolean isFirstCheck, int player) {
    try {
      if (player == 1) {
        if (this.boardMap[row][column+1] == 2) {
          this.checkAvailableMoveRight(column+1, row, false, player);
          if (possibleDirections[4])
            this.computerBestMoveCounter++;
        } else if (this.boardMap[row][column+1] == 1 && !isFirstCheck)
          possibleDirections[4] = true;
        else
          possibleDirections[4] = false;
      } else if (player == 2) {
        if (this.boardMap[row][column+1] == 1)
          this.checkAvailableMoveRight(column+1, row, false, player);
        else if (this.boardMap[row][column+1] == 2 && !isFirstCheck)
          possibleDirections[4] = true;
        else
          possibleDirections[4] = false;
      }
    } catch (IndexOutOfBoundsException e) {
      possibleDirections[4] = false;
    }
  }
  
  public void checkAvailableMoveDownRight(int column, int row, boolean isFirstCheck, int player) {
    try {
      if (player == 1) {
        if (this.boardMap[row+1][column+1] == 2) {
          this.checkAvailableMoveDownRight(column+1, row+1, false, player);
          if (possibleDirections[5])
            this.computerBestMoveCounter++;
        } else if (this.boardMap[row+1][column+1] == 1 && !isFirstCheck)
          possibleDirections[5] = true;
        else
          possibleDirections[5] = false;
      } else if (player == 2) {
        if (this.boardMap[row+1][column+1] == 1)
          this.checkAvailableMoveDownRight(column+1, row+1, false, player);
        else if (this.boardMap[row+1][column+1] == 2 && !isFirstCheck)
          possibleDirections[5] = true;
        else
          possibleDirections[5] = false;
      }
    } catch (IndexOutOfBoundsException e) {
      possibleDirections[5] = false;
    }
  }
  
  public void checkAvailableMoveDown(int column, int row, boolean isFirstCheck, int player) {
    try {
      if (player == 1) {
        if (this.boardMap[row+1][column] == 2) {
          this.checkAvailableMoveDown(column, row+1, false, player);
          if (possibleDirections[6])
            this.computerBestMoveCounter++;
        } else if (this.boardMap[row+1][column] == 1 && !isFirstCheck)
          possibleDirections[6] = true;
        else
          possibleDirections[6] = false;
      } else if (player == 2) {
        if (this.boardMap[row+1][column] == 1)
          this.checkAvailableMoveDown(column, row+1, false, player);
        else if (this.boardMap[row+1][column] == 2 && !isFirstCheck)
          possibleDirections[6] = true;
        else
          possibleDirections[6] = false;
      }
    } catch (IndexOutOfBoundsException e) {
      possibleDirections[6] = false;
    }
  }
  
  public void checkAvailableMoveDownLeft(int column, int row, boolean isFirstCheck, int player) {
    try {
      if (player == 1) {
        if (this.boardMap[row+1][column-1] == 2) {
          this.checkAvailableMoveDownLeft(column-1, row+1, false, player);
          if (possibleDirections[7])
            this.computerBestMoveCounter++;
        } else if (this.boardMap[row+1][column-1] == 1 && !isFirstCheck)
          possibleDirections[7] = true;
        else
          possibleDirections[7] = false;
      } else if (player == 2) {
        if (this.boardMap[row+1][column-1] == 1)
          this.checkAvailableMoveDownLeft(column-1, row+1, false, player);
        else if (this.boardMap[row+1][column-1] == 2 && !isFirstCheck)
          possibleDirections[7] = true;
        else
          possibleDirections[7] = false;
      }
    } catch (IndexOutOfBoundsException e) {
      possibleDirections[7] = false;
    }
  }
  
  // ***************************************************************************************************
  // END OF CHECKAVAILABLEMOVE METHODS
  //****************************************************************************************************
  
  // flipPieces method, simply flips all the pieces that need to be flipped following a move
  public void flipPieces(int column, int row, int player) {
    
    // checks which sides need flipping and flips them respectively
    if (possibleDirections[0])
      this.flipLeft(column, row, player);
    if (possibleDirections[1])
      this.flipUpLeft(column, row, player);
    if (possibleDirections[2])
      this.flipUp(column, row, player);
    if (possibleDirections[3])
      this.flipUpRight(column, row, player);
    if (possibleDirections[4])
      this.flipRight(column, row, player);
    if (possibleDirections[5])
      this.flipDownRight(column, row, player);
    if (possibleDirections[6])
      this.flipDown(column, row, player);
    if (possibleDirections[7])
      this.flipDownLeft(column, row, player);
    
    this.updateView();
  } // end of flipPieces method
  
  // ****************************************************************************************************
  //
  // What follows is the methods for flipping piece on each side. Again, I will just comment the first one
  //
  //****************************************************************************************************
  
  // flipLeft method, flips all the pieces to the left of the placed piece
  public void flipLeft(int column, int row, int player) {
    
    // checks who is the currentPlayer
    if (player == 1) {
      
      // if the piece to the left is a user piece
      if (this.boardMap[row][column-1] == 2) {
        
        // flip it
        this.gameView.setPiece(column-1, row, 1);
        this.boardMap[row][column-1] = 1;
        
        // flip the next one to the left
        this.flipLeft(column-1, row, player);
      }
    } else if (player == 2) {
      
      // if the piece to the left is a computer piece
      if (this.boardMap[row][column-1] == 1) {
        
        // flip it
        this.gameView.setPiece(column-1, row, 2);
        this.boardMap[row][column-1] = 2;
        
        // flip the next one to the left
        this.flipLeft(column-1, row, player);
      }
    }
  } // end of flipLeft
  
  public void flipUpLeft(int column, int row, int player) {
    if (player == 1) {
      if (this.boardMap[row-1][column-1] == 2) {
        this.flipUpLeft(column-1, row-1, player);
        this.gameView.setPiece(column-1, row-1, 1);
        this.boardMap[row-1][column-1] = 1;
      }
    } else if (player == 2) {
      if (this.boardMap[row-1][column-1] == 1) {
        this.flipUpLeft(column-1, row-1, player);
        this.gameView.setPiece(column-1, row-1, 2);
        this.boardMap[row-1][column-1] = 2;
      }
    }
  }
  
  public void flipUp(int column, int row, int player) {
    if (player == 1) {
      if (this.boardMap[row-1][column] == 2) {
        this.flipUp(column, row-1, player);
        this.gameView.setPiece(column, row-1, 1);
        this.boardMap[row-1][column] = 1;
      }
    } else if (player == 2) {
      if (this.boardMap[row-1][column] == 1) {
        this.flipUp(column, row-1, player);
        this.gameView.setPiece(column, row-1, 2);
        this.boardMap[row-1][column] = 2;
      }
    }
  }
  
  public void flipUpRight(int column, int row, int player) {
    if (player == 1) {
      if (this.boardMap[row-1][column+1] == 2) {
        this.flipUpRight(column+1, row-1, player);
        this.gameView.setPiece(column+1, row-1, 1);
        this.boardMap[row-1][column+1] = 1;
      }
    } else if (player == 2) {
      if (this.boardMap[row-1][column+1] == 1) {
        this.flipUpRight(column+1, row-1, player);
        this.gameView.setPiece(column+1, row-1, 2);
        this.boardMap[row-1][column+1] = 2;
      }
    }
  }
  
  public void flipRight(int column, int row, int player) {
    if (player == 1) {
      if (this.boardMap[row][column+1] == 2) {
        this.flipRight(column+1, row, player);
        this.gameView.setPiece(column+1, row, 1);
        this.boardMap[row][column+1] = 1;
      }
    } else if (player == 2) {
      if (this.boardMap[row][column+1] == 1) {
        this.flipRight(column+1, row, player);
        this.gameView.setPiece(column+1, row, 2);
        this.boardMap[row][column+1] = 2;
      }
    }
  }
  
  public void flipDownRight(int column, int row, int player) {
    if (player == 1) {
      if (this.boardMap[row+1][column+1] == 2) {
        this.flipDownRight(column+1, row+1, player);
        this.gameView.setPiece(column+1, row+1, 1);
        this.boardMap[row+1][column+1] = 1;
      }
    } else if (player == 2) {
      if (this.boardMap[row+1][column+1] == 1) {
        this.flipDownRight(column+1, row+1, player);
        this.gameView.setPiece(column+1, row+1, 2);
        this.boardMap[row+1][column+1] = 2;
      }
    }
  }
  
  public void flipDown(int column, int row, int player) {
    if (player == 1) {
      if (this.boardMap[row+1][column] == 2) {
        this.flipDown(column, row+1, player);
        this.gameView.setPiece(column, row+1, 1);
        this.boardMap[row+1][column] = 1;
      }
    } else if (player == 2) {
      if (this.boardMap[row+1][column] == 1) {
        this.flipDown(column, row+1, player);
        this.gameView.setPiece(column, row+1, 2);
        this.boardMap[row+1][column] = 2;
      }
    }
  }
  
  public void flipDownLeft(int column, int row, int player) {
    if (player == 1) {
      if (this.boardMap[row+1][column-1] == 2) {
        this.flipDownLeft(column-1, row+1, player);
        this.gameView.setPiece(column-1, row+1, 1);
        this.boardMap[row+1][column-1] = 1;
      }
    } else if (player == 2) {
      if (this.boardMap[row+1][column-1] == 1) {
        this.flipDownLeft(column-1, row+1, player);
        this.gameView.setPiece(column-1, row+1, 2);
        this.boardMap[row+1][column-1] = 2;
      }
    }
  }
  
  // ***************************************************************************************************
  // END OF FLIP METHODS
  //****************************************************************************************************
  
  // addToPossibleMovesArray method, adds a given move to possibleMoves array
  public void addToPossibleMovesArray(int column, int row, int count) {
    
    // if the array is "empty"
    if (possibleMoves[0][0] == -1) {
      
      // fill in the first spot
      possibleMoves[0][0] = column;
      possibleMoves[0][1] = row;
      possibleMoves[0][2] = count;
    } else {
      // if not, use growing array technique
      
      // make temp array and move everything into it
      int [][] tempArray = new int[possibleMoves.length][3];
      for (int x = 0; x < possibleMoves.length; x++) {
        for (int y = 0; y < 3; y++) {
          tempArray[x][y] = possibleMoves[x][y];
        }
      }
      
      // reset possibleMoves and make it one space bigger then add everything back
      possibleMoves = new int[tempArray.length+1][3];
      for (int x = 0; x < tempArray.length; x++) {
        for (int y = 0; y < 3; y++) {
          possibleMoves[x][y] = tempArray[x][y];
        }
      }
      
      // add the new information to the end
      possibleMoves[possibleMoves.length-1][0] = column;
      possibleMoves[possibleMoves.length-1][1] = row;
      possibleMoves[possibleMoves.length-1][2] = count;
    }
  } // end of addToPossibleMovesArray method
  
  // startGame method, resets game to default settings and sets status to ingame
  public void startGame(int numRounds, String aName) {
    
    // sets status to ingame
    this.status = "ingame";
    
    // resets default settings and sets name and numRounds
    this.currentPlayer = 2;
    this.gameView.setStatusText("");
    this.numRounds = numRounds;
    this.name = aName;
    
    // resets map to be empty
    this.boardMap = new int[8][8];
    for (int x = 0; x < 8; x++) {
      for (int y = 0; y < 8; y++) {
        boardMap[x][y] = 0;
      }
    }
    
    // put the four discs in the middle
    this.boardMap[3][3] = 1;
    this.boardMap[3][4] = 2;
    this.boardMap[4][3] = 2;
    this.boardMap[4][4] = 1;
    
    // resets the actual board on the view
    this.gameView.resetBoard();
    
    this.updateView();
  } // end of startGame
  
  // getStatus method, accessor method of current status of game
  public String getStatus() {
    return this.status;
  } // end of getStatus
  
  // updateView method, updates view
  public void updateView() {
    gameView.update();
  } // end of updateView
}