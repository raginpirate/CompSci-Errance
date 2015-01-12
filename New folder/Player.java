import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Polygon;
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
  boolean sprint;
  boolean held;
  int heldx;
  int heldy;
  
  public void move()
  {
    if (up)
    {
      lastDirection=0;
      if (right)
        moveHelper(20, -20);
      else if (left)
        moveHelper(-20, -20);
      else
        moveHelper(0, -30);
    }
    else if (down)
    {
      lastDirection=1;
      if (right)
        moveHelper(20, 20);
      else if (left)
        moveHelper(-20, 20);
      else
        moveHelper(0, 30);
    }
    else if (left)
    {
      lastDirection=2;
      moveHelper(-30, 0);
    }
    else if (right)
    {
      lastDirection=3;
      moveHelper(30, 0);
    }
  }
  
  private void moveHelper(int k, int l)
  {
    if (sprint)
    {
      k=k*2;
      l=l*2;
    }
    box.x=box.x+k;
    if (!(worldCollision(box, true)))
      box.x=box.x-k;
    box.y=box.y+l;
    if (!(worldCollision(box, true)))
      box.y=box.y-l;
  }
  
  public void input(String s)
  {
    if (!fishing)
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
          held=false;
          selected2=0;
        }
        else
        {
          right=false;
          left=false;
          up=false;
          down=false;
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
              if (Crossing.grid[a][b].state>1)
                Crossing.grid[a][b].update();
            }
          }
        }
      }
      
      if (s=="e")
        Crossing.quit=false;
        //System.out.println(box.x + " " + box.y);
      //System.out.println("Run function hotkey(e)");
      
      if (s=="r")
        sprint=true;
    }
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
    if (x=="r")
      sprint=false;
  }
  
  public void spaceBar()
  {
    try
    {
      if (box.x>4000)
      {
        Rectangle temp = new Rectangle(box.x, box.y, 64, 64);
        if (lastDirection==0)
          temp.y=temp.y-64;
        else if (lastDirection==1)
          temp.y=temp.y+64;
        else if (lastDirection==2)
          temp.x=temp.x-64;
        else if (lastDirection==3)
          temp.x=temp.x+64;
        if (temp.intersects(Crossing.shopKeeper))
          menu=6;
        throw new Exception();
      }
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
                Crossing.fish[Crossing.caught.box.x/64][Crossing.caught.box.y/64].remove(Crossing.caught);
                Crossing.caught=null;
                Crossing.spawn(3);
                throw new Exception();
              }
              else if (f==5 && s==3)
              {
                System.out.println("Inventory is full!");
              }
            }
          }
        }
        throw new Exception();
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
      if (Crossing.grid[k][l] == null || (Crossing.grid[k][l] instanceof Plants && Crossing.grid[k][l].state>1))
      {
        if (y)
        {
          int heldcoord = box.y;
          box.y=(int)(Math.round(box.y/64.0)*64);
          if (!worldCollision(box, true))
          {
            box.y=heldcoord;
            throw new Exception();
          }
        }
        else
        {
          int heldcoord = box.x;
          box.x=(int)(Math.round(box.x/64.0)*64);
          if (!worldCollision(box, true))
          {
            box.x=heldcoord;
            throw new Exception();
          }
        }
        Crossing.grid[k][l]=temp;
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
        if (y)
        {
          int heldcoord = box.y;
          box.y=(int)(Math.round(box.y/64.0)*64);
          if (!worldCollision(box, true))
          {
            box.y=heldcoord;
            throw new Exception();
          }
        }
        else
        {
          int heldcoord = box.x;
          box.x=(int)(Math.round(box.x/64.0)*64);
          if (!worldCollision(box, true))
          {
            box.x=heldcoord;
            throw new Exception();
          }
        }
        Crossing.grid[k][l]=temp;
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
                    Crossing.spawn(2);
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
          for (int d=0; d<Crossing.flyingBugs[temp.x/64+a][temp.y/64+b].size(); d++)
          {
            if (Crossing.flyingBugs[temp.x/64+a][temp.y/64+b].get(d).box.intersects(temp))
            {
              for (int f=0; f<6; f++)
              {
                for (int s=1; s<4; s++)
                {
                  if (Crossing.inventory[f][s]==null)
                  {
                    Crossing.inventory[f][s]=Crossing.flyingBugs[temp.x/64+a][temp.y/64+b].get(d);
                    Crossing.flyingBugs[temp.x/64+a][temp.y/64+b].remove(d);
                    Crossing.spawn(1);
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
        Crossing.bobber.box.y=box.y+y*192;
      }
      else
      {
        Crossing.bobber.box.x=box.x+x*192;
        Crossing.bobber.box.y=box.y+24;
      }
      for (Polygon d:Crossing.water)
      {
        if (d.intersects(Crossing.bobber.box))
        {
          fishing=true;
          right=false;
          left=false;
          up=false;
          down=false;
          throw new Exception();
        }
      }
      Crossing.bobber.box.x=0;
      Crossing.bobber.box.y=0;
      throw new Exception();
    }//catch fish
    if (Crossing.inventory[0][0].equipment==4)
    {
      useWater(x, y);
    }//water
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
    if (Crossing.inventory[selected1x][selected1y] instanceof Plants)
      Crossing.inventory[selected1x][selected1y].state=2;
    else
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
    heldx=selected1x;
    heldy=selected1y;
    held=true;
    menu=1;
  }
  
  private void useWater(int x, int y) throws Exception
  {
    if (Crossing.grid[box.x/64+x][box.y/64+y].water<100)
      Crossing.grid[box.x/64+x][box.y/64+y].water=Crossing.grid[box.x/64+x][box.y/64+y].water+20;
    if (Crossing.grid[box.x/64+x+1][box.y/64+y] instanceof Plants)
      Crossing.grid[box.x/64+x+1][box.y/64+y].water=Crossing.grid[box.x/64+x+1][box.y/64+y].water+20;
    if (Crossing.grid[box.x/64+x][box.y/64+y+1] instanceof Plants)
      Crossing.grid[box.x/64+x][box.y/64+y+1].water=Crossing.grid[box.x/64+x][box.y/64+y+1].water+20;
    if (Crossing.grid[box.x/64+x+1][box.y/64+y+1] instanceof Plants)
      Crossing.grid[box.x/64+x+1][box.y/64+y+1].water=Crossing.grid[box.x/64+x+1][box.y/64+y+1].water+20;
    throw new Exception();
  }
  
  private boolean worldCollision(Rectangle c, boolean playerMoving)
  {
    if (Crossing.shopping)
    {
      if (box.intersects(Crossing.door))
      {
        box.x=640;
        box.y=800;
        Crossing.door.x=640;
        Crossing.door.y=640;
        Crossing.shopping=false;
        return false;
      }
      for (Rectangle x:Crossing.worldWalls)
      {
        if (x.intersects(c))
          return false;
      }
      for (Polygon x:Crossing.water)
      {
        if (x.intersects(c))
          return false;
      }
      return true;
    }
    if (box.intersects(Crossing.door))
    {
      box.x=5000;
      box.y=5000;
      Crossing.door.x=5000;
      Crossing.door.y=5100;
      Crossing.shopping=true;
      return true;
    }
    if (Crossing.grid[box.x/64][box.y/64] instanceof Hole ||  Crossing.grid[(box.x+63)/64][box.y/64] instanceof Hole || Crossing.grid[box.x/64][(box.y+63)/64] instanceof Hole || Crossing.grid[(box.x+63)/64][(box.y+63)/64] instanceof Hole)
      return false;
    for (int a=-1; a<2; a++)
    {
      for (int b=-1; b<2; b++)
      {
        if (!playerMoving)
        {
          for (int d=0; d<Crossing.bugs[c.x/64+a][c.y/64+b].size(); d++)
          {
            if (Crossing.bugs[c.x/64+a][c.y/64+b].get(d).box.intersects(c))
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
    for (Polygon x:Crossing.water)
    {
      if (x.intersects(c))
        return false;
    }
    return true;
  }
}