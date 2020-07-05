import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class AVLTree {

    class Node{
        int N;
        int height = 0;
        Node parent = null, left = null, right = null;
        Node(int n, int h, Node P){
            N = n;
            height = h;
            parent = P;
        }
    }

    Node root = null;

    /** Right rotation **/

    void Rrotation(Node y, Node z){
        y.parent = z.parent;
        if (y.parent == null){
            root = y;
        } else if (y.parent.left == z){
            y.parent.left = y;
        } else {
            y.parent.right = y;
        }

        z.left = y.right;
        if (z.left != null){
            z.left.parent = z;
        }
        y.right = z;
        z.parent = y;
        z.height--;
    }

    /** Left rotation **/

    void Lrotation(Node y, Node z){
        y.parent = z.parent;
        if (z.parent == null){
            root = y;
        } else if (z.parent.left == z){
            y.parent.left = y;
        } else {
            y.parent.right = y;
        }

        z.right = y.left;
        if (z.right != null){
            z.right.parent = z;
        }
        y.left = z;
        z.parent = y;
        z.height --;
    }

    /** Balance Tree **/

    Node dobalance(Node x){
        int h1 = 0, h2 = 0;
        //h1 is the height of x, h2 is the height of x's sibling
        Node n1 = null, rent = null;
        Node y = x, z;

        while (y.parent != null){
            if (y.parent.left == y){
                n1 = y.parent.right;
            } else if (y.parent.right == y){
                n1 = y.parent.left;
            }
            h1 = y.height;
            if (n1 == null){
                h2 = 0;
            } else {
                h2 = n1.height;
            }
            if (Math.abs(h2 - h1) > 1){
                break;
            }
            y.parent.height = 1 + Math.max(h1, h2);
            y = y.parent;
        }
        if (y.parent == null){
            return null;
        }
        z = y.parent;
        rent = z;

        h1 = (z.left == null)?0:z.left.height;
        h2 = (z.right == null)?0:z.right.height;
        if (h1 < h2){
            y = z.right;
        } else {
            y = z.left;
        }

        h1 = (y.left == null)?0:y.left.height;
        h2 = (y.right == null)?0:y.right.height;
        if (h1 < h2){
            x = y.right;
        } else {
            x = y.left;
        }

        y.parent = z;
        x.parent = y;

        if (z.left == y){
            if (y.left == x){
                Rrotation(y,z);
            } else {
                Lrotation(x,y);
                x.height++;
                Lrotation(x,z);
            }
        } else {
            if (y.right == x){
                Lrotation(y,z);
            } else {
                Rrotation(x,y);
                x.height++;
                Lrotation(x,z);
            }
        }

        return rent;
    }

    /** Insert **/

    void insert(int N, Node R){
        if (root == null){
            root = new Node(N, 1, null);
        } else {
            if (N <= R.N){
                if (R.left == null){
                    R.left = new Node(N, 1, R);
                    dobalance(R.left);
                } else {
                    insert(N, R.left);
                }
            } else {
                if (R.right == null){
                    R.right = new Node(N, 1, R);
                    dobalance(R.right);
                } else {
                    insert(N, R.right);
                }
            }
        }
    }

    /** Search **/

    Node search(int N, Node r){
        if (r == null){
            return null;
        }
        if (N == r.N){
            return r;
        } else if (N < r.N){
            return search(N,r.left);
        } else {
            return search(N,r.right);
        }
    }

    /** Delete **/

    void delete(Node x){
        Node y, z = null;
        if (x.left == null || x.right == null){
            y = x;
        } else {
            Node temp = x;
            for (y = temp.parent; y != null && temp == y.right; y = y.parent){
                temp = y;
            }
        }

        if (y.left != null){
            z = y.left;
        } else {
            z = y.right;
        }

        if (z != null){
            z.parent = y.parent;
        }

        if (y.parent == null){
            root = z;
        } else {
            if (y == y.parent.left){
                y.parent.left = z;
            } else {
                y.parent.right = z;
            }
        }
        x.N = y.N;
        y.height = 0;

        do {
            y = dobalance(y);
        } while (y != null);
    }

    static AVLTree B;

    public static void main(String[] args){
        B = new AVLTree();
        new Main();
    }

    static class Main extends JFrame implements ActionListener{

        public Main(){
            this.setSize(500,200);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);
            JPanel jPanel = new JPanel();

            jbtninsert = new JButton("Insert");
            jbtnsearch = new JButton("Find");
            jbtndelete = new JButton("Delete");
            jbtnshowtree = new JButton("Show Tree");
            jbtninsert.addActionListener(this);
            jbtnsearch.addActionListener(this);
            jbtndelete.addActionListener(this);
            jbtnshowtree.addActionListener(this);
            jPanel.add(jbtninsert);
            jPanel.add(jbtnsearch);
            jPanel.add(jbtndelete);
            jPanel.add(jbtnshowtree);

            Border b = BorderFactory.createEmptyBorder(45, 0, 0, 0);
            jPanel.setBorder(b);

            this.setTitle("AVL Tree Program");
            this.add(jPanel);
            this.setVisible(true);
        }
        JPanel jPanel;
        private JButton jbtninsert, jbtnsearch, jbtndelete, jbtnshowtree;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == jbtninsert){
                String s = JOptionPane.showInputDialog("Enter the integer value");
                int i = Integer.parseInt(s);
                B.insert(i,B.root);
            } else if (e.getSource() == jbtnsearch){
                String s = JOptionPane.showInputDialog("Enter the integer value");
                int i = Integer.parseInt(s);
                if (B.search(i,B.root) == null){
                    JOptionPane.showMessageDialog(null, "Not found "+i+" in this tree!");
                } else {
                    JOptionPane.showMessageDialog(null,"Found "+i+" in this tree!");
                }
            } else if (e.getSource() == jbtndelete){
                String s = JOptionPane.showInputDialog("Enter the integer value");
                int i = Integer.parseInt(s);
                Node temp = B.search(i, B.root);
                if (temp == null){
                    JOptionPane.showMessageDialog(null, "Not found "+i+" in this tree!");
                } else {
                    B.delete(temp);
                    JOptionPane.showMessageDialog(null, "Removed "+i+" in this tree!");
                }
            } else if (e.getSource() == jbtnshowtree){
                JFrame f = new JFrame("AVL Tree");
                Drawtree applet = new Drawtree();
                f.getContentPane().add("Center", applet);
                Toolkit tk = Toolkit.getDefaultToolkit();
                int xSize = ((int) tk.getScreenSize().getWidth());
                applet.init(B.root,xSize-50);
                f.pack();
                f.setSize(new Dimension(xSize,500));
                f.setVisible(true);
            }
        }
    }

    static class Drawtree extends JApplet{

        final Color bg = Color.white;
        final Color fg = Color.black;
        final BasicStroke stroke = new BasicStroke(2.0f);
        final BasicStroke wideStroke = new BasicStroke(8.0f);

        Dimension totalSize;
        int height, width;
        Node r = null;
        public void init(Node N, int x){
            setBackground(bg);
            setForeground(fg);
            r = N;
            width = x;
        }
        Graphics2D g2;

        public void paint(Graphics g){
            g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            getSize();
            inorder(r,0,width,80);
        }

        public void draw(int x1, int x2, int y, String n, int d){
            g2.setStroke(stroke);
            g2.setPaint(Color.white);
            int x = (x1 + x2)/2;
            if (d == 1){
                g2.draw(new Line2D.Double(x2, y - 30, x + 15, y));
            } else if (d == 2){
                g2.draw(new Line2D.Double(x + 15, y, x1 + 30, y - 30));
            }
            g2.setPaint(Color.blue);
            Shape circle = new Ellipse2D.Double((x1 + x2)/2, y, 30, 30);
            g2.draw(circle);
            g2.fill(circle);
            g2.setPaint(Color.white);
            g2.drawString(n, x+10, y+18);
        }

        int x1 = 500, y1 = 30;
        void inorder(Node r, int x1, int x2, int y){
            if (r == null){
                return;
            }
            inorder(r.left, x1, (x1 + x2)/2, y + 40);
            if (r.parent == null){
                draw(x1, x2, y, r.N + "", 0);
            } else {
                if (r.parent.N < r.N){
                    draw(x1, x2, y, r.N + "", 2);
                } else {
                    draw(x1, x2, y, r.N + "", 1);
                }
            }
            inorder(r.right, (x1 + x2)/2, x2, y + 40);
        }
    }
}
