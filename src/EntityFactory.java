import processing.core.PImage;

import java.util.List;

public class EntityFactory {
    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;

    public static Spaceship createSpaceship(String id, Point position,
                                        List<PImage> images)
    {
        return new Spaceship(id, position, images, 0);
    }

    public static OctoFull createOctoFull(String id, int resourceLimit,
                                        Point position, int actionPeriod, int animationPeriod,
                                        List<PImage> images)
    {
        return new OctoFull(id, position, images, actionPeriod, animationPeriod, resourceLimit);
    }

    public static OctoNotFull createOctoNotFull(String id, int resourceLimit,
                                           Point position, int actionPeriod, int animationPeriod,
                                           List<PImage> images)
    {
        return new OctoNotFull(id, position, images, actionPeriod, animationPeriod, resourceLimit, 0);
    }

    public static Alien createAlien(String id, int resourceLimit,
                                           Point position, int actionPeriod, int animationPeriod,
                                           List<PImage> images)
    {
        return new Alien(id, position, images, actionPeriod, animationPeriod, resourceLimit, 0);
    }

    public static Obstacle createObstacle(String id, Point position, List<PImage> images)
    {
        return new Obstacle(id, position, images);
    }

    public static Star createStar(String id, Point position, int actionPeriod,
                                    List<PImage> images)
    {
        return new Star(id, position, images, actionPeriod);
    }

    public static Astronaut createAstronaut(String id, Point position,
                                    int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Astronaut(id, position, images, actionPeriod, animationPeriod);
    }


    public static Ufo createUfo(String id, int resourceLimit,
                                          Point position, int actionPeriod, int animationPeriod,
                                          List<PImage> images)
    {
        return new Ufo(id, position, images, actionPeriod, animationPeriod, resourceLimit, 0);
    }

    public static FriendlyUfo createFriendlyUfo(String id, int resourceLimit,
                                Point position, int actionPeriod, int animationPeriod,
                                List<PImage> images)
    {
        return new FriendlyUfo(id, position, images, actionPeriod, animationPeriod, resourceLimit, 0);
    }

    public static Quake createQuake(Point position, List<PImage> images)
    {
        return new Quake(QUAKE_ID, position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    public static Sgrass createSgrass(String id, Point position, int actionPeriod,
                                      List<PImage> images)
    {
        return new Sgrass(id, position, images, actionPeriod);
    }
}
