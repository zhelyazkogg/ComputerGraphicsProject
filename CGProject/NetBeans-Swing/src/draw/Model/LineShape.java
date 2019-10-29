package draw.Model;

import java.awt.*;
import java.awt.geom.AffineTransform;

import javafx.scene.shape.Line;

public class LineShape extends draw.Model.Shape {

	 public LineShape(Rectangle rectangle) {
	     super(rectangle);
	 }
	 
	 public LineShape(draw.Model.RectangleShape rectangle){		 
	 }

	@Override
	 public boolean Contains(Point point){
		 if (super.Contains(point)){
			 float a = getWidth() / 2;
	            float b = getHeight() / 2;
	            float x0 = getLocation().x + a;
	            float y0 = getLocation().y + b;
			
			boolean l = Math.pow((point.x - x0) / a, 2) + Math.pow((point.y - y0) / b, 2) - 1 <= 0;
            
            return l;
		 } else {
		return false;
	 }
		 }
	 
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
	        grfx.setColor(Color.BLACK);
	        grfx.drawLine(r.x, r.y, r.x+r.width, r.y+r.height);
	       
	        
	        
	        g.setTransform(old);
	    }
	
}
