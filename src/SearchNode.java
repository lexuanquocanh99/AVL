public class SearchNode extends Animation {

    /**Instance Variables**/
    private Tree tree;
    private Node node;
    private String value;

    /**Constructor**/
    public SearchNode(Tree tree, String value) {
        super(tree.getMainFrame());
        this.tree = tree;
        this.value = value;
        this.node = new Node(value, tree);
        tree.setNode(this.node);
        this.node.bgColor(node.getFindColor());
    }

    public void run() {
        if(this.tree.getRoot() == null) {
            this.node.advanceToAboveTheRoot();
            tree.getNote().setNote("The tree is empty.");
            waitOnPause();
            tree.getNote().setNote("Not found!");
            this.node.goDown();
            return;
        } else {
            Node n = this.tree.getRoot();
            this.node.advanceToNode(n);
            tree.getNote().setNote("Finding node ["+this.value+"]...");
            waitOnPause();

            Node child = tree.getRoot();

            boolean exit = false;

            while(child != null) {

                n = child;

                boolean lessThan = false;
                boolean moreThan = false;
                boolean equals = false;

                if (tree.isNumeric(this.value) && tree.isNumeric(n.getData())) {
                    int thisValue = Integer.parseInt(this.value);
                    int nValue = Integer.parseInt(n.getData());

                    if (thisValue >= nValue)
                        moreThan = true;
                    else if (thisValue <= nValue)
                        lessThan = true;

                    if (thisValue == nValue)
                        equals = true;
                } else {
                    if (this.value.compareTo(n.getData()) >= 0)
                        moreThan = true;
                    else if (this.value.compareTo(n.getData()) <= 0)
                        lessThan = true;

                    if (this.value.compareTo(n.getData()) == 0)
                        equals = true;
                }

                if (lessThan) {
                    child = n.getLeft();

                    if (!exit) {
                        tree.getNote().setNote("Not Found!");
                        node.goDown();
                    }
                }

                if (moreThan) {
                    child = n.getRight();

                    if (child == null)
                        child = n.getLeft();

                    if (!exit) {
                        tree.getNote().setNote("Not Found!");
                        node.goDown();
                    }
                }

                if(equals) {
                    tree.getNote().setNote("Found node.");
                    waitOnPause();
                    this.node.advanceTo(n);
                    this.node.bgColor(node.getFoundColor());
                    exit = true;
                    waitOnPause();
                }
            }
        }
    }
}
