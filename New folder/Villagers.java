import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Polygon;
public class Villagers extends Entity
{
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
  private double angle=2;
  private int waitCount=0;
  private int maxLoops=0;
  private int animation=0;
  private int aniAdd=1;
  
  public Villagers(String s, int a, int b)
  {
    eat=false;
    this.s=s;
    image = Graphix.buffer(s + "down.png");
    ani1 = Graphix.buffer(s + "down.png");
    ani2 = Graphix.buffer(s + "down1.png");
    ani3 = Graphix.buffer(s + "down2.png");
    ani4 = Graphix.buffer(s + "left.png");
    ani5 = Graphix.buffer(s + "left1.png");
    ani6 = Graphix.buffer(s + "left2.png");
    ani7 = Graphix.buffer(s + "right.png");
    ani8 = Graphix.buffer(s + "right1.png");
    ani9 = Graphix.buffer(s + "right2.png");
    ani10 = Graphix.buffer(s + "up.png");
    ani11 = Graphix.buffer(s + "up1.png");
    ani12 = Graphix.buffer(s + "up2.png");
    box.x=a*64;
    box.y=b*64;
    box.width=90;
    box.height=90;
  }
  
  public void update()
  {
    if (waitCount>0)
      waitCount--;
    else
    {
      Crossing.villagers[box.x/64][box.y/64].remove(this);
      box.x= box.x+(int)(3*Math.cos(angle));
      box.y= box.y+(int)(3*Math.sin(angle)); 
      if(collision())
      {
        box.x= box.x-(int)(3*Math.cos(angle));
        box.y= box.y-(int)(3*Math.sin(angle));
        angle=angle+Math.PI;
      }
      if (maxLoops==0)
      {
        Crossing.villagers[box.x/64][box.y/64].add(this);
        waitCount=(int)(Math.random()*10);
        maxLoops=(int)(Math.random()*40+20);
        angle = Math.random()*Math.PI*2;
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
  
  private boolean collision()
  {
    if (box.intersects(Crossing.player.box))
      return true;
    if (Crossing.grid[box.x/64][box.y/64] instanceof Hole||  Crossing.grid[box.x/64+1][box.y/64] instanceof Hole|| Crossing.grid[box.x/64][box.y/64+1] instanceof Hole|| Crossing.grid[box.x/64+1][box.y/64+1] instanceof Hole)
      return true;
    for (Rectangle x:Crossing.worldWalls)
    {
      if (x.intersects(box))
        return true;
    }
    for (Polygon x:Crossing.water)
    {
      if (x.intersects(box))
        return true;
    }
    for (int a=-1; a<2; a++)
    {
      for (int b=-1; b<2; b++)
      {
        for (int d=0; d<Crossing.villagers[box.x/64+a][box.y/64+b].size(); d++)
        {
          if (Crossing.villagers[box.x/64+a][box.y/64+b].get(d).box.intersects(box) && Crossing.villagers[box.x/64+a][box.y/64+b].get(d) != this)
            return true;
        }
      }
    }
    return false;
  }
  
  public boolean interact()
  {
    return true;
  }
  
  public void paint(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;
    if (s.equals("jenny"))
    {
      if (angle<Math.PI/4 || angle>1.75*Math.PI)
      {
        if (animation==0)
          g2d.drawImage(ani1, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else if (animation==1)
          g2d.drawImage(ani2, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else
          g2d.drawImage(ani3, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
      }
      else if (angle<Math.PI*0.75)
      {
        if (animation==0)
          g2d.drawImage(ani4, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else if (animation==1)
          g2d.drawImage(ani5, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else
          g2d.drawImage(ani6, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
      }
      else if (angle<1.25*Math.PI)
      {
        if (animation==0)
          g2d.drawImage(ani7, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else if (animation==1)
          g2d.drawImage(ani8, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else
          g2d.drawImage(ani9, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
      }
      else
      {
        if (animation==0)
          g2d.drawImage(ani10, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else if (animation==1)
          g2d.drawImage(ani11, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else
          g2d.drawImage(ani12, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
      }
    }
    else
    {
      if (angle<Math.PI/4 || angle>1.75*Math.PI)
      {
        if (animation==0)
          g2d.drawImage(ani7, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else if (animation==1)
          g2d.drawImage(ani8, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else
          g2d.drawImage(ani9, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
      }
      else if (angle<Math.PI*0.75)
      {
        if (animation==0)
          g2d.drawImage(ani1, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else if (animation==1)
          g2d.drawImage(ani2, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else
          g2d.drawImage(ani3, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
      }
      else if (angle<1.25*Math.PI)
      {
        if (animation==0)
          g2d.drawImage(ani4, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else if (animation==1)
          g2d.drawImage(ani5, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else
          g2d.drawImage(ani6, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
      }
      else
      {
        if (animation==0)
          g2d.drawImage(ani10, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else if (animation==1)
          g2d.drawImage(ani11, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
        else
          g2d.drawImage(ani12, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 90, 90, null);
      }
    }
  }
}