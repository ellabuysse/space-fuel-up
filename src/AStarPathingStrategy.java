import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy {

    private double calcG(Point start, Point curr) {
        return Math.abs(start.getX() - curr.getX()) + Math.abs(start.getY() - curr.getY());
    }

    private double calcH(Point end, Point current) {
        return Math.abs(end.getX() - current.getX()) + Math.abs(end.getY() - current.getY());
    }

    private List<Point> findPath(Point curr, Point start, BiPredicate<Point, Point> withinReach, Point end){
        List<Point> path = new ArrayList<>();
        path.add(end);
        Point currPos = curr;
        while (!withinReach.test(currPos, start)) {
            if(currPos == start){
                Collections.reverse(path);
                return path;
            }
            path.add(currPos);
            currPos = currPos.getPrior();
        }
        path.add(currPos);
        Collections.reverse(path);
        return path;
    }

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        Comparator<Point> pointFComparator =  (p1, p2) -> {
            if (p1.getF() > p2.getF())
                return 1;
            else if (p1.getF() == p2.getF()) {
                if (p1.getG() > p2.getG())
                    return 1;
                if(p1.getG() == p2.getG()){
                    if(p1.getH() < p2.getH()){
                        return 1;
                    }
                    if (p1.getH() == p2.getH()){
                        return 0;
                    }
                }
            }
            return -1;
        };

        HashMap<Point, String> closedList = new HashMap<>();
        HashMap<Point, String> openList = new HashMap<>();
        PriorityQueue<Point> open = new PriorityQueue<>(pointFComparator);

        open.add(start);
        openList.put(start,start.toString());

        boolean done = false;
        Point curr;

        while (!done) {

            curr = open.peek();
            closedList.put(curr, curr.toString());

            openList.remove(curr);
            open.remove(curr);

            if (withinReach.test(curr, end)) {
                return findPath(curr, start, withinReach, end);
            }

            List<Point> neighbors = (potentialNeighbors.apply(curr)
                    .filter(pt -> !closedList.containsKey(pt))
                    .filter(canPassThrough)
                    .collect(Collectors.toList()));

            for (Point p : neighbors) {
                if(withinReach.test(p, end)){
                    p.setPrior(curr);
                    return findPath(p, start, withinReach, end);
                }
                if (!openList.containsKey(p)) {
                    p.setPrior(curr);
                    p.setH(this.calcH(end, p));
                    p.setG(this.calcG(start, p));
                    openList.put(p, p.toString());
                    open.add(p);

                } else {
                    if (calcG(start, p) < curr.getG()) {
                        p.setPrior(curr);
                        p.setG(this.calcG(start, p));
                        curr = p.getPrior();
                    }
                }
            }
            if (openList.isEmpty()) {
                return new LinkedList<>();
            }
        }
        return new LinkedList<>();
    }


    private static boolean neighbors(Point p1, Point p2)
    {
        return p1.getX()+1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX()-1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY()+1 == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY()-1 == p2.getY();
    }
}
