import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class PointSET {
    private final SET<Point2D> pointSet;

    public PointSET() {                              // construct an empty set of points
        pointSet = new SET<>();
    }

    public boolean isEmpty() {                     // is the set empty?
        return pointSet.isEmpty();
    }

    public int size() {                        // number of points in the set
        return pointSet.size();
    }

    public void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException("point is null");
        if (!pointSet.contains(p)) {
            pointSet.add(p);
        }
    }

    public boolean contains(Point2D p) {           // does the set contain point p?
        if (p == null) throw new IllegalArgumentException("point is null");
        return pointSet.contains(p);
    }

    public void draw() {                        // draw all points to standard draw
        for (Point2D p : pointSet) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException("rect is null");
        LinkedList<Point2D> result = new LinkedList<>();
        for (Point2D p : pointSet) {
            if (p.x() <= rect.xmax() && p.x() >= rect.xmin() &&
                    p.y() <= rect.ymax() && p.y() >= rect.ymin()) {
                result.add(p);
            }
        }
        return result;
    }

    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException("point is null");
        Point2D result = null;
        double min = Double.MAX_VALUE;
        for (Point2D t : pointSet) {
            double dis = t.distanceTo(p);
            if (dis < min) {
                min = dis;
                result = t;
            }
        }
        return result;
    }

    public static void main(String[] args) {                 // unit testing of the methods (optional)
        StdDraw.enableDoubleBuffering();
        PointSET pointSET = new PointSET();
        pointSET.insert(new Point2D(0.5, 0.5));
        pointSET.insert(new Point2D(0.4, 0.3));
        pointSET.insert(new Point2D(0.3, 0.4));
        pointSET.draw();
        StdDraw.show();
    }
}
