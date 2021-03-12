import processing.core.PImage;

import java.util.*;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

public class WorldModel
{
   private final int numRows;
   private final int numCols;
   private final Background background[][];
   private final Entity occupancy[][];
   private final Set<Entity> entities;

   public static final int PROPERTY_KEY = 0;

   public static final String BGND_KEY = "background";
   public static final int BGND_NUM_PROPERTIES = 4;
   public static final int BGND_ID = 1;
   public static final int BGND_COL = 2;
   public static final int BGND_ROW = 3;

   public static final String FRIENDLY_KEY = "friendlyUfo";

   public static final String OCTO_KEY = "octo";
   public static final int OCTO_NUM_PROPERTIES = 7;
   public static final int OCTO_ID = 1;
   public static final int OCTO_COL = 2;
   public static final int OCTO_ROW = 3;
   public static final int OCTO_LIMIT = 4;
   public static final int OCTO_ACTION_PERIOD = 5;
   public static final int OCTO_ANIMATION_PERIOD = 6;

   public static final String ALIEN_KEY = "alien";
   public static final int ALIEN_NUM_PROPERTIES = 7;
   public static final int ALIEN_ID = 1;
   public static final int ALIEN_COL = 2;
   public static final int ALIEN_ROW = 3;
   public static final int ALIEN_LIMIT = 4;
   public static final int ALIEN_ACTION_PERIOD = 5;
   public static final int ALIEN_ANIMATION_PERIOD = 6;

   public static final String OBSTACLE_KEY = "obstacle";
   public static final int OBSTACLE_NUM_PROPERTIES = 4;
   public static final int OBSTACLE_ID = 1;
   public static final int OBSTACLE_COL = 2;
   public static final int OBSTACLE_ROW = 3;

   public static final String FISH_KEY = "fish";
   public static final int FISH_NUM_PROPERTIES = 5;
   public static final int FISH_ID = 1;
   public static final int FISH_COL = 2;
   public static final int FISH_ROW = 3;
   public static final int FISH_ACTION_PERIOD = 4;
   public static final int FISH_REACH = 1;

   public static final String ATLANTIS_KEY = "atlantis";
   public static final int ATLANTIS_NUM_PROPERTIES = 4;
   public static final int ATLANTIS_ID = 1;
   public static final int ATLANTIS_COL = 2;
   public static final int ATLANTIS_ROW = 3;

   public static final String SGRASS_KEY = "seaGrass";
   public static final int SGRASS_NUM_PROPERTIES = 5;
   public static final int SGRASS_ID = 1;
   public static final int SGRASS_COL = 2;
   public static final int SGRASS_ROW = 3;
   public static final int SGRASS_ACTION_PERIOD = 4;

   public static final String METEOR_KEY = "meteor";


   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public int getNumRows() {
      return numRows;
   }

   public int getNumCols() {
      return numCols;
   }

   public Set<Entity> getEntities() {
      return entities;
   }

   public void load(Scanner in, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), imageStore))
               System.err.println(String.format("invalid entry on line %d", lineNumber));
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d", lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s", lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   public void addEntity(Entity entity)
   {
      if (withinBounds(entity.getPosition()))
      {
         setOccupancyCell(entity.getPosition(), entity);
         entities.add(entity);
      }
   }

   public void moveEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (withinBounds(pos) && !pos.equals(oldPos))
      {
         setOccupancyCell(oldPos, null);
         removeEntityAt(pos);
         setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }

   public void removeEntity(Entity entity) { removeEntityAt(entity.getPosition()); }

   public Optional<Entity> findNearest(Point pos, Class kind)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : entities)
      {
         if (entity.getClass() == kind)
            ofType.add(entity);
      }
      return nearestEntity(ofType, pos);
   }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (withinBounds(pos))
         return Optional.of(getBackgroundCell(pos).getCurrentImage());
      else
         return Optional.empty();
   }

   public Optional<Entity> getOccupant(Point pos)
   {
      if (isOccupied(pos))
         return Optional.of(getOccupancyCell(pos));
      else
         return Optional.empty();
   }

   public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -FISH_REACH; dy <= FISH_REACH; dy++)
      {
         for (int dx = -FISH_REACH; dx <= FISH_REACH; dx++)
         {
            Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
            if (withinBounds(newPt) && !isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public boolean isOccupied(Point pos) { return withinBounds(pos) && getOccupancyCell(pos) != null; }

   private boolean processLine(String line, ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return parseBackground(properties, imageStore);
            case OCTO_KEY:
               return parseUfo(properties, imageStore);
            case OBSTACLE_KEY:
               return parseObstacle(properties, imageStore);
            case FISH_KEY:
               return parseFish(properties, imageStore);
            case ATLANTIS_KEY:
               return parseAtlantis(properties, imageStore);
            case SGRASS_KEY:
               return parseSgrass(properties, imageStore);
            case ALIEN_KEY:
               return parseAlien(properties, imageStore);
//            case FRIENDLY_KEY:
//               return parseFriendlyOcto(properties, imageStore) ;
         }
      }
      return false;
   }

   private boolean parseBackground(String [] properties, ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]), Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         setBackground(pt,new Background(id, imageStore.getImageList(id)));
      }
      return properties.length == BGND_NUM_PROPERTIES;
   }


//   private boolean parseFriendlyOcto(String [] properties, ImageStore imageStore) {
//      if (properties.length == OCTO_NUM_PROPERTIES) {
//         Point pt = new Point(Integer.parseInt(properties[OCTO_COL]), Integer.parseInt(properties[OCTO_ROW]));
//         FriendlyUfo entity = EntityFactory.createFriendlyUfo(properties[OCTO_ID], Integer.parseInt(properties[OCTO_LIMIT]),
//                 pt, Integer.parseInt(properties[OCTO_ACTION_PERIOD]),
//                 Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]),
//                 imageStore.getImageList(FRIENDLY_KEY));
//         tryAddEntity(entity);
//      }
//      return properties.length == OCTO_NUM_PROPERTIES;
//   }

   private boolean parseOcto(String [] properties, ImageStore imageStore)
   {
      if (properties.length == OCTO_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[OCTO_COL]), Integer.parseInt(properties[OCTO_ROW]));
         OctoNotFull entity = EntityFactory.createOctoNotFull(properties[OCTO_ID], Integer.parseInt(properties[OCTO_LIMIT]),
                 pt, Integer.parseInt(properties[OCTO_ACTION_PERIOD]),
                 Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]),
                 imageStore.getImageList(OCTO_KEY));
         tryAddEntity(entity);
      }

      return properties.length == OCTO_NUM_PROPERTIES;
   }

   private boolean parseAlien(String [] properties, ImageStore imageStore)
   {
      if (properties.length == OCTO_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[ALIEN_COL]), Integer.parseInt(properties[ALIEN_ROW]));
         Alien entity = EntityFactory.createAlien(properties[ALIEN_ID], Integer.parseInt(properties[ALIEN_LIMIT]),
                 pt, Integer.parseInt(properties[ALIEN_ACTION_PERIOD]),
                 Integer.parseInt(properties[ALIEN_ANIMATION_PERIOD]),
                 imageStore.getImageList(ALIEN_KEY));
         tryAddEntity(entity);
      }
      return properties.length == ALIEN_NUM_PROPERTIES;
   }

   private boolean parseObstacle(String [] properties, ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[OBSTACLE_COL]), Integer.parseInt(properties[OBSTACLE_ROW]));
         Obstacle entity = EntityFactory.createObstacle(properties[OBSTACLE_ID], pt, imageStore.getImageList(OBSTACLE_KEY));
         tryAddEntity(entity);
      }
      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   private boolean parseFish(String [] properties, ImageStore imageStore)
   {
      if (properties.length == FISH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[FISH_COL]), Integer.parseInt(properties[FISH_ROW]));
         Star entity = EntityFactory.createStar(properties[FISH_ID], pt, Integer.parseInt(properties[FISH_ACTION_PERIOD]),
                                             imageStore.getImageList(FISH_KEY));
         tryAddEntity(entity);
      }
      return properties.length == FISH_NUM_PROPERTIES;
   }

   private boolean parseAtlantis(String [] properties, ImageStore imageStore)
   {
      if (properties.length == ATLANTIS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]), Integer.parseInt(properties[ATLANTIS_ROW]));
         Spaceship entity = EntityFactory.createSpaceship(properties[ATLANTIS_ID], pt, imageStore.getImageList(ATLANTIS_KEY));
         tryAddEntity(entity);
      }
      return properties.length == ATLANTIS_NUM_PROPERTIES;
   }

   private boolean parseSgrass(String [] properties, ImageStore imageStore)
   {
      if (properties.length == SGRASS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]), Integer.parseInt(properties[SGRASS_ROW]));
         Sgrass entity = EntityFactory.createSgrass(properties[SGRASS_ID], pt,
                                               Integer.parseInt(properties[SGRASS_ACTION_PERIOD]),
                                               imageStore.getImageList(SGRASS_KEY));
         tryAddEntity(entity);
      }
      return properties.length == SGRASS_NUM_PROPERTIES;
   }

   private void tryAddEntity(Entity entity)
   {
      if (isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }
      addEntity(entity);
   }

   private Optional<Entity> nearestEntity(List<Entity> entities, Point pos)
   {
      if (entities.isEmpty())
         return Optional.empty();
      else
      {
         Entity nearest = entities.get(0);
         int nearestDistance = distanceSquared(nearest.getPosition(), pos);

         for (Entity other : entities)
         {
            int otherDistance = distanceSquared(other.getPosition(), pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }

   private void removeEntityAt(Point pos)
   {
      if (withinBounds(pos) && getOccupancyCell(pos) != null)
      {
         Entity entity = getOccupancyCell(pos);
         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         entities.remove(entity);
         setOccupancyCell(pos, null);
      }
   }

   private void setBackground(Point pos, Background background)
   {
      if (withinBounds(pos))
         setBackgroundCell(pos, background);
   }

   private Entity getOccupancyCell(Point pos) { return occupancy[pos.getY()][pos.getX()]; }
   private Background getBackgroundCell(Point pos) { return background[pos.getY()][pos.getX()]; }
   private void setOccupancyCell(Point pos, Entity entity) { this.occupancy[pos.getY()][pos.getX()] = entity; }
   private void setBackgroundCell(Point pos, Background background)
   {
      this.background[pos.getY()][pos.getX()] = background;
   }

   private static int distanceSquared(Point p1, Point p2)
   {
      int deltaX = p1.getX() - p2.getX();
      int deltaY = p1.getY() - p2.getY();
      return deltaX * deltaX + deltaY * deltaY;
   }

   private boolean withinBounds(Point pos)
   {
      return pos.getY() >= 0 && pos.getY() < numRows && pos.getX() >= 0 && pos.getX() < numCols;
   }

   private boolean parseUfo(String [] properties, ImageStore imageStore)
   {
      if (properties.length == OCTO_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[OCTO_COL]), Integer.parseInt(properties[OCTO_ROW]));
         Ufo entity = EntityFactory.createUfo(properties[OCTO_ID], Integer.parseInt(properties[OCTO_LIMIT]),
                 pt, Integer.parseInt(properties[OCTO_ACTION_PERIOD]),
                 Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]),
                 imageStore.getImageList(OCTO_KEY));
         tryAddEntity(entity);
      }

      return properties.length == OCTO_NUM_PROPERTIES;
   }
}
