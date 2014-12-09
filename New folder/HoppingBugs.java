import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
public class HoppingBugs extends Entity
{
  private double newAngle=2;
  private double angle=2;
  private int count=0;
  private int animation=0;
  private BufferedImage ani1;
  private BufferedImage ani2;
  private BufferedImage ani3;
  private int boundary;
  private boolean collide=true;
  private int waitCount=0;
  private double rotation=0;
  
  public HoppingBugs(String s, int a, int b, int boundary)
  {
    eat=true;
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
    if (waitCount>10)
    {
      waitCount--;
    }
    else if (waitCount>0)
    {
      waitCount--;
      angle=angle+rotation;
    }
    else
    {
      count--;
      if (collide)
      {
        Crossing.bugs[box.x/64][box.y/64].remove(this);
        box.x= (int)(box.x+8*Math.cos(angle));
        box.y= (int)(box.y+8*Math.sin(angle));
        if (collision())
        {
          collide=false;
          box.x= (int)(box.x-8*Math.cos(angle));
          box.y= (int)(box.y-8*Math.sin(angle));
          Crossing.hoppingBugs[box.x/64][box.y/64].add(this);
        }
        else
          Crossing.hoppingBugs[box.x/64][box.y/64].add(this);
      }
      if (animation!=2)
        animation=2;
      else
        animation=1;
      if (count==-8)
      {
        animation=0;
        count=8;
        waitCount=(int)(Math.random()*20)+10;
        angle=newAngle;
        if (collide==false)
        {
          newAngle = Math.random()*Math.PI/2+angle-5*Math.PI/4;
          if (newAngle<0)
            newAngle = newAngle+Math.PI*2;
          collide=true;
        }
        else
          newAngle = Math.random()*Math.PI*2;
        rotation = newAngle-angle;
        if (Math.abs(rotation)>Math.PI)
        {
          if (rotation>0)
            rotation=(rotation-Math.PI*2)/10;
          else
            rotation=(rotation+Math.PI*2)/10;
        }
        else
          rotation=rotation/10;
      }
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
      g2d.drawImage(translate.filter(ani2, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION-64+(int)(Math.pow(count,2)), null);
    else
      g2d.drawImage(translate.filter(ani3, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION-64+(int)(Math.pow(count,2)), null);
  }
}