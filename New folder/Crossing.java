import javax.swing.JFrame;
import java.awt.Rectangle;
import java.awt.Polygon;
import java.util.ArrayList;
import java.io.*;
/*
 * File Name: Crossing.java
 * Date: Friday, January 16th, 2015
 * Programmed by: Orange Bannana (Daniel Wyckoff, Joshua Anandappa, Izzy Snowden).
 * Description: An openworld free-roaming game inwhich the player can interact with the enviroment using the space bar, move with the arrow keys,
 * open the inventory with q, and sprint with e. This class deals with creating Jpanels, moving information from text files, and updating the world.
 * 
 * To be used in conjunction with Bugs.java, Entity.java, Fish.java, FlyingBugs.java, Graphix.java, Hole.java, Items.java, Plants.java, Player.java, and Villagers.java
 */
public class Crossing
{
  //Initializing alot of important, and some unimportant, variables.
  
  //Creating an instance of the player class, since only one needs to exist it is fine for it to be initialized here
  //and be static.
  static Player player = new Player();
  
  //Initializing the jframe
  static JFrame frame = new JFrame("Crossing v0.2");
  
  //Creating the various arrays that would keep track of rectangles for collision, entity objects (see Entity.java), and the inventory.
  static ArrayList<Bugs>[][] bugs = new ArrayList[76][56];
  static ArrayList<FlyingBugs>[][] flyingBugs = new ArrayList[76][56];
  static ArrayList<Fish>[][] fish = new ArrayList[76][56];
  static ArrayList<Villagers>[][] villagers = new ArrayList[76][56];
  static ArrayList<Integer>[] openSpaces = new ArrayList[2];
  static ArrayList<Integer>[] fishSpaces = new ArrayList[2];
  static Rectangle[] shopWalls = new Rectangle[4];
  static Rectangle worldWalls[] = new Rectangle[87];
  static Entity[][] grid = new Entity[76][56];
  static Entity[][] inventory = new Entity[6][4];
  static Polygon water[] = new Polygon[5];
  
  //Various specific rectangle locations used for menu or location changes.
  static Rectangle npcBoundaries = new Rectangle(400,400,4100,2800);
  static Rectangle door = new Rectangle(4052, 660, 64, 64);
  static Rectangle shopKeeper = new Rectangle (5380, 4102, 64, 64);
  static Rectangle sign = new Rectangle(1000, 1000, 64, 64);
  static Rectangle bridgeOne=new Rectangle(2770,1165,86,480);
  static Rectangle bridgeTwo=new Rectangle(3522,2232,445,86);
  static Rectangle quitGame = new Rectangle(900,904,64,64);
  
  //This clocks how often the AI update for all enitites.
  static int growTimer=0;
  
  //Specific location of the player for graphics use
  static final int PLAYERLOCATION=368;
  
  //Used to stop pointless checks when the player is in the shop
  static boolean shopping=false;
  
  //Used to stop the game
  static boolean quit=true;
  
  //Used for a popup
  static boolean invFull=false;
  
  //Keeping track of the bobber and fish attached to the bobber when fishing.
  static Items bobber = new Items("bobber");
  static Fish caught;
  
  public static void main(String [] args)
    throws InterruptedException
  {
    //Creates Jframe info
    frame();
    //Fills in empty arrays of arraylists to prevent errors
    fillArray();
    //Reads the game's save file
    readSaveFile();
    //Reads a text file filled with rectangular collision boxes attached to the world
    walls();
    //Reads a text file with all of the available spawning locations for entities in the game
    openSpace();
    //Spawns 9 of each entity except villagers and items
    for (int a=1; a<4; a++)
    {
      for (int b=0; b<9; b++)
        spawn(a);
    }
    //Spawns two villagers
    spawn(5);
    spawn(5);
    //Loops until the quit variable is false. Updates AI and the Player
    worldUpdate();
    //Saves the grid and inventory to a text file.
    save();
    //Ends the entire game
    System.exit(0);
  }
  
  //Simple Jframe construction, uses the class Graphix for a graphical and keyboard input component. (See Graphix.java)
  private static void frame()
  {
    frame.setSize(800, 800);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Graphix());
    frame.setVisible(true);
  }
  
  //Fills in empty indices of the various arrays of arraylists
  private static void fillArray()
  {
    for(int a=0; a<76; a++)
    {
      for (int b=0; b<56; b++)
      {
        bugs[a][b] = new ArrayList<Bugs>();
        flyingBugs[a][b] = new ArrayList<FlyingBugs>();
        fish[a][b] = new ArrayList<Fish>();
        villagers[a][b] = new ArrayList<Villagers>();
      }
    }
    //Also adds certain collision locations
    shopWalls[0]=new Rectangle(5380,4102,517,513);
    shopWalls[1]=new Rectangle(5547,4102,349,69);
    shopWalls[2]=new Rectangle(5547,4253,349,69);
    shopWalls[3]=new Rectangle(5548,4415,349,69);
    openSpaces[0]=new ArrayList<Integer>();
    openSpaces[1]=new ArrayList<Integer>();
    fishSpaces[0]=new ArrayList<Integer>();
    fishSpaces[1]=new ArrayList<Integer>();
    water[0]=new Polygon(new int[5], new int[5], 5);
    //Also adds all of the fish spawn points
    fishSpaces[0].add(10);
    fishSpaces[1].add(681/64);
    fishSpaces[0].add(1769/64);
    fishSpaces[1].add(1160/64);
    fishSpaces[0].add(2866/64);
    fishSpaces[1].add(1460/64);
    fishSpaces[0].add(3376/64);
    fishSpaces[1].add(1702/64);
    fishSpaces[0].add(3770/64);
    fishSpaces[1].add(2602/64);
    fishSpaces[0].add(371/64);
    fishSpaces[1].add(2638/64);
    fishSpaces[0].add(372/64);
    fishSpaces[1].add(1672/64);
    fishSpaces[0].add(3810/64);
    fishSpaces[1].add(3070/64);
    fishSpaces[0].add(2325/64);
    fishSpaces[1].add(3078/64);
    fishSpaces[0].add(1195/64);
    fishSpaces[1].add(3091/64);
    fishSpaces[0].add(1551/64);
    fishSpaces[1].add(2353/64);
  }
  
  //Reads save.save for any entites that need to be loaded in from previous gameplay.
  private static void readSaveFile()
  {
    try
    {
      FileReader fr = new FileReader("save.save");
      BufferedReader br = new BufferedReader(fr);
      String line;
      String nameSection;
      String xcoords;
      int ycoords;
      line=br.readLine();
      player.moneta=Integer.parseInt(line);
      while ((line=br.readLine()) != null)
      {
        //First character in a line defines if it exists in the inventory or the grid
        if (line.charAt(0)=='0')
        {
          switch(line.charAt(1))
          {
            //Second character defines its type of entity
            //The remainder of the information simply is coordinates, paired with the name and, in the case of plants, a water value for the entity
            case '0':
              nameSection=line.substring(line.indexOf(',')+1);
              xcoords=nameSection.substring(nameSection.indexOf(',')+1);
              ycoords=Integer.parseInt(xcoords.substring(xcoords.indexOf(',')+1));
              grid[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords]=new Bugs(nameSection.substring(0,nameSection.indexOf(',')), Integer.parseInt(xcoords.substring(0,xcoords.indexOf(','))), ycoords);
              break;
            case '1':
              nameSection=line.substring(line.indexOf(',')+1);
              xcoords=nameSection.substring(nameSection.indexOf(',')+1);
              ycoords=Integer.parseInt(xcoords.substring(xcoords.indexOf(',')+1));
              grid[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords]=new Fish(nameSection.substring(0,nameSection.indexOf(',')), Integer.parseInt(xcoords.substring(0,xcoords.indexOf(','))), ycoords);
              break;
            case '2':
              nameSection=line.substring(line.indexOf(',')+1);
              xcoords=nameSection.substring(nameSection.indexOf(',')+1);
              ycoords=Integer.parseInt(xcoords.substring(xcoords.indexOf(',')+1));
              grid[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords]=new FlyingBugs(nameSection.substring(0,nameSection.indexOf(',')), Integer.parseInt(xcoords.substring(0,xcoords.indexOf(','))), ycoords);
              break;
            case '3':
              xcoords=line.substring(line.indexOf(',')+1);
              ycoords=Integer.parseInt(xcoords.substring(xcoords.indexOf(',')+1));
              grid[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords]=new Hole();
              grid[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords].box.x=Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))*64;
              grid[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords].box.y=ycoords*64;
              break;
            case '4':
              nameSection=line.substring(line.indexOf(',')+1);
              xcoords=nameSection.substring(nameSection.indexOf(',')+1);
              ycoords=Integer.parseInt(xcoords.substring(xcoords.indexOf(',')+1));
              grid[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords]=new Items(nameSection.substring(0,nameSection.indexOf(',')));
              grid[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords].box.x=Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))*64;
              grid[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords].box.y=ycoords*64;
              break;
            case '5':
              nameSection=line.substring(line.indexOf(',')+1);
              xcoords=nameSection.substring(nameSection.indexOf(',')+1);
              ycoords=Integer.parseInt(xcoords.substring(xcoords.indexOf(',')+1));
              grid[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords]=new Plants(nameSection.substring(0,nameSection.indexOf(',')));
              grid[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords].box.x=Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))*64;
              grid[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords].box.y=ycoords*64;
              grid[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords].state=Integer.parseInt(line.substring(2, 3));
              grid[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords].water=Integer.parseInt(line.substring(3, line.indexOf(',')));
              break;
          }
        }
        else
        {
          switch(line.charAt(1))
          {
            case '0':
              nameSection=line.substring(line.indexOf(',')+1);
              xcoords=nameSection.substring(nameSection.indexOf(',')+1);
              ycoords=Integer.parseInt(xcoords.substring(xcoords.indexOf(',')+1));
              inventory[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords]=new Bugs(nameSection.substring(0,nameSection.indexOf(',')), 0, 0);
              break;
            case '1':
              nameSection=line.substring(line.indexOf(',')+1);
              xcoords=nameSection.substring(nameSection.indexOf(',')+1);
              ycoords=Integer.parseInt(xcoords.substring(xcoords.indexOf(',')+1));
              inventory[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords]=new Fish(nameSection.substring(0,nameSection.indexOf(',')), 0, 0);
              break;
            case '2':
              nameSection=line.substring(line.indexOf(',')+1);
              xcoords=nameSection.substring(nameSection.indexOf(',')+1);
              ycoords=Integer.parseInt(xcoords.substring(xcoords.indexOf(',')+1));
              inventory[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords]=new FlyingBugs(nameSection.substring(0,nameSection.indexOf(',')), 0, 0);
              break;
            case '4':
              nameSection=line.substring(line.indexOf(',')+1);
              xcoords=nameSection.substring(nameSection.indexOf(',')+1);
              ycoords=Integer.parseInt(xcoords.substring(xcoords.indexOf(',')+1));
              inventory[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords]=new Items(nameSection.substring(0,nameSection.indexOf(',')));
              break;
            case '5':
              nameSection=line.substring(line.indexOf(',')+1);
              xcoords=nameSection.substring(nameSection.indexOf(',')+1);
              ycoords=Integer.parseInt(xcoords.substring(xcoords.indexOf(',')+1));
              inventory[Integer.parseInt(xcoords.substring(0,xcoords.indexOf(',')))][ycoords]=new Plants(nameSection.substring(0,nameSection.indexOf(',')));
              break;
          }
        }
      }
      br.close();
    }
    catch(IOException e){} 
  }
  
  //Reads collision boxes and locations of water for the fish
  public static void walls()
  {
    String s;
    try
    {
      FileReader fr = new FileReader("walls.wall");
      BufferedReader br = new BufferedReader(fr);
      String line;
      int count=0;
      while ((line=br.readLine()) != null)
      {
        //Pulls out the coordinates of rectangles for world collision
        String a=line.substring(line.indexOf(',')+1);
        String b=a.substring(a.indexOf(',')+1);
        String c=b.substring(b.indexOf(',')+1);
        worldWalls[count]=new Rectangle(Integer.parseInt(line.substring(0,line.indexOf(','))), Integer.parseInt(a.substring(0,a.indexOf(','))), Integer.parseInt(b.substring(0,b.indexOf(','))), Integer.parseInt(c));
        count++;
      }
      fr = new FileReader("trees.wall");
      br = new BufferedReader(fr);
      while ((line=br.readLine()) != null)
      {
        //Same thing, but because it is a tree it only requires one coordinate because all trees are the same height
        String a=line.substring(line.indexOf(',')+1);
        worldWalls[count]=new Rectangle(Integer.parseInt(line.substring(0,line.indexOf(','))), Integer.parseInt(a), 64,128);
        count++;
      }
      fr = new FileReader("water.wall");
      br = new BufferedReader(fr);
      count=0;
      int pointNumber=-1;
      while ((line=br.readLine()) != null)
      {
        //Reading various points of a polygon, seperated by a space to state the closing of a shape. 
        if (line.equals(""))
        {
          count++;
          pointNumber=-1;
        }
        //The number following the space represents the number of points in the next polygon
        else if (pointNumber==-1)
        {
          water[count]=new Polygon(new int[Integer.parseInt(line)], new int[Integer.parseInt(line)], Integer.parseInt(line));
          pointNumber++;
        }
        else
        {
          water[count].xpoints[pointNumber]=Integer.parseInt(line.substring(0,line.indexOf(',')));
          water[count].ypoints[pointNumber]=Integer.parseInt(line.substring(line.indexOf(',')+1));
          pointNumber++;
        }
      }
    }
    catch(IOException e){} 
  }
  
  private static void openSpace()
  {
    String s;
    try
    {
      //Reads every value of openSpaces.wall and pulls out the x and y location of the grid that an entity can spawn on
      FileReader fr = new FileReader("openSpaces.wall");
      BufferedReader br = new BufferedReader(fr);
      String line;
      int count=0;
      while ((line=br.readLine()) != null)
      {
        int a=Integer.parseInt(line.substring(0,line.indexOf(',')));
        int b=Integer.parseInt(line.substring(line.indexOf(',')+1));
        openSpaces[0].add(a);
        openSpaces[1].add(b);
      }
    }
    catch(IOException e){} 
  }
  
  public static void spawn(int type)
  {
    //Spawns a random type of entity amoungst the given type, 1 for flyingbugs, 2 for bugs, 3 for fish, 4 for villagers,
    //and 5 for fossils
    if (type==1)
    {
      //For flying bugs, so long as they spawn somewhere within the boundary they are fine since they have no collision
      //except with the border.
      int tempx=(int)(Math.random()*74+1);
      int tempy=(int)(Math.random()*54+1);
      if (Math.random()<0.3)
        flyingBugs[tempx][tempy].add(new FlyingBugs("dragonfly",tempx,tempy));
      else if (Math.random()<0.5)
        flyingBugs[tempx][tempy].add(new FlyingBugs("butterfly",tempx,tempy));
      else
        flyingBugs[tempx][tempy].add(new FlyingBugs("fly",tempx,tempy));
    }
    else if (type==2)
    {
      //bugs, villagers, and fossils use the openSpaces array to pick a location of spawning
      int spawnIndex=(int)(Math.random()*openSpaces[0].size());
      int tempx=openSpaces[0].get(spawnIndex);
      int tempy=openSpaces[1].get(spawnIndex);
      if (Math.random()<0.3)
        bugs[tempx][tempy].add(new Bugs("beetle",tempx,tempy));
      else if (Math.random()<0.5)
        bugs[tempx][tempy].add(new Bugs("grasshopper",tempx,tempy));
      else
        bugs[tempx][tempy].add(new Bugs("ladybug",tempx,tempy));
    }
    else if (type==3)
    {
      //Fish use their specific fish spawn points.
      int spawnIndex=(int)(Math.random()*fishSpaces[0].size());
      int tempx=fishSpaces[0].get(spawnIndex);
      int tempy=fishSpaces[1].get(spawnIndex);
      if (spawnIndex<7)
        fish[tempx][tempy].add(new Fish("salmon",tempx,tempy));
      else if (spawnIndex<10)
        fish[tempx][tempy].add(new Fish("seabass",tempx,tempy));
      else
        fish[tempx][tempy].add(new Fish("crayfish",tempx,tempy));
    }
    else if (type==5)
    {
      int spawnIndex=(int)(Math.random()*openSpaces[0].size());
      int tempx=openSpaces[0].get(spawnIndex);
      int tempy=openSpaces[1].get(spawnIndex);
      if (Math.random()<0.5)
        villagers[tempx][tempy].add(new Villagers("stan",tempx,tempy));
      else if (Math.random()<0.5)
        villagers[tempx][tempy].add(new Villagers("clide",tempx,tempy));
      else
        villagers[tempx][tempy].add(new Villagers("jenny",tempx,tempy));
    }
    else
    {
      int spawnIndex=(int)(Math.random()*openSpaces[0].size());
      int tempx=openSpaces[0].get(spawnIndex);
      int tempy=openSpaces[1].get(spawnIndex);
      grid[tempx][tempy]=new Items("fossil");
      grid[tempx][tempy].box.x=tempx*64;
      grid[tempx][tempy].box.y=tempy*64;
    }
  }
  
  //The main method that loops over and over for the entire game.
  private static void worldUpdate()
    throws InterruptedException
  {
    //So long as we have not quit,
    while(quit)
    {
      //We start a timer to calculate our wait inbetween frames of the game
      long time=System.nanoTime();
      //We increase our growth timer, which will cause plants to update every 600 loops
      growTimer++;
      if (growTimer==600)
      {
        growTimer=0;
        for(int a=0; a<76; a++)
        {
          for (int b=0; b<56; b++)
          {
            if (grid[a][b] instanceof Plants)
              grid[a][b].update();
          }
        }
      }
      //If the player is not using any tools, they may also update
      if (Crossing.player.dig==0 && Crossing.player.net==0 && Crossing.player.can==0 && Crossing.player.rod==0)
        player.move();
      //and every four loops the game will update the AI's movement, this is slower than the player because they have complex
      //patterns that do not need to necessaraly update every frame, or else the game might run much slower.
      if (growTimer%4==0)
        movement();
      //The graphix class reruns its paint method for updating the graphics
      frame.repaint();
      //The game sleeps for deltatime, making it so framerate stays as constant as possible
      time=20-(int)((System.nanoTime()-time)*0.000001);
      if (time>0)
        Thread.sleep(time);
    }
  }
  
  //All of the entities that exist on the grid (therefor require AI) are allowed run their AI
  private static void movement()
  {
    for (int a=0; a<76; a++)
    {
      for (int b=0; b<56; b++)
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
  
  //The entire grid, inventory, and money is stored to the save.save file for future use
  public static void save()
  {
    String s;
    try
    {
      FileWriter fw = new FileWriter("save.save");
      PrintWriter pw = new PrintWriter(fw);
      //First line is money
      pw.println(player.moneta);
      for(int a=0; a<76; a++)
      {
        for (int b=0; b<56; b++)
        {
          //Then everything in the grid and villagers are added with their specific spacings and required information
          //for replication, including location and state in some cases.
          if (grid[a][b]!=null)
          {
            if (grid[a][b] instanceof Bugs)
              pw.println("00" + grid[a][b].s + "," + a + "," + b);
            else if (grid[a][b] instanceof Fish)
              pw.println("01," + grid[a][b].s + "," + a + "," + b);
            else if (grid[a][b] instanceof FlyingBugs)
              pw.println("02," + grid[a][b].s + "," + a + "," + b);
            else if (grid[a][b] instanceof Hole)
              pw.println("03," + a + "," + b);
            else if (grid[a][b] instanceof Items)
              pw.println("04," + grid[a][b].s + "," + a + "," + b);
            else
              pw.println("05" + grid[a][b].state + grid[a][b].water + "," + grid[a][b].s + "," + a + "," + b);
          }
          if (a<6 && b<4)
          {
            if (inventory[a][b]!=null)
            {
              if (inventory[a][b] instanceof Bugs)
                pw.println("10," + inventory[a][b].s + "," + a + "," + b);
              else if (inventory[a][b] instanceof Fish)
                pw.println("11," + inventory[a][b].s + "," + a + "," + b);
              else if (inventory[a][b] instanceof FlyingBugs)
                pw.println("12," + inventory[a][b].s + "," + a + "," + b);
              else if (inventory[a][b] instanceof Hole)
                pw.println("13," + a + "," + b);
              else if (inventory[a][b] instanceof Items)
                pw.println("14," + inventory[a][b].s + "," + a + "," + b);
              else
                pw.println("15," + inventory[a][b].s + "," + a + "," + b);
            }
          }
        }
      }
      pw.close();
    }
    catch(IOException e){} 
  }
}