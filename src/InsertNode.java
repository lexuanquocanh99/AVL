public class InsertNode extends Animation {

    /**Instance Variables**/
    private Tree tree;
    private Node node;
    private String value;

    /**Constructor**/
    public InsertNode(Tree tree, String value) {
        super(tree.getMainFrame());
        this.mf = tree.getMainFrame();
        this.tree = tree;
        this.value = value;

        this.node = new Node(value, tree);
        this.node.bgColor(node.getInsertColor());
        tree.setNode(this.node);
    }

    public void run() {
        Node currentNode = this.tree.getRoot();

        if (this.tree.getRoot() == null) {
            this.tree.setRoot(this.node = new Node(this.node));
            this.node.advanceToTheRoot();
            tree.getNote().setNote("Inserting a new root node [" + node.getData()+"].");
            waitOnPause();
        } else {
            this.node.advanceToAboveTheRoot();
            tree.getNote().setNote("Starting to insert node ["+this.value+"].");
            waitOnPause();

            while (true) {
                int result = 0;
                boolean exit = false;

                if(tree.isNumeric(currentNode.getData()) && tree.isNumeric(this.value)) {
                    int current = Integer.parseInt(currentNode.getData());
                    int thisValue = Integer.parseInt(this.value);

                    if (current == thisValue)
                        result = 1;

                    if (thisValue < current)
                        result = 2;
                    else if (thisValue > current)
                        result = 3;
                } else {
                    if (currentNode.getData().compareTo(this.value) == 0)
                        result = 1;

                    if (this.value.compareTo(currentNode.getData()) < 0)
                        result = 2;
                    else if (this.value.compareTo(currentNode.getData()) > 0)
                        result = 3;
                }

                switch(result) {
                    case 1:
                        tree.getNote().setNote("Node ["+this.value+"] already exists in the tree.");
                        if(!mf.getOpenF())
                            this.node.bgColor(node.getDeleteColor());
                        else
                            this.node.setColor(node.getInvisibleColor(), node.getInvisibleColor());
                        waitOnPause();
                        this.node.goDown();
                        tree.getNote().setNote("");
                        waitOnPause();
                        return;

                    case 2:
                        tree.getNote().setNote("Checking left side since node [" + this.value +
                                "] is less than node ["+ currentNode.getData()+"].");
                        if (currentNode.getLeft() != null) {
                            currentNode = currentNode.getLeft();
                            break;
                        } else {
                            currentNode.connectNode(this.node = new Node(this.node),"left");
                            tree.getNote().setNote("Node ["+this.value+"] inserted since node ["+currentNode.getData()+
                                    "]'s left child is empty.");
                            exit = true;
                            break;
                        }

                    case 3:
                        tree.getNote().setNote("Going to right side since node [" + this.value +
                                "] is greater than node ["+ currentNode.getData()+"].");

                        if (currentNode.getRight() != null) {
                            currentNode = currentNode.getRight();
                            break;
                        } else {
                            this.node = new Node(this.node);
                            currentNode.connectNode(this.node,"right");
                            tree.getNote().setNote("Node ["+this.value+"] inserted since node ["+currentNode.getData()+
                                    "]'s right child is empty.");
                            exit = true;
                            break;
                        }

                    default:
                        break;
                }

                if(exit)
                    break;

                this.node.advanceToNode(currentNode);
                waitOnPause();
            }

            this.node = (this.tree.node = null);

            if(this.tree.getRoot() != null)
                this.tree.getRoot().repositionTree();

            waitOnPause();

            this.tree.reBalanceNode(currentNode);
        }
        tree.getNote().setNote("Insertion Complete.");
        tree.getMainFrame().getStack().push("i "+this.value);
    }
}
