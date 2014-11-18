import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
public class Fish extends Entity
{
  public double angle=2;
  public double rotation=2;
  private short waitCount=0;
  private short accelleration=0;
  private short animation=0;
  private BufferedImage ani1;
  private BufferedImage ani2;
  private BufferedImage ani3;
  private BufferedImage ani4;
  private BufferedImage ani5;
  
  public Fish(String s)
  {
    eat=true;
    boolean checkFail=true;
    image = Graphix.buffer(s);
    ani1 = Graphix.buffer(s + 1);
    ani2 = Graphix.buffer(s + 2);
    ani3 = Graphix.buffer(s + 3);
    ani4 = Graphix.buffer(s + 4);
    ani5 = Graphix.buffer(s + 5);
    while (checkFail)
    {
      box.x=(int)(Math.random()*300+64);
      box.y=(int)(Math.random()*300+64);
      checkFail= (Crossing.checkColide(box));
    }
    Crossing.entityGrid[box.x/64][box.y/64].add(this);
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
      box.x= (int)(box.x+accelleration*Math.cos(angle));
      box.y= (int)(box.y+accelleration*Math.sin(angle));
      if (Crossing.checkColide(box))
      {
        box.x= (int)(box.x-accelleration*Math.cos(angle));
        box.y= (int)(box.y-accelleration*Math.sin(angle));
        Crossing.entityGrid[box.x/64][box.y/64].add(this);
        accelleration=0;
      }
      if (angle<=0 || angle>=Math.PI*2)
      {
        waitCount=(short)(Math.random()*20);
      }
      else
      {
        Crossing.entityGrid[box.x/64][box.y/64].add(this);
        if (accelleration>0)
          accelleration--;
        if (Math.random()<0.01 && accelleration>0)
          accelleration=(short)(accelleration+10);
        if (accelleration<10)
          angle = angle+rotation/16;
      }
    }
  }
  
  public void paint(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;
    AffineTransform rotate = AffineTransform.getRotateInstance(angle+Math.PI/2, image.getWidth()/2, image.getHeight()/2);
    AffineTransformOp translate = new AffineTransformOp(rotate, AffineTransformOp.TYPE_BILINEAR);
    g2d.drawImage(translate.filter(image, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
  }
}