import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;


public class BoutonTactile {
	
	private int x;
	private int y;
	private int largeur;
	private int hauteur;
	private String imageName;
	
	private Image image;
	
	
	public BoutonTactile(int x, int y, int largeur, int hauteur,
			String label, String imageFile) {
		super();
		this.x = x;
		this.y = y;
		this.largeur = largeur;
		this.hauteur = hauteur;
		this.imageName = imageFile;
		this.label = label;
		
		image = Toolkit.getDefaultToolkit().getImage(imageName) ;
	}

	
	public String getImageFile() {
		return imageName;
	}

	public void setImageFile(String imageFile) {
		this.imageName = imageFile;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	private String label;
	
	
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getLargeur() {
		return largeur;
	}

	public void setLargeur(int largeur) {
		this.largeur = largeur;
	}

	public int getHauteur() {
		return hauteur;
	}

	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}
	

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void draw(Graphics2D g)
	{
		g.drawRect(x, y,largeur,hauteur);
		
		g.drawImage(image, x, y, largeur, hauteur, null);
	}
	

}
