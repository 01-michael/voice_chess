package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import BoardComponents.Board;
import Information.Tag;
import SpeechRecognizer.SpeechCaller;
import SpeechRecognizer.SpeechRecognizerMain;

public class GameGUI {
    // game mover class extends speech caller
    // and calls the speechCalled method on board when 
    // the speak button is pressed
    public static class GameMover implements SpeechCaller {
        public GameMover() {}
        public void Call(String speechReceived) {
            boardGUI.speechCalled(speechReceived);
        }
    }

    // static data members shared during game session
    // this ensures the user can go to the main menu and start a 
    // new game without sphinx4 throwing an error.
    private static Board boardGUI = null;
    private static SpeechCaller speechGameMover = new GameMover();
    private static SpeechRecognizerMain speech = new SpeechRecognizerMain(speechGameMover);

    // const data members to keep track of colors
    private static final Color BUTTON_COLOR = new Color(251, 252, 247);
    private static final Color BACKGROUND_COLOR = new Color(212, 219, 225);

    // private data members
    private JFrame gameGUI;
    private JPanel buttonPanel;

    // constructor
    public GameGUI(String blackPlayerName, String whitePlayerName) { 
        initializeGameGUI(blackPlayerName, whitePlayerName);
    }

    // make game view
    private void initializeGameGUI(String blackPlayerName, String whitePlayerName) {
        makeFrame();
        addButtonsToFrame();
        addLabelsToScreen();
        createBoardGUIAndAddToFrame(blackPlayerName, whitePlayerName);
        setFrameSize();
    }

    // creates game frame
    private void makeFrame() {
        gameGUI = new JFrame("Voice Controlled Chess");
        gameGUI.setIconImage(new ImageIcon(Tag.LAZY_ICON).getImage());
        gameGUI.setLayout(new BorderLayout(0, 0));
        gameGUI.getContentPane().setBackground(BACKGROUND_COLOR);
        gameGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // set frame size
    private void setFrameSize() {
        gameGUI.setSize(gameGUI.getPreferredSize());
        gameGUI.setMinimumSize(gameGUI.getPreferredSize());
        gameGUI.setVisible(true);
        gameGUI.setResizable(false);
    }

    // create board and add to frame
    private void createBoardGUIAndAddToFrame(String blackPlayerName, String whitePlayerName) {
        boardGUI = new Board(this, blackPlayerName, whitePlayerName);
        boardGUI.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        gameGUI.add(boardGUI);
    }

    // add buttons to top of frame
    private void addButtonsToFrame() {
        // create new panel to store buttons
        buttonPanel = new JPanel();
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(0, 6, 20, 0));
        buttonPanel.setLayout(new GridLayout(2, 0, 0, 0));

        // create buttons
        final JButton speak = new JButton("Speak");
        final JButton quite = new JButton("Quit");
        final JButton menu = new JButton("Menu");
        final JButton help = new JButton("Help");

        // set background color
        menu.setBackground(BUTTON_COLOR);
        help.setBackground(BUTTON_COLOR);
        speak.setBackground(BUTTON_COLOR);
        quite.setBackground(BUTTON_COLOR);
        buttons.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // add action listener to buttons
        speak.addActionListener((e) -> speakItemActionPerformed(e));
        quite.addActionListener((e) -> quitItemActionPerformed(e));
        menu.addActionListener((e) ->  mainMenuItemActionPerformed(e));
        help.addActionListener((e) -> helpItemActionPerformed(e));

        // add buttons to screen
        buttons.add(Box.createHorizontalGlue());
        buttons.add(speak, Component.CENTER_ALIGNMENT);
        buttons.add(help, Component.CENTER_ALIGNMENT);
        buttons.add(menu, Component.CENTER_ALIGNMENT);
        buttons.add(quite, Component.CENTER_ALIGNMENT);
        buttons.add(Box.createHorizontalGlue());
        buttonPanel.add(buttons, BorderLayout.NORTH);
    }

    // add labels around board
    private void addLabelsToScreen() {
        JPanel[] componentPanel = new JPanel[4];
        String[] labStrings = { "  1  ", "  2  ", "  3  ", "  4  ", "  5  ", "  6  ", "  7  ", "  8  ",
                                "alpha", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel"};

        // create labels
        for(int k = 0; k < 4; k++) {
            componentPanel[k] = new JPanel();
            componentPanel[k].setLayout(k > 1 ? new GridLayout(1, 8, 0, 0) : new GridLayout(8, 0, 0, 0));
            for(int i = 0; i < 8; i++) {
                JPanel boxes = new JPanel();
                boxes.add(new JLabel((k > 1 ? labStrings[i + 8] :labStrings[7 - i])));
                componentPanel[k].add(boxes);
                boxes.setBackground(BACKGROUND_COLOR);
                componentPanel[k].setBackground(BACKGROUND_COLOR);
            }
        }

        // add labels to screen
        gameGUI.add(buttonPanel, BorderLayout.NORTH);
        gameGUI.add(componentPanel[0], BorderLayout.EAST);
        gameGUI.add(componentPanel[1], BorderLayout.WEST);
        gameGUI.add(componentPanel[2], BorderLayout.SOUTH);
        buttonPanel.add(componentPanel[3], BorderLayout.SOUTH);
    }

    //! start: Methods handling button action events
    private void speakItemActionPerformed(ActionEvent e) {
        try {
            // without delay, mic registers mouse click as command
            Thread.sleep(500);
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        speech.stopIgnoreSpeechRecognitionResults(); // handled in while loop of try statement
        // once no longer ignored, speech is recognized once and then ignored again, result is passed through
        // SpeechCaller which is interface in SpeechRecognizer package and defined within this class, gives it
        // access to variables of this class while being called from SpeechRecognizerMain, allowing
        // SpeechRecognizerMain to pass a string in to this instance of Board
    }

    private void quitItemActionPerformed(ActionEvent e) {
        int quit = JOptionPane.showConfirmDialog(gameGUI, "Are you sure you want to quit?", "Quit", JOptionPane.YES_OPTION);
        if (quit == JOptionPane.YES_OPTION) gameGUI.dispose();
    }

    private void mainMenuItemActionPerformed(ActionEvent e) {
        // if player wins, simply go to main menu. else if active game, ask player for confirmation
        int goToMenu = boardGUI.getTurn() != Tag.Side.OVER ? 
        JOptionPane.showConfirmDialog(gameGUI,"Are you sure you want to go to main menu?" + 
            "\nThis game session will be terminated.", "Main Menu", JOptionPane.YES_NO_OPTION) : JOptionPane.YES_OPTION;

        if (goToMenu == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(new MainGUI());
            gameGUI.dispose();
        }
    }

    private void helpItemActionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(gameGUI, 
        "To play with mouse:" +
        "\n   - left click on square to select and move piece" +
        "\n\nTo play with voice" +
        "\n   - click \'Speak\' once" +
        "\n   - say position to select and move piece" +
        "\n          Example position: alpha two",
        "Help Menu", JOptionPane.INFORMATION_MESSAGE);
    }
    //! end: Methods handling button action events
}