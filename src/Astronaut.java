import processing.core.PImage;
import java.util.List;
import java.util.Optional;
//was crab
public class Astronaut extends MoveableEntity{

    public Astronaut(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        //find a way to make single version of octo- just one instance available everywhere
        //then use that to check if it is still moving
        //if true= quake- game over
//        Optional<Entity> crabTarget = world.findNearest(getPosition(), Star.class);
//        long nextPeriod = getActionPeriod();

        Optional<Entity> crabTarget = world.findNearest(getPosition(), Sgrass.class);
        long nextPeriod = getActionPeriod();

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get().getPosition();

            if (move(world, crabTarget.get(), scheduler))
            {
                Quake quake = EntityFactory.createQuake(tgtPos, imageStore.getImageList(QUAKE_KEY));
                nextPeriod += getActionPeriod();
                addAndSchedule(quake, world, scheduler, imageStore);
            }
            scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), nextPeriod);

        }
        else{
            VirtualWorld.starCounter = 0;
            VirtualWorld.instructions = false;
            VirtualWorld.game = false;
            VirtualWorld.gameOver_lost = true;
        }



//
//        while (!move(world, crabTarget.get(), scheduler))
//        {
//            Point tgtPos = crabTarget.get().getPosition();
//            move(world, crabTarget.get(), scheduler);
//            if (move(world, crabTarget.get(), scheduler))
//            {
//                Quake quake = EntityFactory.createQuake(tgtPos, imageStore.getImageList(QUAKE_KEY));
//                nextPeriod += getActionPeriod();
//                addAndSchedule(quake, world, scheduler, imageStore);
//            }
//        }
//
//        scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), nextPeriod);
    }

    public Point nextPosition(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - getPosition().getX());
        Point newPos = new Point(getPosition().getX() + horiz, getPosition().getY());

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass() == Star.class)))
        {
            int vert = Integer.signum(destPos.getY() - getPosition().getY());
            newPos = new Point(getPosition().getX(), getPosition().getY() + vert);

            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass() == Star.class)))
            {
                newPos = getPosition();
            }
        }
        return newPos;
    }

    protected boolean moveTrue(WorldModel world, Entity target, EventScheduler scheduler){
        removeAndUnschedule(world, target, scheduler);
        return true;
    }

}
