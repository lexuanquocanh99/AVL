import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Tree {

    /**Instance variables**/
    private Node root;
    public Node node;
    private ArrayList<String> temp;
    private MainFrame mf;
    private int radius, xCoordinateSpanLenth, yCoordinateSpanLength, xRootCoordinate,
            yRootCoordinate, heightOfScreen, widthOfScreen;
    private Animation anim;
    private Notes note;

    public Tree(MainFrame mf) {
        this.root = null;
        this.node = null;
        this.mf = mf;
        this.note = new Notes();
        temp = new ArrayList<String>();
    }

    public boolean isNumeric(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void insert(String data) {
        InsertNode insNode = new InsertNode(this, data);
        startAnimationThread(insNode);
    }

    public void delete(String data) {
        DeleteNode delNode = new DeleteNode(this,data);
        startAnimationThread(delNode);
    }

    public void searchnode(String s) {
        SearchNode sn = new SearchNode(this, s);
        startAnimationThread(sn);
    }

    public void rotateNode(Node node) {
        boolean test = false;

        if(node.isLeft())
            test = true;
        else
            test = false;

        Node currentNode = node.getParent();

        if (currentNode.isRootNode()) {
            this.root = node;
            node.setParent(null);
        } else if (currentNode.isLeft())
            currentNode.getParent().connectNode(node,"left");
        else
            currentNode.getParent().connectNode(node,"right");

        /**Left rotation**/
        if(!test) {
            if (node.getLeft() == null)
                currentNode.setRight(null);
            else
                currentNode.connectNode(node.getLeft(),"right");

            node.connectNode(currentNode,"left");
        }

        /**Right rotation**/
        if(test) {
            if (node.getRight() == null)
                currentNode.setLeft(null);
            else
                currentNode.connectNode(node.getRight(),"left");

            node.connectNode(currentNode,"right");
        }

        if (this.root != null)
            this.root.repositionTree();

        if (node.getLeft() != null)
            node.getLeft().performCalculations();

        if (node.getRight() != null)
            node.getRight().performCalculations();

        node.performCalculations();
    }
    private void startAnimationThread(Animation anim) {
        this.anim = anim;
        this.anim.start();
        try {
            this.anim.join();
        } catch (InterruptedException ie) {

        }
    }

    public void clearTree() {
        getNote().setNote("");
        this.root = null;
        this.node = null;
    }

    public void drawOntoTheDisplayPanel(Graphics graph) {
        if (this.root != null) {
            this.root.moveEachNodeInTheTree();
            this.root.drawTree(graph);
        }if (this.node != null) {
            this.node.shiftNodes();
            this.node.drawNode(graph);
        }
    }

    public void readjustTheNodes() {
        int ten = 10;
        this.radius = ten+5;
        this.xCoordinateSpanLenth = ten;
        this.yCoordinateSpanLength = ten;

        this.widthOfScreen = this.mf.getTreePanel().getDimensionSize().width;
        this.heightOfScreen = this.mf.getTreePanel().getDimensionSize().height;

        this.xRootCoordinate = (this.widthOfScreen / 2);
        this.yRootCoordinate = (5 * this.radius + this.yCoordinateSpanLength);

        repositionTheTree();
    }

    public Node getRoot() {
        return root;
    }

    public Node getNode() {
        return node;
    }

    public int getRootX() {
        return xRootCoordinate;
    }

    public int getRootY() {
        return yRootCoordinate;
    }

    public int getRadius() {
        return radius;
    }

    public int getXspan() {
        return xCoordinateSpanLenth;
    }

    public int getYspan() {
        return yCoordinateSpanLength;
    }

    public int getScreenHeight() {
        return heightOfScreen;
    }

    public int getScreenWidth() {
        return widthOfScreen;
    }

    public Notes getNote() {
        return this.note;
    }

    public MainFrame getMainFrame() {
        return this.mf;
    }

    /**Mutator methods**/
    public void setDS(Notes note) {
        this.note = note;
    }
    public void setNode(Node n) {
        this.node = n;
    }
    public void setRoot(Node r) {
        this.root = r;
    }

    public void resetAllNodesColor() {
        resetAllNodesColor(root);
    }

    private void resetAllNodesColor(Node n) {
        if(n == null)
            return;
        else {
            n.bgColor(root.getPresetColor());
            resetAllNodesColor(n.getLeft());
            resetAllNodesColor(n.getRight());
        }
    }

    public int tempListSize() {
        return temp.size();
    }

    public ArrayList<String> getTemp() {
        return temp;
    }

    public void emptyTempArray() {
        temp.clear();
    }

    private int countNodes(Node n) {
        if(n == null){
            return 0;
        } else {
            int sum = 1;
            sum += countNodes(n.getLeft());
            sum += countNodes(n.getRight());
            return sum;
        }
    }

    public int countNodes() {
        return countNodes(root);
    }

    public int max (int left, int right) {
        if(left > right)
            return left;
        else
            return right;
    }

    public int maxDepth(Node n) {
        if(n == null)
            return -1;
        else
        {
            int lHeight = maxDepth(n.getLeft());
            int rHeight = maxDepth(n.getRight());

            return max(lHeight,rHeight)+1;
        }
    }

    public void reBalanceNode(Node currentNode) {
        while (currentNode != null) {
            currentNode.bgColor(Color.PINK);
            currentNode.performCalculations();
            getNote().setNote("Checking balance at node ["+currentNode.getData()+"]...");
            anim.waitOnPause();

            if (currentNode.balanceDifference() == -2) {
                getNote().setNote("Balancing required at node ["+currentNode.getData()+
                        "] since it has a balance difference of 2.");
                anim.waitOnPause();

                if (currentNode.getLeft().balanceDifference() != 1) {
                    getNote().setNote("Performing right rotation at node ["+currentNode.getLeft().getData()+
                            "] by making node ["+currentNode.getLeft().getParent().getData()+"] its right subtree.");
                    currentNode.bgColor(root.getPresetColor());
                    currentNode = currentNode.getLeft();
                    currentNode.bgColor(Color.PINK);
                    rotateNode(currentNode);
                } else {
                    currentNode.bgColor(root.getPresetColor());
                    currentNode = currentNode.getLeft().getRight();
                    currentNode.bgColor(Color.PINK);
                    getNote().setNote("Performing a left-right rotation at node ["+currentNode.getData()+"].");
                    anim.waitOnPause();

                    getNote().setNote("Making node ["+currentNode.getParent().getData()+
                            "] the left subtree of node ["+currentNode.getData()+"].");
                    rotateNode(currentNode);	//rotate this node
                    anim.waitOnPause();

                    getNote().setNote("Performing right rotation at node ["+currentNode.getData()+
                            "] by making node ["+currentNode.getParent().getData()+"] its right subtree.");
                    rotateNode(currentNode);	//rotate this node
                }

                anim.waitOnPause();
            } else if (currentNode.balanceDifference() == 2) {
                getNote().setNote("Balancing required at node ["+currentNode.getData()+
                        "] since it has a balance difference of 2.");
                anim.waitOnPause();

                if (currentNode.getRight().balanceDifference() != -1) {
                    getNote().setNote("Performing left rotation at node ["+currentNode.getRight().getData()+
                            "] by making node ["+currentNode.getRight().getParent().getData()+"] its left subtree.");
                    currentNode.bgColor(root.getPresetColor());
                    currentNode = currentNode.getRight();
                    currentNode.bgColor(Color.PINK);
                    rotateNode(currentNode);
                } else {
                    currentNode.bgColor(root.getPresetColor());
                    currentNode = currentNode.getRight().getLeft();
                    currentNode.bgColor(Color.PINK);
                    getNote().setNote("Performing a right-left rotation at node ["+currentNode.getData()+"].");
                    anim.waitOnPause();

                    getNote().setNote("Making node ["+currentNode.getParent().getData()+
                            "] the right subtree of node ["+currentNode.getData()+"].");
                    rotateNode(currentNode);
                    anim.waitOnPause();

                    getNote().setNote("Performing left rotation at node ["+currentNode.getData()+
                            "] by making node ["+currentNode.getParent().getData()+"] its left subtree.");
                    rotateNode(currentNode);
                }
                anim.waitOnPause();
            }
            currentNode.bgColor(new Color(0x40, 0xFF, 0xFF));

            currentNode = currentNode.getParent();
        }
    }

    private void repositionTheTree()
    {
        if (this.root != null)
            this.root.repositionTree();
    }
}
