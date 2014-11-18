import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
public class Holes extends Entity
{
  public Holes(Rectangle box)
  {
    this.box = box;
  }
  public void move()
  {
    //Does nothing
  }
  public void paint(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(Graphix.wall, (int)(Crossing.PLAYERLOCATION-Crossing.player.box.x)+box.x, (int)(Crossing.PLAYERLOCATION-Crossing.player.box.y)+box.y, null);
  }
}