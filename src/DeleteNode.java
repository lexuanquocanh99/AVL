public class DeleteNode extends Animation {

    /**Instance Variables**/
    private Tree tree;
    private Node node;
    private String value;


    /**Constructor**/
    public DeleteNode(Tree tree, String value) {
        super(tree.getMainFrame());
        this.tree = tree;
        this.value = value;

        this.node = new Node(value,tree);
        tree.setNode(this.node);
        this.node.bgColor(node.getFindColor());
    }

    public void run() {
        if(this.tree.getRoot() == null) {
            this.node.advanceToAboveTheRoot();
            tree.getNote().setNote("The tree is empty.");
            waitOnPause();
            this.node.goDown();
            this.node.bgColor(node.getDeleteColor());
            tree.getNote().setNote("Not found!");
            return;
        } else {
            Node n = this.tree.getRoot();
            this.node.advanceToNode(n);
            tree.getNote().setNote("Finding node ["+this.value+"]...");
            waitOnPause();

            Node parent = tree.getRoot();
            Node delNode = null;
            Node child = tree.getRoot();

            boolean exit = false;

            while(child != null) {
                parent = n;
                n = child;

                boolean lessThan = false;
                boolean moreThan = false;
                boolean equals = false;

                if(tree.isNumeric(this.value) && tree.isNumeric(n.getData())) {
                    int thisValue = Integer.parseInt(this.value);
                    int nValue = Integer.parseInt(n.getData());

                    if(thisValue >= nValue)
                        moreThan = true;
                    else if(thisValue <= nValue)
                        lessThan = true;

                    if(thisValue == nValue)
                        equals = true;
                } else {
                    if(this.value.compareTo(n.getData()) >= 0)
                        moreThan = true;
                    else if(this.value.compareTo(n.getData()) <= 0)
                        lessThan = true;

                    if(this.value.compareTo(n.getData()) == 0)
                        equals = true;
                }

                if(lessThan) {
                    child = n.getLeft();

                    if(!exit) {
                        tree.getNote().setNote("Searching left since node ["+this.value+
                                "] is less than node ["+n.getData()+"].");
                        this.node.advanceToNode(n);
                    }
                }

                if(moreThan) {
                    child = n.getRight();

                    if(child == null)
                        child = n.getLeft();

                    if(!exit) {
                        tree.getNote().setNote("Searching right since node ["+this.value+
                                "] is greater than node ["+n.getData()+"].");
                        this.node.advanceToNode(n);
                    }
                }

                if(equals) {
                    delNode = n;
                    tree.getNote().setNote("Found node.");
                    waitOnPause();

                    this.node.advanceTo(n);
                    tree.getNote().setNote("Deleting node.");
                    this.node.bgColor(node.getDeleteColor());
                    exit = true;
                    waitOnPause();
                }

                if(!exit)
                    waitOnPause();
            }

            if(delNode == null) {
                this.node.goDown();
                tree.getNote().setNote("Node not found.");
                return;
            }

            if(delNode != null) {
                delNode.setData(n.getData());

                if(n.getLeft() != null)
                    child = n.getLeft();
                else
                    child = n.getRight();

                if(tree.getRoot().getData().compareTo(this.value) == 0)
                    this.tree.setRoot(child);
                else {
                    if(parent.getLeft() == n)
                        parent.setLeft(child);
                    else
                        parent.setRight(child);
                }
            }

            this.node.goDown();

            if(this.tree.getRoot() != null)
                this.tree.getRoot().repositionTree();

            tree.getNote().setNote("Node deleted.");
            waitOnPause();

            this.tree.reBalanceNode(parent);
        }
        tree.getNote().setNote("Deletion complete.");
        tree.getMainFrame().getStack().push("d "+this.value);
    }
}
