import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class Sgrass extends ActiveEntity{

    public Sgrass(String id, Point position, List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(getPosition());
        if (openPt.isPresent())
        {
            Star star = EntityFactory.createStar(FISH_ID_PREFIX + getId(),
                    openPt.get(), FISH_CORRUPT_MIN + rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                    imageStore.getImageList(FISH_KEY));
            addAndSchedule(star, world, scheduler, imageStore);
        }
        scheduler.scheduleEvent(this, ActionFactory.createActivityAction(this, world, imageStore), getActionPeriod());
    }
}
