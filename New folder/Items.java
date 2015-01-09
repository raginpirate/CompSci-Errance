import java.awt.Graphics;
import java.awt.image.BufferedImage;
public class Items extends Entity
{
  public Items()
  {
    image = Graphix.buffer("bobber.jpg");
    box.width=16;
    box.height=16;
  }
  public void update(){}
  public void paint(Graphics g)
  {
    g.drawImage(image, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 16, 16, null);
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
  public void water()
  {
    
  }
}