import processing.core.PImage;
import java.util.List;

public class Star extends ActiveEntity{

    public Star(String id, Point position, List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = getPosition();  // store current position before removing
        Astronaut astronaut = EntityFactory.createAstronaut(getId() + CRAB_ID_SUFFIX, pos, getActionPeriod() / CRAB_PERIOD_SCALE,
                CRAB_ANIMATION_MIN + rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
                imageStore.getImageList(CRAB_KEY));
        removeAndThenAdd(this, astronaut, world, scheduler, imageStore);
    }

}
