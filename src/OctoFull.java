import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class OctoFull extends Octo{

    public OctoFull(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int resourceLimit)
    {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit, resourceLimit);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(getPosition(), Spaceship.class);

        if (fullTarget.isPresent() && move(world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation
            ((ActiveEntity) fullTarget.get()).scheduleActions(scheduler, world, imageStore);

            //transform to unfull
            transform(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), getActionPeriod());
        }
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        OctoNotFull octo = EntityFactory.createOctoNotFull(getId(), getResourceLimit(), getPosition(), getActionPeriod(), getAnimationPeriod(), getImages());
        removeAndThenAdd(this, octo, world, scheduler, imageStore);
        return true;
    }

    protected boolean moveTrue(WorldModel world, Entity target, EventScheduler scheduler){
        return true;
    }
}
