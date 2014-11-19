import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
public class FlyingBugs extends Entity
{
  public double angle=2;
  private int waitCount=0;
  private int maxLoops=0;
  private BufferedImage ani1;
  private BufferedImage ani2;
  private BufferedImage ani3;
  private BufferedImage ani4;
  private BufferedImage ani5;
  private BufferedImage ani6;
  private BufferedImage ani7;
  private BufferedImage ani8;
  private BufferedImage ani9;
  private BufferedImage ani10;
  private BufferedImage ani11;
  private BufferedImage ani12;
  private int boundary;
  private int animation=0;
  private int aniAdd=1;
  public FlyingBugs(String s, int a, int b, int boundary)
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
    if (waitCount>0)
      waitCount--;
    else
    {
      Crossing.flyingBugs[box.x/64][box.y/64].remove(this);
      box.x= (int)(box.x+Math.cos(angle));
      box.y= (int)(box.y+Math.sin(angle));
      if (maxLoops==0 || !(Crossing.npcBoundaries[boundary].intersects(box)))
      {
        box.x= (int)(box.x-Math.cos(angle));
        box.y= (int)(box.y-Math.sin(angle));
        Crossing.flyingBugs[box.x/64][box.y/64].add(this);
        waitCount=(int)(Math.random()*10);
        maxLoops=(int)(Math.random()*40);
        angle = angle+Math.PI/4+Math.random()*(Math.PI/2);
        animation=1;
      }
      else
      {
        Crossing.flyingBugs[box.x/64][box.y/64].add(this);
        maxLoops--;
        if (animation==2)
          aniAdd=-1;
        else if (animation==0)
          aniAdd=1;
        animation=animation+aniAdd;
      }
    }
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