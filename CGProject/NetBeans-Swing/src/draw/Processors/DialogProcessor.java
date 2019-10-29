/**
 * DialogProcessor.java
 */

package draw.Processors;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;

import draw.Model.GroupShape;
import draw.Model.Shape;
import javafx.scene.shape.Ellipse;

/**
 * Класът, който ще бъде използван при управляване на диалога.
 */
public class DialogProcessor extends DisplayProcessor {
    /**
     * Избран елемент.
     */
	private ArrayList<draw.Model.Shape> selection = new ArrayList<draw.Model.Shape>();
    
    // Не е необходим в реализацията на Java
    ///**
    // * Дали в момента диалога е в състояние на "влачене" на избрания елемент.
    // */
    //private boolean isDragging;
    
    /**
     * Последна позиция на мишката при "влачене".
     * Използва се за определяне на вектора на транслация.
     */
    private Point lastLocation;

    public DialogProcessor() {
    }

    /**
     * Добавя примитив - правоъгълник на произволно място върху клиентската област.
     */
    public void AddRandomRectangle() {
        int x = 100 + (int)Math.round(Math.random()*900);
        int y = 100 + (int)Math.round(Math.random()*500);
        draw.Model.RectangleShape rect = new draw.Model.RectangleShape(new Rectangle(x,y,100,200));
        rect.setFillColor(java.awt.Color.WHITE);
        shapeList.add(rect);
    }
    
    public void AddRandomEllipse(){
    	int x = 100 + (int)Math.round(Math.random()*900);
    	int y = 100 + (int)Math.round(Math.random()*500);
    	draw.Model.EllipseShape elli = new draw.Model.EllipseShape(new Rectangle(x, y, 100, 200));
    	elli.setFillColor(java.awt.Color.WHITE);
    	shapeList.add(elli);
    	}
    
    public void AddRandomLine(){
    	int x = 100 + (int)Math.round(Math.random()*900.0);
    	int y = 100 + (int)Math.round(Math.random()*900.0);
    	draw.Model.LineShape line = new draw.Model.LineShape(new Rectangle(x, y, 100, 200));
    	line.setFillColor(java.awt.Color.WHITE);
    	shapeList.add(line);
    }
    
    public void AddRandomShape() {
        int x = 100 + (int)Math.round(Math.random()*500);
        int y = 100 + (int)Math.round(Math.random()*900);
        draw.Model.NewShape newshape = new draw.Model.NewShape(new Rectangle(x, y, 200, 100));
        newshape.setFillColor(java.awt.Color.WHITE);
        shapeList.add(newshape);
    }
    
    public void deleteShape() {
    	for (Shape shape : selection) {
    		shapeList.remove(shape);
    		}
    	selection.clear();
		}     	
   
    /*
    * 
    * 
    * Selection All()
    * 
    * 
    * 
    * */

    /**
     * Проверява дали дадена точка е в елемента.
     * Обхожда в ред обратен на визуализацията с цел намиране на
     * "най-горния" елемент т.е. този който виждаме под мишката.
     * @param point - Указана точка.
     * @return Елемента на изображението, на който принадлежи дадената точка.
     */
    public draw.Model.Shape ContainsPoint(Point point) {
        for (int i = shapeList.size() - 1; i >= 0; i--) {
            if (shapeList.get(i).Contains(point)) {
              //  shapeList.get(i).setFillColor(Color.RED);
                return shapeList.get(i);
            }
        }
        return null;
    }
    

    /**
     * Транслация на избраният елемент на вектор определен от <paramref name="p>p< paramref>
     * @param p - Вектор на транслация.
     */
    public void TranslateTo(Point p) {
    	for (draw.Model.Shape shape : selection) {
            shape.setLocation(new Point(shape.getLocation().x + p.x - lastLocation.x, shape.getLocation().y + p.y - lastLocation.y));
        }
    	lastLocation = p;
    }

    //
    
    public ArrayList<Shape> getSelection() {
        return selection;
    }

    public void setSelection(ArrayList<Shape> value) {
        selection = value;
    }

//    public boolean getIsDragging() {
//       return isDragging;
//    }
//
//    public void setIsDragging(boolean value) {
//       isDragging = value;
//    }

    public Point getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Point value) {
        lastLocation = value;
    }

	public void setFillColor(Color newColor) {
		for (draw.Model.Shape shape : selection) {
			shape.setFillColor(newColor);
		}	
	}
	
	@Override
	public void Draw(Graphics grfx) {
        
       super.Draw(grfx);
       Graphics2D g = (Graphics2D) grfx;
       AffineTransform old = g.getTransform();
       for (draw.Model.Shape shape : selection) {
    	   g.rotate(Math.toRadians(shape.getDegree()), 

    			   shape.getLocation().x + shape.getWidth()/2, 
    			   shape.getLocation().y + shape.getHeight()/2);
			grfx.setColor(Color.BLACK);
			grfx.drawRect(shape.getLocation().x - 3, shape.getLocation().y - 3, shape.getWidth() + 6, shape.getHeight() + 6);
			
			g.setTransform(old);
		}
    }
	
	public void GroupSelection(){
		
		if(selection.size() < 2) return;	
		
			int minX = Integer.MAX_VALUE;
			int minY = Integer.MAX_VALUE;
			int maxX = Integer.MIN_VALUE;
			int maxY = Integer.MIN_VALUE;
			
			for (draw.Model.Shape shape : selection)
			{
				if(minX > shape.getLocation().x) minX = shape.getLocation().x;
				if(minY > shape.getLocation().y) minY = shape.getLocation().y;
				if(maxX < shape.getLocation().x + shape.getWidth()) maxX = shape.getLocation().x + shape.getWidth();
				if(maxY < shape.getLocation().y + shape.getHeight()) maxY = shape.getLocation().y + shape.getHeight();
			}
			
			GroupShape group = new GroupShape(new Rectangle(minX, minY, maxX - minX, maxY - minY));
			group.SubItems = selection;
			
			selection = new ArrayList<Shape>();
			selection.add(group);
			
			 for (draw.Model.Shape shape : selection){
				 shapeList.remove(shape);
			 }
			  
			 shapeList.add(group);
	}
	
	public void rotate(int degree) {
		for(draw.Model.Shape item : selection) {
			item.setDegree(degree);
		}
	}
	
	public int degrees() {
		int degrees = 0;
		
		for(draw.Model.Shape item : selection) {
			degrees = item.getDegree();
		}
		
		return degrees;
	}
    
}
