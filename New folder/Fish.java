import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
public class Fish extends Entity
{
  private boolean nibbleAI;
  private double angle=2;
  private double rotation=2;
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
  private boolean collided=false;
  private Rectangle bobberRectangle=new Rectangle(10000,10000,0,0);
  
  public Fish(String s, int a, int b, int boundary)
  {
    eat=false;
    image = Graphix.buffer(s + ".jpg");
    ani1 = Graphix.buffer(s + "1.jpg");
    ani2 = Graphix.buffer(s + "2.jpg");
    ani3 = Graphix.buffer(s + "3.jpg");
    ani4 = Graphix.buffer(s + "4.jpg");
    ani5 = Graphix.buffer(s + "5.jpg");
    box.x=64*a;
    box.y=64*b;
    this.boundary=boundary;
    state=2;
  }
  
  public void update()
  {
    if (nibbleAI)
    {
      System.out.println("nibble!");
    }
    else if (Crossing.bobber.box.intersects(bobberRectangle))
      nibbleAI=true;
    else
    {
      if (waitCount>0)
      {
        waitCount--;
        if (animation==4)
          aniAdd=-1;
        else if (animation==0)
          aniAdd=1;
        animation=animation+aniAdd;
      }
      else if (waitCount==0)
      {
        if (collided)
        {
          angle = Math.random()*Math.PI/2+angle-5*Math.PI/4;
          if (angle<0)
            angle = angle+Math.PI*2;
          collided=false;
        }
        else
          angle = Math.random()*Math.PI*2;
        rotation = Math.PI*2 - angle;
        if (rotation>=Math.PI)
          rotation = rotation-Math.PI*2;
        accelleration = 20;
        waitCount--;
        animation=2;
      }
      else
      {
        Crossing.fish[box.x/64][box.y/64].remove(this);
        box.x= box.x+(int)(accelleration*Math.cos(angle));
        box.y= box.y+(int)(accelleration*Math.sin(angle));
        if (collision())
        {
          collided=true;
          box.x= box.x-(int)(accelleration*Math.cos(angle));
          box.y= box.y-(int)(accelleration*Math.sin(angle));
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
          {
            angle = angle+rotation/16;
            if (animation==4)
              aniAdd=-1;
            else if (animation==0)
              aniAdd=1;
            animation=animation+aniAdd;
          }
          else
          {
            if (animation==3)
              aniAdd=-1;
            else if (animation==1)
              aniAdd=1;
            animation=animation+aniAdd;
          }
          if (angle<Math.PI/2)
          {
            bobberRectangle.x=box.x+64;
            bobberRectangle.y=box.y-128;
            bobberRectangle.width=128;
            bobberRectangle.height=320;
          }
          else if (angle<Math.PI)
          {
            bobberRectangle.x=box.x-128;
            bobberRectangle.y=box.y+64;
            bobberRectangle.width=320;
            bobberRectangle.height=128;
          }
          else if (angle<Math.PI*3/2)
          {
            bobberRectangle.x=box.x-128;
            bobberRectangle.y=box.y-128;
            bobberRectangle.width=128;
            bobberRectangle.height=320;
          }
          else
          {
            bobberRectangle.x=box.x-128;
            bobberRectangle.y=box.y-128;
            bobberRectangle.width=320;
            bobberRectangle.height=128;
          }
        }
      }
    }
  }
  
  private boolean collision()
  {
    if (!(Crossing.npcBoundaries[boundary].intersects(box)))
      return true;
    for (Rectangle x:Crossing.water)
    {
      if (x.intersects(box))
        return false;
    }
    return true;
  }
  
  public boolean interact()
  {
    if (state==0)
      return false;
    loop: for (int f=0; f<6; f++)
    {
      for (int k=1; k<4; k++)
      {
        if (Crossing.inventory[f][k]==null)
        {
          Crossing.inventory[f][k]=this;
          Crossing.grid[box.x/64][box.y/64]=null;
          break loop;
        }
        else if (f==5 && k==3)
        {
          System.out.println("Inventory is full!");
        }
      }
    }
    return true;
  }
  
  public void paint(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawRect(bobberRectangle.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, bobberRectangle.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, bobberRectangle.width, bobberRectangle.height);
    AffineTransform rotate = AffineTransform.getRotateInstance(angle+Math.PI/2, image.getWidth()/2, image.getHeight()/2);
    AffineTransformOp translate = new AffineTransformOp(rotate, AffineTransformOp.TYPE_BILINEAR);
    if (state==0)
      g2d.drawImage(Graphix.star, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
    else if (state==1)
      g2d.drawImage(image, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
    else if (animation==0)
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