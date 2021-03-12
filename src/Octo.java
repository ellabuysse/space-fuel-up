import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public abstract class Octo extends MoveableEntity {

    private int resourceLimit;
    private int resourceCount;

    public Octo(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public abstract boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore);

    public int getResourceLimit() {
        return resourceLimit;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(int num) {
        resourceCount = num;
    }

    public void setResourceLimit(int num){
        resourceLimit = num;
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.getX() - getPosition().getX());
        Point newPos = new Point(getPosition().getX() + horiz, getPosition().getY());

        if (horiz == 0 || (world.isOccupied(newPos) && world.getOccupant(newPos).isPresent()
                && world.getOccupant(newPos).get().getClass() != Star.class)) {
//                    Optional<Entity> occupant = world.getOccupant(newPos);
//                    if(occupant.isPresent() && occupant.get().getClass() == Alien.class){
//                        VirtualWorld.instructions = false;
//                        VirtualWorld.game = false;
//                        VirtualWorld.gameOver_lost = true;
//                    }
                    int vert = Integer.signum(destPos.getY() - getPosition().getY());
                    newPos = new Point(getPosition().getX(), getPosition().getY() + vert);
                    if (vert == 0 || (world.isOccupied(newPos) && world.getOccupant(newPos).isPresent()
                            && world.getOccupant(newPos).get().getClass() != Star.class)) {
//                        occupant = world.getOccupant(newPos);
//                        if(occupant.isPresent() && occupant.get().getClass() == Alien.class){
//                            VirtualWorld.instructions = false;
//                            VirtualWorld.game = false;
//                            VirtualWorld.gameOver_lost = true;
//                        }
                        newPos = getPosition();
                    }
                }
        return newPos;
    }


}
