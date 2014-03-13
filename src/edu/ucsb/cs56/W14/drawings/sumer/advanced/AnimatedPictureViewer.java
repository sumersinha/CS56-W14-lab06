package edu.ucsb.cs56.w14.drawings.sumer.advanced;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.lang.Math;

public class AnimatedPictureViewer {

    private DrawPanel panel = new DrawPanel();
    
    private Ellipse2D.Double circle1 = new Ellipse2D.Double(100, 100, 10, 10);
    
    Thread anim;   
    
    private double x1 = 50;
    private double y1 = 50;
    
    private double u1 = 1;
    private double v1 = 3;

    private double x2 = 150;
    private double y2 = 100;
    
    private double u2 = 1;
    private double v2 = 2;

    private int sh = 100;
    private int sw = 100;

    

    public static void main (String[] args) {
      new AnimatedPictureViewer().go();
    }

    public void go() {
      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      frame.getContentPane().add(panel);
      frame.setSize(640,480);
      frame.setVisible(true);
      
      frame.getContentPane().addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent e){
        System.out.println("mouse entered");
          anim = new Animation();
          anim.start();
        }

        public void mouseExited(MouseEvent e){        
          System.out.println("Mouse exited");
          // Kill the animation thread
          anim.interrupt();
          while (anim.isAlive()){}
          anim = null;         
          panel.repaint();        
        }
      });
      
    } // go()

    class DrawPanel extends JPanel {
       public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

         // Clear the panel first
          g2.setColor(Color.white);
          g2.fillRect(0,0,this.getWidth(), this.getHeight());

          // Draw the animation
          //g2.setColor(Color.RED);
	  Ellipse2D.Double test = new Ellipse2D.Double(x1, y1, 30, 30);
	  //g2.setColor(Color.BLUE);
	  Ellipse2D.Double test2 = new Ellipse2D.Double(x2, y2, 30, 30);
	  g2.setColor(Color.BLACK);
	  StereoWithButtons z = new StereoWithButtons(100,100,sw,sh);
	  g2.draw(z);
	  g2.setColor(Color.RED);
          g2.draw(test);
	  g2.fill(test);
	  g2.setColor(Color.BLUE);
	  g2.draw(test2);
	  g2.fill(test2);
	  g2.setColor(Color.BLACK);
	   g2.drawString("Animation by Sumer", 20,10);
	  g2.drawString("Stereo gets bigger everytime the balls collide.", 20,20);
       }
    }
    
    class Animation extends Thread {
      public void run() {
        try {
          while (true) {
            // Bounce off the walls
	      double A;
	      double v1n;
	      double v1t;
	      double v2n;
	      double v2t;
	      double temp;
    
	      if (Math.sqrt(((x1 - x2) * (x1 - x2))+((y1 -y2)*(y1 - y2))) < 2*15){
		  sh = sh + 10;
		  sw = sw + 10;
		  A = Math.atan((y1-y2)/(x1-x2));
		  if(A>0){
		      v1n = Math.cos(A)*u1 + Math.sin(A)*v1;
		      v1t = -1*Math.sin(A)*u1 + Math.cos(A)*v1;
		      v2n = Math.cos(A)*u2 + Math.sin(A)*v2;
		      v2t = -1*Math.sin(A)*u2 + Math.cos(A)*v2;
		  }
            
		  else{
		      v1n = Math.cos(A)*u1 - Math.sin(A)*v1;
		      v1t = Math.sin(A)*u1 + Math.cos(A)*v1;
		      v2n = Math.cos(A)*u2 - Math.sin(A)*v2;
		      v2t = Math.sin(A)*u2 + Math.cos(A)*v2;
		  }
            

		  temp = v1n;
		  v1n = v2n;
		  v2n = temp;
        
       
		  if(A>0){
		      u1 = Math.cos(A)*v1n - Math.sin(A)*v1t;
		      v1 = Math.sin(A)*v1n + Math.cos(A)*v1t;
		      u2 = Math.cos(A)*v2n - Math.sin(A)*v2t;
		      v2 = Math.sin(A)*v2n + Math.cos(A)*v2t;
		  }

		  else{
		      u1 = Math.cos(A)*v1n + Math.sin(A)*v1t;
		      v1 = -1*Math.sin(A)*v1n + Math.cos(A)*v1t;
		      u2 = Math.cos(A)*v2n + Math.sin(A)*v2t;
		      v2 = -1*Math.sin(A)*v2n + Math.cos(A)*v2t;
		  }
            
	      }
        
         
	      if(x1>200 || x1<0){ u1 = -1*u1; }
	      if(y1>200 || y1<0){ v1 = -1*v1; }
	      x1 = x1 + u1;
	      y1 = y1 + v1;

	      if(x2>200 || x2<0){ u2 = -1*u2; }
	      if(y2>200 || y2<0){ v2 = -1*v2; }
	      x2 = x2 + u2;
	      y2 = v2 + v2;
	      
	      
	      panel.repaint();
	      Thread.sleep(10);
          }
        } catch(Exception ex) {
          if (ex instanceof InterruptedException) {
            // Do nothing - expected on mouseExited
          } else {
            ex.printStackTrace();
            System.exit(1);
          }
        }
      }
    }
    
}



