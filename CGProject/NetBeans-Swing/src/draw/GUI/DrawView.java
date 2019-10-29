/*
 * DrawView.java
 */

package draw.GUI;

import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;

import com.sun.javafx.geom.Shape;

import org.jdesktop.application.ResourceMap;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import draw.Processors.DialogProcessor;

/**
 * The application's main frame.
 */
public class DrawView extends FrameView {
   
    /**
     * Агрегирания диалогов процесор. Улеснява манипулацията на модела.
     */
	
	public int degree;
    private DialogProcessor dialogProcessor;

    /**
     * Достъп до доалоговия процесор.
     * @return Инстанцията на диалоговия процесор
     */
    public DialogProcessor getDialogProcessor() {
        return dialogProcessor;
    }
 
    public DrawView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // Създава се инстанция от класа на диалоговия процесор.
        dialogProcessor = new DialogProcessor();
        
        // Създаваме поле за рисуване и го правим главен компонент в изгледа.
        DrawViewPortPaint drawViewPortPaint = new draw.GUI.DrawViewPortPaint(this);
        setComponent(drawViewPortPaint);
        // Прихващане на събитията на мишката.
        drawViewPortPaint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
            	if (DragToggleButton.isSelected()) {
            		draw.Model.Shape sel = dialogProcessor.ContainsPoint(evt.getPoint());
            		if(sel == null) return;  
            			if(dialogProcessor.getSelection().contains(sel)) {
		                           dialogProcessor.getSelection().remove(sel);
            			}else {
		                    	dialogProcessor.getSelection().add(sel);
            				}	
            			
            statusMessageLabel.setText("Последно действие: Селекция на примитив");
            dialogProcessor.setLastLocation(evt.getPoint());
            dialogProcessor.repaint();
             
            	}
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                //dialogProcessor.setIsDragging(false);
            }
        });
        drawViewPortPaint.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                //if (dialogProcessor.getIsDragging()) {
                    if (dialogProcessor.getSelection() != null) statusMessageLabel.setText("Последно действие: Влачене");
                    dialogProcessor.TranslateTo(evt.getPoint());
                    dialogProcessor.repaint();
                //}
            }
        });
        
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);
        
        // Икона на главния прозорец
        ImageIcon icon = resourceMap.getImageIcon("DrawIcon");
        getFrame().setIconImage(icon.getImage()); 

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    /**
     * Показва диалогова кутия с информация за програмата.
     */
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = DrawApp.getApplication().getMainFrame();
            aboutBox = new DrawAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        DrawApp.getApplication().show(aboutBox);
    }

    /**
     * Бутон, който поставя на произволно място правоъгълник със зададените размери.
     * Променя се лентата със състоянието и се инвалидира контрола, в който визуализираме.
     */
    @Action
    public void drawRectangle() {
        dialogProcessor.AddRandomRectangle();
        statusMessageLabel.setText("Последно действие: Рисуване на правоъгълник");
        dialogProcessor.repaint();
    }
    
    @Action
    public void drawEllipse(){
    	dialogProcessor.AddRandomEllipse();
    	statusMessageLabel.setText("Последно действие: Рисуване на елипса");
    	dialogProcessor.repaint();
    }
    
    @Action
    public void drawLine(){
    	dialogProcessor.AddRandomLine();
    	statusMessageLabel.setText("Последно действие: Рисуване на права линия");
    	dialogProcessor.repaint();
    }
    
    @Action
    public void setFillColor(){	
    	Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.WHITE);
        dialogProcessor.setFillColor(newColor);
    	statusMessageLabel.setText("Последно действие: Оцветяване на рамката");
    	dialogProcessor.repaint();
    }
    
    @Action
    public void groupShape(){	
        dialogProcessor.GroupSelection();
    	statusMessageLabel.setText("Последно действие: Групиране");
    	dialogProcessor.repaint();
    }
    
    @Action
    public void rotateLeft() {
    	degree = dialogProcessor.degrees();
    	degree -= 15;
    	dialogProcessor.rotate(degree);
    	statusMessageLabel.setText("Последно действие: Въртене наляво");
    	dialogProcessor.repaint();
    }
    
    @Action
    public void rotateRight() {
    	degree = dialogProcessor.degrees();
    	degree += 15;
    	dialogProcessor.rotate(degree);
    	statusMessageLabel.setText("Последно действие: Въртене наляво");
    	dialogProcessor.repaint();
    }
    
    @Action
    public void deleteShape() {
    	dialogProcessor.deleteShape();
    	statusMessageLabel.setText("Последно действие: Изтриване");
    	dialogProcessor.repaint();
    }
    
    @Action
    public void NewShape() {
    	dialogProcessor.AddRandomShape();
    	statusMessageLabel.setText("Последно действие: Нова картинка");
    	dialogProcessor.repaint();
    }
    
    /** 
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem delete = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        imageMenu = new javax.swing.JMenu();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        toolBar = new javax.swing.JToolBar();
        DrawRectangleButton = new javax.swing.JButton();
        DrawEllipseButton = new javax.swing.JButton();
        DrawLineButton = new javax.swing.JButton();
        LeftRotateButton = new javax.swing.JButton();
        RightRotateButton = new javax.swing.JButton();
        DragToggleButton = new javax.swing.JToggleButton();
        ColorChooserButton = new javax.swing.JButton();
        GroupButton = new javax.swing.JButton();
        NewShapeButton = new javax.swing.JButton();
        viewPort = new javax.swing.JPanel();

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(draw.GUI.DrawApp.class).getContext().getResourceMap(DrawView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(draw.GUI.DrawApp.class).getContext().getActionMap(DrawView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText(resourceMap.getString("editMenu.text")); // NOI18N
        editMenu.setName("editMenu"); // NOI18N
        menuBar.add(editMenu);
       
        delete.setAction(actionMap.get("deleteShape"));
        delete.setText(resourceMap.getString("delete.text"));
        editMenu.add(delete);
        
        imageMenu.setText(resourceMap.getString("imageMenu.text")); // NOI18N
        imageMenu.setName("imageMenu"); // NOI18N
        menuBar.add(imageMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 421, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        toolBar.setRollover(true);
        toolBar.setName("toolBar"); // NOI18N

        DrawRectangleButton.setAction(actionMap.get("drawRectangle")); // NOI18N
        DrawRectangleButton.setIcon(resourceMap.getIcon("DrawRectangleButton.icon")); // NOI18N
        DrawRectangleButton.setText(resourceMap.getString("DrawRectangleButton.text")); // NOI18N
        DrawRectangleButton.setFocusable(false);
        DrawRectangleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DrawRectangleButton.setName("DrawRectangleButton"); // NOI18N
        DrawRectangleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(DrawRectangleButton);
        
        NewShapeButton.setAction(actionMap.get("NewShape")); // NOI18N
        NewShapeButton.setIcon(resourceMap.getIcon("DrawRectangleButton.icon")); // NOI18N
        NewShapeButton.setText(resourceMap.getString("DrawRectangleButton.text")); // NOI18N
        NewShapeButton.setFocusable(false);
        NewShapeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NewShapeButton.setName("NewShapeButton"); // NOI18N
        NewShapeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(NewShapeButton);
        
        
        DrawEllipseButton.setAction(actionMap.get("drawEllipse")); // NOI18N
        DrawEllipseButton.setIcon(resourceMap.getIcon("DrawEllipseButton.icon")); // NOI18N
        DrawEllipseButton.setText(resourceMap.getString("DrawRectangleButton.text")); // NOI18N
        DrawEllipseButton.setFocusable(false);
        DrawEllipseButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DrawEllipseButton.setName("DrawEllipseButton"); // NOI18N
        DrawEllipseButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(DrawEllipseButton);
        
        
        DrawLineButton.setAction(actionMap.get("drawLine"));
        DrawLineButton.setIcon(resourceMap.getIcon("DrawLineButton.icon"));
        DrawLineButton.setText(resourceMap.getString("DrawRectangleButton.text"));
        DrawLineButton.setFocusable(false);
        DrawLineButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DrawLineButton.setName("DrawLineButton");
        DrawLineButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(DrawLineButton);

        DragToggleButton.setIcon(resourceMap.getIcon("DragToggleButton.icon")); // NOI18N
        DragToggleButton.setText(resourceMap.getString("DragToggleButton.text")); // NOI18N
        DragToggleButton.setFocusable(false);
        DragToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DragToggleButton.setName("DragToggleButton"); // NOI18N
        DragToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(DragToggleButton);
        
        ColorChooserButton.setAction(actionMap.get("setFillColor"));
        ColorChooserButton.setIcon(resourceMap.getIcon("ColorChooserButton.icon")); // NOI18N
        ColorChooserButton.setText(resourceMap.getString("DragToggleButton.text")); // NOI18N
        ColorChooserButton.setFocusable(false);
        ColorChooserButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ColorChooserButton.setName("ColorChooserButton"); // NOI18N
        ColorChooserButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(ColorChooserButton);
        
        GroupButton.setAction(actionMap.get("groupShape")); // NOI18N
        GroupButton.setIcon(resourceMap.getIcon("GroupShapesButton.icon")); // NOI18N
        GroupButton.setText(resourceMap.getString("DrawRectangleButton.text")); // NOI18N
        GroupButton.setFocusable(false);
        GroupButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        GroupButton.setName("GroupButton"); // NOI18N
        GroupButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(GroupButton);
        
        LeftRotateButton.setAction(actionMap.get("rotateLeft")); // NOI18N
        LeftRotateButton.setIcon(resourceMap.getIcon("RotateLeftButton.icon")); // NOI18N
        LeftRotateButton.setText(resourceMap.getString("DrawRectangleButton.text")); // NOI18N
        LeftRotateButton.setFocusable(false);
        LeftRotateButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        LeftRotateButton.setName("LeftRotateButton"); // NOI18N
        LeftRotateButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(LeftRotateButton);

        
        RightRotateButton.setAction(actionMap.get("rotateRight")); // NOI18N
        RightRotateButton.setIcon(resourceMap.getIcon("RotateRightButton.icon")); // NOI18N
        RightRotateButton.setText(resourceMap.getString("DrawRectangleButton.text")); // NOI18N
        RightRotateButton.setFocusable(false);
        RightRotateButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RightRotateButton.setName("RightRotateButton"); // NOI18N
        RightRotateButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(RightRotateButton);
        
        viewPort.setName("viewPort"); // NOI18N

        javax.swing.GroupLayout viewPortLayout = new javax.swing.GroupLayout(viewPort);
        viewPort.setLayout(viewPortLayout);
        viewPortLayout.setHorizontalGroup(
            viewPortLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 591, Short.MAX_VALUE)
        );
        viewPortLayout.setVerticalGroup(
            viewPortLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 399, Short.MAX_VALUE)
        );

        setComponent(viewPort);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
        setToolBar(toolBar);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton DragToggleButton;
    private javax.swing.JButton DrawRectangleButton;
    private javax.swing.JButton DrawEllipseButton;
    private javax.swing.JButton DrawLineButton;
    private javax.swing.JButton ColorChooserButton;
    private javax.swing.JButton GroupButton;
    private javax.swing.JButton LeftRotateButton;
    private javax.swing.JButton RightRotateButton;
    private javax.swing.JButton NewShapeButton;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu imageMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuBar delete;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JPanel viewPort;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
}
