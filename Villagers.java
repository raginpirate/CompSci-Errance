import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
public class Villagers extends Entity
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
  private int animation=0;
  private int aniAdd=1;
  public Villagers(String s, int a, int b)
  {
    eat=true;
    boolean checkFail=true;
    image = Graphix.buffer(s + ".jpg");
    ani1 = Graphix.buffer(s + 1 + ".jpg");
    ani2 = Graphix.buffer(s + 2 + ".jpg");
    ani3 = Graphix.buffer(s + 3 + ".jpg");
    ani4 = Graphix.buffer(s + 4 + ".jpg");
    ani5 = Graphix.buffer(s + 5 + ".jpg");
    ani6 = Graphix.buffer(s + 6 + ".jpg");
    ani7 = Graphix.buffer(s + 7 + ".jpg");
    ani8 = Graphix.buffer(s + 8 + ".jpg");
    ani9 = Graphix.buffer(s + 9 + ".jpg");
    ani10 = Graphix.buffer(s + 10 + ".jpg");
    ani11 = Graphix.buffer(s + 11 + ".jpg");
    ani12 = Graphix.buffer(s + 12 + ".jpg");
    box.x=a*64;
    box.y=b*64;
  }
  public void move()
  {
    if (waitCount>0)
      waitCount--;
    else
    {
      Crossing.villagers[box.x/64][box.y/64].remove(this);
      box.x= (int)(box.x+Math.cos(angle));
      box.y= (int)(box.y+Math.sin(angle));
      if (maxLoops==0 || collision())
      {
        box.x= (int)(box.x-Math.cos(angle));
        box.y= (int)(box.y-Math.sin(angle));
        Crossing.villagers[box.x/64][box.y/64].add(this);
        waitCount=(int)(Math.random()*10);
        maxLoops=(int)(Math.random()*40);
        angle = angle+Math.PI/4+Math.random()*(Math.PI/2);
        animation=1;
      }
      else
      {
        Crossing.villagers[box.x/64][box.y/64].add(this);
        maxLoops--;
        if (animation==2)
          aniAdd=-1;
        else if (animation==0)
          aniAdd=1;
        animation=animation+aniAdd;
      }
    }
  }
  
  public boolean collision()
  {
    if (Crossing.holes[box.x/64][box.y/64] ||  Crossing.holes[box.x/64+1][box.y/64] || Crossing.holes[box.x/64][box.y/64+1] || Crossing.holes[box.x/64+1][box.y/64+1])
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
    if (angle<Math.PI/4 || angle>1.75*Math.PI)
    {
      if (animation==0)
        g2d.drawImage(translate.filter(ani1, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
      else if (animation==1)
        g2d.drawImage(translate.filter(ani2, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
      else
        g2d.drawImage(translate.filter(ani3, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
    }
    else if (angle<Math.PI*0.75)
    {
      if (animation==0)
        g2d.drawImage(translate.filter(ani4, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
      else if (animation==1)
        g2d.drawImage(translate.filter(ani5, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
      else
        g2d.drawImage(translate.filter(ani6, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
    }
    else if (angle<1.25*Math.PI)
    {
      if (animation==0)
        g2d.drawImage(translate.filter(ani7, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
      else if (animation==1)
        g2d.drawImage(translate.filter(ani8, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
      else
        g2d.drawImage(translate.filter(ani9, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
    }
    else
    {
      if (animation==0)
        g2d.drawImage(translate.filter(ani10, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
      else if (animation==1)
        g2d.drawImage(translate.filter(ani11, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
      else
        g2d.drawImage(translate.filter(ani12, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
    }
  }
}