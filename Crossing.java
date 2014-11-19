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
  static final int PLAYERLOCATION=368;
  static JFrame frame = new JFrame("Crossing v0.2");
  static ArrayList<Bugs>[][] bugs = new ArrayList[60][40];
  static ArrayList<FlyingBugs>[][] flyingBugs = new ArrayList[60][40];
  static ArrayList<Fish>[][] fish = new ArrayList[60][40];
  static ArrayList<Villagers>[][] villagers = new ArrayList[60][40];
  static ArrayList<Plants>[][] plants = new ArrayList[60][40];
  static Rectangle npcBoundaries[] = new Rectangle[1];
  static Rectangle worldWalls[] = new Rectangle[1];
  static boolean holes[][] = new boolean[60][40];
  static Player player = new Player();
  static Entity[][] inventory = new Entity[6][3];
  
  public static void main(String [] args)
    throws InterruptedException
  {
    fillArray();
    frame();
    inventory[3][2]=new Fish("fish", 9, 9, 0);
    fish[12][12].add(new Fish("fish", 12, 12, 0));
    worldUpdate();
  }
  
  public static void fillArray()
  {
    for(int a=0; a<60; a++)
    {
      for (int b=0; b<40; b++)
      {
        bugs[a][b] = new ArrayList<Bugs>();
        flyingBugs[a][b] = new ArrayList<FlyingBugs>();
        fish[a][b] = new ArrayList<Fish>();
        villagers[a][b] = new ArrayList<Villagers>();
        plants[a][b] = new ArrayList<Plants>();
        if (a<8 || a>52 || b<8 || b>32)
        {
          holes[a][b] = true;
        }
      }
    }
    npcBoundaries[0]=new Rectangle(0,0,1000, 1000);
    worldWalls[0]=new Rectangle(512,512,1000, 1000);
  }
  
  public static void frame()
  {
    frame.setSize(800, 800);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Graphix());
    frame.setVisible(true);
  }
  
  public static void worldUpdate()
    throws InterruptedException
  {
    while(true)
    {
      movement();
      frame.repaint();
      Thread.sleep(100);
    }
  }
  
  public static void movement()
  {
    player.move();
    for (int a=0; a<60; a++)
    {
      for (int b=0; b<40; b++)
      {
        for (int c=0; c<bugs[a][b].size(); c++)
        {
          bugs[a][b].get(c).move();
        }
        for (int c=0; c<flyingBugs[a][b].size(); c++)
        {
          flyingBugs[a][b].get(c).move();
        }
        for (int c=0; c<fish[a][b].size(); c++)
        {
          fish[a][b].get(c).move();
        }
        for (int c=0; c<villagers[a][b].size(); c++)
        {
          villagers[a][b].get(c).move();
        }
        for (int c=0; c<plants[a][b].size(); c++)
        {
          plants[a][b].get(c).move();
        }
      }
    }
  }
}