import javax.swing.JFrame;
import java.awt.Rectangle;
import java.awt.Polygon;
import java.util.ArrayList;
import java.io.*;
/*
 * This is the main class, which is where everything is initialized and set into motion
 * Please read this in the programs chronological order, AKA skip straight to any method that is called and come back when you finish reading the method
 * EXCEPT FOR IF IT REFERS TO ANOTHER CLASS
 * Once you have finished reading this, read Graphix, Entity and then Bugs. Everything else is the same as bugs except with different methods.
 */

//Check the following:

//Project Charter
//Team Code of Conduct (team norms)
//Work Breakdown Stucture
//Risk Management Plan
//Use Cases
//UML Diagram (original and updated final)
//Any additional Design Documents you used

//Write Reflection Document

//fix bug boundaries
//fix world spawn locations (using commented method, ez)
//reduce fossil spawns
//add various bug animations with timers
//add villager animations and textures
//add use animations to the player
//fix string for bobber
//solve villager / fish / bug array error
//clean and comment code
//add instructions menu?
public class Crossing
{
  static final int PLAYERLOCATION=368;
  static JFrame frame = new JFrame("Crossing v0.2");
  static ArrayList<Bugs>[][] bugs = new ArrayList[76][56];
  static ArrayList<FlyingBugs>[][] flyingBugs = new ArrayList[76][56];
  static ArrayList<Fish>[][] fish = new ArrayList[76][56];
  static ArrayList<Villagers>[][] villagers = new ArrayList[76][56];
  static Entity[][] grid = new Entity[76][56];
  static Entity[][] inventory = new Entity[6][4];
  static Rectangle npcBoundaries[] = new Rectangle[1];
  static Rectangle worldWalls[] = new Rectangle[87];
  static Polygon water[] = new Polygon[5];
  static Player player = new Player();
  static Rectangle[] shopWalls = new Rectangle[4];
  static Items bobber = new Items("bobber");
  static Fish caught;
  static int growTimer=0;
  static boolean shopping=false;
  static Rectangle door = new Rectangle(4052, 660, 64, 64);
  static Rectangle shopKeeper = new Rectangle (5380, 4102, 64, 64);
  static Rectangle sign = new Rectangle(1000, 1000, 64, 64);
  static Rectangle bridgeOne=new Rectangle(2770,1165,86,480);
  static Rectangle bridgeTwo=new Rectangle(3522,2232,445,86);
  static Rectangle quitGame = new Rectangle(900,904,64,64);
  static ArrayList<Integer>[] openSpaces = new ArrayList[2];
  static ArrayList<Integer>[] fishSpaces = new ArrayList[2];
  static boolean quit=true;
  static boolean invFull=false;
  
  public static void main(String [] args)
    throws InterruptedException
  {
    fillArray();
    frame();
    readSaveFile();
    walls();
    trees();
    water();
    openSpace();
    for (int a=0; a<5; a++)
    {
      for (int b=0; b<9; b++)
        spawn(a);
    }
    spawn(5);
    inventory[0][2]=new Items("rod");
    inventory[0][0]=new Items("shovel");
    worldUpdate();
    save();
    System.exit(0);
  }
  
  private static void trees()
  {
    String s;
    try
    {
      FileReader fr = new FileReader("trees.wall");
      BufferedReader br = new BufferedReader(fr);
      String line;
      int count=54;
      while ((line=br.readLine()) != null)
      {
        String a=line.substring(line.indexOf(',')+1);
        worldWalls[count]=new Rectangle(Integer.parseInt(line.substring(0,line.indexOf(','))), Integer.parseInt(a), 64,128);
        count++;
      }
    }
    catch(IOException e){} 
  }
  private static void openSpace()
  {
    String s;
    try
    {
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
//  private static void outPut()
//  {
//    try
//    {
//      FileWriter fw = new FileWriter("openSpaces.wall");
//      PrintWriter pw = new PrintWriter(fw);
//      Rectangle one=new Rectangle(1432,830,153,288);
//      Rectangle two=new Rectangle(3152,1524,119,190);
//      for (int a=10; a<25; a++)
//      {
//        for (int b=13; b<46; b++)
//        {
//          marker:{
//            Rectangle temp=new Rectangle(a*64, b*64, 64, 64);
//            for (Rectangle x:Crossing.worldWalls)
//            {
//              if (x.intersects(temp))
//                break marker;
//            }
//            for (Polygon x:Crossing.water)
//            {
//              if (x.intersects(temp))
//                break marker;
//            }
//            if (one.intersects(temp))
//              break marker;
//            if (two.intersects(temp))
//              break marker;
//            pw.println(a + "," +b);
//          }
//        }
//      }
//      for (int a=25; a<69; a++)
//      {
//        for (int b=9; b<48; b++)
//        {
//          markerT:{
//            Rectangle temp=new Rectangle(a*64, b*64, 64, 64);
//            for (Rectangle x:Crossing.worldWalls)
//            {
//              if (x.intersects(temp))
//                break markerT;
//            }
//            for (Polygon x:Crossing.water)
//            {
//              if (x.intersects(temp))
//                break markerT;
//            }
//            if (one.intersects(temp))
//              break markerT;
//            if (two.intersects(temp))
//              break markerT;
//            pw.println(a + "," +b);
//          }
//        }
//      }
//      pw.close();
//    }
//    catch(Exception e){}
//  }
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
        if (line.charAt(0)=='0')
        {
          switch(line.charAt(1))
          {
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
    shopWalls[0]=new Rectangle(5380,4102,517,513);
    shopWalls[1]=new Rectangle(5547,4102,349,69);
    shopWalls[2]=new Rectangle(5547,4253,349,69);
    shopWalls[3]=new Rectangle(5548,4415,349,69);
    openSpaces[0]=new ArrayList<Integer>();
    openSpaces[1]=new ArrayList<Integer>();
    fishSpaces[0]=new ArrayList<Integer>();
    fishSpaces[1]=new ArrayList<Integer>();
    npcBoundaries[0]=new Rectangle(512,512,2500, 2500);
    water[0]=new Polygon(new int[5], new int[5], 5);
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
//    water[0].xpoints[0]=800;
//    water[0].xpoints[1]=900;
//    water[0].xpoints[2]=850;
//    water[0].xpoints[3]=750;
//    water[0].xpoints[4]=700;
//    water[0].ypoints[0]=800;
//    water[0].ypoints[1]=900;
//    water[0].ypoints[2]=1000;
//    water[0].ypoints[3]=1000;
//    water[0].ypoints[4]=900;
    worldWalls[0]=new Rectangle(0,0,10, 10);
  }
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
        String a=line.substring(line.indexOf(',')+1);
        String b=a.substring(a.indexOf(',')+1);
        String c=b.substring(b.indexOf(',')+1);
        worldWalls[count]=new Rectangle(Integer.parseInt(line.substring(0,line.indexOf(','))), Integer.parseInt(a.substring(0,a.indexOf(','))), Integer.parseInt(b.substring(0,b.indexOf(','))), Integer.parseInt(c));
        count++;
      }
    }
    catch(IOException e){} 
  }
  public static void water()
  {
    String s;
    try
    {
      FileReader fr = new FileReader("water.wall");
      BufferedReader br = new BufferedReader(fr);
      String line;
      int waterNumber=0;
      int pointNumber=-1;
      while ((line=br.readLine()) != null)
      {
        if (line.equals(""))
        {
          waterNumber++;
          pointNumber=-1;
        }
        else if (pointNumber==-1)
        {
          water[waterNumber]=new Polygon(new int[Integer.parseInt(line)], new int[Integer.parseInt(line)], Integer.parseInt(line));
          pointNumber++;
        }
        else
        {
          water[waterNumber].xpoints[pointNumber]=Integer.parseInt(line.substring(0,line.indexOf(',')));
          water[waterNumber].ypoints[pointNumber]=Integer.parseInt(line.substring(line.indexOf(',')+1));
          pointNumber++;
        }
      }
    }
    catch(IOException e){} 
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
    while(quit)
    {
      long time=System.nanoTime();
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
      player.move();
      if (growTimer%4==0)
        movement();
      frame.repaint();
      time=20-(int)((System.nanoTime()-time)*0.000001);
      if (time>0)
        Thread.sleep(time);
    }
  }
  
  public static void save()
  {
    String s;
    try
    {
      FileWriter fw = new FileWriter("save.save");
      PrintWriter pw = new PrintWriter(fw);
      pw.println(player.moneta);
      for(int a=0; a<76; a++)
      {
        for (int b=0; b<56; b++)
        {
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
  public static void spawn(int type)
  {
    if (type==1)
    {
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
      if (Math.random()<0.3)
        villagers[tempx][tempy].add(new Villagers("a",tempx,tempy));
      else if (Math.random()<0.5)
        villagers[tempx][tempy].add(new Villagers("b",tempx,tempy));
      else
        villagers[tempx][tempy].add(new Villagers("c",tempx,tempy));
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
}