import processing.core.PImage;
import java.util.List;
//was atlantis
public class Spaceship extends AnimatedEntity{

    public Spaceship(String id, Point position, List<PImage> images, int animationPeriod)
    {
        super(id, position, images, 0, animationPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this, ActionFactory.createAnimationAction(this, getRepeatCount()), 0);
    }

    protected int getRepeatCount(){return ATLANTIS_ANIMATION_REPEAT_COUNT;}
}
