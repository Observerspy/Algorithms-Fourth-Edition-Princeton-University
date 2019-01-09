import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> segmentList = new ArrayList<LineSegment>();
    private final int num;
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] inpoints) {
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
        for (int i = 0; i < myPoints.length-1; i++) {
            Point origin = myPoints[i];
            int k = 0;
            Point[] otherPoints = new Point[myPoints.length-i-1];
            for (int j = i+1; j < myPoints.length; j++) {
                otherPoints[k++] = myPoints[j];
            }
            Comparator<Point> comparator = origin.slopeOrder();
            Arrays.sort(otherPoints, comparator); 
            Point start = otherPoints[0];
            int count = 0;
            double[] slopesBefore = new double[i]; // slopes of previous points to pointSet[i]
            for (int j = 0; j < i; ++j) 
                slopesBefore[j] = origin.slopeTo(myPoints[j]);
            Arrays.sort(slopesBefore);
            double lastSlope = Double.NEGATIVE_INFINITY;
            for (int j = 1; j < k; j++) {
                if (comparator.compare(start, otherPoints[j]) == 0) {
                    count++;
                    //                    System.out.println(count+":"+start.toString()+" "+otherPoints[j].toString());
                }
                else {
                    if (count >= 2 && !isSubSgmt(slopesBefore, lastSlope)) {
                        segmentList.add(new LineSegment(origin, start)); 
                    }
                    count = 0;
                }
                start = otherPoints[j];
                lastSlope = origin.slopeTo(otherPoints[j]);
            }
            if (count >= 2 && !isSubSgmt(slopesBefore, lastSlope)) {
                segmentList.add(new LineSegment(origin, otherPoints[k-1])); 
                count = 0;
            }
        }
        num = segmentList.size();
    }   
    // determine if the segment is a sub-segment of the previous segments 
    private boolean isSubSgmt(double[] slopes, double slope) {
        int lo = 0; 
        int hi = slopes.length - 1; // use binary search 
        while (lo <= hi) { 
            int mid = lo + (hi - lo) / 2; 
            if (slope < slopes[mid]) hi = mid - 1; 
            else if (slope > slopes[mid]) lo = mid + 1; 
            else return true; 
        } 
        return false;
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
    //        FastCollinearPoints collinear = new FastCollinearPoints(points);
    //        for (LineSegment segment : collinear.segments()) {
    //            StdOut.println(segment);
    //            segment.draw();
    //        }
    //        StdDraw.show();
    //    }
}
