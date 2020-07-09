import java.awt.*;

public class Node implements Comparable<Node> {
    /**Instance Variables**/
    private Node left = null, right = null, parent = null;
    private String data;
    private int sumOfTheHeight = 1, size = 1, height = 1;
    private int numOfSteps, leftSize, rightSize, x_coord, y_coord, advanceToX, advanceToY;
    private Color fgcolor, bgcolor;
    private Tree tree;
    private int [] p = new int[6];
    private Font valueFont = new Font("Courier New", Font.BOLD, 15);

    public Node(String data, Tree tree, int xPos, int yPos) {
        this.tree = tree;
        this.data = data;

        this.advanceToX = xPos;
        this.x_coord = advanceToX;
        this.advanceToY = yPos;
        this.y_coord = advanceToY;

        setColor(getBlackColor(), getPresetColor());

        this.numOfSteps = 0;
    }

    public Node(String data, Tree p) {
        this(data, p, p.getScreenWidth()/2, -20);
    }

    public Node(Node n) {
        this(n.data, n.tree, n.x_coord, n.y_coord);
    }

    public void setLeft(Node l) {
        this.left = l;
    }

    public void setRight(Node r) {
        this.right = r;
    }

    public void setHeight(int h) {
        this.height = h;
    }

    public void setData(String s) {
        this.data = s;
    }

    public void setParent(Node p) {
        this.parent = p;
    }

    public void fgColor(Color color) {
        this.fgcolor = color;
    }

    public void bgColor(Color color) {
        this.bgcolor = color;
    }

    public void setArrayP(int value, int index) {
        p[index] = value;
    }

    public void advanceToNode(Node node) {
        advanceTo(node.advanceToY, node.advanceToY -30 -15);
    }

    public void advanceToTheRoot() {
        advanceTo(this.tree.getRootX(), this.tree.getRootY());
    }

    public void advanceToAboveTheRoot() {
        advanceTo(this.tree.getRootX(), this.tree.getRootY() - 30 - this.tree.getYspan());
    }

    public void goDown() {
        advanceTo(this.advanceToX, this.tree.getScreenHeight() + 45);
    }

    public Node getLeft() {
        return this.left;
    }

    public Node getRight() {
        return this.right;
    }

    public int getHeight() {
        return this.height;
    }

    public String getData() {
        return this.data;
    }

    public Node getParent() {
        return this.parent;
    }

    public int getX() {
        return x_coord;
    }

    public int getY() {
        return y_coord;
    }

    public boolean isRootNode() {
        return this.parent == null;
    }

    public boolean isLeft() {
        return this.parent.left == this;
    }

    public int getArrayP(int index) {
        return p[index];
    }

    public Color getPresetColor() {
        return new Color(0x40, 0xFF, 0xFF);
    }

    public Color getInsertColor() {
        return Color.yellow.brighter();
    }

    public Color getDeleteColor() {
        return Color.red;
    }

    public Color getFindColor() {
        return Color.orange;
    }

    public Color getFoundColor() {
        return Color.green;
    }

    public Color getInvisibleColor() {
        return Color.white;
    }

    public Color getBlackColor() {
        return Color.black;
    }

    @Override
    public int compareTo(Node o) {
        if(this.getData().equals(o.getData()))
            return 0;

        if(this.getData().compareTo(o.getData()) < 0)
            return -1;

        if(this.getData().compareTo(o.getData()) > 0)
            return 1;

        return 0;
    }

    public void setColor(Color color1, Color color2) {
        this.fgcolor = color1;
        this.bgcolor = color2;
    }

    public void connectNode(Node node, String position) {
        if(position.equals("right"))
            this.right = node;
        else
            this.left = node;

        if (node != null)
            node.parent = this;
    }

    public void performCalculations() {
        for(int i=0; i<p.length; i++)
            p[i] = 0;

        if (this.right != null) {
            setArrayP(this.right.height,3);
            setArrayP(this.right.sumOfTheHeight,5);
            setArrayP(this.right.size,1);
        }

        if (this.left != null) {
            setArrayP(this.left.height,2);
            setArrayP(this.left.sumOfTheHeight,4);
            setArrayP(this.left.size,0);
        }
        setCalculations();
    }

    public int balanceDifference() {

        int result, x1, x2;

        if(this.left == null)
            x1 = 0;
        else
            x1 = this.left.height;

        if(this.right == null)
            x2 = 0;
        else
            x2 = this.right.height;

        result = x2 - x1;

        return result;
    }

    public void drawTree(Graphics graph) {
        ((Graphics2D) graph).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graph.setColor(getBlackColor());

        if (this.left != null) {
            graph.drawLine(this.x_coord, this.y_coord, this.left.x_coord, this.left.y_coord);
            this.left.drawTree(graph);
        }

        if (this.right != null) {
            graph.drawLine(this.x_coord, this.y_coord, this.right.x_coord, this.right.y_coord);
            this.right.drawTree(graph);
        }

        drawNode(graph);
    }

    public void drawNode(Graphics graph) {
        graph.setColor(this.bgcolor);
        graph.fillOval(this.x_coord - 15, this.y_coord - 15, 30, 30);
        graph.setColor(this.fgcolor);
        graph.drawOval(this.x_coord - 15, this.y_coord - 15, 30, 30);

        graph.setFont(valueFont);
        graph.setColor(this.fgcolor);
        FontMetrics fontMetrics = graph.getFontMetrics(valueFont);
        graph.drawString(this.data, this.x_coord - fontMetrics.stringWidth(""+this.data) / 2, this.y_coord - fontMetrics.getHeight() / 2 + fontMetrics.getAscent());
    }

    public void moveEachNodeInTheTree() {
        if (this.right != null)
            this.right.moveEachNodeInTheTree();

        if (this.left != null)
            this.left.moveEachNodeInTheTree();

        shiftNodes();
    }

    private void setCalculations() {
        this.height = (tree.max(getArrayP(2), getArrayP(3))) + 1;
        this.size = 1 + getArrayP(0) + getArrayP(1);
        this.sumOfTheHeight = this.size + getArrayP(4) + getArrayP(5);

        balanceDifference();
    }

    public void shiftNodes() {
        if (this.numOfSteps > 0)
        {
            this.x_coord = ((this.advanceToX - this.x_coord) / this.numOfSteps ) + this.x_coord;
            this.y_coord = ((this.advanceToY - this.y_coord) / this.numOfSteps) + this.y_coord;
            this.numOfSteps--;
        }
    }

    public void calculateAngleBetweenNodes() {
        if(this.left != null)
            this.leftSize = this.left.leftSize + this.left.rightSize;
        else
            this.leftSize = this.tree.getXspan() + this.tree.getRadius() + 5;

        if(this.right != null)
            this.rightSize = this.right.rightSize + this.right.leftSize;
        else
            this.rightSize = this.tree.getRadius() + this.tree.getXspan() + 5;
    }

    public void repositionTree() {
        if (this.left != null)
            this.left.repositionTree();

        if (this.right != null)
            this.right.repositionTree();

        calculateAngleBetweenNodes();
        setNodePositon();
    }

    private void setNodePositon() {
        if (this.parent == null)
            advanceToTheRoot();

        if (this.right != null) {
            this.right.advanceTo(this.advanceToX + this.right.leftSize, this.advanceToY + 40);
            this.right.setNodePositon();
        }

        if (this.left != null) {
            this.left.advanceTo(this.advanceToX - this.left.rightSize, this.advanceToY + 40);
            this.left.setNodePositon();
        }
    }

    public void advanceTo(int x, int y) {
        this.advanceToX = x;
        this.advanceToY = y;
        this.numOfSteps = this.tree.getMainFrame().getSteps();
    }

    public void advanceTo(Node node) {
        advanceTo(node.advanceToX, node.advanceToY);
    }
}
