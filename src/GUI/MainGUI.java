package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import Information.Tag;

public class MainGUI implements Runnable {
    // const values for main menu
    private static final int VERTICAL_SPACE = 50;
    private static final int HORIZONTAL_SPACE = 10;
    private static final Color BUTTON_COLOR = new Color(251, 252, 247);
    private static final Color BACKGROUND_COLOR = new Color(212, 219, 225);

    // private data members
    private JFrame mainGUI;
    private Box mainGUIComponents;
    private JPanel blackPlayerPanel;
    private JPanel whitePlayerPanel;
    private JTextField blackPlayerTextField;
    private JTextField whitePlayerTextField;


    // entry point of program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainGUI());
    }
    
    public void run() {
        initializeMainMenu();
        mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGUI.setVisible(true);
    }

    // creates menu
    private void initializeMainMenu() {
        createFrame();
        createBoxComponents();
        addGameTitle();
        addPlayerFields();
        addPlayerTextField();
        addButtons();
    }

    // creates menu frame
    private void createFrame() {
        mainGUI = new JFrame(Tag.TITLE);
        mainGUI.getContentPane().setBackground(BACKGROUND_COLOR);
        mainGUI.setIconImage(new ImageIcon(Tag.LAZY_ICON).getImage());
        mainGUI.setSize(Tag.IMAGE_WIDTH * 8, Tag.IMAGE_HEIGHT * 8);
        mainGUI.setResizable(false);
    }

    // adds boxes to screen to give space between elements
    private void createBoxComponents() {
        mainGUIComponents = Box.createVerticalBox();
        mainGUI.add(mainGUIComponents);
    }

    // sets window title
    private void addGameTitle() {
        // create title panel
        JPanel title = new JPanel();
        JLabel gameTitle = new JLabel(Tag.TITLE);
        gameTitle.setFont(new Font("Serif", Font.BOLD, 80));

        // add game title and set background
        title.add(gameTitle);
        title.setBackground(BACKGROUND_COLOR);
        
        // add title to view
        mainGUIComponents.add(Box.createVerticalStrut(VERTICAL_SPACE));
        mainGUIComponents.add(title, BorderLayout.CENTER);
    }

    // adds player icon
    private void addPlayerFields() {
        // create player icons
        final JLabel whiteIcon = new JLabel(new ImageIcon((Tag.WHITE_KING)));
        final JLabel blackIcon = new JLabel(new ImageIcon((Tag.BLACK_KING)));

        // create new panel for player one
        whitePlayerPanel = new JPanel();
        whitePlayerPanel.setBackground(BACKGROUND_COLOR);
        mainGUIComponents.add(whitePlayerPanel);
        whitePlayerPanel.add(whiteIcon);
        
        // create new panel for player two
        blackPlayerPanel = new JPanel();
        blackPlayerPanel.setBackground(BACKGROUND_COLOR);
        mainGUIComponents.add(blackPlayerPanel, BorderLayout.EAST);
        blackPlayerPanel.add(blackIcon); 
    }

    // adds text field for player name
    private void addPlayerTextField() {
        // create text field for player name
        blackPlayerTextField = new JTextField();
        whitePlayerTextField = new JTextField();
        
        // add text field to screen
        blackPlayerPanel.add(blackPlayerTextField);
        whitePlayerPanel.add(whitePlayerTextField);
        
        // set tool tip
        blackPlayerTextField.setToolTipText("Enter Player 2 Name Here");
        whitePlayerTextField.setToolTipText("Enter Player 1 Name Here");
        
        // set width of text field
        blackPlayerTextField.setColumns(HORIZONTAL_SPACE);
        whitePlayerTextField.setColumns(HORIZONTAL_SPACE);
    }

    // adds play, quit, and help buttons to screen
    private void addButtons() {
        // create containers
        JPanel buttonPanel = new JPanel();
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(0, 5, 20, 0));

        // create buttons
        final JButton play = new JButton("Play");
        final JButton help = new JButton("Help");
        final JButton quit = new JButton("Quit");

        // set background color
        play.setBackground(BUTTON_COLOR);
        help.setBackground(BUTTON_COLOR);
        quit.setBackground(BUTTON_COLOR);
        buttons.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBackground(BACKGROUND_COLOR);
        mainGUIComponents.setBackground(BACKGROUND_COLOR);

        // add button action listener
        play.addActionListener(e -> playItemActionPerformed(e));
        help.addActionListener(e -> helpItemActionPerformed(e));
        quit.addActionListener(e -> quitItemActionPerformed(e));

        // add buttons to containers
        buttons.add(Box.createHorizontalGlue());
        buttons.add(play);
        buttons.add(help);
        buttons.add(quit);
        buttons.add(Box.createHorizontalGlue());
        buttonPanel.add(buttons);

        // add containers to screen
        mainGUIComponents.add(buttonPanel, BorderLayout.CENTER);
        mainGUIComponents.add(Box.createGlue());
    }

    //! start: Methods handling button action events
    private void playItemActionPerformed(ActionEvent e) {
        // create game view
        new GameGUI(blackPlayerTextField.getText(), whitePlayerTextField.getText());

        // dispose current view
        mainGUI.dispose();
    }

    private void helpItemActionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(mainGUI,
        "Input name for Player 1\n" +
        "Input name for player 2\n" +
        "Click \'Play\' to start playing!",
        "Help Menu", JOptionPane.INFORMATION_MESSAGE);
    }

    private void quitItemActionPerformed(java.awt.event.ActionEvent e) {
        int quit = JOptionPane.showConfirmDialog(mainGUI, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION);
        if(quit == JOptionPane.YES_OPTION) mainGUI.dispose();
    }
    //! end: Methods handling button action events
}