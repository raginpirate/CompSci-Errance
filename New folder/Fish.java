import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Polygon;
/*
 * File Name: Fish.java
 * Date: Friday, January 16th, 2015
 * Programmed by: Orange Bannana (Daniel Wyckoff, Joshua Anandappa, Izzy Snowden).
 * Description: An openworld free-roaming game inwhich the player can interact with the enviroment using the space bar, move with the arrow keys,
 * open the inventory with q, and sprint with e. This class deals with the bug entitys AI.
 * 
 * PLEASE REFERENCE Entity.java for general information about how this class works, along with a few others.
 * 
 * To be used in conjunction with Crossing.java, Entity.java, Bugs.java, FlyingBugs.java, Graphix.java, Hole.java, Items.java, Plants.java, Player.java, and Villagers.java
 */
public class Fish extends Entity
{
  //basic five animation phases
  private BufferedImage ani1;
  private BufferedImage ani2;
  private BufferedImage ani3;
  private BufferedImage ani4;
  private BufferedImage ani5;
  //Rectangle for the fish's field of view when searching for the bobber
  private Rectangle bobberRectangle=new Rectangle(10000,10000,0,0);
  //angles for rotation
  private double angle=2;
  private double rotation=2;
  //Acceleration variable for accelerated dashing
  private int accelleration=0;
  //Animation counters
  private int animation=0;
  private int aniAdd=1;
  //Timers for AI
  private int waitCount=0;
  private int biteTimer=-1;
  private int didntBite=0;
  //If we collided this is true
  private boolean collided=false;
  //When the bobber is found, we switch AI sequences
  private boolean nibbleAI;
  
  public Fish(String s, int a, int b)
  {
    //Fish arent food
    eat=false;
    //Copy our name
    this.s=s;
    //Generate our image files using Graphix
    //All fish shadows look the same so you cannot tell what you are catching
    image = Graphix.buffer(s + ".png");
    ani1 = Graphix.buffer("fish1.png");
    ani2 = Graphix.buffer("fish2.png");
    ani3 = Graphix.buffer("fish3.png");
    ani4 = Graphix.buffer("fish4.png");
    ani5 = Graphix.buffer("fish5.png");
    //Set money values
    if (s.equals("salmon"))
      moneta=100;
    if (s.equals("seabass"))
      moneta=100;
    if (s.equals("crawdaddy"))
      moneta=500;
    //Set proper collision location
    box.x=64*a;
    box.y=64*b;
    //Make the fish alive
    state=2;
  }
  
  public void update()
  {
    //If we have found a bobber
    if (nibbleAI)
    {
      //We continue our animation
      if (animation==4)
        aniAdd=-1;
      else if (animation==0)
        aniAdd=1;
      animation=animation+aniAdd;
      //We remove the fish from the grid to try and move it
      Crossing.fish[box.x/64][box.y/64].remove(this);
      //So long as we have not actually bit down on the bobber
      if (biteTimer==-1)
      {
        //fish accelerates in its appropriate direction
        box.x= box.x+(int)(accelleration*Math.cos(angle));
        box.y= box.y+(int)(accelleration*Math.sin(angle));
        //Fish slows down its acceleration, forwards or backwards
        if(accelleration>0)
          accelleration--;
        else
          accelleration++;
        //If we collide with a bobber
        if (Crossing.bobber.box.intersects(box))
        {
          //We declare we nibbled on something, setting the didntBite counter to 0.
          didntBite=0;
          //We accelerate backwards
          accelleration=-15;
          //We move backwards as to not remain intersected with the bobber
          box.x= box.x+(int)(accelleration*Math.cos(angle));
          box.y= box.y+(int)(accelleration*Math.sin(angle));
          //We then have a 40% chance to sucessfully bite, starting the biteTimer
          if (Math.random()<0.4)
          {
            Crossing.caught=this;
            biteTimer=(int)(Math.random()*10)+5;
          }
        }
        //Otherwise, if we didnt collide with a bobber and we collided with a wall or came to a complete stop, we start to accelerate again
        else if (collision() || accelleration==0)
        {
          accelleration=15;
          didntBite++;
        }
        //If we did nothing, we increment our didntBite counter
        else
          didntBite++;
      }
      //If we are busy biting the bobber, then we will reduce our biteTimer by one
      else
        biteTimer--;
      //If the biteTimer has not run out, and we have bit within 15 counts, and the bobber has not been reeled in and reset to 0,0
      if (Crossing.bobber.box.x!=0 && biteTimer!=0 && didntBite<15)
        //We keep our fish alive by adding it back to the grid
        Crossing.fish[box.x/64][box.y/64].add(this);
      //Otherwise, if one of those conditions werent met, we spawn a new fish and remove ourselves completely
      else
      {
        Crossing.spawn(3);
        Crossing.caught=null;
      }
    }
    //Otherwise, if we encounter the bobber
    else if (Crossing.bobber.box.intersects(bobberRectangle))
    {
      //We will sucessfully do trigonometry to find out the angle our fish needs to move at in order for it to swim directly at the bobber
      if (Crossing.bobber.box.x-8-(box.x+32)>0 && Crossing.bobber.box.y-8.0-(box.y+32) !=0)
        angle=Math.atan((Crossing.bobber.box.y-8-(box.y+32))/(Crossing.bobber.box.x-8.0-(box.x+32)));
      else if (Crossing.bobber.box.x-8-(box.x+32)==0)
      {
        if (Crossing.bobber.box.y-8-(box.y+32)>0)
          angle=Math.PI/2;
        else
          angle=-Math.PI/2;
      }
      else if (Crossing.bobber.box.y-8.0-(box.y+32)>0)
        angle=Math.atan(Math.abs((Crossing.bobber.box.y-8-(box.y+32))/(Crossing.bobber.box.x-8.0-(box.x+32))))+Math.PI/2;
      else if (Crossing.bobber.box.y-(box.y+32)<0)
        angle=Math.atan(Math.abs((Crossing.bobber.box.y-8-(box.y+32))/(Crossing.bobber.box.x-8.0-(box.x+32))))+Math.PI;
      else if (Crossing.bobber.box.x-8.0-(box.x+32)>0)
        angle=0;
      else
        angle=Math.PI;
      //We then make sure to run the nibbleAI from now on, and reset our acceleration
      nibbleAI=true;
      accelleration = 15;
    }
    //Otherwise, if we have not observed a bobber yet
    else
    {
      //We wait around if we want to wait around, by continuing our animations
      if (waitCount>0)
      {
        waitCount--;
        if (animation==4)
          aniAdd=-1;
        else if (animation==0)
          aniAdd=1;
        animation=animation+aniAdd;
      }
      //If our wait is about to end we will set new values for all our counters and angles and acceleration
      else if (waitCount==0)
      {
        angle = Math.random()*Math.PI*2;
        rotation = Math.PI*2 - angle;
        if (rotation>=Math.PI)
          rotation = rotation-Math.PI*2;
        accelleration = 20;
        waitCount--;
        animation=2;
      }
      //Then if we are not waiting at all
      else
      {
        //We remove the fish from the grid
        Crossing.fish[box.x/64][box.y/64].remove(this);
        //We move the fish using its acceleration and angle
        box.x= box.x+(int)(accelleration*Math.cos(angle));
        box.y= box.y+(int)(accelleration*Math.sin(angle));
        //We check for collision
        if (collision())
        {
          //If there was collision, the fish reverts its position and reverses direction, so it continues swimming without sticking to walls
          //Then re-add the fish to the grid
          box.x= box.x-(int)(accelleration*Math.cos(angle));
          box.y= box.y-(int)(accelleration*Math.sin(angle));
          angle=angle+Math.PI;
          rotation=rotation-Math.PI;
          accelleration++;
          Crossing.fish[box.x/64][box.y/64].add(this);
        }
        //If there was no collision add the fish back to the grid
        else
          Crossing.fish[box.x/64][box.y/64].add(this);
        //If our angle is at or slightly above 0 degrees, we will start to wait in order to refresh our values (explained a litle better below)
        if (angle<=0 || angle>=Math.PI*2)
        {
          waitCount=(short)(Math.random()*20);
        }
        //If our angle isnt at 0, we will
        else
        {
          //Reduce our acceleration, and on a very small change gain a burst of speed again
          if (accelleration>0)
            accelleration--;
          if (Math.random()<0.01 && accelleration>0)
            accelleration=(short)(accelleration+10);
          //If our acceleration is dwindling, then we will start to rotate to face 0 degrees, which when we meet, we will stop and reset our values
          if (accelleration<10)
          {
            angle = angle+rotation/16;
            if (animation==4)
              aniAdd=-1;
            else if (animation==0)
              aniAdd=1;
            animation=animation+aniAdd;
          }
          //Otherwise if our acceleration is much higher, we complete a shorter, nicer looking animation sequence for a fast moving fish
          else
          {
            if (animation==3)
              aniAdd=-1;
            else if (animation==1)
              aniAdd=1;
            animation=animation+aniAdd;
          }
          
          //We then approprately move the bobberRectangle, used for bobber collision detection, to a new location based on our angle.
          if (angle<Math.PI/4 || angle>Math.PI*7/4)
          {
            bobberRectangle.x=box.x+64;
            bobberRectangle.y=box.y-128;
            bobberRectangle.width=128;
            bobberRectangle.height=320;
          }
          else if (angle<Math.PI*3/4)
          {
            bobberRectangle.x=box.x-128;
            bobberRectangle.y=box.y+64;
            bobberRectangle.width=320;
            bobberRectangle.height=128;
          }
          else if (angle<Math.PI*5/4)
          {
            bobberRectangle.x=box.x-128;
            bobberRectangle.y=box.y-128;
            bobberRectangle.width=128;
            bobberRectangle.height=320;
          }
          else
          {
            bobberRectangle.x=box.x-128;
            bobberRectangle.y=box.y-128;
            bobberRectangle.width=320;
            bobberRectangle.height=128;
          }
        }
      }
    }
  }
  
  //If the fish isnt contained by water, than it is colliding with land and therefor is bad
  private boolean collision()
  {
    for (Polygon x:Crossing.water)
    {
      if (x.contains(box))
        return false;
    }
    return true;
  }
  
  public boolean interact()
  {
    //If the fish is burried, return nothing
    if (state==0)
      return false;
    //Otherwise, the fish is being caught and looks for an open inventory space
    loop: for (int f=0; f<6; f++)
    {
      for (int k=1; k<4; k++)
      {
        if (Crossing.inventory[f][k]==null)
        {
          //If there is an open space, place the fish inside and display the caught popup
          Crossing.inventory[f][k]=this;
          Crossing.player.caught=s;
          Graphix.popupTimer=0;
          //Delete the fish from the grid
          Crossing.grid[box.x/64][box.y/64]=null;
          break loop;
        }
        else if (f==5 && k==3)
        {
          //Otherwise display the inventory full popup and delete the fish anyways, because you reeled in so it swims away
          Crossing.invFull=true;
          Graphix.popupTimer=0;
          Crossing.grid[box.x/64][box.y/64]=null;
        }
      }
    }
    return true;
  }

  //Drawing the fish with affine transforms and animation statges
  public void paint(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;
    //Rotates the fish based off its center to face angle
    AffineTransform rotate = AffineTransform.getRotateInstance(angle+Math.PI/2, image.getWidth()/2, image.getHeight()/2);
    AffineTransformOp translate = new AffineTransformOp(rotate, AffineTransformOp.TYPE_BILINEAR);
    //If burried draw a star
    if (state==0)
      g2d.drawImage(Graphix.star, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
    //If dropped, draw its first image
    else if (state==1)
      g2d.drawImage(image, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
    //Otherwise draw various animation stages
    else if (animation==0)
      g2d.drawImage(translate.filter(ani1, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
    else if (animation==1)
      g2d.drawImage(translate.filter(ani2, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
    else if (animation==2)
      g2d.drawImage(translate.filter(ani3, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
    else if (animation==3)
      g2d.drawImage(translate.filter(ani4, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
    else
      g2d.drawImage(translate.filter(ani5, null), box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, null);
  }
}