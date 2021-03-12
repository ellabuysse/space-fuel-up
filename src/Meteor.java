//import processing.core.PImage;
//
//import java.util.List;
//import java.util.Optional;
//
//public class Meteor extends MoveableEntity{
//
//    public static final String METEOR_KEY = "meteor";
//
//    public Meteor(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
//    {
//        super(id, position, images, actionPeriod, animationPeriod);
//    }
//
//    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
//    {
//        //find a way to make single version of octo- just one instance available everywhere
//        //then use that to check if it is still moving
//        //if true= quake- game over
////        Optional<Entity> crabTarget = world.findNearest(getPosition(), Star.class);
////        long nextPeriod = getActionPeriod();
//        if(VirtualWorld.mousePressed){
//            Point pos = new Point(VirtualWorld.clickX, VirtualWorld.clickY);
//            Meteor meteor = EntityFactory.createMeteor(getId(), pos, getActionPeriod(), 50, imageStore.getImageList(METEOR_KEY));
//            addAndSchedule(meteor, world, scheduler, imageStore);
//
//            Optional<Entity> meteorTarget = world.findNearest(getPosition(), Ufo.class);
//            long nextPeriod = getActionPeriod();
//
//            if (meteorTarget.isPresent())
//            {
//                Point tgtPos = meteorTarget.get().getPosition();
//
//                if (move(world, meteorTarget.get(), scheduler))
//                {
//                    Quake quake = EntityFactory.createQuake(tgtPos, imageStore.getImageList(QUAKE_KEY));
//                    nextPeriod += getActionPeriod();
//                    addAndSchedule(quake, world, scheduler, imageStore);
//                }
//            }
//
//            scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), nextPeriod);
//
//
//        }
//
//
//
//
//
//    }
//
//    public Point nextPosition(WorldModel world, Point destPos)
//    {
//        int horiz = Integer.signum(destPos.getX() - getPosition().getX());
//        Point newPos = new Point(getPosition().getX() + horiz, getPosition().getY());
//
//        Optional<Entity> occupant = world.getOccupant(newPos);
//
//        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass() == Star.class)))
//        {
//            int vert = Integer.signum(destPos.getY() - getPosition().getY());
//            newPos = new Point(getPosition().getX(), getPosition().getY() + vert);
//
//            occupant = world.getOccupant(newPos);
//
//            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass() == Star.class)))
//            {
//                newPos = getPosition();
//            }
//        }
//        return newPos;
//    }
//
//    protected boolean moveTrue(WorldModel world, Entity target, EventScheduler scheduler){
//        removeAndUnschedule(world, target, scheduler);
//        return true;
//    }
//
//}
