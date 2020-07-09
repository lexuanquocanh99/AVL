import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener {
    /**Instance Variables**/
    private TreePanel treePanel;
    private Tree tree;
    public JPanel rightPanel, center, bottom;
    private JLabel heightLabel, sizeLabel;
    private JTextField insertField, deleteField, searchField;
    private JTextArea history;
    private JButton insertButton, deleteButton, clearButton, searchButton,
            resetSearchButton, playButton, pauseButton;
    private Notes notes;
    private boolean pause = false;
    private int STEPS = 10;
    private JSlider slider;
    private ArrayList<String> words;
    private Stack<String> stack;
    private boolean openF = false;
    public static final int PREF_W = 1200;
    public static final int PREF_H = 600;

    public MainFrame() {
        this.setSize(1100,680);
        this.setLocation(150,100);
        this.setTitle("AVL Tree Demo");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(PREF_W, PREF_H));

        treePanel = new TreePanel();
        tree = new Tree(this);
        stack = new Stack<String>();

        this.layoutComponents();

        slider.setValue(50);
        tree.setDS(notes);
        treePanel.setObjects(tree, this);
        treePanel.startDrawingThread();
    }

    public void layoutComponents() {
        center = new JPanel(new BorderLayout());
        center.add(treePanel);
        this.add(center,BorderLayout.CENTER);

        createRightPanel();

        bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        notes = new Notes();
        JScrollPane sp = new JScrollPane(notes);
        bottom.add(sp);
        this.add(bottom, BorderLayout.SOUTH);

        insertButton.addActionListener(this);
        deleteButton.addActionListener(this);
        clearButton.addActionListener(this);
        searchButton.addActionListener(this);
        resetSearchButton.addActionListener(this);
        playButton.addActionListener(this);
        pauseButton.addActionListener(this);
    }

    public void createRightPanel() {
        insertButton = new JButton("Insert");
        deleteButton = new JButton("Delete");
        searchButton = new JButton("Search");
        searchField = new JTextField(5);
        insertField = new JTextField(5);
        deleteField = new JTextField(5);
        heightLabel = new JLabel("Height: -1");
        sizeLabel = new JLabel("Size: 0");
        clearButton = new JButton("Clear Tree");
        resetSearchButton = new JButton("Reset Search");

        rightPanel = new JPanel(new GridLayout(3,1));
        rightPanel.setBackground(Color.lightGray);
        rightPanel.setPreferredSize(new Dimension(330,150));

        JPanel tree_pan = new JPanel(new GridLayout(5,1));
        JPanel tree_1 = new JPanel();
        JPanel tree_2 = new JPanel();
        JPanel tree_3 = new JPanel();
        JPanel tree_4 = new JPanel();
        JPanel tree_5 = new JPanel();

        tree_1.add(insertButton);
        tree_1.add(insertField);
        tree_4.add(deleteButton);
        tree_4.add(deleteField);
        tree_5.add(searchButton);
        tree_5.add(searchField);

        tree_2.add(heightLabel);
        tree_2.add(Box.createHorizontalStrut(50));
        tree_2.add(sizeLabel);
        tree_3.add(clearButton);
        tree_3.add(resetSearchButton);

        tree_pan.add(tree_1);
        tree_pan.add(tree_4);
        tree_pan.add(tree_5);
        tree_pan.add(tree_2);
        tree_pan.add(tree_3);
        tree_pan.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Tree Properties"));
        rightPanel.add(tree_pan);

        JPanel pan1 = new JPanel(new GridLayout(2,1));

        playButton = new JButton("Play");
        pauseButton = new JButton("Pause");
        playButton.setEnabled(false);
        slider = new JSlider(1,250);

        JPanel animPanel = new JPanel(new GridLayout(2,1));
        JPanel animButtons = new JPanel(new GridLayout(1,2));

        animButtons.add(playButton);
        animButtons.add(pauseButton);
        animPanel.add(animButtons);

        JPanel animB2 = new JPanel();
        JLabel speed = new JLabel("Speed:");
        Font f = speed.getFont();
        speed.setFont(f.deriveFont(f.getStyle() ^ Font.ITALIC));

        animB2.add(speed);
        animB2.add(new JLabel("fast"));
        animB2.add(slider);
        animB2.add(new JLabel("slow"));
        animPanel.add(animB2);
        animPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Animation Options"));
        pan1.add(animPanel);

        rightPanel.add(pan1);

        JPanel pan3 = new JPanel(new BorderLayout());
        history = new JTextArea(8,23);
        JScrollPane scrollPane = new JScrollPane(history);
        history.setEditable(false);
        history.setLineWrap(true);
        history.setWrapStyleWord(true);


        pan3.add(scrollPane);
        pan3.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Log"));

        rightPanel.add(pan3);
        this.add(rightPanel,BorderLayout.EAST);
    }

    public void refresh() {
        heightLabel.setText("Height: "+tree.maxDepth(tree.getRoot()));
        sizeLabel.setText("Size: "+tree.countNodes());
    }

    public void newTree(String msg, String hMsg) {
        boolean newTree = true;
        if(this.tree.getRoot() != null) {
            int reply = JOptionPane.showConfirmDialog(null,
                    msg, "Warning Message", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (reply == JOptionPane.YES_OPTION) {
                newTree = true;
            } else {
                newTree = false;
            }
        }

        if(newTree) {
            history.append("~ "+hMsg+"\n");
            tree.clearTree();
            tree.getNote().setNote("");
            stack.clear();
            refresh();
        }
    }

    public void clearInputFields() {
        insertField.setText("");
        deleteField.setText("");
        searchField.setText("");
    }

    public String trimString(String s) {
        s = s.trim();

        if(s.length() > 3)
            s = s.substring(0,3);

        if(s.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have not entered a valid input(s).",
                    "Error: Empty Input Detected!", JOptionPane.ERROR_MESSAGE);
            return null;
        } else if(s.contains(".")) {
            JOptionPane.showMessageDialog(null, "Please input a non-decimal value.",
                    "Error: Decimal Detected!", JOptionPane.ERROR_MESSAGE);
            return null;
        } else
            return s;
    }

    public void multipleInputs(String input) {
        Scanner in = new Scanner(input);
        tree.emptyTempArray();
        String[] specialCh = {"!","@","]","#","$","%","^","&","*",",","+","=",")","£"};

        while(in.hasNext()) {
            String word = in.next();
            String trimmed = trimString(word);

            if(trimmed == null)
                return;

            tree.getTemp().add(trimmed);

            for (int i=0; i<specialCh.length; i++) {
                if(trimmed.contains(specialCh[i])) {
                    JOptionPane.showMessageDialog(null, "You have entered an input(s) using special characters!"
                            ,"Error Message", JOptionPane.ERROR_MESSAGE);
                    tree.emptyTempArray();
                    return;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object s = e.getSource();

        if(s == insertButton) {
            String text = trimString(insertField.getText());

            if(text != null) {
                multipleInputs(insertField.getText());
                MainThread mainThread = new MainThread(this,0,text);
                mainThread.start();
            }
            clearInputFields();
            insertField.requestFocus();
        } else if(s == deleteButton) {
            String text = trimString(deleteField.getText());

            if(text != null) {
                multipleInputs(deleteField.getText());
                MainThread mainThread = new MainThread(this,1,text);
                mainThread.start();
            }
            clearInputFields();
            deleteField.requestFocus();
        }
        else if(s == searchButton) {
            String text = trimString(searchField.getText());

            if (text != null) {
                if (tree.isNumeric(text)) {
                    tree.resetAllNodesColor();
                    history.append("~ Searching node ["+text+"] in the tree.\n");
                    MainThread localThread = new MainThread(this,2,text);
                    localThread.start();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "The input data is invalid!",
                            "Error: Invalid Input!", JOptionPane.ERROR_MESSAGE);
                }
            }
            clearInputFields();
            searchField.requestFocus();
        } else if(s == clearButton) {
            newTree("Are you sure you want to clear the current tree?", "Current tree cleared.");
        } else if(s == playButton) {
            deactivatePauseButton();
            history.append("~ Resuming Animation.\n");
        } else if(s == pauseButton) {
            activatePauseButton();
            history.append("~ Paused Animation.\n");
        } else if(s == resetSearchButton) {
            tree.resetAllNodesColor();
            notes.clearNotes();
            clearInputFields();
            history.append("~ Resetting the node search.\n");
        }
    }

    private void activatePauseButton() {
        this.pause = true;
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
    }

    private void deactivatePauseButton() {
        this.pause = false;
        playButton.setEnabled(false);
        pauseButton.setEnabled(true);
    }

    public void performOperation(int x, String s) {
        if(x == 0) {
            for(int i=0; i<tree.tempListSize(); i++) {
                tree.insert(tree.getTemp().get(i));
                history.append("~ Inserting: "+tree.getTemp().get(i)+"\n");
                refresh();
            }
        } else if(x == 1) {
            for(int i=0; i<tree.tempListSize(); i++) {
                tree.delete(tree.getTemp().get(i));
                history.append("~ Deleting: "+tree.getTemp().get(i)+"\n");
                refresh();
            }
        } else if(x == 2) {
            tree.searchnode(s);
            refresh();
        }
    }

    /**Accessor Methods**/
    public TreePanel getTreePanel() {
        return treePanel;
    }

    public JSlider getSpeedSlider() {
        return slider;
    }

    public boolean isPause() {
        return pause;
    }

    public int getSteps() {
        return STEPS;
    }

    public boolean getOpenF() {
        return openF;
    }

    public Stack<String> getStack() {
        return stack;
    }
}
