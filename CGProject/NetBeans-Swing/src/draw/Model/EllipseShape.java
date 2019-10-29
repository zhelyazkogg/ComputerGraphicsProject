package draw.Model;

import java.awt.*;
import java.awt.geom.AffineTransform;

import javafx.scene.shape.Ellipse;

public class EllipseShape extends draw.Model.Shape {
    
    public EllipseShape(Rectangle rectangle) {
        super(rectangle);
    }

    public EllipseShape(draw.Model.RectangleShape rectangle) {
    }
    
    @Override
    public boolean Contains(Point point) {
        if ( super.Contains(point) ) {
            
        	float a = getWidth() / 2;
            float b = getHeight() / 2;
            float x0 = getLocation().x + a;
            float y0 = getLocation().y + b;
            
            boolean k = Math.pow((point.x - x0) / a, 2) + Math.pow((point.y - y0) / b, 2) - 1 <= 0;
            
            return k;
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
        grfx.fillOval(r.x, r.y, r.width, r.height);
        grfx.setColor(Color.BLACK);
        grfx.drawOval(r.x, r.y, r.width, r.height);
        g.setTransform(old);
        
    }
}

