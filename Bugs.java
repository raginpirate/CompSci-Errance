import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
//Extends the class Entity to make sure it can fit inside the entityGrid, and has all of Entity's fields
public class Bugs extends Entity
{
  private double angle=2;
  private int maxLoops=0;
  private int count=0;
  private int animation=0;
  private int aniAdd=1;
  private BufferedImage ani1;
  private BufferedImage ani2;
  private BufferedImage ani3;
  
  public Bugs(String s, int a, int b)
  {
    //Upon creation all bugs are edible (used for the inventory)
    eat=true;
    //Sets the checkfail to be true
    boolean checkFail=true;
    //Buffers all the required images that this specific bug needs based off s
    image = Graphix.buffer(s + ".jpg");
    ani1 = Graphix.buffer(s + 1 + ".jpg");
    ani2 = Graphix.buffer(s + 2 + ".jpg");
    ani3 = Graphix.buffer(s + 3 + ".jpg");
    //It then places it's box at the supplied a b grid location
    box.x=a*64;
    box.y=b*64;
  }
  
  //Runs durring worldUpdate
  public void move()
  {
    //First it removes itself from the entityGrid so it does not collide with itself
    Crossing.entityGrid[(int)(box.x/64)][(int)(box.y/64)].remove(this);
    //It then increments a counter which is used to help make sure the bug's current mathematical function is at the next coordinate
    count++;
    //The mathematical function for a sin function rotated by angle "angle" around the orgin for x and y based on count
    box.y= box.y+(int)((count)*Math.cos(angle)-5*Math.sin(count)*Math.sin(angle));
    box.x= box.x+(int)((count)*Math.sin(angle)+5*Math.sin(count)*Math.cos(angle));
    //Then, we make sure our new position does not overlap anything else and maxLoop hasnt reached zero
    //(maxLoops is used to make sure the bug regularly changes direction, even if it doesnt collide with anything)
    if (maxLoops==0|| Crossing.checkColide(box))
    {
      //If maxLoops is at 0 or we are overlapping with something we will reverse our function
      box.y= box.y-(int)((count)*Math.cos(angle)-5*Math.sin(count)*Math.sin(angle));
      box.x= box.x-(int)((count)*Math.sin(angle)+5*Math.sin(count)*Math.cos(angle));
      //and then place our bug back onto the entityGrid at the correct location
      Crossing.entityGrid[box.x/64][box.y/64].add(this);
      //we then randomly generate a new maxLoops, angle, and set our mathematical function count to 0
      maxLoops=(int)(Math.random()*40);
      angle = angle+Math.PI/4+Math.random()*(Math.PI/2);
      count=0;
      animation=1;
    }
    else
    {
      //If our new box location passed all checks, then we add it back to the entityGrid at the right location 
      //and decrease maxLoops by one
      Crossing.entityGrid[box.x/64][box.y/64].add(this);
      maxLoops--;
      if (animation==2)
        aniAdd=-1;
      else if (animation==0)
        aniAdd=1;
      animation=animation+aniAdd;
    }
  }
  
  //The classes paint method
  public void paint(Graphics g)
  {
    //It simply looks at what stage in the animation cycle it is in and draws the appropriate image rotated by angle "amgle"
    Graphics2D g2d = (Graphics2D) g;
    AffineTransform rotate = AffineTransform.getRotateInstance(angle+Math.PI/2, image.getWidth()/2, image.getHeight()/2);
    AffineTransformOp translate = new AffineTransformOp(rotate, AffineTransformOp.TYPE_BILINEAR);
    if (animation==0)
    g2d.drawImage(translate.filter(ani1, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
    else if (animation==1)
    g2d.drawImage(translate.filter(ani2, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
    else
    g2d.drawImage(translate.filter(ani3, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
  }
}