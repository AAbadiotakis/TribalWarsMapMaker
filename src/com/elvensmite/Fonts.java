package com.elvensmite;

import java.awt.*;

public class Fonts extends Frame
{
   public void paint(Graphics g)
   {
      setBackground(Color.black);
      setForeground(Color.white);
      Font tr = new Font("TimesRoman", Font.PLAIN, 18);
      Font trb = new Font("TimesRoman", Font.BOLD, 18);
      Font tri = new Font("TimesRoman", Font.ITALIC, 18);
      Font trbi = new Font("TimesRoman", Font.BOLD+Font.ITALIC, 18);
      Font h = new Font("Helvetica", Font.PLAIN, 18);
      Font c = new Font("Courier", Font.PLAIN, 18);
      Font d = new Font("Dialog", Font.PLAIN, 18);      
      Font z = new Font("ZapfDingbats", Font.PLAIN, 18);            

      g.setFont(tr);
      g.drawString("Hello World (times roman plain)",10,25);
      g.setFont(trb);
      g.drawString("Hello World (times roman bold)",10,50);
      g.setFont(tri);
      g.drawString("Hello World (times roman italic)",10,75);
      g.setFont(trbi);
      g.drawString("Hello World (times roman bold & italic)",10,100);
      g.setFont(h);
      g.drawString("Hello World (helvetica)",10,125);
      g.setFont(c);
      g.drawString("Hello World (courier)",10,150);
      g.setFont(d);
      g.drawString("Hello World (dialog)",10,175);
      g.setFont(z);
      g.drawString("Hello World (zapf dingbats)",10,200);
   }
   
   public static void main (String args[])
   {
      Frame ff = new Fonts();
      ff.resize(400,400);
      ff.show();
   }
}