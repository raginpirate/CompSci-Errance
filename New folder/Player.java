import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
public class Player
{
  Rectangle box = new Rectangle(605,605,64,64);
  private boolean up;
  private boolean down;
  private boolean left;
  private boolean right;
  //Rectangle temp = new Rectangle(0,0,0,0);
  private int lastDirection=0; // 0 up 1 down 2 left 3 right
  int selected1x;
  int selected1y;
  int selected2;
  private int buryx;
  private int buryy;
  int menu=0;
  boolean held;
  private int heldx;
  private int heldy;
  
  public void move()
  {
    if (up)
    {
      if (right)
      {
        box.x=box.x+4;
        if (!(worldCollision(box, true)))
          box.x=box.x-4;
        box.y=box.y-4;
        if (!(worldCollision(box, true)))
          box.y=box.y+4;
      }
      else if (left)
      {
        box.x=box.x-4;
        if (!(worldCollision(box, true)))
          box.x=box.x+4;
        box.y=box.y-4;
        if (!(worldCollision(box, true)))
          box.y=box.y+4;
      }
      else
      {
        box.y=box.y-6;
        if (!(worldCollision(box, true)))
          box.y=box.y+6;
      }
    }
    else if (down)
    {
      if (right)
      {
        box.x=box.x+4;
        if (!(worldCollision(box, true)))
          box.x=box.x-4;
        box.y=box.y+4;
        if (!(worldCollision(box, true)))
          box.y=box.y-4;
      }
      else if (left)
      {
        box.x=box.x-4;
        if (!(worldCollision(box, true)))
          box.x=box.x+4;
        box.y=box.y+4;
        if (!(worldCollision(box, true)))
          box.y=box.y-4;
      }
      else
      {
        box.y=box.y+6;
        if (!(worldCollision(box, true)))
          box.y=box.y-6;
      }
    }
    else if (left)
    {
      box.x=box.x-6;
      if (!(worldCollision(box, true)))
        box.x=box.x+6;
    }
    else if (right)
    {
      box.x=box.x+6;
      if (!(worldCollision(box, true)))
        box.x=box.x-6;
    }
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
            functionOne(0, 0);
          else
            functionOne(0, 1);
        }
        else if (box.y%64<32)
          functionOne(1, 0);
        else
          functionOne(1, 1);
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
      System.out.println("Run function hotkey(e)");
    
    if (s=="r")
      System.out.println("Run function hotkey(r)");
    
    if (s==" ")
    {
      switch(menu)
      {
        //When we are not using the inventory (case 0)
        case 0:if (box.x%64<32)
        {
          if (box.y%64<32)
          {
            if (Crossing.grid[box.x/64][box.y/64] != null)
            {
              if (Crossing.grid[box.x/64][box.y/64].interact())
                break;
            }
            if (Crossing.grid[box.x/64+1][box.y/64] != null)
            {
              if (Crossing.grid[box.x/64+1][box.y/64].interact())
                break;
            }
            if (Crossing.grid[box.x/64][box.y/64+1] != null)
            {
              if (Crossing.grid[box.x/64][box.y/64+1].interact())
                break;
            }
            if (Crossing.grid[box.x/64+1][box.y/64+1] != null)
            {
              if (Crossing.grid[box.x/64+1][box.y/64+1].interact())
                break;
            }
            if (Crossing.inventory[0][0]==null)
              break;
            if (Crossing.inventory[0][0].equipment==0)
              break;
            if (Crossing.inventory[0][0].equipment==1) 
            {
              if (lastDirection==0)
              {
                if (Crossing.grid[box.x/64][box.y/64-1] instanceof Hole)
                {
                  Crossing.grid[box.x/64][box.y/64-1]=null;
                  break;
                }
                Hole temp = new Hole();
                temp.box.x=Math.round(box.x/64)*64;
                temp.box.y=Math.round(box.y/64+1)*64;
                if (worldCollision(temp.box, false))
                {
                  if (Crossing.grid[box.x/64][box.y/64-1] == null || (Crossing.grid[box.x/64][box.y/64-1] instanceof Plants && Crossing.grid[box.x/64][box.y/64-1].state==0))
                  {
                    Crossing.grid[box.x/64][box.y/64-1]=temp;
                    box.y=(int)(Math.round(box.y/64.0)*64);
                    break;
                  }
                  if (Crossing.grid[box.x/64][box.y/64-1].state==0)
                  {
                    for (int f=0; f<6; f++)
                    {
                      for (int k=1; k<4; k++)
                      {
                        if (Crossing.inventory[f][k]==null)
                        {
                          Crossing.inventory[f][k]=Crossing.grid[box.x/64][box.y/64-1];
                          f=7;
                          k=4;
                        }
                        else if (f==5 && k==3)
                        {
                          System.out.println("Inventory is full!");
                        }
                      }
                    }
                    Crossing.grid[box.x/64][box.y/64-1]=temp;
                    box.y=(int)(Math.round(box.y/64.0)*64);
                    break;
                  }
                }
                break;
              }
              if (lastDirection==1)
              {
                if (Crossing.grid[box.x/64][box.y/64+1] instanceof Hole)
                {
                  Crossing.grid[box.x/64][box.y/64+1]=null;
                  break;
                }
                Hole temp = new Hole();
                temp.box.x=Math.round(box.x/64)*64;
                temp.box.y=Math.round(box.y/64+1)*64;
                if (worldCollision(temp.box, false))
                {
                  if (Crossing.grid[box.x/64][box.y/64+1] == null || (Crossing.grid[box.x/64][box.y/64+1] instanceof Plants && Crossing.grid[box.x/64][box.y/64+1].state==0))
                  {
                    Crossing.grid[box.x/64][box.y/64+1]=temp;
                    box.y=(int)(Math.round(box.y/64.0)*64);
                    break;
                  }
                  if (Crossing.grid[box.x/64][box.y/64+1].state==0)
                  {
                    for (int f=0; f<6; f++)
                    {
                      for (int k=1; k<4; k++)
                      {
                        if (Crossing.inventory[f][k]==null)
                        {
                          Crossing.inventory[f][k]=Crossing.grid[box.x/64][box.y/64+1];
                          f=7;
                          k=4;
                        }
                        else if (f==5 && k==3)
                        {
                          System.out.println("Inventory is full!");
                        }
                      }
                    }
                    Crossing.grid[box.x/64][box.y/64+1]=temp;
                    box.y=(int)(Math.round(box.y/64.0)*64);
                    break;
                  }
                }
                break;
              }
              if (lastDirection==2)
              {
                if (Crossing.grid[box.x/64-1][box.y/64] instanceof Hole)
                {
                  Crossing.grid[box.x/64-1][box.y/64]=null;
                  break;
                }
                Hole temp = new Hole();
                temp.box.x=Math.round(box.x/64-1)*64;
                temp.box.y=Math.round(box.y/64)*64;
                if (worldCollision(temp.box, false))
                {
                  if (Crossing.grid[box.x/64-1][box.y/64] == null || (Crossing.grid[box.x/64-1][box.y/64] instanceof Plants && Crossing.grid[box.x/64-1][box.y/64].state==0))
                  {
                    Crossing.grid[box.x/64-1][box.y/64]=temp;
                    box.x=(int)(Math.round(box.x/64.0)*64);
                    break;
                  }
                  if (Crossing.grid[box.x/64-1][box.y/64].state==0)
                  {
                    for (int f=0; f<6; f++)
                    {
                      for (int k=1; k<4; k++)
                      {
                        if (Crossing.inventory[f][k]==null)
                        {
                          Crossing.inventory[f][k]=Crossing.grid[box.x/64-1][box.y/64];
                          f=7;
                          k=4;
                        }
                        else if (f==5 && k==3)
                        {
                          System.out.println("Inventory is full!");
                        }
                      }
                    }
                    Crossing.grid[box.x/64-1][box.y/64]=temp;
                    box.x=(int)(Math.round(box.x/64.0)*64);
                    break;
                  }
                }
                break;
              }
              if (Crossing.grid[box.x/64+1][box.y/64] instanceof Hole)
              {
                Crossing.grid[box.x/64+1][box.y/64]=null;
                break;
              }
              Hole temp = new Hole();
              temp.box.x=Math.round(box.x/64+1)*64;
              temp.box.y=Math.round(box.y/64)*64;
              if (worldCollision(temp.box, false))
              {
                if (Crossing.grid[box.x/64+1][box.y/64] == null || (Crossing.grid[box.x/64+1][box.y/64] instanceof Plants && Crossing.grid[box.x/64+1][box.y/64].state==0))
                {
                  Crossing.grid[box.x/64+1][box.y/64]=temp;
                  box.x=(int)(Math.round(box.x/64.0)*64);
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64].state==0)
                {
                  for (int f=0; f<6; f++)
                  {
                    for (int k=1; k<4; k++)
                    {
                      if (Crossing.inventory[f][k]==null)
                      {
                        Crossing.inventory[f][k]=Crossing.grid[box.x/64+1][box.y/64];
                        f=7;
                        k=4;
                      }
                      else if (f==5 && k==3)
                      {
                        System.out.println("Inventory is full!");
                      }
                    }
                  }
                  Crossing.grid[box.x/64+1][box.y/64]=temp;
                  box.x=(int)(Math.round(box.x/64.0)*64);
                  break;
                }
              }
              break;
            }//dig
            if (Crossing.inventory[0][0].equipment==2)
            {
              net();
              break;
            }//catch bug
            if (Crossing.inventory[0][0].equipment==3)
            {}//catch fish
            if (Crossing.inventory[0][0].equipment==4)
            {}//water
            if (Crossing.inventory[0][0].equipment==5)
            {}//axe
          }
          else
          {
            if (Crossing.grid[box.x/64][box.y/64+1] != null)
            {
              if (Crossing.grid[box.x/64][box.y/64+1].interact())
                break;
            }
            if (Crossing.grid[box.x/64][box.y/64] != null)
            {
              if (Crossing.grid[box.x/64][box.y/64].interact())
                break;
            }
            if (Crossing.grid[box.x/64+1][box.y/64+1] != null)
            {
              if (Crossing.grid[box.x/64+1][box.y/64+1].interact())
                break;
            }
            if (Crossing.grid[box.x/64+1][box.y/64] != null)
            {
              if (Crossing.grid[box.x/64+1][box.y/64].interact())
                break;
            }
            if (Crossing.inventory[0][0]==null)
              break;
            if (Crossing.inventory[0][0].equipment==0)
              break;
            if (Crossing.inventory[0][0].equipment==1) 
            {
              if (lastDirection==0)
              {
                if (Crossing.grid[box.x/64][box.y/64] instanceof Hole)
                {
                  Crossing.grid[box.x/64][box.y/64]=null;
                  break;
                }
                Hole temp = new Hole();
                temp.box.x=Math.round(box.x/64)*64;
                temp.box.y=Math.round(box.y/64)*64;
                if (worldCollision(temp.box, false))
                {
                  if (Crossing.grid[box.x/64][box.y/64] == null || (Crossing.grid[box.x/64][box.y/64] instanceof Plants && Crossing.grid[box.x/64][box.y/64].state==0))
                  {
                    Crossing.grid[box.x/64][box.y/64]=temp;
                    box.y=(int)(Math.round(box.y/64.0)*64);
                    break;
                  }
                  if (Crossing.grid[box.x/64][box.y/64].state==0)
                  {
                    for (int f=0; f<6; f++)
                    {
                      for (int k=1; k<4; k++)
                      {
                        if (Crossing.inventory[f][k]==null)
                        {
                          Crossing.inventory[f][k]=Crossing.grid[box.x/64][box.y/64];
                          f=7;
                          k=4;
                        }
                        else if (f==5 && k==3)
                        {
                          System.out.println("Inventory is full!");
                        }
                      }
                    }
                    Crossing.grid[box.x/64][box.y/64]=temp;
                    box.y=(int)(Math.round(box.y/64.0)*64);
                    break;
                  }
                }
                break;
              }
              if (lastDirection==1)
              {
                if (Crossing.grid[box.x/64][box.y/64+2] instanceof Hole)
                {
                  Crossing.grid[box.x/64][box.y/64+2]=null;
                  break;
                }
                Hole temp = new Hole();
                temp.box.x=Math.round(box.x/64)*64;
                temp.box.y=Math.round(box.y/64+2)*64;
                if (worldCollision(temp.box, false))
                {
                  if (Crossing.grid[box.x/64][box.y/64+2] == null || (Crossing.grid[box.x/64][box.y/64+2] instanceof Plants && Crossing.grid[box.x/64][box.y/64+2].state==0))
                  {
                    Crossing.grid[box.x/64][box.y/64+2]=temp;
                    box.y=(int)(Math.round(box.y/64.0)*64);
                    break;
                  }
                  if (Crossing.grid[box.x/64][box.y/64+2].state==0)
                  {
                    for (int f=0; f<6; f++)
                    {
                      for (int k=1; k<4; k++)
                      {
                        if (Crossing.inventory[f][k]==null)
                        {
                          Crossing.inventory[f][k]=Crossing.grid[box.x/64][box.y/64+2];
                          f=7;
                          k=4;
                        }
                        else if (f==5 && k==3)
                        {
                          System.out.println("Inventory is full!");
                        }
                      }
                    }
                    Crossing.grid[box.x/64][box.y/64+2]=temp;
                    box.y=(int)(Math.round(box.y/64.0)*64);
                    break;
                  }
                }
                break;
              }
              if (lastDirection==2)
              {
                if (Crossing.grid[box.x/64-1][box.y/64+1] instanceof Hole)
                {
                  Crossing.grid[box.x/64-1][box.y/64+1]=null;
                  break;
                }
                Hole temp = new Hole();
                temp.box.x=Math.round(box.x/64-1)*64;
                temp.box.y=Math.round(box.y/64+1)*64;
                if (worldCollision(temp.box, false))
                {
                  if (Crossing.grid[box.x/64-1][box.y/64+1] == null || (Crossing.grid[box.x/64-1][box.y/64+1] instanceof Plants && Crossing.grid[box.x/64-1][box.y/64+1].state==0))
                  {
                    Crossing.grid[box.x/64-1][box.y/64+1]=temp;
                    box.x=(int)(Math.round(box.x/64.0)*64);
                    break;
                  }
                  if (Crossing.grid[box.x/64-1][box.y/64+1].state==0)
                  {
                    for (int f=0; f<6; f++)
                    {
                      for (int k=1; k<4; k++)
                      {
                        if (Crossing.inventory[f][k]==null)
                        {
                          Crossing.inventory[f][k]=Crossing.grid[box.x/64-1][box.y/64+1];
                          f=7;
                          k=4;
                        }
                        else if (f==5 && k==3)
                        {
                          System.out.println("Inventory is full!");
                        }
                      }
                    }
                    Crossing.grid[box.x/64-1][box.y/64+1]=temp;
                    box.x=(int)(Math.round(box.x/64.0)*64);
                    break;
                  }
                }
                break;
              }
              if (Crossing.grid[box.x/64+1][box.y/64+1] instanceof Hole)
              {
                Crossing.grid[box.x/64+1][box.y/64+1]=null;
                break;
              }
              Hole temp = new Hole();
              temp.box.x=Math.round(box.x/64+1)*64;
              temp.box.y=Math.round(box.y/64+1)*64;
              if (worldCollision(temp.box, false))
              {
                if (Crossing.grid[box.x/64+1][box.y/64+1] == null || (Crossing.grid[box.x/64+1][box.y/64+1] instanceof Plants && Crossing.grid[box.x/64+1][box.y/64+1].state==0))
                {
                  Crossing.grid[box.x/64+1][box.y/64+1]=temp;
                  box.x=(int)(Math.round(box.x/64.0)*64);
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64+1].state==0)
                {
                  for (int f=0; f<6; f++)
                  {
                    for (int k=1; k<4; k++)
                    {
                      if (Crossing.inventory[f][k]==null)
                      {
                        Crossing.inventory[f][k]=Crossing.grid[box.x/64+1][box.y/64+1];
                        f=7;
                        k=4;
                      }
                      else if (f==5 && k==3)
                      {
                        System.out.println("Inventory is full!");
                      }
                    }
                  }
                  Crossing.grid[box.x/64+1][box.y/64+1]=temp;
                  box.x=(int)(Math.round(box.x/64.0)*64);
                  break;
                }
              }
              break;
            }
            if (Crossing.inventory[0][0].equipment==2)
            {
              net();
              break;
            }//catch bug
            if (Crossing.inventory[0][0].equipment==3)
            {}//catch fish
            if (Crossing.inventory[0][0].equipment==4)
            {}//water
            if (Crossing.inventory[0][0].equipment==5)
            {}//axe
          }
        }
        else if (box.y%64<32)
        {
          if (Crossing.grid[box.x/64+1][box.y/64] != null)
          {
            if (Crossing.grid[box.x/64+1][box.y/64].interact())
              break;
          }
          if (Crossing.grid[box.x/64][box.y/64] != null)
          {
            if (Crossing.grid[box.x/64][box.y/64].interact())
              break;
          }
          if (Crossing.grid[box.x/64+1][box.y/64+1] != null)
          {
            if (Crossing.grid[box.x/64+1][box.y/64+1].interact())
              break;
          }
          if (Crossing.grid[box.x/64][box.y/64+1] != null)
          {
            if (Crossing.grid[box.x/64][box.y/64+1].interact())
              break;
          }
          if (Crossing.inventory[0][0]==null)
            break;
          if (Crossing.inventory[0][0].equipment==0)
            break;
          if (Crossing.inventory[0][0].equipment==1) 
          {
            if (lastDirection==0)
            {
              if (Crossing.grid[box.x/64+1][box.y/64-1] instanceof Hole)
              {
                Crossing.grid[box.x/64+1][box.y/64-1]=null;
                break;
              }
              Hole temp = new Hole();
              temp.box.x=Math.round(box.x/64+1)*64;
              temp.box.y=Math.round(box.y/64-1)*64;
              if (worldCollision(temp.box, false))
              {
                if (Crossing.grid[box.x/64+1][box.y/64-1] == null || (Crossing.grid[box.x/64+1][box.y/64-1] instanceof Plants && Crossing.grid[box.x/64+1][box.y/64-1].state==0))
                {
                  Crossing.grid[box.x/64+1][box.y/64-1]=temp;
                  box.y=(int)(Math.round(box.y/64.0)*64);
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64-1].state==0)
                {
                  for (int f=0; f<6; f++)
                  {
                    for (int k=1; k<4; k++)
                    {
                      if (Crossing.inventory[f][k]==null)
                      {
                        Crossing.inventory[f][k]=Crossing.grid[box.x/64+1][box.y/64-1];
                        f=7;
                        k=4;
                      }
                      else if (f==5 && k==3)
                      {
                        System.out.println("Inventory is full!");
                      }
                    }
                  }
                  Crossing.grid[box.x/64+1][box.y/64-1]=temp;
                  box.y=(int)(Math.round(box.y/64.0)*64);
                  break;
                }
              }
              break;
            }
            if (lastDirection==1)
            {
              if (Crossing.grid[box.x/64+1][box.y/64+1] instanceof Hole)
              {
                Crossing.grid[box.x/64+1][box.y/64+1]=null;
                break;
              }
              Hole temp = new Hole();
              temp.box.x=Math.round(box.x/64+1)*64;
              temp.box.y=Math.round(box.y/64+1)*64;
              if (worldCollision(temp.box, false))
              {
                if (Crossing.grid[box.x/64+1][box.y/64+1] == null || (Crossing.grid[box.x/64+1][box.y/64+1] instanceof Plants && Crossing.grid[box.x/64+1][box.y/64+1].state==0))
                {
                  Crossing.grid[box.x/64+1][box.y/64+1]=temp;
                  box.y=(int)(Math.round(box.y/64.0)*64);
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64+1].state==0)
                {
                  for (int f=0; f<6; f++)
                  {
                    for (int k=1; k<4; k++)
                    {
                      if (Crossing.inventory[f][k]==null)
                      {
                        Crossing.inventory[f][k]=Crossing.grid[box.x/64+1][box.y/64+1];
                        f=7;
                        k=4;
                      }
                      else if (f==5 && k==3)
                      {
                        System.out.println("Inventory is full!");
                      }
                    }
                  }
                  Crossing.grid[box.x/64+1][box.y/64+1]=temp;
                  box.y=(int)(Math.round(box.y/64.0)*64);
                  break;
                }
              }
              break;
            }
            if (lastDirection==2)
            {
              if (Crossing.grid[box.x/64][box.y/64] instanceof Hole)
              {
                Crossing.grid[box.x/64][box.y/64]=null;
                break;
              }
              Hole temp = new Hole();
              temp.box.x=Math.round(box.x/64)*64;
              temp.box.y=Math.round(box.y/64)*64;
              if (worldCollision(temp.box, false))
              {
                if (Crossing.grid[box.x/64][box.y/64] == null || (Crossing.grid[box.x/64][box.y/64] instanceof Plants && Crossing.grid[box.x/64][box.y/64].state==0))
                {
                  Crossing.grid[box.x/64][box.y/64]=temp;
                  box.x=(int)(Math.round(box.x/64.0)*64);
                  break;
                }
                if (Crossing.grid[box.x/64][box.y/64].state==0)
                {
                  for (int f=0; f<6; f++)
                  {
                    for (int k=1; k<4; k++)
                    {
                      if (Crossing.inventory[f][k]==null)
                      {
                        Crossing.inventory[f][k]=Crossing.grid[box.x/64][box.y/64];
                        f=7;
                        k=4;
                      }
                      else if (f==5 && k==3)
                      {
                        System.out.println("Inventory is full!");
                      }
                    }
                  }
                  Crossing.grid[box.x/64][box.y/64]=temp;
                  box.x=(int)(Math.round(box.x/64.0)*64);
                  break;
                }
              }
              break;
            }
            if (Crossing.grid[box.x/64+2][box.y/64] instanceof Hole)
            {
              Crossing.grid[box.x/64+2][box.y/64]=null;
              break;
            }
            Hole temp = new Hole();
            temp.box.x=Math.round(box.x/64+2)*64;
            temp.box.y=Math.round(box.y/64)*64;
            if (worldCollision(temp.box, false))
            {
              if (Crossing.grid[box.x/64+2][box.y/64] == null || (Crossing.grid[box.x/64+2][box.y/64] instanceof Plants && Crossing.grid[box.x/64+2][box.y/64].state==0))
              {
                Crossing.grid[box.x/64+2][box.y/64]=temp;
                box.x=(int)(Math.round(box.x/64.0)*64);
                break;
              }
              if (Crossing.grid[box.x/64+2][box.y/64].state==0)
              {
                for (int f=0; f<6; f++)
                {
                  for (int k=1; k<4; k++)
                  {
                    if (Crossing.inventory[f][k]==null)
                    {
                      Crossing.inventory[f][k]=Crossing.grid[box.x/64+2][box.y/64];
                      f=7;
                      k=4;
                    }
                    else if (f==5 && k==3)
                    {
                      System.out.println("Inventory is full!");
                    }
                  }
                }
                Crossing.grid[box.x/64+2][box.y/64]=temp;
                box.x=(int)(Math.round(box.x/64.0)*64);
                break;
              }
            }
            break;
          }
          if (Crossing.inventory[0][0].equipment==2)
          {
            net();
            break;
          }//catch bug
          if (Crossing.inventory[0][0].equipment==3)
          {}//catch fish
          if (Crossing.inventory[0][0].equipment==4)
          {}//water
          if (Crossing.inventory[0][0].equipment==5)
          {}//axe
        }
        else
        {
          if (Crossing.grid[box.x/64+1][box.y/64+1] != null)
          {
            if (Crossing.grid[box.x/64+1][box.y/64+1].interact())
              break;
          }
          if (Crossing.grid[box.x/64][box.y/64+1] != null)
          {
            if (Crossing.grid[box.x/64][box.y/64+1].interact())
              break;
          }
          if (Crossing.grid[box.x/64+1][box.y/64] != null)
          {
            if (Crossing.grid[box.x/64+1][box.y/64].interact())
              break;
          }
          if (Crossing.grid[box.x/64][box.y/64] != null)
          {
            if (Crossing.grid[box.x/64][box.y/64].interact())
              break;
          }
          if (Crossing.inventory[0][0]==null)
            break;
          if (Crossing.inventory[0][0].equipment==0)
            break;
          if (Crossing.inventory[0][0].equipment==1) 
          {
            if (lastDirection==0)
            {
              if (Crossing.grid[box.x/64+1][box.y/64] instanceof Hole)
              {
                Crossing.grid[box.x/64+1][box.y/64]=null;
                break;
              }
              Hole temp = new Hole();
              temp.box.x=Math.round(box.x/64+1)*64;
              temp.box.y=Math.round(box.y/64)*64;
              if (worldCollision(temp.box, false))
              {
                if (Crossing.grid[box.x/64+1][box.y/64] == null || (Crossing.grid[box.x/64+1][box.y/64] instanceof Plants && Crossing.grid[box.x/64+1][box.y/64].state==0))
                {
                  Crossing.grid[box.x/64+1][box.y/64]=temp;
                  box.y=(int)(Math.round(box.y/64.0)*64);
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64].state==0)
                {
                  for (int f=0; f<6; f++)
                  {
                    for (int k=1; k<4; k++)
                    {
                      if (Crossing.inventory[f][k]==null)
                      {
                        Crossing.inventory[f][k]=Crossing.grid[box.x/64+1][box.y/64];
                        f=7;
                        k=4;
                      }
                      else if (f==5 && k==3)
                      {
                        System.out.println("Inventory is full!");
                      }
                    }
                  }
                  Crossing.grid[box.x/64+1][box.y/64]=temp;
                  box.y=(int)(Math.round(box.y/64.0)*64);
                  break;
                }
              }
              break;
            }
            if (lastDirection==1)
            {
              if (Crossing.grid[box.x/64+1][box.y/64+2] instanceof Hole)
              {
                Crossing.grid[box.x/64+1][box.y/64+2]=null;
                break;
              }
              Hole temp = new Hole();
              temp.box.x=Math.round(box.x/64+1)*64;
              temp.box.y=Math.round(box.y/64+2)*64;
              if (worldCollision(temp.box, false))
              {
                if (Crossing.grid[box.x/64+1][box.y/64+2] == null || (Crossing.grid[box.x/64+1][box.y/64+2] instanceof Plants && Crossing.grid[box.x/64+1][box.y/64+2].state==0))
                {
                  Crossing.grid[box.x/64+1][box.y/64+2]=temp;
                  box.y=(int)(Math.round(box.y/64.0)*64);
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64+2].state==0)
                {
                  for (int f=0; f<6; f++)
                  {
                    for (int k=1; k<4; k++)
                    {
                      if (Crossing.inventory[f][k]==null)
                      {
                        Crossing.inventory[f][k]=Crossing.grid[box.x/64+1][box.y/64+2];
                        f=7;
                        k=4;
                      }
                      else if (f==5 && k==3)
                      {
                        System.out.println("Inventory is full!");
                      }
                    }
                  }
                  Crossing.grid[box.x/64+1][box.y/64+2]=temp;
                  box.y=(int)(Math.round(box.y/64.0)*64);
                  break;
                }
              }
              break;
            }
            if (lastDirection==2)
            {
              if (Crossing.grid[box.x/64][box.y/64+1] instanceof Hole)
              {
                Crossing.grid[box.x/64][box.y/64+1]=null;
                break;
              }
              Hole temp = new Hole();
              temp.box.x=Math.round(box.x/64)*64;
              temp.box.y=Math.round(box.y/64+1)*64;
              if (worldCollision(temp.box, false))
              {
                if (Crossing.grid[box.x/64][box.y/64+1] == null || (Crossing.grid[box.x/64][box.y/64+1] instanceof Plants && Crossing.grid[box.x/64][box.y/64+1].state==0))
                {
                  Crossing.grid[box.x/64][box.y/64+1]=temp;
                  box.x=(int)(Math.round(box.x/64.0)*64);
                  break;
                }
                if (Crossing.grid[box.x/64][box.y/64+1].state==0)
                {
                  for (int f=0; f<6; f++)
                  {
                    for (int k=1; k<4; k++)
                    {
                      if (Crossing.inventory[f][k]==null)
                      {
                        Crossing.inventory[f][k]=Crossing.grid[box.x/64][box.y/64+1];
                        f=7;
                        k=4;
                      }
                      else if (f==5 && k==3)
                      {
                        System.out.println("Inventory is full!");
                      }
                    }
                  }
                  Crossing.grid[box.x/64][box.y/64+1]=temp;
                  box.x=(int)(Math.round(box.x/64.0)*64);
                  break;
                }
              }
              break;
            }
            if (Crossing.grid[box.x/64+2][box.y/64+1] instanceof Hole)
            {
              Crossing.grid[box.x/64+2][box.y/64+1]=null;
              break;
            }
            Hole temp = new Hole();
            temp.box.x=Math.round(box.x/64+2)*64;
            temp.box.y=Math.round(box.y/64+1)*64;
            if (worldCollision(temp.box, false))
            {
              if (Crossing.grid[box.x/64+2][box.y/64+1] == null || (Crossing.grid[box.x/64+2][box.y/64+1] instanceof Plants && Crossing.grid[box.x/64+2][box.y/64+1].state==0))
              {
                Crossing.grid[box.x/64+2][box.y/64+1]=temp;
                box.x=(int)(Math.round(box.x/64.0)*64);
                break;
              }
              if (Crossing.grid[box.x/64+2][box.y/64+1].state==0)
              {
                for (int f=0; f<6; f++)
                {
                  for (int k=1; k<4; k++)
                  {
                    if (Crossing.inventory[f][k]==null)
                    {
                      Crossing.inventory[f][k]=Crossing.grid[box.x/64+2][box.y/64+1];
                      f=7;
                      k=4;
                    }
                    else if (f==5 && k==3)
                    {
                      System.out.println("Inventory is full!");
                    }
                  }
                }
                Crossing.grid[box.x/64+2][box.y/64+1]=temp;
                box.x=(int)(Math.round(box.x/64.0)*64);
                break;
              }
            }
            break;
          }
          if (Crossing.inventory[0][0].equipment==2)
          {
            net();
            break;
          }//catch bug
          if (Crossing.inventory[0][0].equipment==3)
          {}//catch fish
          if (Crossing.inventory[0][0].equipment==4)
          {}//water
          if (Crossing.inventory[0][0].equipment==5)
          {}//axe
        }
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
          case 0:
            Crossing.inventory[selected1x][selected1y].state=1;
            menu=0;
            selected2=0;
            if (box.x%64<32)
            {
              if (box.y%64<32)
              {
                if (Crossing.grid[box.x/64][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                  Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                  Crossing.grid[box.x/64+1][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                System.out.println("No space to drop!");
                break;
              }
              else
              {
                if (Crossing.grid[box.x/64][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                  Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                System.out.println("No space to drop!");
                break;
              }
            }
            else if (box.y%64<32)
            {
              if (Crossing.grid[box.x/64+1][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              System.out.println("No space to drop!");
              break;
            }
            else
            {
              if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64+1][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              System.out.println("No space to drop!");
              break;
            }
            
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
          
          case 1:
            Crossing.inventory[selected1x][selected1y].state=1;
            menu=0;
            selected2=0;
            if (box.x%64<32)
            {
              if (box.y%64<32)
              {
                if (Crossing.grid[box.x/64][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                  Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                  Crossing.grid[box.x/64+1][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                System.out.println("No space to drop!");
                break;
              }
              else
              {
                if (Crossing.grid[box.x/64][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                  Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                System.out.println("No space to drop!");
                break;
              }
            }
            else if (box.y%64<32)
            {
              if (Crossing.grid[box.x/64+1][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              System.out.println("No space to drop!");
              break;
            }
            else
            {
              if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64+1][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              System.out.println("No space to drop!");
              break;
            }
            
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
            Crossing.inventory[selected1x][selected1y].state=0;
            Crossing.inventory[selected1x][selected1y].box.x=buryx*64;
            Crossing.inventory[selected1x][selected1y].box.y=buryy*64;
            Crossing.grid[buryx][buryy]=Crossing.inventory[selected1x][selected1y];
            Crossing.inventory[selected1x][selected1y]=null;
            buryx=0;
            menu=0;
            selected2=0;
            break;
            
          case 1:
            Crossing.inventory[selected1x][selected1y].state=1;
            menu=0;
            selected2=0;
            if (box.x%64<32)
            {
              if (box.y%64<32)
              {
                if (Crossing.grid[box.x/64][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                  Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                  Crossing.grid[box.x/64+1][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                System.out.println("No space to drop!");
                break;
              }
              else
              {
                if (Crossing.grid[box.x/64][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                  Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                System.out.println("No space to drop!");
                break;
              }
            }
            else if (box.y%64<32)
            {
              if (Crossing.grid[box.x/64+1][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              System.out.println("No space to drop!");
              break;
            }
            else
            {
              if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64+1][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              System.out.println("No space to drop!");
              break;
            }
            
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
            Crossing.inventory[selected1x][selected1y].state=0;
            Crossing.inventory[selected1x][selected1y].box.x=buryx*64;
            Crossing.inventory[selected1x][selected1y].box.y=buryy*64;
            Crossing.grid[buryx][buryy]=Crossing.inventory[selected1x][selected1y];
            Crossing.inventory[selected1x][selected1y]=null;
            buryx=0;
            menu=0;
            selected2=0;
            break;
            
          case 2:
            Crossing.inventory[selected1x][selected1y].state=1;
            menu=0;
            selected2=0;
            if (box.x%64<32)
            {
              if (box.y%64<32)
              {
                if (Crossing.grid[box.x/64][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                  Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                  Crossing.grid[box.x/64+1][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                System.out.println("No space to drop!");
                break;
              }
              else
              {
                if (Crossing.grid[box.x/64][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                  Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                if (Crossing.grid[box.x/64+1][box.y/64] == null)
                {
                  Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                  Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                  Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                  Crossing.inventory[selected1x][selected1y]=null;
                  break;
                }
                System.out.println("No space to drop!");
                break;
              }
            }
            else if (box.y%64<32)
            {
              if (Crossing.grid[box.x/64+1][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              System.out.println("No space to drop!");
              break;
            }
            else
            {
              if (Crossing.grid[box.x/64+1][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64+1] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64+1)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64+1] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64+1][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64+1)*64;
                Crossing.grid[box.x/64+1][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              if (Crossing.grid[box.x/64][box.y/64] == null)
              {
                Crossing.inventory[selected1x][selected1y].box.y=Math.round(box.y/64)*64;
                Crossing.inventory[selected1x][selected1y].box.x=Math.round(box.x/64)*64;
                Crossing.grid[box.x/64][box.y/64] = Crossing.inventory[selected1x][selected1y];
                Crossing.inventory[selected1x][selected1y]=null;
                break;
              }
              System.out.println("No space to drop!");
              break;
            }
            
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
  
  public void functionOne(int k, int l)
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
  
  public void net()
  {
    Rectangle temp;
    if (lastDirection==0)
      temp = new Rectangle(box.x, box.y-64, 64, 64);
    else if (lastDirection==1)
      temp = new Rectangle(box.x, box.y+64, 64, 64);
    else if (lastDirection==2)
      temp = new Rectangle(box.x-64, box.y, 64, 64);
    else
      temp = new Rectangle(box.x+64, box.y, 64, 64);
    loop: for (int a=-1; a<2; a++)
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
                  break loop;
                }
                else if (f==5 && s==3)
                {
                  System.out.println("Inventory is full!");
                }
              }
            }
          }
        }
      }
    }
  }
  public boolean worldCollision(Rectangle c, boolean playerMoving)
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
            {
              Crossing.bugs[c.x/64+a][c.y/64+b].get(d).steppedOn();
            }
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