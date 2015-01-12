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
//ladybug spawning on plants
//money money money
//make menus
//make a quit game object
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
  static Polygon water[] = new Polygon[1];
  static int[][] spawn = new int[12][2];
  static int[][] fishSpawn = new int[12][2];
  static Player player = new Player();
  static Items bobber = new Items("bobber");
  static Fish caught;
  static int growTimer=0;
  static boolean shopping=false;
  static Rectangle door = new Rectangle(640, 640, 64, 64);
  static Rectangle shopKeeper = new Rectangle (5000, 4800, 64, 64);
  static boolean quit=true;
  
  public static void main(String [] args)
    throws InterruptedException
  {
    fillArray();
    frame();
    readSaveFile();
    inventory[0][0]= new Plants("plant");
    //inventory[3][2]=new Items("net");
   // inventory[1][1]=new Plants("plant seeds");
    for (int a=1; a<3; a++)
    {
      for (int b=0; b<9; b++)
        spawn(a);
    }
    spawn(4);
    worldUpdate();
    save();
    System.exit(0);
  }
  
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
          grid[a][b].box.x=a*64;
          grid[a][b].box.y=b*64;
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
    npcBoundaries[0]=new Rectangle(512,512,2500, 2500);
    water[0]=new Polygon(new int[5], new int[5], 5);
    water[0].xpoints[0]=800;
    water[0].xpoints[1]=900;
    water[0].xpoints[2]=850;
    water[0].xpoints[3]=750;
    water[0].xpoints[4]=700;
    water[0].ypoints[0]=800;
    water[0].ypoints[1]=900;
    water[0].ypoints[2]=1000;
    water[0].ypoints[3]=1000;
    water[0].ypoints[4]=900;
    worldWalls[0]=new Rectangle(0,0,10, 10);
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
        for(int a=0; a<60; a++)
        {
          for (int b=0; b<40; b++)
          {
            if (grid[a][b] instanceof Plants)
              grid[a][b].update();
          }
        }
      }
      movement();
      frame.repaint();
      time=100-(int)((System.nanoTime()-time)*0.000001);
      if (time<=0)
        Thread.sleep(0);
      else
        Thread.sleep(100);
    }
  }
  
  public static void save()
  {
    String s;
    try
    {
      FileWriter fw = new FileWriter("save.save");
      PrintWriter pw = new PrintWriter(fw);
      for(int a=0; a<60; a++)
      {
        for (int b=0; b<40; b++)
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
        flyingBugs[spawn[nearPlayer.get(temp)][0]/64][spawn[nearPlayer.get(temp)][1]/64].add(new FlyingBugs("butterfly", spawn[nearPlayer.get(temp)][0]/64, spawn[nearPlayer.get(temp)][1]/64));
      }
      else if (Math.random()<0.5)
      {
        int temp=(int)(Math.random()*nearPlayer.size());
        System.out.println(spawn[nearPlayer.get(temp)][0] + ", " + spawn[nearPlayer.get(temp)][1]);
        flyingBugs[spawn[nearPlayer.get(temp)][0]/64][spawn[nearPlayer.get(temp)][1]/64].add(new FlyingBugs("dragonfly", spawn[nearPlayer.get(temp)][0]/64, spawn[nearPlayer.get(temp)][1]/64));
      }
      else
      {
        int temp=(int)(Math.random()*nearPlayer.size());
        System.out.println(spawn[nearPlayer.get(temp)][0] + ", " + spawn[nearPlayer.get(temp)][1]);
        flyingBugs[spawn[nearPlayer.get(temp)][0]/64][spawn[nearPlayer.get(temp)][1]/64].add(new FlyingBugs("fly", spawn[nearPlayer.get(temp)][0]/64, spawn[nearPlayer.get(temp)][1]/64));
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
        fish[fishSpawn[nearPlayer.get(temp)][0]/64][fishSpawn[nearPlayer.get(temp)][1]/64].add(new Fish("seabass", fishSpawn[nearPlayer.get(temp)][0]/64, fishSpawn[nearPlayer.get(temp)][1]/64));
      }
      else if (Math.random()<0.5)
      {
        int temp=(int)(Math.random()*nearPlayer.size());
        fish[fishSpawn[nearPlayer.get(temp)][0]/64][fishSpawn[nearPlayer.get(temp)][1]/64].add(new Fish("salmon", fishSpawn[nearPlayer.get(temp)][0]/64, fishSpawn[nearPlayer.get(temp)][1]/64));
      }
      else
      {
        int temp=(int)(Math.random()*nearPlayer.size());
        fish[fishSpawn[nearPlayer.get(temp)][0]/64][fishSpawn[nearPlayer.get(temp)][1]/64].add(new Fish("crayfish", fishSpawn[nearPlayer.get(temp)][0]/64, fishSpawn[nearPlayer.get(temp)][1]/64));
      }
    }
    else if (type==4)
    {
      int temp=(int)(Math.random()*12);
      int tempb=temp;
      villagers[spawn[temp][0]/64][spawn[temp][1]/64].add(new Villagers("villager1", spawn[temp][0]/64, spawn[temp][1]/64));
      while(tempb==temp)
        tempb=(int)(Math.random()*12);
      villagers[spawn[temp][0]/64][spawn[temp][1]/64].add(new Villagers("villager2", spawn[temp][0]/64, spawn[temp][1]/64));
      tempb=temp;
      while(tempb==temp)
        tempb=(int)(Math.random()*12);
      villagers[spawn[temp][0]/64][spawn[temp][1]/64].add(new Villagers("villager3", spawn[temp][0]/64, spawn[temp][1]/64));
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