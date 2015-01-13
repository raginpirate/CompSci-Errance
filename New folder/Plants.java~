import java.awt.Graphics;
import java.awt.image.BufferedImage;
public class Plants extends Entity
{
  private boolean lootable=false;
  private int growCount=0;
  private BufferedImage stage1;
  private BufferedImage stage2;
  private BufferedImage stage3;
  private BufferedImage stage1w;
  private BufferedImage stage2w;
  private BufferedImage stage3w;
  
  public Plants(String s)
  {
    this.s=s;
    water=50;
    if (s.contains("strawberry"))
      lootable=true;
    else if (s.contains("carrot"))
      lootable=false;
    else if (s.contains("turnip"))
      lootable=false;
    else
      lootable=true;
    if (s.contains("seeds"))
    {
      eat=false;
      image = Graphix.buffer(s + ".jpg");
      stage1 = Graphix.buffer(s.substring(0, s.indexOf(" ")) + "1.jpg");
      stage2 = Graphix.buffer(s.substring(0, s.indexOf(" ")) + "2.jpg");
      stage3 = Graphix.buffer(s.substring(0, s.indexOf(" ")) + "3.jpg");
      stage1w = Graphix.buffer(s.substring(0, s.indexOf(" ")) + "1w.jpg");
      stage2w = Graphix.buffer(s.substring(0, s.indexOf(" ")) + "2w.jpg");
      stage3w = Graphix.buffer(s.substring(0, s.indexOf(" ")) + "3w.jpg");
    }
    else
    {
      eat=true;
      image = Graphix.buffer(s + ".jpg");
      stage1 = Graphix.buffer(s + "1.jpg");
      stage2 = Graphix.buffer(s + "2.jpg");
      stage3 = Graphix.buffer(s + "3.jpg");
      stage1w = Graphix.buffer(s + "1w.jpg");
      stage2w = Graphix.buffer(s + "2w.jpg");
      stage3w = Graphix.buffer(s + "3w.jpg");
    }
    growCount=(int)(Math.random()*2+1);
  }
  
  public void update()
  {
    if (growCount!=0)
    {
      growCount--;
      if (growCount==0)
      {
        state++;
        if (state!=4)
          growCount=(int)(Math.random()*2+1);
        else if (s.contains("seeds"))
        {
          s=s.substring(0, s.indexOf(' '));
          image = Graphix.buffer(s + ".jpg");
        }
      }
    }
    water=water-10;
    if (water<=0)
      Crossing.grid[box.x/64][box.y/64]=null;
  }
  
  public boolean interact()
  {
    if (state==1)
    {
      loop: for (int f=0; f<6; f++)
      {
        for (int k=1; k<4; k++)
        {
          if (Crossing.inventory[f][k]==null)
          {
            Crossing.inventory[f][k]=this;
            Crossing.grid[box.x/64][box.y/64]=null;
            break loop;
          }
          else if (f==5 && k==3){}
          //Drops to the grid
        }
      }
      return true;
    }
    System.out.println("weo!");
    System.out.println(state);
    if (state==4)
    {
      if (lootable)
      {
        state=2;
        growCount=(int)(Math.random()*2+1);
        loop: for (int f=0; f<6; f++)
        {
          for (int k=1; k<4; k++)
          {
            if (Crossing.inventory[f][k]==null)
            {
              Crossing.inventory[f][k]=new Plants(s);
              break loop;
            }
            else if (f==5 && k==3){}
            //Drops to the grid
          }
        }
        return true;
      }
      water=50;
      growCount=(int)(Math.random()*2+1);
      loop: for (int f=0; f<6; f++)
      {
        for (int k=1; k<4; k++)
        {
          if (Crossing.inventory[f][k]==null)
          {
            Crossing.inventory[f][k]=this;
            Crossing.grid[box.x/64][box.y/64]=null;
            break loop;
          }
          else if (f==5 && k==3)
          {
            //Drops to the grid
            //Crossing.grid[box.x/64][box.y/64]=null;
          }
        }
      }
      return true;
    }
    return false;
  }
  
  public void paint(Graphics g)
  {
    if (state == 1)
      g.drawImage(image, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
    else if (state==2)
    {
      if (water<30)
        g.drawImage(stage1w, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
      else
      {
        g.drawImage(stage1, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
      }
    }
    else if (state==3)
    {
      if (water<30)
        g.drawImage(stage2w, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
      else
        g.drawImage(stage2, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
    }
    else
    {
      if (water<30)
        g.drawImage(stage3w, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
      else
        g.drawImage(stage3, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
    }
  }
}