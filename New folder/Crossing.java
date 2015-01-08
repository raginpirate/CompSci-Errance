import javax.swing.JFrame;
import java.awt.Rectangle;
import java.util.ArrayList;
/*
 * This is the main class, which is where everything is initialized and set into motion
 * Please read this in the programs chronological order, AKA skip straight to any method that is called and come back when you finish reading the method
 * EXCEPT FOR IF IT REFERS TO ANOTHER CLASS
 * Once you have finished reading this, read Graphix, Entity and then Bugs. Everything else is the same as bugs except with different methods.
 */


//fix fish animation bug
//check out flying bugs
//make new methods for the different types of bugs / fish
//make a system to use the different methods
//create the spawning method
//make an instanced room
//make a shop keeper
//make a save and quit game object
//make a text reader for saving
public class Crossing
{
  static final int PLAYERLOCATION=368;
  static JFrame frame = new JFrame("Crossing v0.2");
  static ArrayList<Bugs>[][] bugs = new ArrayList[60][40];
  static ArrayList<FlyingBugs>[][] flyingBugs = new ArrayList[60][40];
  static ArrayList<Fish>[][] fish = new ArrayList[60][40];
  static ArrayList<Villagers>[][] villagers = new ArrayList[60][40];
  static Entity[][] grid = new Entity[60][40];
  static Entity[][] inventory = new Entity[6][4];
  static Rectangle npcBoundaries[] = new Rectangle[1];
  static Rectangle worldWalls[] = new Rectangle[1];
  static Rectangle water[] = new Rectangle[1];
  static Player player = new Player();
  static Items bobber = new Items();
  static Fish caught;
  
  public static void main(String [] args)
    throws InterruptedException
  {
    fillArray();
    frame();
    inventory[0][0]=new Fish("fish", 9, 9, 0);
    inventory[0][0].equipment=3;
    inventory[3][2]=new Fish("fish", 9, 9, 0);
    inventory[3][2].equipment=1;
    inventory[4][1]=new Plants("plant", true);
    inventory[5][1]=new Plants("plant", true);
    inventory[4][2]=new Plants("plant", true);
    inventory[5][2]=new Plants("plant", true);
    fish[14][13].add(new Fish("fish", 14, 13, 0));
    bugs[11][11].add(new Bugs("bug", 11, 11, 0));
    villagers[11][11].add(new Villagers("villager", 11, 11));
    bugs[12][12].add(new Bugs("bug", 12, 12, 0));
    grid[13][13]=new Hole();
    worldUpdate();
  }
  
  private static void fillArray()
  {
    for(int a=0; a<60; a++)
    {
      for (int b=0; b<40; b++)
      {
        bugs[a][b] = new ArrayList<Bugs>();
        flyingBugs[a][b] = new ArrayList<FlyingBugs>();
        fish[a][b] = new ArrayList<Fish>();
        villagers[a][b] = new ArrayList<Villagers>();
        if (a<8 || a>52 || b<8 || b>32)
        {
          grid[a][b] = new Hole();
        }
      }
    }
    npcBoundaries[0]=new Rectangle(512,512,1000, 1000);
    water[0]=new Rectangle(800,800,500,500);
    worldWalls[0]=new Rectangle(1000,1000,1000, 1000);
  }
  
  private static void frame()
  {
    frame.setSize(800, 800);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Graphix());
    frame.setVisible(true);
  }
  
  private static void worldUpdate()
    throws InterruptedException
  {
    while(true)
    {
      movement();
      frame.repaint();
      Thread.sleep(100);
    }
  }
  
  private static void movement()
  {
    player.move();
    for (int a=0; a<60; a++)
    {
      for (int b=0; b<40; b++)
      {
        for (int c=0; c<bugs[a][b].size(); c++)
          bugs[a][b].get(c).update();
        for (int c=0; c<flyingBugs[a][b].size(); c++)
          flyingBugs[a][b].get(c).update();
        for (int c=0; c<fish[a][b].size(); c++)
          fish[a][b].get(c).update();
        for (int c=0; c<villagers[a][b].size(); c++)
          villagers[a][b].get(c).update();
      }
    }
  }
}