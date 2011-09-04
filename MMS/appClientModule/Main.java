import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;


public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("V1.0");
		
		GraphicsDevice[] gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		for (int i = 0; i < gd.length; i++) {
		    DisplayMode dm = gd[i].getDisplayMode();
		    System.out.println(dm.getWidth() + " x " + dm.getHeight());
		}
		
		
		
	}

	/* (non-Java-doc)
	 * @see java.lang.Object#Object()
	 */
	public Main() {
		super();
	}

}