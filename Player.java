import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
public class Player extends Entity
{
  private boolean up;
  private boolean down;
  private boolean left;
  private boolean right;
  int selected1x;
  int selected1y;
  int selected2;
  private boolean bury;
  int menu=0;
  boolean held;
  private int heldx;
  private int heldy;
  
  public Player()
  {
    box = new Rectangle(512,512,64,64);
    eat = false;
  }
  
  public void move()
  {
    Crossing.entityGrid[box.x/64][box.y/64].remove(this);
    if (up)
    {
      if (right)
      {
        box.x=box.x+4;
        if (Crossing.checkColide(box))
        {
          box.x=box.x-4;
        }
        box.y=box.y-4;
        if (Crossing.checkColide(box))
        {
          box.y=box.y+4;
        }
      }
      else if (left)
      {
        box.x=box.x-4;
        if (Crossing.checkColide(box))
        {
          box.x=box.x+4;
        }
        box.y=box.y-4;
        if (Crossing.checkColide(box))
        {
          box.y=box.y+4;
        }
      }
      else
      {
        box.y=box.y-6;
        if (Crossing.checkColide(box))
        {
          box.y=box.y+6;
        }
      }
    }
    else if (down)
    {
      if (right)
      {
        box.x=box.x+4;
        if (Crossing.checkColide(box))
        {
          box.x=box.x-4;
        }
        box.y=box.y+4;
        if (Crossing.checkColide(box))
        {
          box.y=box.y-4;
        }
      }
      else if (left)
      {
        box.x=box.x-4;
        if (Crossing.checkColide(box))
        {
          box.x=box.x+4;
        }
        box.y=box.y+4;
        if (Crossing.checkColide(box))
        {
          box.y=box.y-4;
        }
      }
      else
      {
        box.y=box.y+6;
        if (Crossing.checkColide(box))
        {
          box.y=box.y-6;
        }
      }
    }
    else if (left)
    {
      box.x=box.x-6;
      if (Crossing.checkColide(box))
      {
        box.x=box.x+6;
      }
    }
    else if (right)
    {
      box.x=box.x+6;
      if (Crossing.checkColide(box))
      {
        box.x=box.x-6;
      }
    }
    Crossing.entityGrid[box.x/64][box.y/64].add(this);
  }
  
  public void input(String s)
  {
    if (s=="up")
    {
      if (menu>1)
      {
        if (selected2>0)
          selected2--;
      }
      else if (menu==1)
      {
        if (selected1y>0)
          selected1y--;
      }
      else
        up=true;
    }
    if (s=="down")
    {
      if (menu>1)
      {
        if (selected2<(menu-1)/2+2)
          selected2++;
      }
      else if (menu==1)
      {
        if (selected1y<2)
          selected1y++;
      }
      else 
        down=true;
    }
    if (s=="left")
    {
      if (menu==1)
      {
        if (selected1x>0)
          selected1x--;
      }
      else if (menu == 0)
        left=true;
    }
    if (s=="right")
    {
      if (menu==1)
      {
        if (selected1x<5)
          selected1x++;
      }
      else if (menu == 0)
        right=true;
    }
    if (s=="q")
    {
      if (menu>0)
      {
        menu=0;
        bury=false;
      }
      else
      {
        menu=1;
        if (box.x%64<32)
        {
          if (box.y%64<32)
          {
            for (int a=-1; a<2; a++)
            {
              for (int b=-1; b<2; b++)
              {
                for (int c=0; c<Crossing.entityGrid[(int)(box.x/64+a)][(int)(box.y/64+b)].size(); c++)
                {
                  if (Crossing.entityGrid[(int)(box.x/64+a)][(int)(box.y/64+b)].get(c) instanceof Holes)
                    bury=true;
                }
              }
            }
          }
          else
          {
            for (int a=-1; a<2; a++)
            {
              for (int b=-1; b<2; b++)
              {
                for (int c=0; c<Crossing.entityGrid[(int)(box.x/64+a)][(int)(box.y/64+b)].size(); c++)
                {
                  if (Crossing.entityGrid[(int)(box.x/64+a)][(int)(box.y/64+b)].get(c) instanceof Holes)
                    bury=true;
                }
              }
            }
          }
        }
        else if (box.y%64<32)
        {
          for (int a=-1; a<2; a++)
          {
            for (int b=-1; b<2; b++)
            {
              for (int c=0; c<Crossing.entityGrid[(int)(box.x/64+a)][(int)(box.y/64+b)].size(); c++)
              {
                if (Crossing.entityGrid[(int)(box.x/64+a)][(int)(box.y/64+b)].get(c) instanceof Holes)
                  bury=true;
              }
            }
          }
        }
        else
        {
          for (int a=-1; a<2; a++)
          {
            for (int b=-1; b<2; b++)
            {
              for (int c=0; c<Crossing.entityGrid[(int)(box.x/64+a)][(int)(box.y/64+b)].size(); c++)
              {
                if (Crossing.entityGrid[(int)(box.x/64+a)][(int)(box.y/64+b)].get(c) instanceof Holes)
                  bury=true;
              }
            }
          }
        }
      }
      System.out.println(bury);
    }
    if (s=="w")
      System.out.println("Run function hotkey(w)");
    if (s=="e")
      System.out.println("Run function hotkey(e)");
    if (s=="r")
      System.out.println("Run function hotkey(r)");
    if (s==" ")
    {
      switch(menu)
      {
        //When we are not using the inventory (case 0)
        case 0: 
          break; 
          //When we are using the inventory (case 1-5)
        case 1:if (held)
        {
          Entity temp = Crossing.inventory[selected1x][selected1y];
          Crossing.inventory[selected1x][selected1y] = Crossing.inventory[heldx][heldy];
          Crossing.inventory[heldx][heldy] = temp;
          held = false;
          break;
        }
        if (Crossing.inventory[selected1x][selected1y] != null)
        {
          if (bury)
          {
            if (Crossing.inventory[selected1x][selected1y].eat)
            {
              menu=5;
              break;
            }
            else
            {
              menu=4;
              break;
            }
          }
          if (Crossing.inventory[selected1x][selected1y].eat)
          {
            menu=3;
            break;
          }
          menu=2;
        }
        break;
        
        case 2: switch (selected2)
        {
          case 0:Crossing.inventory[selected1x][selected1y]=null;
          menu=0;
          selected2=0;
          break;
          
          case 1: held = true;
          heldx = selected1x;
          heldy = selected1y;
          menu=1;
          selected2=0;
          break;
          
          case 2: menu=1;
          selected2=0;
          break;
        }
        break;
        
        case 3: switch (selected2)
        {
          case 0:Crossing.inventory[selected1x][selected1y]=null;
          menu=0;
          selected2=0;
          break;
          
          case 1: Crossing.inventory[selected1x][selected1y]=null;
          menu=0;
          selected2=0;
          break;
          
          case 2: held = true;
          heldx = selected1x;
          heldy = selected1y;
          menu=1;
          selected2=0;
          break;
          
          case 3: menu=1;
          selected2=0;
          break;
        }
        break;
        
        case 4: switch (selected2)
        {
          case 0://burry
            menu=1;
            selected2=0;
            break;
            
          case 1:Crossing.inventory[selected1x][selected1y]=null;
          menu=0;
          selected2=0;
          break;
          
          case 2: held = true;
          heldx = selected1x;
          heldy = selected1y;
          menu=1;
          selected2=0;
          break;
          
          case 3: menu=1;
          selected2=0;
          break;
        }
        break;
        
        case 5: switch (selected2)
        {
          case 0:Crossing.inventory[selected1x][selected1y]=null;
          menu=0;
          selected2=0;
          break;
          
          case 1://burry
            menu=1;
            selected2=0;
            break;
            
          case 2:Crossing.inventory[selected1x][selected1y]=null;
          menu=0;
          break;
          
          case 3: held = true;
          heldx = selected1x;
          heldy = selected1y;
          menu=1;
          selected2=0;
          break;
          
          case 4: menu=1;
          selected2=0;
          break;
        }
        break;
      }
    }
  }
  
  public void release(String x)
  {
    if (x=="up")
      up=false;
    if (x=="down")
      down=false;
    if (x=="left")
      left=false;
    if (x=="right")
      right=false;
  }
  
  public void paint(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.orange);
    g2d.fillRect(Crossing.PLAYERLOCATION, Crossing.PLAYERLOCATION, 64, 64);
  }
}