/**
 * RectangleShape.java
 */

package draw.Model;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Класът правоъгълник е основен примитив, който е наследник на базовия Shape.
 */
public class RectangleShape extends draw.Model.Shape {
    
    public RectangleShape(Rectangle rect) {
        super(rect);
    }

    public RectangleShape(draw.Model.RectangleShape rectangle) {
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
        grfx.fillRect(r.x, r.y, r.width, r.height);
        grfx.setColor(Color.BLACK);
        grfx.drawRect(r.x, r.y, r.width, r.height);
        
        
       
        g.setTransform(old);
    }
}
