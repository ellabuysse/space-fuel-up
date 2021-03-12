/*
Action: ideally what our various entities might do in our virutal world
 */

public abstract class Action
{
   private final WorldModel world;
   private final ImageStore imageStore;
   private final int repeatCount;

   public Action(WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }

   public WorldModel getWorld() {
      return world;
   }
   public ImageStore getImageStore() {
      return imageStore;
   }
   public int getRepeatCount() {
      return repeatCount;
   }
   public abstract void executeAction(EventScheduler scheduler);
//   private final ActionKind kind;
//   private final Entity entity;
//      this.kind = kind;
//      this.entity = entity;
   //   public ActiveEntity getEntity() {
//      return entity;
//   }
//   {
//      switch (kind)
//      {
//         case ACTIVITY:
//            executeActivityAction(scheduler);
//            break;
//
//         case ANIMATION:
//            executeAnimationAction(scheduler);
//            break;
//      }
//   }

//   public void executeActivityAction(EventScheduler scheduler)
//   {
//      if(entity.getClass() != Obstacle.class)
//         ((ActiveEntity) entity).executeActivity(world, imageStore, scheduler);
//      switch (entity.getKind())
//      {
//         case OCTO_FULL:
//            entity.executeOctoFullActivity(world, imageStore, scheduler);
//            break;

//         case OCTO_NOT_FULL:
//            entity.executeOctoNotFullActivity(world, imageStore, scheduler);
//            break;

//         case FISH:
//            entity.executeFishActivity(world, imageStore, scheduler);
//            break;

//         case CRAB:
//            entity.executeCrabActivity(world, imageStore, scheduler);
//            break;

//         case QUAKE:
//            entity.executeQuakeActivity(world, imageStore, scheduler);
//            break;

//         case SGRASS:
//            entity.executeSgrassActivity(world, imageStore, scheduler);
//            break;

//         case ATLANTIS:
//            entity.executeAtlantisActivity(world, imageStore, scheduler);
//            break;

//         default:
//            throw new UnsupportedOperationException(
//                    String.format("executeActivityAction not supported for %s", entity.getKind()));
//      }
//   }

//   private void executeAnimationAction(EventScheduler scheduler)
//   {
//      entity.nextImage();
//
//      if (repeatCount != 1)
//      {
//         if(entity instanceof AnimatedEntity)
//         {
//            scheduler.scheduleEvent(entity, entity.createAnimationAction(Math.max(repeatCount - 1, 0)),
//                    ((AnimatedEntity) entity).getAnimationPeriod());
//         }
//         else{
//            scheduler.scheduleEvent(entity, entity.createAnimationAction(Math.max(repeatCount - 1, 0)),
//                    0);
//         }
//      }
//   }
}
