import processing.core.PImage;

import java.util.List;

public abstract class ActiveEntity extends Entity{

    private final int actionPeriod;

    public ActiveEntity(String id, Point position, List<PImage> images, int actionPeriod)
    {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod() {
        return actionPeriod;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), getActionPeriod());
    }
    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    protected void addAndSchedule(ActiveEntity entity, WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        world.addEntity(entity);
        entity.scheduleActions(scheduler, world, imageStore);
    }

    protected void removeAndUnschedule(WorldModel world, Entity target, EventScheduler scheduler){
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }

    protected void removeAndThenAdd(Entity oldEntity, ActiveEntity newEntity, WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        removeAndUnschedule(world, oldEntity, scheduler);
        addAndSchedule(newEntity, world, scheduler, imageStore);
    }
}
