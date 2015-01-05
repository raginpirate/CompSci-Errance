import java.awt.Graphics;
import java.awt.image.BufferedImage;
public class Plants extends Entity
{
  private boolean lootable=false;
  private String s;
  private int growCount=0;
  private int stage=0;
  private int water=50;
  private BufferedImage stage1;
  private BufferedImage stage2;
  private BufferedImage stage3;
  private BufferedImage stage1w;
  private BufferedImage stage2w;
  private BufferedImage stage3w;
  
  public Plants(String s, boolean fruit)
  {
    this.s=s;
    if (fruit)
    {
      image = Graphix.buffer(s + "f.jpg");
      eat=true;
    }
    else
    {
      image = Graphix.buffer(s + ".jpg");
      eat=false;
    }
    stage1 = Graphix.buffer(s + "1.jpg");
    stage2 = Graphix.buffer(s + "2.jpg");
    stage3 = Graphix.buffer(s + "3.jpg");
    stage1w = Graphix.buffer(s + "1w.jpg");
    stage2w = Graphix.buffer(s + "2w.jpg");
    stage3w = Graphix.buffer(s + "3w.jpg");
    growCount=(int)(Math.random()*2+1);
  }
  
  public void update()
  {
    if (growCount!=0)
    {
      growCount--;
      if (growCount==0)
      {
        stage++;
        if (stage!=2)
          growCount=(int)(Math.random()*2+1);
        else
          image = Graphix.buffer(s + "f.jpg");
      }
    }
    water=water-10;
    if (water==0)
      Crossing.grid[box.x/64][box.y/64]=null;
  }
  
  public void water()
  {
    if (water+20>100)
      water=100;
    else
      water=water+20;
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
    if (stage==2)
    {
      if (lootable)
      {
        stage=1;
        growCount=(int)(Math.random()*2+1);
        loop: for (int f=0; f<6; f++)
        {
          for (int k=1; k<4; k++)
          {
            if (Crossing.inventory[f][k]==null)
            {
              Crossing.inventory[f][k]=new Plants(s, true);
              break loop;
            }
            else if (f==5 && k==3){}
            //Drops to the grid
          }
        }
        return true;
      }
      stage=0;
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
    else if (stage==0)
    {
      if (water<30)
        g.drawImage(stage1w, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
      else
      {
        g.drawImage(stage1, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
      }
    }
    else if (stage==1)
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