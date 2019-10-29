package draw.Model;

import java.awt.*;
import java.util.ArrayList;

public class GroupShape extends draw.Model.Shape {
	public GroupShape(Rectangle rectangle) {
        super(rectangle);
    }

    public GroupShape(draw.Model.RectangleShape rectangle) {
    }
    
    public ArrayList<draw.Model.Shape> SubItems = new ArrayList<draw.Model.Shape>();
    
    
    @Override
    public boolean Contains(Point point) {
     
     if ( super.Contains(point) ) {
     	for (draw.Model.Shape item : SubItems) {
             if (item.Contains(point)) return true;
         }
         return false;
     }
     else {
         return false;
     }
         
    }
    
    @Override
    public void setLocation(Point value) {
    	int dx = value.x - getLocation().x;
        int dy = value.y - getLocation().y;
        
        super.setLocation(value);
       
        for (Shape item : SubItems) {
            item.setLocation(new Point(item.getLocation().x + dx, item.getLocation().y + dy));
        }
    }
    
    @Override
    public void setFillColor(java.awt.Color value) {
        super.setFillColor(value);
        for (draw.Model.Shape shape : SubItems){
    		shape.setFillColor(value);
    	}
    }

    /**
     * Частта, визуализираща конкретния примитив.
     */
    @Override
    public void DrawSelf(Graphics grfx) {
        super.DrawSelf(grfx);
        
        for (Shape shape : SubItems) {
        	shape.DrawSelf(grfx);
		}
    }
    
    @Override
    public void setDegree(int degree) {
    	super.setDegree(degree);
    	for(draw.Model.Shape item : SubItems) {
    		item.setDegree(degree);
    	}
    }
}
