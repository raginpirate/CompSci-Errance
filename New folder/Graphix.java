import java.awt.Graphics2D;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import java.awt.Color;
import javax.imageio.*;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Polygon;
//This class represents the graphical and physical components for the Jframe, which does drawing of the images and creation of the key and action maps
public class Graphix extends JPanel
{
  static BufferedImage hole = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
  static BufferedImage back = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
  static BufferedImage door = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
  static BufferedImage inventory = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
  static BufferedImage menu2 = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
  static BufferedImage menu3 = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
  static BufferedImage menu4 = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
  static BufferedImage menu5 = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
  static BufferedImage menuPointer = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
  static BufferedImage star = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
  static BufferedImage fileNotFound = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
  //Constructor that creates the action and key maps for the game when the component is first created and loads images
  public Graphix()
  {
    Action up = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e) 
      {
        Crossing.player.input("up");
      }
    };
    Action down = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e)
      {
        Crossing.player.input("down");
      }
    };
    Action left = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e) 
      {
        Crossing.player.input("left");
      }
    };
    Action right = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e) 
      {
        Crossing.player.input("right");
      }
    };
    Action upR = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e) 
      {
        Crossing.player.release("up");
      }
    };
    Action downR = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e) 
      {
        Crossing.player.release("down");
      }
    };
    Action leftR = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e) 
      {
        Crossing.player.release("left");
      }
    };
    Action rightR = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e) 
      {
        Crossing.player.release("right");
      }
    };
    Action rR = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e) 
      {
        Crossing.player.release("r");
      }
    };
    Action space = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e)
      {
        Crossing.player.spaceBar();
      }
    };
    Action q = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e) 
      {
        Crossing.player.input("q");
      }
    };
    Action w = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e) 
      {
        Crossing.player.input("w");
      }
    };
    Action e = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e) 
      {
        Crossing.player.input("e");
      }
    };
    Action r = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e) 
      {
        Crossing.player.input("r");
      }
    };
    this.getInputMap().put(KeyStroke.getKeyStroke(38, 0), "up");
    this.getActionMap().put("up", up);
    this.getInputMap().put(KeyStroke.getKeyStroke(40, 0), "down");
    this.getActionMap().put("down", down);
    this.getInputMap().put(KeyStroke.getKeyStroke(37, 0), "left");
    this.getActionMap().put("left", left);
    this.getInputMap().put(KeyStroke.getKeyStroke(39, 0), "right");
    this.getActionMap().put("right", right);
    this.getInputMap().put(KeyStroke.getKeyStroke(38, 0, true), "upR");
    this.getActionMap().put("upR", upR);
    this.getInputMap().put(KeyStroke.getKeyStroke(40, 0, true), "downR");
    this.getActionMap().put("downR", downR);
    this.getInputMap().put(KeyStroke.getKeyStroke(37, 0, true), "leftR");
    this.getActionMap().put("leftR", leftR);
    this.getInputMap().put(KeyStroke.getKeyStroke(39, 0, true), "rightR");
    this.getActionMap().put("rightR", rightR);
    this.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), " ");
    this.getActionMap().put(" ", space);
    this.getInputMap().put(KeyStroke.getKeyStroke("Q"), "q");
    this.getActionMap().put("q", q);
    this.getInputMap().put(KeyStroke.getKeyStroke("W"), "w");
    this.getActionMap().put("w", w);
    this.getInputMap().put(KeyStroke.getKeyStroke("E"), "e");
    this.getActionMap().put("e", e);
    this.getInputMap().put(KeyStroke.getKeyStroke(82, 0, true), "rR");
    this.getActionMap().put("rR", rR);
    this.getInputMap().put(KeyStroke.getKeyStroke("R"), "r");
    this.getActionMap().put("r", r);
    setFocusable(true);
    try {
      door = ImageIO.read(new File("door.png"));
      hole = ImageIO.read(new File("hole.jpg"));
      back = ImageIO.read(new File("CollisionMap.png"));
      fileNotFound = ImageIO.read(new File("fileNotFound.png"));
      inventory = ImageIO.read(new File("inventory.png"));
      menu2 = ImageIO.read(new File("menu2.png"));
      menu3 = ImageIO.read(new File("menu3.png"));
      menu4 = ImageIO.read(new File("menu4.png"));
      menu5 = ImageIO.read(new File("menu5.png"));
      menuPointer = ImageIO.read(new File("menuPointer.png"));
      star = ImageIO.read(new File("star.jpg"));
    } catch (Exception p) {}
  }
  
  public static BufferedImage buffer(String s)
  {
    try{
      return ImageIO.read(new File(s));
    }
    catch (Exception p){
      return fileNotFound;
    }
  }
  
  //Overrides the JPane paint method, uses the painting method from Crossing to do the actual drawing
  public void paint(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;
    super.paintComponent(g);
    g2d.drawImage(back, (int)(Crossing.PLAYERLOCATION-Crossing.player.box.x), (int)(Crossing.PLAYERLOCATION-Crossing.player.box.y), this);
    g2d.setColor(Color.black);
    for (Rectangle x:Crossing.worldWalls)
      g2d.drawRect(x.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, x.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, x.width, x.height);
    g2d.setColor(Color.blue);
    for (Polygon x:Crossing.water)
    {
      x.translate(0-Crossing.player.box.x+Crossing.PLAYERLOCATION, 0-Crossing.player.box.y+Crossing.PLAYERLOCATION);
      g2d.drawPolygon(x);
      x.translate(Crossing.player.box.x-Crossing.PLAYERLOCATION, Crossing.player.box.y-Crossing.PLAYERLOCATION);
    }
    g2d.drawImage(door, Crossing.door.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, Crossing.door.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
    g2d.fillRect(Crossing.shopKeeper.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, Crossing.shopKeeper.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64);
    Crossing.bobber.paint(g);
    if (Crossing.shopping==false)
    {
      for (int a=-8; a<8; a++)
      {
        for (int b=-8; b<8; b++)
        {
          if (Crossing.grid[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b]!=null)
            Crossing.grid[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].paint(g);
        }
      }
      for (int a=-8; a<8; a++)
      {
        for (int b=-8; b<8; b++)
        {
          for (int x=0; x<Crossing.fish[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].size(); x++)
            Crossing.fish[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].get(x).paint(g);
          for (int x=0; x<Crossing.bugs[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].size(); x++)
            Crossing.bugs[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].get(x).paint(g);
        }
      }
      for (int a=-8; a<8; a++)
      {
        for (int b=-8; b<8; b++)
        {
          for (int x=0; x<Crossing.villagers[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].size(); x++)
            Crossing.villagers[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].get(x).paint(g);
        }
      }
      g2d.setColor(Color.orange);
      g2d.fillRect(Crossing.PLAYERLOCATION, Crossing.PLAYERLOCATION, 64, 64);
      for (int a=-8; a<8; a++)
      {
        for (int b=-8; b<8; b++)
        {
          for (int x=0; x<Crossing.flyingBugs[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].size(); x++)
            Crossing.flyingBugs[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].get(x).paint(g);
        }
      }
    }
    if (Crossing.player.menu>0)
    {
      g2d.drawImage(inventory, null, 80, 373);
      for (int a=0; a<6; a++)
      {
        for (int b=1; b<4; b++)
        {
          if (Crossing.inventory[a][b] != null)
            g2d.drawImage(Crossing.inventory[a][b].image, 158+a*90,526+b*58,32,32, this);
        }
      }
      if (Crossing.inventory[0][0] != null)
        g2d.drawImage(Crossing.inventory[0][0].image, 384, 384, 32, 32, this);
      if (Crossing.player.held==true)
        g2d.setColor(Color.red);
      else
        g2d.setColor(Color.blue);
      if (Crossing.player.selected1y==0)
      {
        g2d.drawRect(384, 384, 32, 32);
        if (Crossing.inventory[0][0]!=null)
          g2d.drawString(Crossing.inventory[0][0].s, 400, 380);
      }
      else
      {
        g2d.drawRect(Crossing.player.selected1x*90+158,Crossing.player.selected1y*58+526,32,32);
        if (Crossing.inventory[Crossing.player.selected1x][Crossing.player.selected1y]!=null)
          g2d.drawString(Crossing.inventory[Crossing.player.selected1x][Crossing.player.selected1y].s, Crossing.player.selected1x*90+174, Crossing.player.selected1y*58+522);
      }
      if (Crossing.player.held==true)
      {
        if (Crossing.player.heldy==0)
          g2d.drawRect(384, 384, 32, 32);
        else
          g2d.drawRect(Crossing.player.heldx*90+158,Crossing.player.heldy*58+526,32,32);
      }
      switch(Crossing.player.menu)
      {
        case 1: break;
        case 2: if (Crossing.player.selected1y!=0)
        {
          g2d.drawImage(menu2, null, Crossing.player.selected1x*90+190,Crossing.player.selected1y*58+492);
          g2d.drawImage(menuPointer, null, Crossing.player.selected1x*90+185,Crossing.player.selected1y*58+503+Crossing.player.selected2*31);
        }
        else
        {
          g2d.drawImage(menu2, null, 416,350);
          g2d.drawImage(menuPointer, null, 411,360+Crossing.player.selected2*31);
        }
        break;
        case 3: if (Crossing.player.selected1y!=0)
        {
          g2d.drawImage(menu3, null, Crossing.player.selected1x*90+190,Crossing.player.selected1y*58+478);
          g2d.drawImage(menuPointer, null, Crossing.player.selected1x*90+185,Crossing.player.selected1y*58+490+Crossing.player.selected2*30);
        }
        else
        {
          g2d.drawImage(menu3, null, 416,336);
          g2d.drawImage(menuPointer, null, 411,347+Crossing.player.selected2*30);
        }
        break;
        case 4: if (Crossing.player.selected1y!=0)
        {
          g2d.drawImage(menu4, null, Crossing.player.selected1x*90+190,Crossing.player.selected1y*58+478);
          g2d.drawImage(menuPointer, null, Crossing.player.selected1x*90+183,Crossing.player.selected1y*58+490+Crossing.player.selected2*30);
        }
        else
        {
          g2d.drawImage(menu4, null, 416,336);
          g2d.drawImage(menuPointer, null, 411,347+Crossing.player.selected2*30);
        }
        break;
        case 5:if (Crossing.player.selected1y!=0)
        {
          g2d.drawImage(menu5, null, Crossing.player.selected1x*90+190,Crossing.player.selected1y*58+461);
          g2d.drawImage(menuPointer, null, Crossing.player.selected1x*90+180+5,Crossing.player.selected1y*58+465+5+Crossing.player.selected2*31);
        }
        else
        {
          g2d.drawImage(menu5, null, 416,319);
          g2d.drawImage(menuPointer, null, 411,329+Crossing.player.selected2*31);
        }
        break;
        //case 6:g2d.drawImage(menu6, null, 416,400);
      }
    }
  }
}