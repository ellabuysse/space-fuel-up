public class Activity extends Action{

    private final ActiveEntity entity;

    public Activity(ActiveEntity entity, WorldModel world, ImageStore imageStore, int repeatCount)
    {
        super(world, imageStore, repeatCount);
        this.entity = entity;
    }

    public void executeAction(EventScheduler scheduler) {
        entity.executeActivity(getWorld(), getImageStore(), scheduler);
    }
}
