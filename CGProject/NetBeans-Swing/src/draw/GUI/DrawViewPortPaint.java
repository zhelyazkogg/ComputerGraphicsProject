/**
 * DrawViewPortPaint.java
 */

package draw.GUI;

import java.awt.*;
import javax.swing.*;

/**
 * Помощен компоненти за извършване на визуализацията в неговия
 * paintComponent метод.
 */
public class DrawViewPortPaint extends JPanel {   
    
    /**
     * Използва се за достъп до диалоговия процесор и др.
     */
    private DrawView parent;
            
    public DrawViewPortPaint(DrawView view) {
        super();
        setLayout(new BorderLayout());
        
        parent = view;
    }

    @Override public Dimension getPreferredSize() {
        Dimension layoutSize = super.getPreferredSize();
        int max = Math.max(layoutSize.width,layoutSize.height);
        return new Dimension(max+100,max+100);
    }

    /**
     * Пренаписваме метода за да извършим визуализация при необходимост.
     * @param g - къде да се визуализира сцената.
     */
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);    
        parent.getDialogProcessor().ReDraw(this, g);
    }
}
