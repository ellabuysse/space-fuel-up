import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class OctoNotFull extends Octo{

    public OctoNotFull(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod , int resourceLimit, int resourceCount)
    {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit, resourceCount);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(getPosition(), Alien.class);

        if (!notFullTarget.isPresent() ||
                !move(world, notFullTarget.get(), scheduler) ||
                !transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), getActionPeriod());
        }
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (getResourceCount() >= getResourceLimit())
        {
            OctoFull octo = EntityFactory.createOctoFull(getId(), getResourceLimit(), getPosition(), getActionPeriod(), getAnimationPeriod(), getImages());
            removeAndThenAdd(this, octo, world, scheduler, imageStore);
            return true;
        }
        return false;
    }

    protected boolean moveTrue(WorldModel world, Entity target, EventScheduler scheduler){
        setResourceCount(getResourceCount() + 1);
        removeAndUnschedule(world, target, scheduler);
        return true;
    }
}
