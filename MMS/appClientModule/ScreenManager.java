import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

/**
 * Cette classe est un gestionnaire d'écran et permet de changer la résolution
 * de l'écran ou de mettre le mode plein-écran.
 * 
 * @author Julien CHABLE (webmaster@neogamedev.com)
 * @version 1.0 20/06/2004
 */
public class ScreenManager {

    // Notre device (abstraction de la carte graphique ...) graphique sur lequel
    // nous allons effectuer les changements de résolution, le plein-écran, ...
    private GraphicsDevice gd;

    /**
     * Constructeur par défaut. Initialise la variable du GraphicsDevice.
     *  
     */
    public ScreenManager() {
        // On récupère l'environnement graphique du système d'exploitation,
        // celui-ci va nous permettre de récupérer un certain nombre d'éléments
        // sur la configuration graphique du système ...
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        // On récupère le device par défaut ... vous pouvez récupérer l'ensemble
        // des devices du système avec la méthode getScreenDevices(), le centre
        // de l'écran avec getCenterPoint(), ...
        gd = ge.getDefaultScreenDevice();
    }

    /**
     * Passage de la fenêtre en mode plein écran. Changement de mode graphique (résolution et profondeur). Regarder la
     * classe DisplayMode pour plus d'informations sur les résolutions d'écran.
     * 
     * @param window
     *            La fenêtre à passer en plein-écran.
     * @param displayMode
     *            Le mode graphique à appliquer.
     */
    public void setFullScreen(Frame window, DisplayMode displayMode) {
        if (window == null) return;
        // On ne veut pas des bords de la fenêtre
        window.setUndecorated(true);
        // La fenêtre n'est pas redimensionnable
        window.setResizable(false);
        // Passage en mode plein-écran
        if (gd.isFullScreenSupported()) gd.setFullScreenWindow(window);
        // Changement de résolution
        if (displayMode != null && gd.isDisplayChangeSupported()) {
            try {
                // Affectation du mode graphique au device graphique.
                gd.setDisplayMode(displayMode);
            } catch (Exception e) {
                // Do nothing
            }
        }
    }

    /**
     * Obtenir la fenêtre en plein-écran.
     */
    public Window getFullScreen() {
        return gd.getFullScreenWindow();
    }
    
    /**
     * Obtenir le GraphicsDevice par défaut de la fenêtre.
     */
    /*public GraphicsDevice getGraphicsDevice(){
        return gd;
    }*/

    /**
     * Arrête le mode plein-écran pour revenir en mode fenêtré ...
     */
    public void restoreFullScreen() {
        // On récupère notre fenêtre en mode plein-écran
        Window window = getFullScreen();
        //
        if (window != null) window.dispose();
        // On arrête le plein-écran.
        gd.setFullScreenWindow(null);
    }
}


 
