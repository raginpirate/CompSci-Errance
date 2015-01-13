import java.awt.Graphics;
import java.awt.image.BufferedImage;
public class Items extends Entity
{
  public Items(String s)
  {
    box.width=64;
    box.height=64;
    this.s=s;
    image=Graphix.buffer(s + ".jpg");
    if (s=="bobber")
    {
      box.width=16;
      box.height=16;
    }
    else if (s.equals("shovel"))
    {
      equipment=1;
      moneta=50;
    }
    else if (s.equals("net"))
      {
      equipment=2;
      moneta=100;
    }
    else if (s.equals("rod"))
      {
      equipment=3;
      moneta=250;
    }
    else if (s.equals("can"))
      {
      equipment=4;
      moneta=60;
    }
  }
  public void update(){}
  public void paint(Graphics g)
  {
    if (state==0)
      g.drawImage(Graphix.star, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
    else
      g.drawImage(image, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
  }
  public boolean interact()
  {
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
        }
      }
    }
    return true;
  }
}