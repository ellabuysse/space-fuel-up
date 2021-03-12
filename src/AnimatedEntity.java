import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActiveEntity{

    private final int animationPeriod;

    public AnimatedEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        removeAndUnschedule(world, this, scheduler);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, ActionFactory.createAnimationAction(this, getRepeatCount()), getAnimationPeriod());
    }

    protected abstract int getRepeatCount();
}
