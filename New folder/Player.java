import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
public class Player
{
  Rectangle box = new Rectangle(605,605,64,64);
  int menu=0;
  int selected1x;
  int selected1y;
  int selected2;
  private boolean fishing=false;
  private boolean up;
  private boolean down;
  private boolean left;
  private boolean right;
  private int lastDirection=0; // 0 up 1 down 2 left 3 right
  private int buryx;
  private int buryy;
  private boolean held;
  private int heldx;
  private int heldy;
  
  public void move()
  {
    if (up)
    {
      if (right)
        moveHelper(4, -4);
      else if (left)
        moveHelper(-4, -4);
      else
        moveHelper(0, -6);
    }
    else if (down)
    {
      if (right)
        moveHelper(4, 4);
      else if (left)
        moveHelper(-4, 4);
      else
        moveHelper(0, 6);
    }
    else if (left)
      moveHelper(-6, 0);
    else if (right)
      moveHelper(6, 0);
  }
  
  private void moveHelper(int k, int l)
  {
    box.x=box.x+k;
    if (!(worldCollision(box, true)))
      box.x=box.x-k;
    box.y=box.y+l;
    if (!(worldCollision(box, true)))
      box.y=box.y-l;
  }
  
  public void input(String s)
  {
    if (s=="up")
    {
      lastDirection=0;
      if (menu>1)
      {
        if (selected2>0)
          selected2--;
      }
      else if (menu==1)
      {
        if (selected1y>0)
        {
          selected1y--;
          if (selected1y==0)
            selected1x=0;
        }
      }
      else
        up=true;
    }
    
    if (s=="down")
    {
      lastDirection=1;
      if (menu>1)
      {
        if (selected2<(menu-1)/2+2)
          selected2++;
      }
      else if (menu==1)
      {
        if (selected1y<3)
          selected1y++;
      }
      else 
        down=true;
    }
    
    if (s=="left")
    {
      lastDirection=2;
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
      lastDirection=3;
      if (menu==1)
      {
        if (selected1x<5 && selected1y!=0)
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
        buryx=0;
      }
      else
      {
        menu=1;
        if (box.x%64<32)
        {
          if (box.y%64<32)
            checkNearbyHoles(0, 0);
          else
            checkNearbyHoles(0, 1);
        }
        else if (box.y%64<32)
          checkNearbyHoles(1, 0);
        else
          checkNearbyHoles(1, 1);
      }
    }
    
    if (s=="w")
    {
      for(int a=0; a<60; a++)
      {
        for (int b=0; b<40; b++)
        {
          if (Crossing.grid[a][b] instanceof Plants)
          {
            if (Crossing.grid[a][b].state==0)
              Crossing.grid[a][b].update();
          }
        }
      }
    }
    
    if (s=="e")
      System.out.println(box.x + " " + box.y);
      //System.out.println("Run function hotkey(e)");
    
    if (s=="r")
      System.out.println("Run function hotkey(r)");
  }
  
  private void checkNearbyHoles(int k, int l)
  {
    for (int a=-1; a<2; a++)
    {
      for (int b=-1; b<2; b++)
      {
        if (Crossing.grid[box.x/64+a+k][box.y/64+b+l] instanceof Hole)
        {
          buryx=box.x/64+a+k;
          buryy=box.y/64+b+l;
        }
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
  
  public void spaceBar()
  {
    try
    {
      if (fishing)
      {
        fishing=false;
        Crossing.bobber.box.x=0;
        Crossing.bobber.box.y=0;
        if(Crossing.caught!= null)
        {
          for (int f=0; f<6; f++)
          {
            for (int s=1; s<4; s++)
            {
              if (Crossing.inventory[f][s]==null)
              {
                Crossing.inventory[f][s]=Crossing.caught;
                Crossing.fish[Crossing.bobber.box.x][Crossing.bobber.box.y].remove(Crossing.caught);
                Crossing.caught=null;
                throw new Exception();
              }
              else if (f==5 && s==3)
              {
                System.out.println("Inventory is full!");
                throw new Exception();
              }
            }
          }
        }
      }
      switch(menu)
      {
        //When we are not using the inventory (case 0)
        case 0:
          if (box.x%64<32)
        {
          if (box.y%64<32)
          {
            attemptPickup(box.x/64, box.y/64);
            attemptPickup(box.x/64+1, box.y/64);
            attemptPickup(box.x/64, box.y/64+1);
            attemptPickup(box.x/64+1,box.y/64+1);
            checkEquipment(box.x/64, box.y/64);
          }
          attemptPickup(box.x/64, box.y/64+1);
          attemptPickup(box.x/64, box.y/64);
          attemptPickup(box.x/64+1, box.y/64+1);
          attemptPickup(box.x/64+1,box.y/64);
          checkEquipment(box.x/64, box.y/64+1);
        }
          if (box.y%64<32)
          {
            attemptPickup(box.x/64+1, box.y/64);
            attemptPickup(box.x/64, box.y/64);
            attemptPickup(box.x/64+1, box.y/64+1);
            attemptPickup(box.x/64,box.y/64+1);
            checkEquipment(box.x/64+1, box.y/64);
          }
          attemptPickup(box.x/64+1, box.y/64+1);
          attemptPickup(box.x/64, box.y/64+1);
          attemptPickup(box.x/64+1, box.y/64);
          attemptPickup(box.x/64,box.y/64);
          checkEquipment(box.x/64+1, box.y/64+1);
          
          //When we are using the inventory (case 1-5)
        case 1:
          if (held)
        {
          Entity temp = Crossing.inventory[selected1x][selected1y];
          Crossing.inventory[selected1x][selected1y] = Crossing.inventory[heldx][heldy];
          Crossing.inventory[heldx][heldy] = temp;
          held = false;
          break;
        }
          if (Crossing.inventory[selected1x][selected1y] != null)
          {
            if (buryx!=0)
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
          case 0:drop();
          break;
          
          case 1:hold();
          break;
          
          case 2: menu=1;
          selected2=0;
          break;
        }
        break;
        
        case 3: switch (selected2)
        {
          case 0:eat();
          break;
          
          case 1:drop();
          break;
          
          case 2:hold();
          break;
          
          case 3: menu=1;
          selected2=0;
          break;
        }
        break;
        
        case 4: switch (selected2)
        {
          case 0:burry();
          break;
          
          case 1:drop();
          break;
          
          case 2:hold();
          break;
          
          case 3: menu=1;
          selected2=0;
          break;
        }
        break;
        
        case 5: switch (selected2)
        {
          case 0:eat();
          break;
          
          case 1:burry();
          break;
          
          case 2:drop();
          break;
          
          case 3:hold();
          break;
          
          case 4: menu=1;
          selected2=0;
          break;
        }
        break;
      }
    }
    catch (Exception e) {}
  }
  
  private void attemptPickup(int k, int l) throws Exception
  {
    if (Crossing.grid[k][l] != null)
    {
      if (Crossing.grid[k][l].interact())
        throw new Exception();
    }
  }
  
  private void useShovel(int k, int l, boolean y) throws Exception
  {
    if (Crossing.grid[k][l] instanceof Hole)
    {
      Crossing.grid[k][l]=null;
      throw new Exception();
    }
    Hole temp = new Hole();
    temp.box.x=Math.round(k)*64;
    temp.box.y=Math.round(l)*64;
    if (worldCollision(temp.box, false))
    {
      if (Crossing.grid[k][l] == null || (Crossing.grid[k][l] instanceof Plants && Crossing.grid[k][l].state==0))
      {
        Crossing.grid[k][l]=temp;
        if (y)
          box.y=(int)(Math.round(box.y/64.0)*64);
        else
          box.x=(int)(Math.round(box.x/64.0)*64);
        throw new Exception();
      }
      if (Crossing.grid[k][l].state==0)
      {
        loop: for (int f=0; f<6; f++)
        {
          for (int s=1; s<4; k++)
          {
            if (Crossing.inventory[f][s]==null)
            {
              Crossing.inventory[f][s]=Crossing.grid[k][l];
              break loop;
            }
            else if (f==5 && s==3)
            {
              System.out.println("Inventory is full!");
            }
          }
        }
        Crossing.grid[k][l]=temp;
        if (y)
          box.y=(int)(Math.round(box.y/64.0)*64);
        else
          box.x=(int)(Math.round(box.x/64.0)*64);
        throw new Exception();
      }
    }
    throw new Exception();
  }
  
  private void checkEquipment(int k, int l) throws Exception
  {
    if (Crossing.inventory[0][0]==null)
      throw new Exception();
    if (Crossing.inventory[0][0].equipment==0)
      throw new Exception();
    if (lastDirection==0)
      useEquipment(k, l, 0, -1);
    if (lastDirection==1)
      useEquipment(k, l, 0, 1);
    if (lastDirection==2)
      useEquipment(k, l, -1, 0);
    useEquipment(k, l, 1, 0);
  }
  
  private void useEquipment(int k, int l, int x, int y) throws Exception
  {
    if (Crossing.inventory[0][0].equipment==1) 
    {
      if (x!=0)
        useShovel(k+x, l+y, false);
      useShovel(k+x, l+y, true);
    }
    if (Crossing.inventory[0][0].equipment==2)
    {
      Rectangle temp = new Rectangle(box.x+x*64, box.y+y*64, 64, 64);
      for (int a=-1; a<2; a++)
      {
        for (int b=-1; b<2; b++)
        {
          for (int d=0; d<Crossing.bugs[temp.x/64+a][temp.y/64+b].size(); d++)
          {
            if (Crossing.bugs[temp.x/64+a][temp.y/64+b].get(d).box.intersects(temp))
            {
              for (int f=0; f<6; f++)
              {
                for (int s=1; s<4; s++)
                {
                  if (Crossing.inventory[f][s]==null)
                  {
                    Crossing.inventory[f][s]=Crossing.bugs[temp.x/64+a][temp.y/64+b].get(d);
                    Crossing.bugs[temp.x/64+a][temp.y/64+b].remove(d);
                    throw new Exception();
                  }
                  else if (f==5 && s==3)
                  {
                    System.out.println("Inventory is full!");
                    throw new Exception();
                  }
                }
              }
            }
          }
        }
      }
      throw new Exception();
    }//catch bug
    if (Crossing.inventory[0][0].equipment==3)
    {
      if (x==0)
      {
        Crossing.bobber.box.x=box.x+24;
        Crossing.bobber.box.y=box.x+y*192;
      }
      else
      {
        Crossing.bobber.box.x=box.x+x*192;
        Crossing.bobber.box.y=box.x+24;
      }
      for (Rectangle d:Crossing.water)
      {
        if (Crossing.bobber.box.intersects(d))
        {
          fishing=true;
          throw new Exception();
        }
      }
      Crossing.bobber.box.x=0;
      Crossing.bobber.box.y=0;
      throw new Exception();
    }//catch fish
    if (Crossing.inventory[0][0].equipment==4)
    {}//water
    if (Crossing.inventory[0][0].equipment==5)
    {}//axe
  }
  
  private void drop() throws Exception
  {
    Crossing.inventory[selected1x][selected1y].state=1;
    menu=0;
    selected2=0;
    if (box.x%64<32)
    {
      if (box.y%64<32)
      {
        dropHelper(box.x/64, box.y/64);
        dropHelper(box.x/64+1, box.y/64);
        dropHelper(box.x/64, box.y/64+1);
        dropHelper(box.x/64+1,box.y/64+1);
      }
      else
      {
        dropHelper(box.x/64, box.y/64+1);
        dropHelper(box.x/64, box.y/64);
        dropHelper(box.x/64+1, box.y/64+1);
        dropHelper(box.x/64+1,box.y/64);
      }
    }
    else if (box.y%64<32)
    {
      dropHelper(box.x/64+1, box.y/64);
      dropHelper(box.x/64, box.y/64);
      dropHelper(box.x/64+1, box.y/64+1);
      dropHelper(box.x/64,box.y/64+1);
    }
    else
    {
      dropHelper(box.x/64+1, box.y/64+1);
      dropHelper(box.x/64, box.y/64+1);
      dropHelper(box.x/64+1, box.y/64);
      dropHelper(box.x/64,box.y/64);
    }
  }
  
  private void dropHelper(int k, int l) throws Exception
  {
    if (Crossing.grid[k][l] == null)
    {
      Crossing.inventory[selected1x][selected1y].box.y=Math.round(l)*64;
      Crossing.inventory[selected1x][selected1y].box.x=Math.round(k)*64;
      Crossing.grid[k][l] = Crossing.inventory[selected1x][selected1y];
      Crossing.inventory[selected1x][selected1y]=null;
      throw new Exception();
    }
  }
  
  private void eat()
  {
    Crossing.inventory[selected1x][selected1y]=null;
    menu=0;
    selected2=0;
  }
  
  private void burry()
  {
    Crossing.inventory[selected1x][selected1y].state=0;
    Crossing.inventory[selected1x][selected1y].box.x=buryx*64;
    Crossing.inventory[selected1x][selected1y].box.y=buryy*64;
    Crossing.grid[buryx][buryy]=Crossing.inventory[selected1x][selected1y];
    Crossing.inventory[selected1x][selected1y]=null;
    buryx=0;
    menu=0;
    selected2=0;
  }
  
  private void hold()
  {
    Crossing.inventory[selected1x][selected1y].state=1;
    menu=0;
    selected2=0;
  }
  
  private boolean worldCollision(Rectangle c, boolean playerMoving)
  {
    if (Crossing.grid[box.x/64][box.y/64] instanceof Hole ||  Crossing.grid[(box.x+63)/64][box.y/64] instanceof Hole || Crossing.grid[box.x/64][(box.y+63)/64] instanceof Hole || Crossing.grid[(box.x+63)/64][(box.y+63)/64] instanceof Hole)
      return false;
    for (int a=-1; a<2; a++)
    {
      for (int b=-1; b<2; b++)
      {
        for (int d=0; d<Crossing.bugs[c.x/64+a][c.y/64+b].size(); d++)
        {
          if (Crossing.bugs[c.x/64+a][c.y/64+b].get(d).box.intersects(c))
          {
            if (playerMoving)
              Crossing.bugs[c.x/64+a][c.y/64+b].get(d).steppedOn();
            else
              return false;
          }
        }
        for (int d=0; d<Crossing.villagers[c.x/64+a][c.y/64+b].size(); d++)
        {
          if (Crossing.villagers[c.x/64+a][c.y/64+b].get(d).box.intersects(c))
            return false;
        }
      }
    }
    for (Rectangle x:Crossing.worldWalls)
    {
      if (x.intersects(c))
        return false;
    }
    return true;
  }
}