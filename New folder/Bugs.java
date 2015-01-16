import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Polygon;
/*
 * File Name: Bugs.java
 * Date: Friday, January 16th, 2015
 * Programmed by: Orange Bannana (Daniel Wyckoff, Joshua Anandappa, Izzy Snowden).
 * Description: An openworld free-roaming game inwhich the player can interact with the enviroment using the space bar, move with the arrow keys,
 * open the inventory with q, and sprint with e. This class deals with the bug entitys AI.
 * 
 * PLEASE REFERENCE Entity.java for general information about how this class works, along with a few others.
 * 
 * To be used in conjunction with Crossing.java, Entity.java, Fish.java, FlyingBugs.java, Graphix.java, Hole.java, Items.java, Plants.java, Player.java, and Villagers.java
 */
public class Bugs extends Entity
{
  //basic three animation phases
  private BufferedImage ani1;
  private BufferedImage ani2;
  private BufferedImage ani3;
  //angles and rotation locations for smooth transitions between movements
  private double angle=2;
  private double rotation=0;
  private double newAngle=0;
  //various counters for the AI algorithm
  private int count=0;
  private int animation=0;
  private int aniAdd=1;
  private int maxLoops=0;
  private int waitCount=0;
  //Which Ai should be used?
  private int aiValue;
  //The collision check variable
  private boolean collided=false;
  
  public Bugs(String s, int a, int b)
  {
    //Bugs are not a food
    eat=false;
    //All images are loaded using Graphix.java
    image = Graphix.buffer(s + ".png");
    ani1 = Graphix.buffer(s + "1.png");
    ani2 = Graphix.buffer(s + "2.png");
    ani3 = Graphix.buffer(s + "3.png");
    //Box location is set to the correct position
    box.x=a*64;
    box.y=b*64;
    //The bugs state is set to alive
    state=2;
    //The bugs name is saved
    this.s=s;
    //The specific ai to use and value of the bug is set
    if (s.equals("beetle"))
    {
      aiValue=1;
      moneta=20;
    }
    if (s.equals("grasshopper"))
    {
      aiValue=2;
      moneta=25;
    }
    if (s.equals("ladybug"))
    {
      aiValue=3;
      moneta=750;
    }
  }
  
  public void update()
  {
    switch(aiValue)
    {
      //Runs a very complex algorithm for moving the beetle in a nice squigly, morphing sign curve on any angle with collision
      case 1://Removes the bug from its current grid location
      Crossing.bugs[box.x/64][box.y/64].remove(this);
      count=count+1;
      //Moves the bug appropriately
      box.y= box.y+(int)(5*Math.cos(angle)-5*Math.sin(count)*Math.sin(angle));
      box.x= box.x+(int)(5*Math.sin(angle)+5*Math.sin(count)*Math.cos(angle));
      //If there was a collision or the counter maxLoops has run out, revert the movement, and pick a new angle and reset all values.
      if (maxLoops==0 || collision())
      {
        box.y= box.y-(int)(5*Math.cos(angle)-5*Math.sin(count)*Math.sin(angle));
        box.x= box.x-(int)(5*Math.sin(angle)+5*Math.sin(count)*Math.cos(angle));
        Crossing.bugs[box.x/64][box.y/64].add(this);
        maxLoops=(int)(Math.random()*20);
        if (collided)
        {
          angle = Math.random()*Math.PI/2+angle-5*Math.PI/4;
          if (angle<0)
            angle = angle+Math.PI*2;
          collided=false;
        }
        else
          angle = Math.random()*Math.PI*2;
        count=0;
      }
      //Otherwise, add the bug back to the grid in its new spot and progress the animation stage
      else
      {
        Crossing.bugs[box.x/64][box.y/64].add(this);
        maxLoops--;
        if (animation==2)
          aniAdd=-1;
        else if (animation==0)
          aniAdd=1;
        animation=animation+aniAdd;
      }
      break;
      
      //A very, very complex algoritm for making a perfect arc for a hopping grasshopper with collsion, including smooth rotations between
      //leaps
      case 2://If the bug is lazy between hops, keep doing nothing
      if (waitCount>10)
      {
        waitCount--;
      }
      //If the bug is soon to not be lazy, start rotating towards its new direction
      else if (waitCount>0)
      {
        waitCount--;
        angle=angle+rotation;
      }
      else
      {
        //Reduce our counter and remove the bug from the grid
        count=count-2;
        Crossing.bugs[box.x/64][box.y/64].remove(this);
        //If we have yet to collide with something, try moving horizontally
        if (collided)
        {
          box.x= (int)(box.x+10*Math.cos(angle));
          box.y= (int)(box.y+10*Math.sin(angle));
          //If we have a collision issue, revert our movement
          if (collision())
          {
            collided=false;
            box.x= (int)(box.x-10*Math.cos(angle));
            box.y= (int)(box.y-10*Math.sin(angle));
          }
        }
        //Make our horizontal movement, add ourselves back to the grid, progress animations
        box.y=box.y-count;
        Crossing.bugs[box.x/64][box.y/64].add(this);
        if (animation!=2)
          animation=2;
        else
          animation=1;
        //Once our landing has occured, reset all our counters and generate new values for movement and lazyness (waitcount).
        if (count==-8)
        {
          animation=0;
          count=10;
          waitCount=(int)(Math.random()*20)+10;
          angle=newAngle;
          if (collided==false)
          {
            newAngle = Math.random()*Math.PI/2+angle-5*Math.PI/4;
            if (newAngle<0)
              newAngle = newAngle+Math.PI*2;
            collided=true;
          }
          else
            newAngle = Math.random()*Math.PI*2;
          rotation = newAngle-angle;
          if (Math.abs(rotation)>Math.PI)
          {
            if (rotation>0)
              rotation=(rotation-Math.PI*2)/10;
            else
              rotation=(rotation+Math.PI*2)/10;
          }
          else
            rotation=rotation/10;
        }
      }
      break;
      //Makes a very pretty ladybug that sits there and blends in very nicely
      case 3:
      {
        //If the player walks on it, it flys away and disapears
        if (box.intersects(Crossing.player.box))
          Crossing.bugs[box.x/64][box.y/64].remove(this);
      }
      break;
    }
  }
  
  public boolean collision()
  {
    //If the bug is on a hole, that is bad
    if (Crossing.grid[box.x/64][box.y/64] instanceof Hole ||  Crossing.grid[box.x/64+1][box.y/64] instanceof Hole || Crossing.grid[box.x/64][box.y/64+1] instanceof Hole || Crossing.grid[box.x/64+1][box.y/64+1] instanceof Hole)
    {
      collided=true;
      return true;
    }
    //If the bug is on a world collision box, that is bad
    for (Rectangle x:Crossing.worldWalls)
    {
      if (x.intersects(box))
      {
        collided=true;
        return true;
      }
    }
    //If the bug is on water, that is bad
    for (Polygon x:Crossing.water)
    {
      if (x.intersects(box))
      {
        collided=true;
        return true;
      }
    }
    //Otherwise we are in the clear and didnt collide with anything
    return false;
  }
  
  public boolean interact()
  {
    //If the bug is burried, then it will not respond to interaction
    if (state==0)
      return false;
    //Otherwise it is fine to be put in the players inventory
    loop: for (int f=0; f<6; f++)
    {
      for (int k=1; k<4; k++)
      {
        //Checking to find an empty inventory slot
        if (Crossing.inventory[f][k]==null)
        {
          //Put the item in the inventory, set the popup for obtaining the bug, delete the bug from the grid.
          Crossing.inventory[f][k]=this;
          Crossing.player.caught=s;
          Graphix.popupTimer=0;
          Crossing.grid[box.x/64][box.y/64]=null;
          break loop;
        }
        else if (f==5 && k==3)
        {
          //otherwise set the popup for a full inventory
          Crossing.invFull=true;
          Graphix.popupTimer=0;
        }
      }
    }
    return true;
  }
  
  public void paint(Graphics g)
  {
    //Draw a transformed image of the bug based on its current rotation and its state of animation
    Graphics2D g2d = (Graphics2D) g;
    //rotate the image by its angle around its center
    AffineTransform rotate = AffineTransform.getRotateInstance(angle, image.getWidth()/2, image.getHeight()/2);
    AffineTransformOp translate = new AffineTransformOp(rotate, AffineTransformOp.TYPE_BILINEAR);
    //Draw the specific animation stage with the assigned affinetransform
    if (state==0)
      g2d.drawImage(Graphix.star, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
    else if (state==1)
      g2d.drawImage(image, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
    else if (animation==0)
      g2d.drawImage(translate.filter(ani1, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
    else if (animation==1)
      g2d.drawImage(translate.filter(ani2, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
    else
      g2d.drawImage(translate.filter(ani3, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
  }
}