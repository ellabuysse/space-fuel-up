public class ActionFactory {

    public static Action createAnimationAction(AnimatedEntity entity, int repeatCount)
    {
        return new Animation(entity, null, null, repeatCount);
    }

    public static Action createActivityAction(ActiveEntity entity, WorldModel world, ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore, 0);
    }
}
