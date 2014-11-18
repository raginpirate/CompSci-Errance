import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
public class Villagers extends Entity
{
  public Color color;
  public double angle=2;
  private int waitCount=0;
  private int maxLoops=0;
  
  public Villagers()
  {
    eat=false;
    int a = (int)(Math.random()*5);
    if (a==0)
      color = Color.blue;
    else if (a==1)
      color = Color.red;
    else if (a==2)
      color = Color.yellow;
    else if (a==3)
      color = Color.cyan;
    else if (a==4)
      color = Color.orange;
    boolean checkFail=true;
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
    else
    {
      box.x= (int)(box.x+Math.cos(angle));
      box.y= (int)(box.y+Math.sin(angle));
      if (maxLoops==0|| Crossing.checkColide(box))
      {
        box.x= (int)(box.x-Math.cos(angle));
        box.y= (int)(box.y-Math.sin(angle));
        Crossing.entityGrid[box.x/64][box.y/64].add(this);
        waitCount=(int)(Math.random()*10);
        maxLoops=(int)(Math.random()*40);
        angle = angle+Math.PI/4+Math.random()*(Math.PI/2);
      }
      else
      {
        Crossing.entityGrid[box.x/64][box.y/64].add(this);
        maxLoops--;
      }
    }
  }
  
  public void paint(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(color);
    g2d.drawRect((int)(box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION), (int)(box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION), 64, 64);
  }
}