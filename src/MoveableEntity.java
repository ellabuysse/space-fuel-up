import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public abstract class MoveableEntity extends AnimatedEntity{

    public MoveableEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);
    }

    protected abstract boolean moveTrue(WorldModel world, Entity target, EventScheduler scheduler);
    public abstract Point nextPosition(WorldModel world, Point destPos);

    protected int getRepeatCount(){ return 0;}

    public boolean move(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            moveTrue(world, target, scheduler);
            return true;
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }
}
