import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
//This class was created so that way Bugs, Fish, Player, Villagers, Holes, and Plants can all exist in the same array
//It simply contains the required fields and methods that any entity needs to be in entityGrid
abstract class Entity
{
  Rectangle box = new Rectangle(0,0,64,64);
  boolean eat;
  BufferedImage image;
  abstract void move();
  abstract void paint(Graphics g);
}