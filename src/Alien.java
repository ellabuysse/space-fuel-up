
import processing.core.PImage;

import java.util.List;
import java.util.Optional;


public class Alien extends Octo{

    public Alien(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int resourceLimit, int resourceCount){
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit, resourceCount);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {


        Optional<Entity> notFullTarget;
        if(transform(world, scheduler, imageStore))
            notFullTarget = world.findNearest(getPosition(), Spaceship.class);
        else
            notFullTarget = world.findNearest(getPosition(), Star.class);

        if (notFullTarget.isPresent() && getPosition().equals(notFullTarget.get().getPosition())) {
            moveTrue(world, notFullTarget.get(), scheduler);
        }

        if(VirtualWorld.game){
            Point next = nextPosition(world, getPosition());
            world.moveEntity(this, next);
            scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), getActionPeriod());
//
        }
//
//            if(!move(world, notFullTarget.get(), scheduler)){
//                scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), getActionPeriod());
//            }
//        }
//        else{
//            notFullTarget = world.findNearest(getPosition(), Star.class);
//            if (!notFullTarget.isPresent() ||
//                    !move(world, notFullTarget.get(), scheduler) ||
//                    !transform(world, scheduler, imageStore))
//            {
//                scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), getActionPeriod());
//            }
//        }
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (getResourceCount() >= getResourceLimit())
        {
            return true;
        }
        return false;
    }

    protected boolean moveTrue(WorldModel world, Entity target, EventScheduler scheduler){
        setResourceCount(getResourceCount() + 1);
        removeAndUnschedule(world, target, scheduler);
        return true;
    }

     public Point nextPosition(WorldModel world, Point destPos) {
        VirtualWorld.starsNeeded = getResourceLimit();
         int horiz = VirtualWorld.dx;
         int vert = VirtualWorld.dy;
         Point newPos = new Point(getPosition().getX() + horiz, getPosition().getY() + VirtualWorld.dy);
         if(world.isOccupied(newPos)) {
             Optional<Entity> occupant = world.getOccupant(newPos);
             if (occupant.isPresent() && occupant.get().getClass() == Star.class) {
                 VirtualWorld.starCounter++;
             }
             else if (occupant.isPresent() && occupant.get().getClass() == Spaceship.class && VirtualWorld.starCounter >= getResourceLimit()) {
                 VirtualWorld.starCounter = 0;
                 VirtualWorld.game = false;
                 VirtualWorld.instructions = false;
                 VirtualWorld.gameOver_won = true;
             }
             else if (occupant.isPresent() && occupant.get().getClass() == Sgrass.class) {
                 VirtualWorld.starCounter--;
             }
             else if(occupant.isPresent() && occupant.get().getClass() == Astronaut.class){
                 VirtualWorld.starCounter = 0;
                 VirtualWorld.instructions = false;
                 VirtualWorld.game = false;
                 VirtualWorld.gameOver_lost = true;
             }
             else if(occupant.isPresent() && occupant.get().getClass() == Ufo.class){
                 VirtualWorld.starCounter = 0;
                 VirtualWorld.instructions = false;
                 VirtualWorld.game = false;
                 VirtualWorld.gameOver_lost = true;
             }
             else{
                 newPos = getPosition();
             }
         }
//         if (horiz == 0 || world.isOccupied(newPos)) {
//             newPos = new Point(getPosition().getX(), getPosition().getY() + vert);
//
//             if (vert == 0 || world.isOccupied(newPos)) {
//                 newPos = getPosition();
//             }
//         }
         VirtualWorld.dy = 0;
         VirtualWorld.dx = 0;
         return newPos;
     }

    public boolean move(WorldModel world, Entity target, EventScheduler scheduler) {
        return true;
//        if (getPosition().equals(target.getPosition())) {
//            moveTrue(world, target, scheduler);
//            return true;
//        }
//        else {
//            Point nextPos = nextPosition(world, getPosition());
//            if (!getPosition().equals(nextPos)) {
//                System.out.println(1);
//                Optional<Entity> occupant = world.getOccupant(nextPos);
//                if (occupant.isPresent()) {
//                    System.out.println();
//                    scheduler.unscheduleAllEvents(occupant.get());
//                }
//                world.moveEntity(this, nextPos);
//
//
//            }
//            return true;
//        }
    }

}
