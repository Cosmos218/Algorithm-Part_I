import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.function.DoubleBinaryOperator;

public class KdTree {
    private Node root;
    private static final int LB = 0;
    private static final int RT = 1;
    private Point2D result;
    private Double min;
    private int size;

    private static class Node { //static because there is no generic type for KdTree
        private final Point2D p;
        private final RectHV rect;
        private Node lb;
        private Node rt;

        public Node(Point2D p, RectHV rect, Node lb, Node rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }
    }

    public KdTree() {                              // construct an empty set of points
        root = null;
        size = 0;
    }

    public boolean isEmpty() {                     // is the set empty?
        return size == 0;
    }

    public int size() {                        // number of points in the set
        return size;
    }

    public void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException("point is null");
        if (contains(p)) return;
        if (root == null) root = new Node(p, new RectHV(0, 0, 1, 1), null, null);
        else root = insert(root, p, 0, null, 0);
        size++;
    }

    // code:0 for compare x, 1 for compare y
    // code2: 0 for lb, 1 for rt
    private Node insert(Node x, Point2D p, int code, Node parent, int code2) {
        if (x == null) {
            RectHV rect;
            if (code == 1) {
                if (code2 == LB) {
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), parent.rect.ymax());
                } else {
                    rect = new RectHV(parent.p.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                }
            } else {
                if (code2 == LB) {
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.p.y());
                } else {
                    rect = new RectHV(parent.rect.xmin(), parent.p.y(), parent.rect.xmax(), parent.rect.ymax());
                }
            }
            return new Node(p, rect, null, null);
        }
        double cmp = code == 0 ? p.x() - x.p.x() : p.y() - x.p.y();
        if (cmp < 0) x.lb = insert(x.lb, p, 1 - code, x, LB);
        else x.rt = insert(x.rt, p, 1 - code, x, RT);
        return x;
    }

    public boolean contains(Point2D p) {           // does the set contain point p?
        if (p == null) throw new IllegalArgumentException("point is null");
        return contains(root, p, 0);
    }

    private boolean contains(Node x, Point2D p, int code) {
        if (x == null) return false;
        double cmp = code == 0 ? p.x() - x.p.x() : p.y() - x.p.y();
        if (cmp < 0) return contains(x.lb, p, 1 - code);
        else if (cmp > 0) return contains(x.rt, p, 1 - code);
        else {
            double cmp2 = code == 1 ? p.x() - x.p.x() : p.y() - x.p.y();
            if (cmp2 == 0) return true;
            else return contains(x.rt, p, 1 - code);
        }
    }

    public void draw() {                        // draw all points to standard draw
        StdDraw.enableDoubleBuffering();
        draw(root, 0);
        StdDraw.show();
    }

    private void draw(Node x, int code) {
        if (x == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(x.p.x(), x.p.y());
        if (code == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        draw(x.lb, 1 - code);
        draw(x.rt, 1 - code);
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException("rect is null");
        LinkedList<Point2D> result = new LinkedList<>();
        range(rect, root, 0, result);
        return result;
    }

    private void range(RectHV rect, Node x, int code, LinkedList<Point2D> result) {
        if (x == null) return;
        if (rect.contains(x.p)) {
            result.add(x.p);
            range(rect, x.lb, 1 - code, result);
            range(rect, x.rt, 1 - code, result);
        } else if (code == 0) {
            if (rect.xmax() < x.p.x()) range(rect, x.lb, 1 - code, result);
            else if (rect.xmin() > x.p.x()) range(rect, x.rt, 1 - code, result);
            else {
                range(rect, x.lb, 1 - code, result);
                range(rect, x.rt, 1 - code, result);
            }
        } else {
            if (rect.ymax() < x.p.y()) range(rect, x.lb, 1 - code, result);
            else if (rect.ymin() > x.p.y()) range(rect, x.rt, 1 - code, result);
            else {
                range(rect, x.lb, 1 - code, result);
                range(rect, x.rt, 1 - code, result);
            }
        }
    }

    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException("point is null");
        result = null;
        min = Double.MAX_VALUE;
        nearest(root, p, 0);
        return result;
    }

    private void nearest(Node x, Point2D p, int code) {
        if (x == null) return;
        if (x.p.distanceTo(p) < min) {
            min = x.p.distanceTo(p);
            result = x.p;
        }
        if (x.lb != null && x.rt != null) {
            double lbDis = x.lb.rect.distanceTo(p);
            double rtDis = x.rt.rect.distanceTo(p);
            if (lbDis < rtDis) {
                if (min > x.lb.rect.distanceTo(p)) {
                    nearest(x.lb, p, 1-code);
                }
                if (min > x.rt.rect.distanceTo(p)) {
                    nearest(x.rt, p, 1-code);
                }
            } else {
                if (min > x.rt.rect.distanceTo(p)) {
                    nearest(x.rt, p, 1-code);
                }
                if (min > x.lb.rect.distanceTo(p)) {
                    nearest(x.lb, p, 1-code);
                }
            }
        } else {
            if (x.lb != null && min > x.lb.rect.distanceTo(p)) {
                nearest(x.lb, p, 1-code);
            }
            if (x.rt != null && min > x.rt.rect.distanceTo(p)) {
                nearest(x.rt, p, 1-code);
            }
        }
    }

    public static void main(String[] args) {                 // unit testing of the methods (optional)e
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.5, 0.5));
        kdTree.insert(new Point2D(0.3, 0.3));
        kdTree.insert(new Point2D(0.2, 0.7));
        kdTree.insert(new Point2D(0.12, 0.9));
        kdTree.draw();
//        System.out.println(kdTree.contains(new Point2D(0.5, 0.5)));
    }
}
