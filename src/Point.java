public class Point
{
   private final int x;
   private final int y;
   public double g = 0;
   public double h = 0;
   public double f = g + h;
   public Point prior;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

   public int getX(){
      return x;
   }

   public int getY(){
      return y;
   }


   public void setG(double g){
      this.g = g;
   }
   public double getG(){
      return g;
   }
   public void setH(double h){
      this.h = h;
   }
   public double getH(){
      return h;
   }
   public void setF(double f){
      this.f = f;
   }
   public double getF(){
      return f;
   }
   public void setPrior(Point p){ this.prior = p; }
   public Point getPrior(){ return prior; }


   public boolean adjacent(Point p2)
   {
      return (this.getX() == p2.getX() && Math.abs(this.getY() - p2.getY()) == 1) || (this.getY() == p2.getY() && Math.abs(this.getX() - p2.getX()) == 1);
   }
}
