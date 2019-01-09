import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> segmentList = new ArrayList<LineSegment>();
    private final int num;
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] inpoints) {
        if (inpoints == null)
            throw new IllegalArgumentException();
        for (int i = 0; i < inpoints.length; i++) {
            if (inpoints[i] == null)
                throw new IllegalArgumentException();
            for (int j = i+1; j < inpoints.length; j++) {
                if (inpoints[j] == null)
                    throw new IllegalArgumentException();
                if (inpoints[i].compareTo(inpoints[j]) == 0)
                    throw new IllegalArgumentException();
            }
        }

        Point[] myPoints = Arrays.copyOf(inpoints, inpoints.length);
        Arrays.sort(myPoints);
        for (int i = 0; i < myPoints.length-3; i++) {
            Comparator<Point> comparator = myPoints[i].slopeOrder();
            for (int j = i+1; j < myPoints.length-2; j++) {
                for (int m = j+1; m < myPoints.length-1; m++) {
                    if (comparator.compare(myPoints[j], myPoints[m]) == 0)
                        for (int n = m+1; n < myPoints.length; n++) {
                            if (comparator.compare(myPoints[m], myPoints[n]) == 0) {
                                LineSegment ls = new LineSegment(myPoints[i], myPoints[n]);
                                segmentList.add(ls);
                            }
                        }
                }
            }
        }
        num = segmentList.size();
    } 
    // the number of line segments
    public int numberOfSegments() {
        return num;

    }      
    // the line segments
    public LineSegment[] segments() {
        return segmentList.toArray(new LineSegment[numberOfSegments()]);
    }   

    //    public static void main(String[] args) {
    //
    //        // read the n points from a file
    //        In in = new In(args[0]);
    //        int n = in.readInt();
    //        Point[] points = new Point[n];
    //        for (int i = 0; i < n; i++) {
    //            int x = in.readInt();
    //            int y = in.readInt();
    //            points[i] = new Point(x, y);
    //        }
    //
    //        // draw the points
    //        StdDraw.enableDoubleBuffering();
    //        StdDraw.setXscale(0, 32768);
    //        StdDraw.setYscale(0, 32768);
    //        for (Point p : points) {
    //            p.draw();
    //        }
    //        StdDraw.show();
    //
    //        // print and draw the line segments
    //        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    //        for (LineSegment segment : collinear.segments()) {
    //            StdOut.println(segment);
    //            segment.draw();
    //        }
    //        StdDraw.show();
    //    }
}
