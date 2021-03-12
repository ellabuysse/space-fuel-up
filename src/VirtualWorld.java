import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import processing.core.*;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class  VirtualWorld extends PApplet
{
   public static final int TIMER_ACTION_PERIOD = 100;

   public static final int VIEW_WIDTH = 640;
   public static final int VIEW_HEIGHT = 448;
   public static final int TILE_WIDTH = 32;
   public static final int TILE_HEIGHT = 32;
   public static final int WORLD_WIDTH_SCALE = 2;
   public static final int WORLD_HEIGHT_SCALE = 2;

   public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   public static final int WORLD_COLS = VIEW_COLS;
   public static final int WORLD_ROWS = VIEW_ROWS;

   public static final String IMAGE_LIST_FILE_NAME = "src/imagelist";
   public static final String DEFAULT_IMAGE_NAME = "background_default";
   public static final int DEFAULT_IMAGE_COLOR = 0x808080;

   public static final String LOAD_FILE_NAME = "src/world.sav";

   public static final String FAST_FLAG = "-fast";
   public static final String FASTER_FLAG = "-faster";
   public static final String FASTEST_FLAG = "-fastest";
   public static final double FAST_SCALE = 0.5;
   public static final double FASTER_SCALE = 0.25;
   public static final double FASTEST_SCALE = 0.10;

   public static double timeScale = 1.0;

   public ImageStore imageStore;
   public WorldModel world;
   public WorldView view;
   public EventScheduler scheduler;

   public static int dx =0;
   public static int dy = 0;
   public static int step = 0;
   public static int clickX = 0;
   public static int clickY = 0;
   public static boolean mousePressed = false;
   public static int starCounter = 0;
   public static int starsNeeded = 10;

   public long next_time;
   private boolean start = true;
   public static boolean game = false;
   public static boolean instructions = false;
   public static boolean gameOver_lost = false;
   public static boolean gameOver_won = false;
   private int space_bar_count = 0;






   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

//      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;

      }

   public void draw()
   {
         //load with fully background image

      if (gameOver_lost) {
         space_bar_count = 0;
         clear();
         background(0, 0, 0);
         textSize(20);
         text("Game over! You lose. ", 230, 180);
         textSize(15);
         text("Better luck next time!", 250, 210);
         text("Press space bar to try again", 235, 240);
         text("Or press e to exit", 260, 270);
         gameOver_lost = false;
         mousePressed = false;
         setup();
      }

      if (gameOver_won) {
         space_bar_count = 0;
         clear();
         background(199,21,133);
         textSize(20);
         text("Congrats! You won!", 236, 190);
         textSize(15);
         text("Press space bar to try again", 235, 220);
         text("Or press e to exit", 255, 250);
         gameOver_won = false;
         mousePressed = false;
         setup();
      }
      
      if (start) {
         textSize(30);
         background(0,26,51);
         text("Welcome to Space Fuel-Up!", 120, 200);
         textSize(20);
         fill(225,221,51);
         text("Press the space bar to get started.", 150, 250);
         start = false;
      }
      if (instructions) {
         clear();
         background(0, 26, 51);
         fill(225, 225, 225);
         textSize(20);
         text("Instructions", 265, 50);
         textSize(15);
         text("You are an alien in space who must beat the astronauts to the stars!", 50, 100);
         text("Your mission: collect 10 stars and return to the spaceship.", 50, 130);
         text("Don't hit the meteors or the astronauts!", 50, 160);
         text("Don't let the ufo hit you!", 50, 190);
         text("Navigate with your arrow keys.", 50, 220);
         fill(225,221,51);
         text("Press the space bar to begin.", 50, 260);
         }

      if (game) {
            long time = System.currentTimeMillis();
            scheduler.updateOnTime(time);
//            if (time >= next_time) {
//               scheduler.updateOnTime(time);
//               next_time = time + TIMER_ACTION_PERIOD;
//
//            }

            view.drawViewport();
            textSize(15);
            fill(225, 225, 225);
            text("stars collected: " + starCounter + " / " + starsNeeded, 460, 20);
         }
      

   }

   public void mousePressed(){
      if(game) {
         clickX = mouseX;
         clickY = mouseY;
         mousePressed = true;
         step = 0;
         starCounter = starCounter - 3;
         view.drawViewport();
      }
   }



   public void keyPressed()
   {
      switch(key){
         case ' ':
            space_bar_count++;
            if(space_bar_count == 1){
               instructions = true;

            }
            if(space_bar_count == 2){
               game = true;
            }
            break;
         case 'e':
            exit();
            break;

      }
      if (key == CODED)
      {
         dy = 0;
         dx = 0;
         if(step == 8){
            mousePressed = false;
         }
         switch (keyCode)
         {
            case UP:
               dy = -1;
               dx = 0;
               if(mousePressed)
               step++;
               break;
            case DOWN:
               dy = 1;
               dx = 0;
               if(mousePressed)
               step++;
               break;
            case LEFT:
               dx = -1;
               dy = 0;
               if(mousePressed)
               step++;
               break;
            case RIGHT:
               dx = 1;
               dy = 0;
               if(mousePressed)
               step++;
               break;
         }
          view.drawViewport();
      }
   }

   public static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
         imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   public static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void loadWorld(WorldModel world, String filename, ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         world.load(in, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.getEntities())
      {
         //Only start actions for entities that include action (not those with just animations)
         if(entity instanceof ActiveEntity)
         {
            //typecasting!
            if (((ActiveEntity) entity).getActionPeriod() > 0)
               ((ActiveEntity) entity).scheduleActions(scheduler, world, imageStore);
         }
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
