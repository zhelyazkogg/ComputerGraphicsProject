/**
 * DisplayProcessor.java
 */

package draw.Processors;

import java.awt.*;
import java.util.*;
import draw.GUI.DrawApp;

/**
 * Класът, който ще бъде използван при управляване на дисплейната система.
 */
public class DisplayProcessor {
    /**
     * Списък с всички елементи формиращи изображението.
     */
    public Vector<draw.Model.Shape> shapeList = new Vector<draw.Model.Shape>();

    public DisplayProcessor() {
    }

    /**
     * Прерисува всички елементи в shapeList върху grfx
     */
    public void ReDraw(Object sender, Graphics grfx) {
        Graphics2D grfx2 = (Graphics2D)grfx;
        grfx2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        
        Draw(grfx);
    }

    /**
     * Визуализация.
     * Обхождане на всички елементи в списъка и извикване на визуализиращия им метод.
     * @param grfx - Къде да се извърши визуализацията.
     */
    public void Draw(Graphics grfx) {
        for (int i = 0; i < shapeList.size(); i++) {
            DrawShape(grfx, shapeList.get(i));
        }
    }

    /**
     * Визуализира даден елемент от изображението.
     * @param grfx - Къде да се извърши визуализацията.
     * @param item - Елемент за визуализиране.
     */
    public void DrawShape(Graphics grfx, draw.Model.Shape item) {
        item.DrawSelf(grfx);
    }

    //
    
    public Vector<draw.Model.Shape> getShapeList() {
        return shapeList;
    }

    public void setShapeList(Vector<draw.Model.Shape> value) {
        shapeList = value;
    }

    public void repaint() {
        DrawApp.getApplication().getMainView().getComponent().repaint();
    }
}
