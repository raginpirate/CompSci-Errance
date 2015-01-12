public class test
{
  public static void main(String[] args)
  {
    String s="0970,68,41";
    String xcoords=s.substring(s.indexOf(',')+1);
    int ycoords=Integer.parseInt(xcoords.substring(xcoords.indexOf(',')+1));
    System.out.println(Integer.parseInt(xcoords.substring(0,xcoords.indexOf(','))));
    System.out.println(ycoords);
    System.out.println(s.substring(2, s.indexOf(',')));
  }
}