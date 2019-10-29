/*
 * DrawApp.java
 */

package draw.GUI;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * Главен клас на програмата с входната й точка.
 */
public class DrawApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */	
    @Override protected void startup() {
        show(new DrawView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of DrawApp
     */
    public static DrawApp getApplication() {
        return Application.getInstance(DrawApp.class);
    }

    /**
     * Входна точка на програмата.
     */
    public static void main(String[] args) {
        launch(DrawApp.class, args);
    }
}
