import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;

    private class Node {
        private final Point2D p;
        private Node left, right;
        private final RectHV rect;
        private final boolean xCoordinate;

        public Node(Point2D p, RectHV rect, boolean xCoordinate) {
            this.p = p;
            this.rect = rect;
            this.xCoordinate = xCoordinate;
        }
    }
    // construct an empty set of points     
    public KdTree() {

    }
    // is the set empty? 
    public boolean isEmpty() {
        return size == 0;
    }                   
    // number of points in the set 
    public int size() {
        return size;
    }      
    private int compare(Point2D p, Point2D q, boolean xCoordinate) {
        if (p == null || q == null) throw new IllegalArgumentException();
        if (p.equals(q)) return 0;
        if (xCoordinate) return p.x() < q.x() ? -1 : 1;
        else return p.y() < q.y() ? -1 : 1;
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, null, p, 0);
    }
    private Node insert(Node x, Node parent, Point2D p, int compare) {
        if (x == null) {
            if (size == 0) {
                size++;
                return new Node(p, new RectHV(0, 0, 1, 1), true);
            }
            RectHV rect = parent.rect;
            if (compare < 0) {
                if (parent.xCoordinate) // left 
                    rect = new RectHV(rect.xmin(), rect.ymin(), parent.p.x(), rect.ymax());
                else // down
                    rect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), parent.p.y());
            }
            else if (compare > 0) {
                if (parent.xCoordinate) // right
                    rect = new RectHV(parent.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                else // up
                    rect = new RectHV(rect.xmin(), parent.p.y(), rect.xmax(), rect.ymax());
            }
            size++;
            return new Node(p, rect, !parent.xCoordinate);
        }
        else {
            int cmp = compare(p, x.p, x.xCoordinate);
            if (cmp > 0) x.right = insert(x.right, x, p, cmp);
            else if (cmp < 0) x.left = insert(x.left, x, p, cmp); 
            return x;
        }
    }
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return contains(root, p);
    }           
    private boolean contains(Node x, Point2D p) {
        if (x == null) return false;
        int cmp = compare(p, x.p, x.xCoordinate);
        if (cmp == 0) return true;
        else if (cmp > 0) return contains(x.right, p);
        else return contains(x.left, p);
    }
    // draw all points to standard draw 
    public void draw() {
        draw(root);
    }                        
    private void draw(Node x) {
        if (x == null) return;
        draw(x.left);
        draw(x.right);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();
        StdDraw.setPenRadius();
        if (x.xCoordinate) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
    }
    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        range(root, rect, points);
        return points;        
    }      
    private void range(Node x, RectHV rect, ArrayList<Point2D> points) {
        if (x == null) return;
        if (rect.contains(x.p)) points.add(x.p);
        if (x.left != null && x.left.rect.intersects(rect)) range(x.left, rect, points);
        if (x.right != null && x.right.rect.intersects(rect)) range(x.right, rect, points);
    }
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;
        return nearest(root, root.p, p);
    }
    private Point2D nearest(Node x, Point2D near, Point2D p) {
        if (x == null) return near;
        int cmp = compare(p, x.p, x.xCoordinate);
        if (p.distanceSquaredTo(x.p) < p.distanceSquaredTo(near)) near = x.p;
        if (cmp > 0) {
            near = nearest(x.right, near, p);
            if (x.left != null) {
                if (near.distanceSquaredTo(p) > x.left.rect.distanceSquaredTo(p))
                    near = nearest(x.left, near, p);
            }
        }
        else if (cmp < 0) {
            near = nearest(x.left, near, p);
            if (x.right != null) {
                if (near.distanceSquaredTo(p) > x.right.rect.distanceSquaredTo(p))
                    near = nearest(x.right, near, p);
            }
        }
        return near;
    }
}
