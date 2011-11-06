import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.List;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Vector;

public class MMS extends Frame implements MouseListener {

	// Notre classe qui va nous permettre de gérer notre écran
	private ScreenManager sm = new ScreenManager();

	int widthScreen;
	int heightScreen;

	Image image;

	ArrayList<TouchButton> buttonArray = new ArrayList<TouchButton>();

	public void mouseClicked(MouseEvent me) {
		System.out.println("Click=" + me.getX() + "/" + me.getY());

		for (TouchButton bouton : buttonArray) {

			if ((bouton.getX() < me.getX())
					&& (me.getX() < (bouton.getX() + bouton.getLargeur()))
					&& (bouton.getY() < me.getY())
					&& (me.getY() < (bouton.getY() + bouton.getHauteur()))) {
				System.out.println(">" + bouton.getLabel());
				
				PlaySound soundReader = new PlaySound(
						"C:\\STEPHANE\\workspace\\bipbip.wav");
				soundReader.run();
				
			}
		}
		
		

	}

	public void mouseEntered(MouseEvent me) {
	}

	public void mousePressed(MouseEvent me) {
	}

	public void mouseReleased(MouseEvent me) {
	}

	public void mouseExited(MouseEvent me) {
	}



	public void testFullScreen(DisplayMode dm) {
		sm.setFullScreen(this, dm);

		widthScreen = Toolkit.getDefaultToolkit().getScreenSize().height;
		heightScreen = Toolkit.getDefaultToolkit().getScreenSize().width;

		DescriptionLoader.readFile("c:/temp/listeBouton.txt", buttonArray);

		addMouseListener(this);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// Do nothing
		} finally {
			sm.restoreFullScreen();
		}
	}
	
	public void testDualScreen()
	{
		GraphicsDevice[] gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		for (int i = 0; i < gd.length; i++) {
		    System.out.println("Ecran : "+gd[i].getConfigurations()[0].toString());
		    
		}
	
	}

	public void paint(Graphics gd) {

		Graphics2D g = (Graphics2D) gd;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawRect(5, 5, heightScreen - 10, widthScreen - 10);

		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("V1.0", 50, 50);

		for (TouchButton bouton : buttonArray) {

			bouton.draw(g);
		}

	}
	
	// La fonction principale
	public static void main(String[] args) {
		DisplayMode dm;

		// Construction d'une résolution d'écran
		if (args.length == 3) {

			System.out.println("Résolution par argument");

			dm = new DisplayMode(Integer.parseInt(args[0]),
					Integer.parseInt(args[1]), Integer.parseInt(args[2]),
					DisplayMode.REFRESH_RATE_UNKNOWN);
		} else {
			// Résolution par défaut, la plus courante

			System.out.println("Résolution par defaut");

			dm = new DisplayMode(1366, 768, 16,
					DisplayMode.REFRESH_RATE_UNKNOWN);
		}
		
		
		MMS screen = new MMS();
		
		screen.testDualScreen();
		// Test le plein écran !
		screen.testFullScreen(dm);
		
		
		

	}
}
