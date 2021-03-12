import processing.core.PImage;
import java.util.List;

public class Quake extends AnimatedEntity{

    public Quake(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    protected int getRepeatCount(){
        return QUAKE_ANIMATION_REPEAT_COUNT;
    }
}
