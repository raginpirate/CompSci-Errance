import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Color;
public class Player
{
  Rectangle box = new Rectangle(900,1000,64,64);
  boolean[][] selling= new boolean[6][3];
  int bought;
  int sold;
  boolean insFunds;
  String caught;
  int menu=0;
  int selected1x=0;
  int selected1y=0;
  int selected2;
  int moneta=1000;
  boolean fishing=false;
  private boolean up;
  private boolean down;
  private boolean left;
  private boolean right;
  int lastDirection=0; // 0 up 1 down 2 left 3 right
  private int buryx;
  private int buryy;
  boolean sprint;
  boolean held;
  int heldx;
  int heldy;
  int animation=0;
  int animationCount=0;
  public void move()
  {
    if (up)
    {
      lastDirection=0;
      if (right)
        moveHelper(1, -1);
      else if (left)
        moveHelper(-1, -1);
      else
        moveHelper(0, -2);
      animationCount++;
      if (sprint)
        animationCount++;
      if (animationCount==40 || animationCount==41)
      {
        animationCount=0;
        animation=2;
      }
      else if (animationCount==20 || animationCount==21)
        animation=1;
      else if (animationCount==10 || animationCount==30 || animationCount==11 || animationCount==31)
        animation=0;
    }
    else if (down)
    {
      lastDirection=1;
      if (right)
        moveHelper(1, 1);
      else if (left)
        moveHelper(-1, 1);
      else
        moveHelper(0, 2);
      animationCount++;
      if (sprint)
        animationCount++;
      if (animationCount==40 || animationCount==41)
      {
        animationCount=0;
        animation=2;
      }
      else if (animationCount==20 || animationCount==21)
        animation=1;
      else if (animationCount==10 || animationCount==30 || animationCount==11 || animationCount==31)
        animation=0;
    }
    else if (left)
    {
      lastDirection=2;
      moveHelper(-2, 0);
      animationCount++;
      if (sprint)
        animationCount++;
      if (animationCount==40 || animationCount==41)
      {
        animationCount=0;
        animation=2;
      }
      else if (animationCount==20 || animationCount==21)
        animation=1;
      else if (animationCount==10 || animationCount==30 || animationCount==11 || animationCount==31)
        animation=0;
    }
    else if (right)
    {
      lastDirection=3;
      moveHelper(2, 0);
      animationCount++;
      if (sprint)
        animationCount++;
      if (animationCount==40 || animationCount==41)
      {
        animationCount=0;
        animation=2;
      }
      else if (animationCount==20 || animationCount==21)
        animation=1;
      else if (animationCount==10 || animationCount==30 || animationCount==11 || animationCount==31)
        animation=0;
    }
    else
    {
      animation=0;
      animationCount=0;
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
          if (menu==8)
          {
            if (selected1y>0)
              selected1y--;
          }
          else if (selected2>0 && menu!=11)
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
          if (menu==6) 
          {
            if (selected2<3)
              selected2++;
          }
          else if (menu==7)
          {
            if (selected2<7)
              selected2++;
          }
          else if (menu==8)
          {
            if (selected1y<2)
              selected1y++;
          }
          else if(menu==10)
          {
            if (selected2<1)
              selected2++;
          }
          else if (selected2<(menu-1)/2+2 && menu!=11)
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
        if (menu==8)
        {
          if (selected1x>-1)
            selected1x--;
        }
        else if (menu==1)
        {
          if (selected1x>0)
            selected1x--;
        }
        else if (menu==11)
        {
          if (selected2==1)
            selected2=0;
        }
        else if (menu == 0)
          left=true;
      }
      
      if (s=="right")
      {
        if (menu==8)
        {
          if (selected1x<6)
            selected1x++;
        }
        else if (menu==1)
        {
          if (selected1x<5 && selected1y!=0)
            selected1x++;
        }
        else if (menu==11)
        {
          if (selected2==0)
            selected2=1;
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
          if (Crossing.shopping)
            buryx=0;
          else
          {
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
      }
      
      if (s=="w")
      {
        for(int a=0; a<76; a++)
        {
          for (int b=0; b<56; b++)
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
        //Crossing.quit=false;
        System.out.println(box.x + " " + box.y);
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
  
  private void purchase(int cost, String itemName)
  {
    loop: for (int f=0; f<6; f++)
    {
      for (int k=1; k<4; k++)
      {
        if (Crossing.inventory[f][k]==null)
        {
          if (itemName.contains("seed"))
            Crossing.inventory[f][k]=new Plants(itemName);
          else
            Crossing.inventory[f][k]=new Items(itemName);
          moneta=moneta-cost;
          bought=cost;
          Graphix.popupTimer=0;
          break loop;
        }
        else if (f==5 && k==3){Crossing.invFull=true;Graphix.popupTimer=0;}
      }
    }
  }
  
  public void spaceBar()
  {
    try
    {
      if (box.x>4500)
      {
        switch (menu)
        {
          case 0:Rectangle temp = new Rectangle(box.x, box.y, 64, 64);
          if (lastDirection==0)
            temp.y=temp.y-64;
          else if (lastDirection==1)
            temp.y=temp.y+64;
          else if (lastDirection==2)
            temp.x=temp.x-64;
          else if (lastDirection==3)
            temp.x=temp.x+64;
          if (temp.intersects(Crossing.shopKeeper))
          {
            right=false;
            left=false;
            up=false;
            down=false;
            menu=6;
          }
          break;
          case 1:
            if (held)
          {
            Entity tempz = Crossing.inventory[selected1x][selected1y];
            Crossing.inventory[selected1x][selected1y] = Crossing.inventory[heldx][heldy];
            Crossing.inventory[heldx][heldy] = tempz;
            held = false;
            break;
          }
            if (Crossing.inventory[selected1x][selected1y] != null)
              menu=10;
            break;
            
          case 10: switch (selected2)
          {
            case 0:hold();
            break;
            
            case 1: menu=1;
            selected2=0;
            break;
          }
          break;
          case 6:switch(selected2)
          {
            case 0:menu=7;
            break;
            case 1:menu=8;
            break;
            case 2:menu=9;
            break;
            case 3:menu=0;
            break;
          }
          selected2=0;
          break;
          case 7:switch(selected2)
          {
            case 0:
              if (moneta>=100)
              purchase(100, "shovel");
              else
              {
                insFunds=true;
                Graphix.popupTimer=0;
              }
              break;
            case 1:
              if (moneta>=125)
              purchase(125, "can");
              else
              {
                insFunds=true;
                Graphix.popupTimer=0;
              }
              break;
            case 2:
              if (moneta>=200)
              purchase(200, "net");
              else
              {
                insFunds=true;
                Graphix.popupTimer=0;
              }
              break;
            case 3:
              if (moneta>=500)
              purchase(500, "rod");
              else
              {
                insFunds=true;
                Graphix.popupTimer=0;
              }
              break;
            case 4:
              if (moneta>=1000)
              purchase(1000, "turnip seeds");
              else
              {
                insFunds=true;
                Graphix.popupTimer=0;
              }
              break;
            case 5:
              if (moneta>=1000)
              purchase(1000, "strawberry seeds");
              else
              {
                insFunds=true;
                Graphix.popupTimer=0;
              }
              break;
            case 6:
              if (moneta>=5000)
              purchase(5000, "carrot seeds");
              else
              {
                insFunds=true;
                Graphix.popupTimer=0;
              }
              break;
            case 7:menu=6;
            selected2=0;
            break;
          }
          break;
          case 8:if (selected1x==-1)
          {
            menu=6;
            for (int a=0; a<6; a++)
            {
              for (int b=0; b<3; b++)
                selling[a][b]=false;
            }
            selected1x=0;
            selected1y=0;
          }
          else if (selected1x==6)
          {
            int profit=0;
            for (int a=0; a<6; a++)
            {
              for (int b=0; b<3; b++)
              {
                if (selling[a][b])
                {
                  profit=profit+Crossing.inventory[a][b+1].moneta;
                  Crossing.inventory[a][b+1]=null;
                  selling[a][b]=false;
                }
              }
            }
            moneta=moneta+profit;
            sold=profit;
            Graphix.popupTimer=0;
            selected1x=0;
            selected1y=0;
            menu=6;
          }
          else if (selling[selected1x][selected1y])
            selling[selected1x][selected1y]=false;
          else
            selling[selected1x][selected1y]=true;
          break;
        }
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
                caught=Crossing.caught.s;
                Graphix.popupTimer=0;
                Crossing.fish[Crossing.caught.box.x/64][Crossing.caught.box.y/64].remove(Crossing.caught);
                Crossing.caught=null;
                Crossing.spawn(3);
                throw new Exception();
              }
              else if (f==5 && s==3)
              {
                Crossing.invFull=true;
                Graphix.popupTimer=0;
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
          Rectangle temp = new Rectangle(box.x, box.y, 64, 64);
          if (lastDirection==0)
            temp.y=temp.y-64;
          else if (lastDirection==1)
            temp.y=temp.y+64;
          else if (lastDirection==2)
            temp.x=temp.x-64;
          else if (lastDirection==3)
            temp.x=temp.x+64;
          if (temp.intersects(Crossing.sign))
          {
            right=false;
            left=false;
            up=false;
            down=false;
            menu=12;
          }
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
          Entity temps = Crossing.inventory[selected1x][selected1y];
          Crossing.inventory[selected1x][selected1y] = Crossing.inventory[heldx][heldy];
          Crossing.inventory[heldx][heldy] = temps;
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
        case 11:if (selected2==0)
          Crossing.quit=false;
        else
        {
          menu=0;
          selected2=0;
        }
        break;
        case 12:menu=0; 
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
          for (int s=1; s<4; s++)
          {
            if (Crossing.inventory[f][s]==null)
            {
              Crossing.inventory[f][s]=Crossing.grid[k][l];
              caught=Crossing.inventory[f][s].s;
              Graphix.popupTimer=0;
              break loop;
            }
            else if (f==5 && s==3)
            {
              Crossing.invFull=true;
              Graphix.popupTimer=0;
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
                    caught=Crossing.inventory[f][s].s;
                    Graphix.popupTimer=0;
                    Crossing.bugs[temp.x/64+a][temp.y/64+b].remove(d);
                    Crossing.spawn(2);
                    throw new Exception();
                  }
                  else if (f==5 && s==3)
                  {
                    Crossing.invFull=true;
                    Graphix.popupTimer=0;
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
                    caught=Crossing.inventory[f][s].s;
                    Graphix.popupTimer=0;
                    Crossing.spawn(1);
                    throw new Exception();
                  }
                  else if (f==5 && s==3)
                  {
                    Crossing.invFull=true;
                    Graphix.popupTimer=0;
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
        if (y<0)
          Crossing.bobber.box.y=box.y+y*160;
        else
          Crossing.bobber.box.y=box.y+y*224;
      }
      else
      {
        Crossing.bobber.box.y=box.y+24;
        if (x<0)
          Crossing.bobber.box.x=box.x+x*160;
        else
          Crossing.bobber.box.x=box.x+x*224;
      }
      for (Polygon d:Crossing.water)
      {
        if (d.contains(Crossing.bobber.box) && !(Crossing.bobber.box.intersects(Crossing.bridgeOne) || Crossing.bobber.box.intersects(Crossing.bridgeTwo)))
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
        box.x=4052;
        box.y=740;
        Crossing.door.x=4052;
        Crossing.door.y=660;
        Crossing.shopping=false;
        return false;
      }
      if (!(Crossing.shopWalls[0].contains(box)))
        return false;
      if (Crossing.shopWalls[1].intersects(box))
        return false;
      if (Crossing.shopWalls[2].intersects(box))
        return false;
      if (Crossing.shopWalls[3].intersects(box))
        return false;
      return true;
    }
    if (box.intersects(Crossing.door))
    {
      box.x=5768;
      box.y=4516;
      Crossing.door.x=5768;
      Crossing.door.y=4600;
      Crossing.shopping=true;
      return false;
    }
    if (box.intersects(Crossing.quitGame))
    {
      box.y=box.y+10;
      menu=11;
      up=false;
      down=false;
      left=false;
      right=false;
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
    if (!(playerMoving && (Crossing.bridgeOne.contains(box) || Crossing.bridgeTwo.contains(box))))
    {
      for (Polygon x:Crossing.water)
      {
        if (x.intersects(c))
          return false;
      }
    }
    return true;
  }
}