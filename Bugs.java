import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
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
  private int boundary;
  
  public Bugs(String s, int a, int b, int boundary)
  {
    eat=true;
    boolean checkFail=true;
    image = Graphix.buffer(s + ".jpg");
    ani1 = Graphix.buffer(s + 1 + ".jpg");
    ani2 = Graphix.buffer(s + 2 + ".jpg");
    ani3 = Graphix.buffer(s + 3 + ".jpg");
    this.boundary = boundary;
    box.x=a*64;
    box.y=b*64;
  }
  
  public void move()
  {
    Crossing.bugs[box.x/64][box.y/64].remove(this);
    count++;
    box.y= box.y+(int)((count)*Math.cos(angle)-5*Math.sin(count)*Math.sin(angle));
    box.x= box.x+(int)((count)*Math.sin(angle)+5*Math.sin(count)*Math.cos(angle));
    if (maxLoops==0 || collision())
    {
      box.y= box.y-(int)((count)*Math.cos(angle)-5*Math.sin(count)*Math.sin(angle));
      box.x= box.x-(int)((count)*Math.sin(angle)+5*Math.sin(count)*Math.cos(angle));
      Crossing.bugs[box.x/64][box.y/64].add(this);
      maxLoops=(int)(Math.random()*40);
      angle = angle+Math.PI/4+Math.random()*(Math.PI/2);
      count=0;
      animation=1;
    }
    else
    {
      Crossing.bugs[box.x/64][box.y/64].add(this);
      maxLoops--;
      if (animation==2)
        aniAdd=-1;
      else if (animation==0)
        aniAdd=1;
      animation=animation+aniAdd;
    }
  }
  
  public boolean collision()
  {
    if (!(Crossing.npcBoundaries[boundary].intersects(box)) || Crossing.holes[box.x/64][box.y/64] ||  Crossing.holes[box.x/64+1][box.y/64] || Crossing.holes[box.x/64][box.y/64+1] || Crossing.holes[box.x/64+1][box.y/64+1])
      return true;
    for (Rectangle x:Crossing.worldWalls)
    {
      if (x.intersects(box))
        return true;
    }
    return false;
  }
  
  public void paint(Graphics g)
  {
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