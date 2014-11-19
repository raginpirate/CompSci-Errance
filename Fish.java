import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
public class Fish extends Entity
{
  public double angle=2;
  public double rotation=2;
  private short waitCount=0;
  private short accelleration=0;
  private int animation=0;
  private int aniAdd=1;
  private BufferedImage ani1;
  private BufferedImage ani2;
  private BufferedImage ani3;
  private BufferedImage ani4;
  private BufferedImage ani5;
  private int boundary;
  
  public Fish(String s, int a, int b, int boundary)
  {
    eat=true;
    boolean checkFail=true;
    image = Graphix.buffer(s);
    ani1 = Graphix.buffer(s + 1);
    ani2 = Graphix.buffer(s + 2);
    ani3 = Graphix.buffer(s + 3);
    ani4 = Graphix.buffer(s + 4);
    ani5 = Graphix.buffer(s + 5);
    box.x=64*a;
    box.y=64*b;
    this.boundary=boundary;
  }
  
  public void move()
  {
    if (waitCount>0)
      waitCount--;
    if (waitCount==0)
    {
      angle = Math.random()*Math.PI*2;
      rotation = Math.PI*2 - angle;
      if (rotation>=Math.PI)
        rotation = rotation-Math.PI*2;
      accelleration = 20;
      waitCount--;
    }
    else
    {
      Crossing.fish[box.x/64][box.y/64].remove(this);
      box.x= (int)(box.x+accelleration*Math.cos(angle));
      box.y= (int)(box.y+accelleration*Math.sin(angle));
      if (collision())
      {
        box.x= (int)(box.x-accelleration*Math.cos(angle));
        box.y= (int)(box.y-accelleration*Math.sin(angle));
        Crossing.fish[box.x/64][box.y/64].add(this);
        accelleration=0;
      }
      else
        Crossing.fish[box.x/64][box.y/64].add(this);
      if (angle<=0 || angle>=Math.PI*2)
      {
        waitCount=(short)(Math.random()*20);
      }
      else
      {
        if (accelleration>0)
          accelleration--;
        if (Math.random()<0.01 && accelleration>0)
          accelleration=(short)(accelleration+10);
        if (accelleration<10)
          angle = angle+rotation/16;
      }
      if (animation==4)
        aniAdd=-1;
      else if (animation==0)
        aniAdd=1;
      animation=animation+aniAdd;
    }
  }
  
  public boolean collision()
  {
    if (!(Crossing.npcBoundaries[boundary].intersects(box)))
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
    else if (animation==2)
      g2d.drawImage(translate.filter(ani3, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
    else if (animation==3)
      g2d.drawImage(translate.filter(ani4, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
    else
      g2d.drawImage(translate.filter(ani5, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
  }
}