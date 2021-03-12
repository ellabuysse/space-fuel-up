import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

        import java.util.LinkedList;
        import java.util.List;
        import java.util.Optional;
//was crab
public class FriendlyUfo extends Octo{

    private PathingStrategy strategy = new AStarPathingStrategy();

    public FriendlyUfo(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int resourceLimit, int resourceCount)
    {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit, resourceCount);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        if(VirtualWorld.mousePressed) {
            Optional<Entity> ufoTarget = world.findNearest(getPosition(), Ufo.class);
            long nextPeriod = getActionPeriod();

            if (ufoTarget.isPresent()) {
                Point tgtPos = ufoTarget.get().getPosition();
                Point next = nextPosition(world, tgtPos);
                System.out.println(next);
                world.moveEntity(this, next);

                //           if (move(world, ufoTarget.get(), scheduler))
                //           {
                //               Quake quake = EntityFactory.createQuake(tgtPos, imageStore.getImageList(QUAKE_KEY));
                //               nextPeriod += getActionPeriod();
                //               addAndSchedule(quake, world, scheduler, imageStore);
                //           }
                //       }
                //
                scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), nextPeriod);
            }
        }
    }

    public Point nextPosition(WorldModel world, Point destPos)
    {
        List<Point> path;
        path = plotPath(this.getPosition(), destPos, world);

        Point newPos = path.get(0);
        return newPos;

        //    int horiz = Integer.signum(destPos.getX() - getPosition().getX());
        //    Point newPos = new Point(getPosition().getX() + horiz, getPosition().getY());

        //    Optional<Entity> occupant = world.getOccupant(newPos);

        //    if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass() == Star.class)))
        //    {
        //        int vert = Integer.signum(destPos.getY() - getPosition().getY());
        //        newPos = new Point(getPosition().getX(), getPosition().getY() + vert);

        //        occupant = world.getOccupant(newPos);

        //        if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass() == Star.class)))
        //        {
        //            newPos = getPosition();
        //        }
        //    }
        //    return newPos;
    }

    public boolean withinBounds(Point p, WorldModel world){
        return p.getY() >= 0 && p.getY() < world.getNumCols() &&
                p.getX() >= 0 && p.getX() < world.getNumRows();
    }

    public List<Point> plotPath(Point pos, Point goal, WorldModel world){
        List<Point> path = new LinkedList<>();
        path.addAll(strategy.computePath(pos, goal,
                p -> withinBounds(p, world) && !world.getOccupant(p).isPresent(),
                (p1, p2) -> neighbors(p1, p2),
                PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS));

        return path;
    }

    private static boolean neighbors(Point p1, Point p2)
    {
        return p1.getX()+1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX()-1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY()+1 == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY()-1 == p2.getY();
    }

    protected boolean moveTrue(WorldModel world, Entity target, EventScheduler scheduler){
        removeAndUnschedule(world, target, scheduler);
        return true;
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (getResourceCount() >= getResourceLimit())
        {
            return true;
        }
        return false;
    }

}

