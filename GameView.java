// View Class
// Description: 
// Created By: Abdelrahman Moustafa
// Last Modified: 01/20/23
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class GameView extends JPanel
{
  // Instance Variables 
  private GameModel game; //The Model
  private Scanner inputFile;
  private PrintWriter outputFile;
  
  // number of games won and lost of user
  private int gamesWonCount;
  private int gamesLostCount;
  
  // array to hold all users information
  private String [] names;
  private int [] gamesWonArray;
  private int [] gamesLostArray;
  
  // The map of the squares and discs ingame and the counter for each number of discs
  private ArrayList<GameSquareIcon> boardMap = new ArrayList<GameSquareIcon>();
  private int blackCounter;
  private int whiteCounter;
  
  // Every main panel for each screen
  private StartupPanel startupScreen = new StartupPanel();
  private JPanel roundSelectScreen = new JPanel();
  private JPanel gamePanel = new JPanel();
  private JPanel endOfRoundPanel = new JPanel();
  private JPanel leaderboardPanel = new JPanel();
  
  // the title of the startup screen
  private StartupPanel titlePanel = new StartupPanel();
  private JLabel gameTitleOth = new JLabel("Oth");
  private JLabel gameTitleEllo = new JLabel("ello");
  
  // the buttons on the startup screen, play, leaderboard and exit
  private StartupPanel startButtonPanel = new StartupPanel();
  private JButton playButton = new JButton("Player Vs. Comp");
  private JButton statsButton = new JButton("Leaderboard");
  private JButton exitButton = new JButton("Exit");
  
  // title for round select screen
  private JLabel roundSelectTitle = new JLabel("Player Vs. Computer");
  private JLabel roundSelectScreenLabel1 = new JLabel("Please enter your name and the");
  private JLabel roundSelectScreenLabel2 = new JLabel("number of rounds you would like to play");
  private JLabel roundSelectError = new JLabel("Name or Num of Rounds invalid.");
  
  // the panel and components where the user enters their name and number of rounds
  private JPanel roundSelectInfoPanel = new JPanel();
  private JLabel nameInputText = new JLabel("Name:");
  private JLabel roundInputText = new JLabel("Num of Rounds:");
  private JTextField nameInput = new JTextField();
  private JTextField roundInput = new JTextField();
  
  // begin and back buttons on the round select screen
  private JButton beginButton = new JButton("Begin");
  private JButton roundSelectScreenBack = new JButton("Back");
  
  // the two main game panels
  private JPanel gameLeftPanel = new JPanel();
  private JPanel boardPanel = new JPanel();
  
  // the left side components
  private JLabel gameViewTitle = new JLabel("Othello");
  private JPanel discCounterPanelBlack = new JPanel();
  private JPanel discCounterPanelWhite = new JPanel();
  
  // the counters and pictures of each player
  private GameSquareIcon blackIcon = new GameSquareIcon();
  private GameSquareIcon whiteIcon = new GameSquareIcon();
  private JLabel blackCount = new JLabel();
  private JLabel whiteCount = new JLabel();
  
  // status text to inform the user about something and forfeit button to end game
  private JLabel gameStatusText = new JLabel();
  private JButton forfeitButton = new JButton("Forfeit");
  
  // the end of round screen labels, including title and round results
  private JLabel roundFinishedTitle = new JLabel("Round Finished");
  private JLabel roundFinishedLabel1 = new JLabel();
  private JLabel roundFinishedLabel2 = new JLabel("Here are your current scores");
  
  // panel that shows the players all time results
  private JPanel roundFinishedInfoPanel = new JPanel();
  private JLabel gamesWonText = new JLabel("Games Won: ");
  private JLabel gamesLostText = new JLabel("Games Lost: ");
  private JLabel gamesWon = new JLabel("0");
  private JLabel gamesLost = new JLabel("0");
  
  // play again and back to main menu buttons on end of round screen
  private JButton replayButton = new JButton("Go To Next Round");
  private JButton roundFinishedBackButton = new JButton("Back To Main Menu");
  
  // leaderboard screen title and top 10 label
  private JLabel leaderboardTitle = new JLabel("Leaderboard");
  private JLabel leaderboardLabel = new JLabel("Top 10 (Based On Wins)");
  
  // gridlayout that holds the information on all players
  private JPanel leaderboardPlayerNamePanel = new JPanel();
  private JPanel leaderboardPlayerWonPanel = new JPanel();
  private JPanel leaderboardPlayerLostPanel = new JPanel();
  private JPanel leaderboardPlayerFullPanel = new JPanel();
  private JLabel leaderboardNameLabel = new JLabel("Name           ");
  private JLabel leaderboardWonLabel = new JLabel("Games Won    ");
  private JLabel leaderboardLostLabel = new JLabel("Games Lost  ");
  
  // leaderboard screen back button
  private JButton leaderboardBackButton = new JButton("Back To Main Menu");
  
  // constructor
  public GameView(GameModel newGame) {
    super();
    
    // sets up board map with the green square buttons
    for (int x = 0; x < 64; x++) {
      boardMap.add(new GameSquareIcon());
    }
    
    // sets the view up and updates with start screen
    this.game = newGame;
    this.game.setGUI(this);
    this.layoutView();
    this.registerControllers();
    this.update();
  } // end of constructor
  
  // layoutView method, draws everything
  private void layoutView() {
    
    // setting up the layout of the top panel that shows everything
    FlowLayout layout = (FlowLayout) this.getLayout();
    layout.setVgap(0);
    this.setLayout(layout);
    
    // setting size for all the main panels for each screen
    startupScreen.setPreferredSize(new Dimension(1200,800));
    roundSelectScreen.setPreferredSize(new Dimension(1200,800));
    gamePanel.setPreferredSize(new Dimension(1200,800));
    endOfRoundPanel.setPreferredSize(new Dimension(1200,800));
    leaderboardPanel.setPreferredSize(new Dimension(1200,800));
    
    // sets the background for each main panel to white except game panel
    startupScreen.setBackground(Color.WHITE);
    roundSelectScreen.setBackground(Color.WHITE);
    endOfRoundPanel.setBackground(Color.WHITE);
    leaderboardPanel.setBackground(Color.WHITE);
    
    // draws main menu title
    gameTitleOth.setFont(new Font("Arial Black", Font.PLAIN, 155));
    gameTitleOth.setForeground(Color.WHITE);
    gameTitleEllo.setFont(new Font("Arial Black", Font.PLAIN, 155));
    gameTitleEllo.setForeground(Color.BLACK);
    
    // panel to hold main menu title
    titlePanel.setPreferredSize(new Dimension(800,200));
    titlePanel.add(gameTitleOth);
    titlePanel.add(gameTitleEllo);
    
    // sets size for main menu buttons
    playButton.setPreferredSize(new Dimension(400,60));
    statsButton.setPreferredSize(new Dimension(400,60));
    exitButton.setPreferredSize(new Dimension(400,60));
    playButton.setMaximumSize(new Dimension(400,60));
    statsButton.setMaximumSize(new Dimension(400,60));
    exitButton.setMaximumSize(new Dimension(400,60));
    
    // aligns main menu buttons to center
    playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    statsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    // draws text on main menu buttons
    playButton.setFont(new Font("Arial Black", Font.PLAIN, 17));
    statsButton.setFont(new Font("Arial Black", Font.PLAIN, 17));
    exitButton.setFont(new Font("Arial Black", Font.PLAIN, 17));
    
    // colors the background of main menu buttons black
    playButton.setBackground(Color.BLACK);
    statsButton.setBackground(Color.BLACK);
    exitButton.setBackground(Color.BLACK);
    
    // colors text on main menu buttons to white
    playButton.setForeground(Color.WHITE);
    statsButton.setForeground(Color.WHITE);
    exitButton.setForeground(Color.WHITE);
    
    // removes borders on main menu buttons that make them look weird
    playButton.setBorder(BorderFactory.createEmptyBorder());
    statsButton.setBorder(BorderFactory.createEmptyBorder());
    exitButton.setBorder(BorderFactory.createEmptyBorder());
    playButton.setFocusable(false);
    statsButton.setFocusable(false);
    exitButton.setFocusable(false);
    
    // put all main menu buttons in one panel
    startButtonPanel.setLayout(new BoxLayout(startButtonPanel, BoxLayout.Y_AXIS));
    startButtonPanel.add(Box.createRigidArea(new Dimension(0, 100)));
    startButtonPanel.add(playButton);
    startButtonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
    startButtonPanel.add(statsButton);
    startButtonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
    startButtonPanel.add(exitButton);
    
    // add everything to main menu panel
    startupScreen.add(titlePanel);
    startupScreen.add(startButtonPanel);
    
    // draws title for round select screen
    roundSelectTitle.setFont(new Font("Arial Black", Font.PLAIN, 70));
    roundSelectTitle.setForeground(Color.BLACK);
    roundSelectTitle.setBorder(new EmptyBorder(30, 0, 60, 0));
    
    // draws informational labels to user for round select screen
    roundSelectScreenLabel1.setFont(new Font("MS Gothic", Font.PLAIN, 35));
    roundSelectScreenLabel1.setForeground(Color.BLACK);
    roundSelectScreenLabel2.setFont(new Font("MS Gothic", Font.PLAIN, 35));
    roundSelectScreenLabel2.setForeground(Color.BLACK);
    roundSelectScreenLabel2.setBorder(new EmptyBorder(0, 0, 25, 0));
    
    // draws labels to tell user which info to enter where on round select screen
    nameInputText.setFont(new Font("MS Gothic", Font.PLAIN, 35));
    nameInputText.setForeground(Color.BLACK);
    roundInputText.setFont(new Font("MS Gothic", Font.PLAIN, 35));
    roundInputText.setForeground(Color.BLACK);
    
    // text fields to input user name and num rounds
    nameInput.setFont(new Font("Arial", Font.PLAIN, 20));
    roundInput.setFont(new Font("Arial", Font.PLAIN, 20));
    
    // setting up layout for round select info input
    GridLayout roundSelectInfoPanelLayout = new GridLayout(2,2);
    roundSelectInfoPanelLayout.setVgap(10);
    roundSelectInfoPanelLayout.setHgap(5);
    roundSelectInfoPanel.setLayout(roundSelectInfoPanelLayout);
    
    // puts all info input into panel for round select
    roundSelectInfoPanel.setBorder(new EmptyBorder(0, 0, 60, 0));
    roundSelectInfoPanel.setBackground(Color.WHITE);
    roundSelectInfoPanel.add(nameInputText);
    roundSelectInfoPanel.add(nameInput);
    roundSelectInfoPanel.add(roundInputText);
    roundSelectInfoPanel.add(roundInput);
    
    // drawing start game button on round select screen
    beginButton.setPreferredSize(new Dimension(700,70));
    beginButton.setFont(new Font("Arial Black", Font.PLAIN, 20));
    beginButton.setBackground(Color.BLACK);
    beginButton.setForeground(Color.WHITE);
    beginButton.setBorder(BorderFactory.createEmptyBorder());
    beginButton.setFocusable(false);
    
    // drawing back button on round select screen
    roundSelectScreenBack.setPreferredSize(new Dimension(700,70));
    roundSelectScreenBack.setFont(new Font("Arial Black", Font.PLAIN, 20));
    roundSelectScreenBack.setBackground(Color.BLACK);
    roundSelectScreenBack.setForeground(Color.WHITE);
    roundSelectScreenBack.setBorder(BorderFactory.createEmptyBorder());
    roundSelectScreenBack.setFocusable(false);
    
    // draws error incase user enters invalid information
    roundSelectError.setFont(new Font("MS Gothic", Font.PLAIN, 25));
    roundSelectError.setForeground(Color.RED);
    roundSelectError.setBorder(new EmptyBorder(20, 200, 0, 200));
    roundSelectError.setVisible(false);
    
    // puts everything together for round select screen
    roundSelectScreen.add(roundSelectTitle);
    roundSelectScreen.add(roundSelectScreenLabel1);
    roundSelectScreen.add(roundSelectScreenLabel2);
    roundSelectScreen.add(roundSelectInfoPanel);
    roundSelectScreen.add(beginButton);
    roundSelectScreen.add(roundSelectScreenBack);
    roundSelectScreen.add(roundSelectError);
    
    // title for game panel
    gameViewTitle.setFont(new Font("Arial Black", Font.PLAIN, 80));
    gameViewTitle.setForeground(Color.WHITE);
    gameViewTitle.setBorder(new EmptyBorder(0, 0, 60, 0));
    
    // displays each player on game panel
    blackIcon.setPreferredSize(new Dimension(90,90));
    blackIcon.setMaximumSize(new Dimension(135,92));
    blackIcon.setPieceColor(2);
    whiteIcon.setPreferredSize(new Dimension(90,90));
    whiteIcon.setMaximumSize(new Dimension(135,92));
    whiteIcon.setPieceColor(1);
    
    // displays each player's disc count of the current round on game panel
    blackCount.setBackground(Color.BLACK);
    blackCount.setFont(new Font("MS Gothic", Font.PLAIN, 18));
    blackCount.setForeground(Color.WHITE);
    whiteCount.setBackground(Color.BLACK);
    whiteCount.setFont(new Font("MS Gothic", Font.PLAIN, 18));
    whiteCount.setForeground(Color.WHITE);
    
    // puts black icon and counter together
    discCounterPanelBlack.setBackground(Color.BLACK);
    discCounterPanelBlack.setLayout(new BoxLayout(discCounterPanelBlack, BoxLayout.X_AXIS));
    discCounterPanelBlack.add(blackIcon);
    discCounterPanelBlack.add(Box.createRigidArea(new Dimension(20,0)));
    discCounterPanelBlack.add(blackCount);
    
    // puts white icon and counter together
    discCounterPanelWhite.setBackground(Color.BLACK);
    discCounterPanelWhite.setLayout(new BoxLayout(discCounterPanelWhite, BoxLayout.X_AXIS));
    discCounterPanelWhite.add(whiteIcon);
    discCounterPanelWhite.add(Box.createRigidArea(new Dimension(20,0)));
    discCounterPanelWhite.add(whiteCount);
    
    // status text to inform user about what is happening on game panel
    gameStatusText.setFont(new Font("MS Gothic", Font.PLAIN, 18));
    gameStatusText.setForeground(Color.WHITE);
    gameStatusText.setBorder(new EmptyBorder(40, 0, 160, 0));
    
    // draws forfeit button to allow user to end game at any point
    forfeitButton.setPreferredSize(new Dimension(300,65));
    forfeitButton.setMaximumSize(new Dimension(300,65));
    forfeitButton.setFont(new Font("Arial Black", Font.PLAIN, 22));
    forfeitButton.setBackground(Color.WHITE);
    forfeitButton.setForeground(Color.BLACK);
    forfeitButton.setBorder(BorderFactory.createEmptyBorder());
    forfeitButton.setFocusable(false);
    
    // adds everything for the left side of game panel
    gameLeftPanel.setBackground(Color.BLACK);
    gameLeftPanel.setLayout(new BoxLayout(gameLeftPanel, BoxLayout.Y_AXIS));
    gameLeftPanel.add(gameViewTitle);
    gameLeftPanel.add(discCounterPanelBlack);
    gameLeftPanel.add(discCounterPanelWhite);
    gameLeftPanel.add(gameStatusText);
    gameLeftPanel.add(forfeitButton);
    gameLeftPanel.setAlignmentY(JPanel.TOP_ALIGNMENT);
    
    // draws all the buttons on the board panel for game panel
    boardPanel.setLayout(new GridLayout(8,8));
    for (int x = 0; x < 64; x++) {
      boardPanel.add(boardMap.get(x));
    }
    
    // extra settings for board panel
    boardPanel.setBackground(Color.BLACK);
    boardPanel.setPreferredSize(new Dimension(720,720));
    boardPanel.setMaximumSize(new Dimension(720,1300));
    boardPanel.setBorder(new EmptyBorder(27,0,0,20));
    
    // puts everything together on the game panel
    gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.X_AXIS));
    gamePanel.setBackground(Color.BLACK);
    gamePanel.add(Box.createRigidArea(new Dimension(40, 0)));
    gamePanel.add(gameLeftPanel);
    gamePanel.add(Box.createRigidArea(new Dimension(10, 0)));
    gamePanel.add(boardPanel);
    
    // draws title for end of round screen
    roundFinishedTitle.setFont(new Font("Arial Black", Font.PLAIN, 70));
    roundFinishedTitle.setForeground(Color.BLACK);
    roundFinishedTitle.setBorder(new EmptyBorder(30, 300, 60, 300));
    
    // labels to tell user the result of the game and their current scores on end of round panel
    roundFinishedLabel1.setFont(new Font("MS Gothic", Font.PLAIN, 35));
    roundFinishedLabel1.setForeground(Color.BLACK);
    roundFinishedLabel1.setBorder(new EmptyBorder(0, 500, 0, 500));
    roundFinishedLabel2.setFont(new Font("MS Gothic", Font.PLAIN, 35));
    roundFinishedLabel2.setForeground(Color.BLACK);
    roundFinishedLabel2.setBorder(new EmptyBorder(0, 500, 25, 500));
    
    // draws pointers to which number is won and lost on end of round panel
    gamesWonText.setFont(new Font("MS Gothic", Font.PLAIN, 35));
    gamesWonText.setForeground(Color.BLACK);
    gamesLostText.setFont(new Font("MS Gothic", Font.PLAIN, 35));
    gamesLostText.setForeground(Color.BLACK);
    
    // draws the number of won and lost on end of round panel
    gamesWon.setFont(new Font("MS Gothic", Font.PLAIN, 35));
    gamesWon.setForeground(Color.BLACK);
    gamesLost.setFont(new Font("MS Gothic", Font.PLAIN, 35));
    gamesLost.setForeground(Color.BLACK);
    
    // makes layout for games won and lost on end of round panel
    GridLayout roundFinishedInfoPanelLayout = new GridLayout(2,2);
    roundFinishedInfoPanelLayout.setVgap(10);
    roundFinishedInfoPanelLayout.setHgap(5);
    roundFinishedInfoPanel.setLayout(roundFinishedInfoPanelLayout);
    
    // adds all information about won and lost counters to show to user on end of round panel
    roundFinishedInfoPanel.setBorder(new EmptyBorder(0, 190, 60, 0));
    roundFinishedInfoPanel.setBackground(Color.WHITE);
    roundFinishedInfoPanel.add(gamesWonText);
    roundFinishedInfoPanel.add(gamesWon);
    roundFinishedInfoPanel.add(gamesLostText);
    roundFinishedInfoPanel.add(gamesLost);
    
    // draws button to allow user to play their next round from end of round screen
    replayButton.setPreferredSize(new Dimension(900,70));
    replayButton.setFont(new Font("Arial Black", Font.PLAIN, 20));
    replayButton.setBackground(Color.BLACK);
    replayButton.setForeground(Color.WHITE);
    replayButton.setBorder(BorderFactory.createEmptyBorder());
    replayButton.setFocusable(false);
    
    // draws button to go back to main menu from end of round screen
    roundFinishedBackButton.setPreferredSize(new Dimension(900,70));
    roundFinishedBackButton.setFont(new Font("Arial Black", Font.PLAIN, 20));
    roundFinishedBackButton.setBackground(Color.BLACK);
    roundFinishedBackButton.setForeground(Color.WHITE);
    roundFinishedBackButton.setBorder(BorderFactory.createEmptyBorder());
    roundFinishedBackButton.setFocusable(false);
    
    // adds everything to end of round panel
    endOfRoundPanel.add(roundFinishedTitle);
    endOfRoundPanel.add(roundFinishedLabel1);
    endOfRoundPanel.add(roundFinishedLabel2);
    endOfRoundPanel.add(roundFinishedInfoPanel);
    endOfRoundPanel.add(replayButton);
    endOfRoundPanel.add(roundFinishedBackButton);
    
    // draws title for leaderboard screen
    leaderboardTitle.setFont(new Font("Arial Black", Font.PLAIN, 70));
    leaderboardTitle.setForeground(Color.BLACK);
    leaderboardTitle.setBorder(new EmptyBorder(30, 300, 60, 300));
    
    // shows user what the leaderboard is based on
    leaderboardLabel.setFont(new Font("MS Gothic", Font.PLAIN, 35));
    leaderboardLabel.setForeground(Color.BLACK);
    leaderboardLabel.setBorder(new EmptyBorder(0, 500, 25, 500));
    
    // labels to start each column of the leaderboard panel
    leaderboardNameLabel.setFont(new Font("MS Gothic", Font.PLAIN, 27));
    leaderboardNameLabel.setForeground(Color.BLACK);
    leaderboardWonLabel.setFont(new Font("MS Gothic", Font.PLAIN, 27));
    leaderboardWonLabel.setForeground(Color.BLACK);
    leaderboardLostLabel.setFont(new Font("MS Gothic", Font.PLAIN, 27));
    leaderboardLostLabel.setForeground(Color.BLACK);
    
    // sets up all the leaderboard panels
    leaderboardPlayerNamePanel.setLayout(new BoxLayout(leaderboardPlayerNamePanel, BoxLayout.Y_AXIS));
    leaderboardPlayerNamePanel.setBackground(Color.WHITE);
    leaderboardPlayerWonPanel.setLayout(new BoxLayout(leaderboardPlayerWonPanel, BoxLayout.Y_AXIS));
    leaderboardPlayerWonPanel.setBackground(Color.WHITE);
    leaderboardPlayerLostPanel.setLayout(new BoxLayout(leaderboardPlayerLostPanel, BoxLayout.Y_AXIS));
    leaderboardPlayerLostPanel.setBackground(Color.WHITE);
    
    // adds all the leaderboard panels into one main leaderboard panel
    leaderboardPlayerFullPanel.setLayout(new BoxLayout(leaderboardPlayerFullPanel, BoxLayout.X_AXIS));
    leaderboardPlayerFullPanel.setBorder(new EmptyBorder(0,0,60,0));
    leaderboardPlayerFullPanel.setBackground(Color.WHITE);
    leaderboardPlayerFullPanel.add(leaderboardPlayerNamePanel);
    leaderboardPlayerFullPanel.add(leaderboardPlayerWonPanel);
    leaderboardPlayerFullPanel.add(leaderboardPlayerLostPanel);
    
    // draws back button to main menu from leaderboard screen
    leaderboardBackButton.setPreferredSize(new Dimension(900,70));
    leaderboardBackButton.setFont(new Font("Arial Black", Font.PLAIN, 20));
    leaderboardBackButton.setBackground(Color.BLACK);
    leaderboardBackButton.setForeground(Color.WHITE);
    leaderboardBackButton.setBorder(BorderFactory.createEmptyBorder());
    leaderboardBackButton.setFocusable(false);
    
    // adds everything on leaderboard panel
    leaderboardPanel.add(leaderboardTitle);
    leaderboardPanel.add(leaderboardLabel);
    leaderboardPanel.add(leaderboardPlayerFullPanel);
    leaderboardPanel.add(leaderboardBackButton);
  }
  
  // registerControllers method, adds action listeners for each button on the view
  private void registerControllers() {
    // sets up all the controllers for each button
    playButton.addActionListener(new ButtonPressedController(this.game, playButton));
    statsButton.addActionListener(new ButtonPressedController(this.game, statsButton));
    exitButton.addActionListener(new ButtonPressedController(this.game, exitButton));
    roundSelectScreenBack.addActionListener(new ButtonPressedController(this.game, roundSelectScreenBack));
    leaderboardBackButton.addActionListener(new ButtonPressedController(this.game, leaderboardBackButton));
    forfeitButton.addActionListener(new ButtonPressedController(this.game, forfeitButton));
    replayButton.addActionListener(new ButtonPressedController(this.game, replayButton));
    roundFinishedBackButton.addActionListener(new ButtonPressedController(this.game, roundFinishedBackButton));
    
    // sets up controller for each button on the board
    for (int x = 0; x < 64; x++) {
      boardMap.get(x).addActionListener(new BoardButtonController(this.game, x%8, x/8));
    }
    
    // sets up controller for start game button
    beginButton.addActionListener(new StartGameController(this.game, this, nameInput, roundInput));
  } // end of registerControllers()
  
  // update method, updates view based on game status
  public void update() {
    // removes all panels
    this.removeAll();
    
    // adds main menu screen when appropriate
    if (this.game.getStatus().equals("start")) {
      this.add(startupScreen);
      
    // adds round select screen when appropriate
    } else if (this.game.getStatus().equals("round select")) {
      this.add(roundSelectScreen);
      
    // adds ingame screen when appropriate
    } else if (this.game.getStatus().equals("ingame")) {
      this.add(gamePanel);
      this.updateColorCounters();
      
    // adds end of round screen when appropriate
    } else if (this.game.getStatus().equals("end of round")) {
      this.add(endOfRoundPanel);
      this.updateEndScreen();
      
    // adds leaderboard screen when appropriate
    } else if (this.game.getStatus().equals("leaderboard")) {
      this.add(leaderboardPanel);
      this.updateLeaderboard();
    }
    
    this.updateUI();
  } // end of update method
  
  
  // getUserStats method, gets all user data from file
  public void getUserStats(String name) {
    
    // set all arrays to be "empty"
    names = new String[]{"/"};
    gamesWonArray = new int[]{-1};
    gamesLostArray = new int[]{-1};
    
    // try incase we dont find file
    try {
      
      // open file and set delimiter
      this.inputFile = new Scanner(new File("leaderboard.txt"));
      this.inputFile.useDelimiter("/");
      
      // check if file has another user entry
      while (this.inputFile.hasNextLine()) {
        
        // add their stats to the arrays
        this.inputFile.next();
        this.addToNamesArray(this.inputFile.next());
        this.inputFile.next();
        this.addToIntArray(Integer.parseInt(this.inputFile.next()), 1);
        this.inputFile.next();
        this.addToIntArray(Integer.parseInt(this.inputFile.next()), 2);
        this.inputFile.nextLine();
      }
      
      // check if we are trying to get a specific persons stats
      if (!name.equals("")) {
        
        // if so, find their gamess won and games lost
        this.gamesWonCount = -1;
        for (int x = 0; x < names.length; x++) {
          if (names[x].equals(name)) {
            this.gamesWonCount = this.gamesWonArray[x];
            this.gamesLostCount = this.gamesLostArray[x];
          }
        }
        
        // if we cant find their stats, add them to the array, we will add them later to the file
        if (this.gamesWonCount == -1) {
          this.addToNamesArray(name);
          this.addToIntArray(0, 1);
          this.addToIntArray(0, 2);
          this.gamesWonCount = 0;
          this.gamesLostCount = 0;
        }
      }
      
      // close input file
      this.inputFile.close();
      
    // if we cant find file and we need to add new player, add them to array, we will make a file later
    } catch (FileNotFoundException e) {
      if (!name.equals("")) {
        this.addToNamesArray(name);
        this.addToIntArray(0, 1);
        this.addToIntArray(0, 2);
        this.gamesWonCount = 0;
        this.gamesLostCount = 0;
      }
    }
  } // end of getUserStats method
  
  // printUserStats method, prints the information in the arrays to a file
  public void printUserStats(String name) {
    
    // try incase no file found
    try {
      
      // open connection to output file
      outputFile = new PrintWriter(new File("leaderboard.txt"));
      
      // run through everyone in the array
      for (int x = 0; x < names.length; x++) {
        
        // print their stats to the file
        if (names[x].equals(name))
          outputFile.println("name/" + name + "/gamesWon/" 
                               + this.gamesWonCount + "/gamesLost/" + this.gamesLostCount + "/filler");
        else
          outputFile.println("name/" + names[x] + "/gamesWon/" 
                               + this.gamesWonArray[x] + "/gamesLost/" + this.gamesLostArray[x] + "/filler");
      }
      
      // close output file
      outputFile.close();
    
    // this will never be called because if no file found it will make one and print to it
    } catch (FileNotFoundException e) {}
  } // end of printUserStats method
  
  // addToNamesArray method, adds an entry to names array
  public void addToNamesArray(String info) {
    
    // checks if array is "empty"
    if (this.names[0].equals("/")) {
      
      // if so, fill that spot in with info
      this.names[0] = info;
    } else {
      
      // if not, make temp array and move everything to it
      String [] tempArray = new String[this.names.length];
      for (int x = 0; x < this.names.length; x++) {
        tempArray[x] = this.names[x];
      }
      
      // reset names array with one extra space and move everything back into it
      this.names = new String [tempArray.length+1];
      for (int x = 0; x < tempArray.length; x++) {
        this.names[x] = tempArray[x];
      }
      
      // add info at the end
      this.names[this.names.length-1] = info;
    }
  } // end of addToNamesArray method
  
  // addToIntArray method, adds int to int array
  public void addToIntArray(int info, int identifier) {
    
    // checks if gamesWon method
    if (identifier == 1) {
      
      // checks if empty
      if (this.gamesWonArray[0] == -1) {
        
        // if so, fill in first spot with info
        this.gamesWonArray[0] = info;
      } else {
        
        // if not, make temp array and move everything to it
        int [] tempArray = new int[this.gamesWonArray.length];
        for (int x = 0; x < this.gamesWonArray.length; x++) {
          tempArray[x] = this.gamesWonArray[x];
        }
        
        // reset array with one extra space and move everything back into it
        this.gamesWonArray = new int[tempArray.length+1];
        for (int x = 0; x < tempArray.length; x++) {
          this.gamesWonArray[x] = tempArray[x];
        }
        
        // add info at the end
        this.gamesWonArray[this.gamesWonArray.length-1] = info;
      }
    } else {
      
      // checks if empty
      if (this.gamesLostArray[0] == -1) {
        
        // if so, fill in first spot with info
        this.gamesLostArray[0] = info;
      } else {
        
        // if not, make temp array and move everything to it
        int [] tempArray = new int[this.gamesLostArray.length];
        for (int x = 0; x < this.gamesLostArray.length; x++) {
          tempArray[x] = this.gamesLostArray[x];
        }
        
        // reset array with one extra space and move everything back into it
        this.gamesLostArray = new int[tempArray.length+1];
        for (int x = 0; x < tempArray.length; x++) {
          this.gamesLostArray[x] = tempArray[x];
        }
        // add info at the end
        this.gamesLostArray[this.gamesLostArray.length-1] = info;
      }
    }
  } // end of addToIntArray method
  
  // updateLeaderboard method, updates the leaderboard with the current stats
  public void updateLeaderboard() {
    
    // gets user stats in arrays
    this.getUserStats("");
    
    // clears leaderboard
    leaderboardPlayerNamePanel.removeAll();
    leaderboardPlayerWonPanel.removeAll();
    leaderboardPlayerLostPanel.removeAll();
    
    // adds column titles
    leaderboardPlayerNamePanel.add(leaderboardNameLabel);
    leaderboardPlayerWonPanel.add(leaderboardWonLabel);
    leaderboardPlayerLostPanel.add(leaderboardLostLabel);
    
    // sort arrays
    this.sort();
    
    // draw the leaderboard stats on the view 
    for (int x = 0; x < 10; x++) {
      try {
        if (!names[x].equals("/")) {
          leaderboardPlayerNamePanel.add(new JLabel(names[x]));
          leaderboardPlayerWonPanel.add(new JLabel(Integer.toString(gamesWonArray[x])));
          leaderboardPlayerLostPanel.add(new JLabel(Integer.toString(gamesLostArray[x])));
        }
      } catch (ArrayIndexOutOfBoundsException e) {}
    }
  } // end of updateLeaderboard method
  
  // updateEndScreen method, updates end of round screen
  public void updateEndScreen() {
    
    // draws result on end of round screen and shows replay button if more rounds
    this.roundFinishedLabel1.setText(this.game.getEndScreenText());
    this.replayButton.setVisible(this.game.getMoreRounds());
    
    // get the current user's stats
    this.getUserStats(this.game.getName().toLowerCase());
    
    // increment the appropriate value depending on if they won or lost
    if (this.roundFinishedLabel1.getText().equals("You Won!"))
      this.gamesWonCount++;
    else if (this.roundFinishedLabel1.getText().equals("You Lost..."))
      this.gamesLostCount++;
    
    // show the current user's stats on end of game panel
    this.gamesWon.setText(Integer.toString(this.gamesWonCount));
    this.gamesLost.setText(Integer.toString(this.gamesLostCount));
    
    // print stats to file
    this.printUserStats(this.game.getName().toLowerCase());
  } // end of updateEndScreen method
  
  // updateColorCounters method, 
  public void updateColorCounters() {
    this.whiteCounter = 0;
    this.blackCounter = 0;
    for (int x = 0; x < 64; x++) {
      if (this.boardMap.get(x).getPieceColor() == 1)
        whiteCounter++;
      else if (this.boardMap.get(x).getPieceColor() == 2)
        blackCounter++;
    }
    this.whiteCount.setText(Integer.toString(whiteCounter));
    this.blackCount.setText(Integer.toString(blackCounter));
  } // end of updateColorCounters method
  
  // sort method, sorts user stats arrays
  public void sort() {
    int a;
    
    // checks all values past the first one
    for (int x = 1; x < names.length; x++) {
      a = x;
      
      // while it doesnt have a value to the left of it and the one to the left is less
      while (a > 0 && this.gamesWonArray[a] > this.gamesWonArray[a-1]) {
        
        // swap the two players
        this.swapString(a , a-1);
        this.swapInt(a, a-1, 1);
        this.swapInt(a, a-1, 2);
        a--;
      }
    }
  } // end of sort method
  
  // swapString method, swaps names in name array
  public void swapString(int x, int y) {
    String info = names[x];
    names[x] = names[y];
    names[y] = info;
  } // end of swapString method
  
  // swapInt method, swaps ints in games won and lost array
  public void swapInt(int x, int y, int i) {
    int info;
    if (i == 1) {
      info = this.gamesWonArray[x];
      this.gamesWonArray[x] = this.gamesWonArray[y];
      this.gamesWonArray[y] = info;
    } else {
      info = this.gamesLostArray[x];
      this.gamesLostArray[x] = this.gamesLostArray[y];
      this.gamesLostArray[y] = info;
    }
  } // end of swapInt method
  
  // setStatusText method, 
  public void setStatusText(String string) {
    
    // set color to black when "-" so it shows nothing
    if (string.equals("-"))
      this.gameStatusText.setForeground(Color.BLACK);
    else
      this.gameStatusText.setForeground(Color.WHITE);
    
    // change status text on game screen
    this.gameStatusText.setText(string);
    this.update();
  } // end of setStatusText method
  
  // setEnabledForfeit method, changes enabled status of forfeit button to not allow user to 
  // forfeit during computer turn
  public void setEnabledForfeit(boolean status) {
    this.forfeitButton.setEnabled(status);
    this.update();
  } // end of setEnabledForfeit method
  
  // showRoundSelectError method, shows error on the round select screen to tell user they input 
  // invalid information
  public void showRoundSelectError(boolean check) {
    this.roundSelectError.setVisible(check);
    this.update();
  } // end of showRoundSelectError method
  
  // resetBoard method, resets board to default
  public void resetBoard() {
    
    // resets board to be empty
    for (int x = 0; x < 64; x++) {
      this.boardMap.get(x).setPieceColor(0);
    }
    
    // puts 4 pieces in the middle
    this.boardMap.get(27).setPieceColor(1);
    this.boardMap.get(28).setPieceColor(2);
    this.boardMap.get(35).setPieceColor(2);
    this.boardMap.get(36).setPieceColor(1);
    
    // informs user that they move first
    this.setStatusText("Black (You) moves first");
    this.update();
  } // end of resetBoard method
  
  // setPiece method, sets the color of a piece on a square to current player
  public void setPiece(int column, int row, int currentPlayer) {
    this.boardMap.get(column+(row*8)).setPieceColor(currentPlayer);
  } // end of setPiece method
}