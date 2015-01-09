import javax.swing.JFrame;
import java.awt.Rectangle;
import java.util.ArrayList;
/*
 * This is the main class, which is where everything is initialized and set into motion
 * Please read this in the programs chronological order, AKA skip straight to any method that is called and come back when you finish reading the method
 * EXCEPT FOR IF IT REFERS TO ANOTHER CLASS
 * Once you have finished reading this, read Graphix, Entity and then Bugs. Everything else is the same as bugs except with different methods.
 */

//make an instanced room
//make a shop keeper
//make a save and quit game object, needs to save the grid, inventory, and shop items
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
  static int[][] spawn = new int[12][2];
  static int[][] fishSpawn = new int[12][2];
  static Player player = new Player();
  static Items bobber = new Items();
  static Fish caught;
  
  public static void main(String [] args)
    throws InterruptedException
  {
    fillArray();
    frame();
    inventory[0][0]=new Fish("fish", 9, 9, 0);
    inventory[0][0].equipment=2;
    inventory[3][2]=new Fish("fish", 9, 9, 0);
    inventory[3][2].equipment=1;
    inventory[4][1]=new Plants("turnip", true);
    inventory[5][1]=new Plants("turnip", false);
    inventory[4][2]=new Plants("turnip", true);
    inventory[5][2]=new Plants("turnip", true);
    flyingBugs[11][11].add(new FlyingBugs("butterfly", 11, 11, 0));
    fish[13][13].add(new Fish("fish", 13, 13, 0));
    bugs[11][11].add(new Bugs("ladybug", 11, 11));
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
    spawn[0][0]=585;
    spawn[0][1]=575;
    spawn[1][0]=1252;
    spawn[1][1]=575;
    spawn[2][0]=1920;
    spawn[2][1]=575;
    spawn[3][0]=3255;
    spawn[3][1]=575;
    spawn[4][0]=585;
    spawn[4][1]=1280;
    spawn[5][0]=1252;
    spawn[5][1]=1280;
    spawn[6][0]=1920;
    spawn[6][1]=1280;
    spawn[7][0]=3255;
    spawn[7][1]=1280;
    spawn[8][0]=585;
    spawn[8][1]=1985;
    spawn[9][0]=1252;
    spawn[9][1]=1985;
    spawn[10][0]=1920;
    spawn[10][1]=1985;
    spawn[11][0]=3255;
    spawn[11][1]=1985;
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
  
  public static void spawn(int type)
  {
    ArrayList <Integer>nearPlayer = new ArrayList<Integer>();
    if (type==1)
    {
      for (int a=0; a<12; a++)
      {
        if (!(spawn[a][0]<player.box.x+512 && spawn[a][0]>player.box.x-512 && spawn[a][1]<player.box.y+512 && spawn[a][1]>player.box.y-512))
          nearPlayer.add(a);
      }
      if (Math.random()<0.34)
      {
        int temp=(int)(Math.random()*nearPlayer.size());
        System.out.println(spawn[nearPlayer.get(temp)][0] + ", " + spawn[nearPlayer.get(temp)][1]);
        flyingBugs[spawn[nearPlayer.get(temp)][0]/64][spawn[nearPlayer.get(temp)][1]/64].add(new FlyingBugs("butterfly", spawn[nearPlayer.get(temp)][0]/64, spawn[nearPlayer.get(temp)][1]/64, 0));
      }
      else if (Math.random()<0.5)
      {
        int temp=(int)(Math.random()*nearPlayer.size());
        System.out.println(spawn[nearPlayer.get(temp)][0] + ", " + spawn[nearPlayer.get(temp)][1]);
        flyingBugs[spawn[nearPlayer.get(temp)][0]/64][spawn[nearPlayer.get(temp)][1]/64].add(new FlyingBugs("dragonfly", spawn[nearPlayer.get(temp)][0]/64, spawn[nearPlayer.get(temp)][1]/64, 0));
      }
      else
      {
        int temp=(int)(Math.random()*nearPlayer.size());
        System.out.println(spawn[nearPlayer.get(temp)][0] + ", " + spawn[nearPlayer.get(temp)][1]);
        flyingBugs[spawn[nearPlayer.get(temp)][0]/64][spawn[nearPlayer.get(temp)][1]/64].add(new FlyingBugs("fly", spawn[nearPlayer.get(temp)][0]/64, spawn[nearPlayer.get(temp)][1]/64, 0));
      }
    }
    else if (type==2)
    {
      for (int a=0; a<12; a++)
      {
        if (!(spawn[a][0]<player.box.x+512 && spawn[a][0]>player.box.x-512 && spawn[a][1]<player.box.y+512 && spawn[a][1]>player.box.y-512))
          nearPlayer.add(a);
      }
      if (Math.random()<0.34)
      {
        int temp=(int)(Math.random()*nearPlayer.size());
        bugs[spawn[nearPlayer.get(temp)][0]/64][spawn[nearPlayer.get(temp)][1]/64].add(new Bugs("beetle", spawn[nearPlayer.get(temp)][0]/64, spawn[nearPlayer.get(temp)][1]/64));
      }
      else if (Math.random()<0.5)
      {
        int temp=(int)(Math.random()*nearPlayer.size());
        bugs[spawn[nearPlayer.get(temp)][0]/64][spawn[nearPlayer.get(temp)][1]/64].add(new Bugs("grasshopper", spawn[nearPlayer.get(temp)][0]/64, spawn[nearPlayer.get(temp)][1]/64));
      }
      else
      {
        int temp=(int)(Math.random()*nearPlayer.size());
        bugs[spawn[nearPlayer.get(temp)][0]/64][spawn[nearPlayer.get(temp)][1]/64].add(new Bugs("ladybug", spawn[nearPlayer.get(temp)][0]/64, spawn[nearPlayer.get(temp)][1]/64));
      }
    }
    else if (type==3)
    {
      for (int a=0; a<12; a++)
      {
        if (!(fishSpawn[a][0]<player.box.x+512 && fishSpawn[a][0]>player.box.x-512 && fishSpawn[a][1]<player.box.y+512 && fishSpawn[a][1]>player.box.y-512))
          nearPlayer.add(a);
      }
      if (Math.random()<0.34)
      {
        int temp=(int)(Math.random()*nearPlayer.size());
        fish[fishSpawn[nearPlayer.get(temp)][0]/64][fishSpawn[nearPlayer.get(temp)][1]/64].add(new Fish("seabass", fishSpawn[nearPlayer.get(temp)][0]/64, fishSpawn[nearPlayer.get(temp)][1]/64, 0));
      }
      else if (Math.random()<0.5)
      {
        int temp=(int)(Math.random()*nearPlayer.size());
        fish[fishSpawn[nearPlayer.get(temp)][0]/64][fishSpawn[nearPlayer.get(temp)][1]/64].add(new Fish("salmon", fishSpawn[nearPlayer.get(temp)][0]/64, fishSpawn[nearPlayer.get(temp)][1]/64, 0));
      }
      else
      {
        int temp=(int)(Math.random()*nearPlayer.size());
        fish[fishSpawn[nearPlayer.get(temp)][0]/64][fishSpawn[nearPlayer.get(temp)][1]/64].add(new Fish("crayfish", fishSpawn[nearPlayer.get(temp)][0]/64, fishSpawn[nearPlayer.get(temp)][1]/64, 0));
      }
    }
    else if (type==4)
    {
      int temp=(int)(Math.random()*12);
      int tempb=temp;
      villagers[spawn[temp][0]/64][spawn[temp][1]/64].add(new Villagers("villager1", spawn[temp][0]/64, spawn[temp][1]));
      while(tempb==temp)
        tempb=(int)(Math.random()*12);
      villagers[spawn[temp][0]/64][spawn[temp][1]/64].add(new Villagers("villager2", spawn[temp][0]/64, spawn[temp][1]));
      tempb=temp;
      while(tempb==temp)
        tempb=(int)(Math.random()*12);
      villagers[spawn[temp][0]/64][spawn[temp][1]/64].add(new Villagers("villager3", spawn[temp][0]/64, spawn[temp][1]));
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