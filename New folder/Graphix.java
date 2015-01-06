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
//This class represents the graphical and physical components for the Jframe, which does drawing of the images and creation of the key and action maps
public class Graphix extends JPanel
{
  static BufferedImage wall = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
  static BufferedImage back = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
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
    this.getInputMap().put(KeyStroke.getKeyStroke("R"), "r");
    this.getActionMap().put("r", r);
    setFocusable(true);
    try {
      wall = ImageIO.read(new File("wall.jpg"));
      back = ImageIO.read(new File("ColisionMap.jpg"));
      fileNotFound = ImageIO.read(new File("fileNotFound.jpg"));
      //inventory = ImageIO.read(new File("inventory.jpg"));
      menu2 = ImageIO.read(new File("menu2.jpg"));
      menu3 = ImageIO.read(new File("menu3.jpg"));
      menu4 = ImageIO.read(new File("menu4.jpg"));
      menu5 = ImageIO.read(new File("menu5.jpg"));
      menuPointer = ImageIO.read(new File("menuPointer.jpg"));
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
    g2d.setColor(Color.orange);
    g2d.fillRect(Crossing.PLAYERLOCATION, Crossing.PLAYERLOCATION, 64, 64);
    for (int a=-8; a<8; a++)
    {
      for (int b=-8; b<8; b++)
      {
        if (Crossing.grid[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b] instanceof Hole)
          g2d.drawImage(wall, (int)(Crossing.player.box.x/64+a)*64-Crossing.player.box.x+Crossing.PLAYERLOCATION, (int)(Crossing.player.box.y/64+b)*64-Crossing.player.box.y+Crossing.PLAYERLOCATION, this);
        for (int x=0; x<Crossing.bugs[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].size(); x++)
          Crossing.bugs[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].get(x).paint(g);
        for (int x=0; x<Crossing.flyingBugs[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].size(); x++)
          Crossing.flyingBugs[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].get(x).paint(g);
        for (int x=0; x<Crossing.fish[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].size(); x++)
          Crossing.fish[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].get(x).paint(g);
        for (int x=0; x<Crossing.villagers[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].size(); x++)
          Crossing.villagers[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].get(x).paint(g);
        if (Crossing.grid[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b]!=null)
          Crossing.grid[Crossing.player.box.x/64+a][Crossing.player.box.y/64+b].paint(g);
      }
    }
    if (Crossing.player.menu>0)
    {
      g2d.drawImage(inventory, null, 100, 500); //600p by 200p
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
      g2d.setColor(Color.blue);
      if (Crossing.player.selected1y==0)
        g2d.drawRect(384, 384, 32, 32);
      else
      g2d.drawRect(Crossing.player.selected1x*90+158,Crossing.player.selected1y*58+526,32,32);
      switch(Crossing.player.menu)
      {
        case 1: break;
        case 2: g2d.drawImage(menu2, null, Crossing.player.selected1x*90+190,Crossing.player.selected1y*58+542);
        g2d.drawImage(menuPointer, null, Crossing.player.selected1x*90+190+5,Crossing.player.selected1y*58+542+5+Crossing.player.selected2*20);
          break;
        case 3: g2d.drawImage(menu3, null, Crossing.player.selected1x*90+190,Crossing.player.selected1y*58+542);
        g2d.drawImage(menuPointer, null, Crossing.player.selected1x*90+190+5,Crossing.player.selected1y*58+542+5+Crossing.player.selected2*20);
          break;
        case 4: g2d.drawImage(menu4, null, Crossing.player.selected1x*90+190,Crossing.player.selected1y*58+542);
        g2d.drawImage(menuPointer, null, Crossing.player.selected1x*90+190+5,Crossing.player.selected1y*58+542+5+Crossing.player.selected2*20);
          break;
        case 5: g2d.drawImage(menu5, null, Crossing.player.selected1x*90+190,Crossing.player.selected1y*58+542);
        g2d.drawImage(menuPointer, null, Crossing.player.selected1x*90+190+5,Crossing.player.selected1y*58+542+5+Crossing.player.selected2*20);
          break;
      }
    }
  }
}