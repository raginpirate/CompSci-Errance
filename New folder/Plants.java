import java.awt.Graphics;
import java.awt.image.BufferedImage;
public class Plants extends Entity
{
  private BufferedImage stage1;
  private BufferedImage stage2;
  private BufferedImage stage3;
  private BufferedImage stage1w;
  private BufferedImage stage2w;
  private BufferedImage stage3w;
  private boolean lootable=false;
  private int growCount=0;
  
  public Plants(String s)
  {
    this.s=s;
    water=50;
    if (s.contains("strawberry"))
    {
      moneta=400;
      lootable=true;
    }
    else if (s.contains("carrot"))
    {
      moneta=10000;
      lootable=false;
    }
    else
    {
      moneta=2000;
      lootable=false;
    }
    if (s.contains("seeds"))
    {
      moneta=moneta/4;
      eat=false;
      image = Graphix.buffer(s + ".png");
      stage1 = Graphix.buffer(s.substring(0, s.indexOf(" ")) + "1.png");
      stage2 = Graphix.buffer(s.substring(0, s.indexOf(" ")) + "2.png");
      stage3 = Graphix.buffer(s.substring(0, s.indexOf(" ")) + "3.png");
      stage1w = Graphix.buffer(s.substring(0, s.indexOf(" ")) + "1w.png");
      stage2w = Graphix.buffer(s.substring(0, s.indexOf(" ")) + "2w.png");
      stage3w = Graphix.buffer(s.substring(0, s.indexOf(" ")) + "3w.png");
    }
    else
    {
      eat=true;
      image = Graphix.buffer(s + ".png");
      stage1 = Graphix.buffer(s + "1.png");
      stage2 = Graphix.buffer(s + "2.png");
      stage3 = Graphix.buffer(s + "3.png");
      stage1w = Graphix.buffer(s + "1w.png");
      stage2w = Graphix.buffer(s + "2w.png");
      stage3w = Graphix.buffer(s + "3w.png");
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
          image = Graphix.buffer(s + ".png");
          moneta=moneta*4;
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
            Crossing.player.caught=s;
            Graphix.popupTimer=0;
            Crossing.grid[box.x/64][box.y/64]=null;
            break loop;
          }
          else if (f==5 && k==3){Crossing.invFull=true;
          Graphix.popupTimer=0;}
        }
      }
      return true;
    }
    if (state==4)
    {
      if (lootable)
      {
        loop: for (int f=0; f<6; f++)
        {
          for (int k=1; k<4; k++)
          {
            if (Crossing.inventory[f][k]==null)
            {
              Crossing.inventory[f][k]=new Plants(s);
              Crossing.player.caught=s;
              Graphix.popupTimer=0;
              state=2;
              growCount=(int)(Math.random()*2+1);
              break loop;
            }
            else if (f==5 && k==3){Crossing.invFull=true;
          Graphix.popupTimer=0;}
          }
        }
        return true;
      }
      loop: for (int f=0; f<6; f++)
      {
        for (int k=1; k<4; k++)
        {
          if (Crossing.inventory[f][k]==null)
          {
            water=50;
            growCount=(int)(Math.random()*2+1);
            Crossing.player.caught=s;
            Graphix.popupTimer=0;
            Crossing.inventory[f][k]=this;
            Crossing.grid[box.x/64][box.y/64]=null;
            break loop;
          }
          else if (f==5 && k==3)
          {
            Crossing.invFull=true;
            Graphix.popupTimer=0;
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
        g.drawImage(stage1, box.x-Crossing.player.box.x+Crossing.PLAYERLOCATION, box.y-Crossing.player.box.y+Crossing.PLAYERLOCATION, 64, 64, null);
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