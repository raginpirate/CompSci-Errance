import java.awt.Graphics;
//This class was created so that way Bugs, Fish, Player, Villagers, Holes, and Plants can all exist in the same array
//It simply contains the required fields and methods that any entity needs to be in entityGrid
public class Hole extends Entity
{
  public void update()
  {
    
  }
  public void paint(Graphics g)
  {
    g.drawImage(Graphix.hole, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
  }
  public boolean interact()
  {
    return false;
  }
}