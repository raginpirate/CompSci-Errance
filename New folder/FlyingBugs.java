import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
public class FlyingBugs extends Entity
{
  private double angle=2;
  private int maxLoops=0;
  private BufferedImage ani1;
  private BufferedImage ani2;
  private BufferedImage ani3;
  private int boundary;
  private int animation=0;
  private int counter=0;
  private int aniAdd=1;
  private int waitCount=0;
  private int count=0;
  private int aiValue;
  
  public FlyingBugs(String s, int a, int b, int boundary)
  {
    eat=false;
    image = Graphix.buffer(s + ".jpg");
    ani1 = Graphix.buffer(s + "1.jpg");
    ani2 = Graphix.buffer(s + "2.jpg");
    ani3 = Graphix.buffer(s + "3.jpg");
    this.boundary = boundary;
    box.x=a*64;
    box.y=b*64;
    state=2;
    if (s=="butterfly")
      aiValue=1;
    if (s=="dragonfly")
      aiValue=2;
    if (s=="fly")
      aiValue=3;
  }
  
  public void update()
  {
    switch(aiValue)
    {
      case 1:Crossing.flyingBugs[box.x/64][box.y/64].remove(this);
      box.x= box.x+(int)(3*Math.cos(angle));
      box.y= box.y+(int)(3*Math.sin(angle));
      if (maxLoops==0 || !(Crossing.npcBoundaries[boundary].intersects(box)))
      {
        box.x= box.x-(int)(3*Math.cos(angle));
        box.y= box.y-(int)(3*Math.sin(angle));
        Crossing.flyingBugs[box.x/64][box.y/64].add(this);
        maxLoops=(int)(Math.random()*160)+80;
        angle = Math.random()*Math.PI*2;
      }
      else
      {
        Crossing.flyingBugs[box.x/64][box.y/64].add(this);
        maxLoops--;
        counter++;
        if (counter==8)
        {
          counter=0;
          if (animation==2)
            aniAdd=-1;
          else if (animation==0)
            aniAdd=1;
          animation=animation+aniAdd;
        }
      }
      break;
      case 2:if (animation==2)
        aniAdd=-1;
      else if (animation==0)
        aniAdd=1;
      animation=animation+aniAdd;
      if (waitCount>0)
      {
        waitCount--;
        if (waitCount>38)
        {
          Crossing.flyingBugs[box.x/64][box.y/64].remove(this);
          box.x= box.x-(int)(5*Math.cos(angle));
          box.y= box.y-(int)(5*Math.sin(angle));
          Crossing.flyingBugs[box.x/64][box.y/64].add(this);
        }
      }
      else if (waitCount==0)
      {
        maxLoops=(int)(Math.random()*5)+5;
        angle = Math.random()*Math.PI*2;
        waitCount--;
      }
      else
      {
        Crossing.flyingBugs[box.x/64][box.y/64].remove(this);
        box.x= box.x+(int)(30*Math.cos(angle));
        box.y= box.y+(int)(30*Math.sin(angle));
        if (maxLoops==0 || !(Crossing.npcBoundaries[boundary].intersects(box)))
        {
          box.x= box.x-(int)(30*Math.cos(angle));
          box.y= box.y-(int)(30*Math.sin(angle));
          Crossing.flyingBugs[box.x/64][box.y/64].add(this);
          waitCount=40;
        }
        else
        {
          Crossing.flyingBugs[box.x/64][box.y/64].add(this);
          maxLoops--;
          counter++;
        }
      }
      break;
      case 3:Crossing.flyingBugs[box.x/64][box.y/64].remove(this);
      count=count+1;
      box.y= box.y+(int)(5*Math.cos(angle)-5*Math.sin(count)*Math.sin(angle));
      box.x= box.x+(int)(5*Math.sin(angle)+5*Math.sin(count)*Math.cos(angle));
      if (maxLoops==0 || !(Crossing.npcBoundaries[boundary].intersects(box)))
      {
        box.y= box.y-(int)(5*Math.cos(angle)-5*Math.sin(count)*Math.sin(angle));
        box.x= box.x-(int)(5*Math.sin(angle)+5*Math.sin(count)*Math.cos(angle));
        Crossing.flyingBugs[box.x/64][box.y/64].add(this);
        maxLoops=(int)(Math.random()*20);
        angle = Math.random()*Math.PI*2;
        count=0;
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
      break;
    }
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
          //Drops to the grid
          //Crossing.grid[box.x/64][box.y/64]=null;
          System.out.println("Inventory is full!");
        }
      }
    }
    return true;
  }
  
  public void water(){}
  
  public void paint(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;
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
    else
      g2d.drawImage(translate.filter(ani3, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
  }
}