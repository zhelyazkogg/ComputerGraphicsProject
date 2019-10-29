package draw.Model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class NewShape extends draw.Model.Shape {

	 public NewShape(Rectangle rect) {
	        super(rect);
	    }

	    public NewShape(draw.Model.RectangleShape rectangle) {
	    }

	    /**
	     * Проверка за принадлежност на точка point към правоъгълника.
	     * В случая на правоъгълник този метод може да не бъде пренаписван, защото
	     * Реализацията съвпада с тази на абстрактния клас Shape, който проверява
	     * дали точката е в обхващащия правоъгълник на елемента (а той съвпада с
	     * елемента в този случай).
	     */
	    @Override
	    public boolean Contains(Point point) {
	        if ( super.Contains(point) ) {
	        	Rectangle r = getRectangle();
	            
	        	float a = r.width / 2;
	            float b = r.height / 2;
	            float x0 = r.getLocation().x + a;
	            float y0 = r.getLocation().y + b;
	            
	            boolean m = Math.pow((point.x - x0) / a, 2) + Math.pow((point.y - y0) / b, 2) - 1 <= 0;
	            
	            return m;
	        }
	        else {
	            return false;
	        }
	    }

	    /**
	     * Частта, визуализираща конкретния примитив.
	     */
	    @Override
	    public void DrawSelf(Graphics grfx) {
	        super.DrawSelf(grfx);        
	        Rectangle r = getRectangle();
	        Graphics2D g = (Graphics2D) grfx;
	        AffineTransform old = g.getTransform();
	        int x = getLocation().x;
	        int y = getLocation().y;
	        int width = getWidth();
	        int height = getHeight();
		    
	        g.rotate(Math.toRadians(getDegree()), x+width/2, y+height/2);
	       
	        
	        grfx.setColor(getFillColor());
	        g.fillRect(r.x, r.y, r.width, r.height);
	        g.setColor(Color.BLACK);
	        
		 	        
//	        g.drawRect(x, y, width, height);
	        g.drawLine(x+width/2, y+height/2, x+width/2, y);
	        g.drawLine(x+width/2, y+height/2, x+width/300, y+height);
	        g.drawLine(x+width, y+height, x+width/2, y+height/2);
	        grfx.drawRect(r.x, r.y, r.width, r.height);
	        g.setTransform(old);
	    }
	}
