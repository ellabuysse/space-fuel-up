public class Animation extends Action{

    private final AnimatedEntity entity;

    public Animation(AnimatedEntity entity, WorldModel world, ImageStore imageStore, int repeatCount)
    {
        super(world, imageStore, repeatCount);
        this.entity = entity;
    }

    public void executeAction(EventScheduler scheduler)
    {
        entity.nextImage();

        if (getRepeatCount() != 1)
        {
            scheduler.scheduleEvent(entity, ActionFactory.createAnimationAction(entity, Math.max(getRepeatCount() - 1, 0)),
                    entity.getAnimationPeriod());
        }
    }
}
