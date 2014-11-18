import javax.swing.JFrame;
import java.awt.Rectangle;
import java.util.ArrayList;
/*
 * This is the main class, which is where everything is initialized and set into motion
 * Please read this in the programs chronological order, AKA skip straight to any method that is called and come back when you finish reading the method
 * EXCEPT FOR IF IT REFERS TO ANOTHER CLASS
 * Once you have finished reading this, read Graphix, Entity and then Bugs. Everything else is the same as bugs except with different methods.
 */
public class Crossing
{
  //This is the location upon which the player sits RELATIVE to the screen. This variable is modified if we ever
  //decide to change the dimensions of the game box itself. It is simply the width of the screen divided by 2 + 32
  //(half the player's size)
  static final int PLAYERLOCATION=368;
  //Simple jframe
  static JFrame frame = new JFrame("Crossing v0.2");
  //This is the most important concept of the program. The game itself is split up into entities, or moving non-player objects,
  //that rest upon locations on a 60x40 grid. Each slot of this array represents a location on the 60x40 grid, and each of these slots
  //has an arraylist that contains the entities that rest on said location. Now why might it be useful to know the grid space upon which
  //each entity lies? Well it makes our life alot easier when we get to unit collision.
  static ArrayList<Entity>[][] entityGrid = new ArrayList[60][40];
  //This is another array, however much smaller (6x3), that represents the player's inventory. The inventory is filled with various
  //entities that the player captures or aquires. The entities themselves are stored so it is easy to reference their associated image
  //for drawing in the inventory aswell as making it easy to reinstanciate them into the world when they are dropped.
  static Entity[][] inventory = new Entity[6][3];
  //This is an instance of the player class that is very similar to all other entities, but is unique and should never be replicated.
  static Player player = new Player();
  
  //our main method
  public static void main(String [] args)
    throws InterruptedException
  {
    //The first thing to do in the game is making sure the entityGrid's arraylists are instantiated at each location.
    fillArray();
    //Then the JFrame is created for graphics and keybindings
    frame();
    //This is the instantiation of a bug class with its associated string ID and grid location. This ID assigns various image files and values
    //to the bug. This is a temporary placement of a bug, and only exists here for testing.
    Crossing.entityGrid[10][10].add(new Bugs("bug", 10, 10));
    for (int a=0; a<6; a++)
    {
      for (int b=0; b<3; b++)
        inventory[a][b] = new Bugs("bug", 0, 0);
    }
    //Then we begin to loop our actual game (so far)
    worldUpdate();
  }
  
  public static void fillArray()
  {
    //This simply runs through the entire index of the array and places an arraylist at each one
    for(int a=0; a<60; a++)
    {
      for (int b=0; b<40; b++)
      {
        entityGrid[a][b] = new ArrayList<Entity>();
        //This is simply done for testing purposes. It places some holes as temporary world borders
        if (a<8 || a>52 || b<8 || b>32)
        {
          Crossing.entityGrid[a][b].add(new Holes(new Rectangle(a*64, b*64, 64, 64)));
        }
      }
    }
    //This places the player entity in the correct starting grid location. Since the player is instantiated as a field, it cannot be done
    //in its constructor like all others.
    Crossing.entityGrid[8][8].add(player);
  }
  
  public static void frame()
  {
    //Very simple Jframe
    frame.setSize(800, 800);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Graphix());
    frame.setVisible(true);
  }
  
  public static void worldUpdate()
    throws InterruptedException
  {
    //Very simple does three steps over and over, moves all the entites, repaints the frame, and waits 0.1 seconds to repeat
    while(true)
    {
      movement();
      frame.repaint();
      Thread.sleep(100);
    }
  }
  
  public static void movement()
  {
    //For every index of the array
    for (int a=0; a<60; a++)
    {
      for (int b=0; b<40; b++)
      {
        //and for every index of every arraylist
        for (int c=0; c<entityGrid[a][b].size(); c++)
        {
          //we run the related entities move method.
          //Each entity class has one of these, but I do not reccomend trying to understand how they work
          //as it is still a work in progress and subject to change
          entityGrid[a][b].get(c).move();
        }
      }
    }
  }
  
  //This method is run durring an objects movement cycle
  public static boolean checkColide(Rectangle box)
  {
    //Very simply does collision for every entity to make sure they dont overlap
    for (int a=-1; a<2; a++)
    {
      for (int b=-1; b<2; b++)
      {
        for (int c=0; c<entityGrid[(int)(box.x/64+a)][(int)(box.y/64+b)].size(); c++)
        {
          //For all grid locations surrounding and including the supplied box's grid location within a distance of one space, 
          //we check to see if any of their entities' boxes collide with the supplied one.
          //we do not need to check any further RIGHT NOW as everything that exists is equal in size to one grid space.
          //This is why the entity grid is so useful, because instead of looking at everything that exists in the game
          //we only have to check collisions with those located near our box in question. Every other box does not matter.
          if (box.intersects(entityGrid[(int)(box.x/64+a)][(int)(box.y/64+b)].get(c).box))
            return true;
        }
      }
    }
    return false;
  }
}