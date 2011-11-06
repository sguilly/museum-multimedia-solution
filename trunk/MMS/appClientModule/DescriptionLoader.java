import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class DescriptionLoader {

	static void readFile(String filePath,
			ArrayList<TouchButton> listeDesBoutons) {
		Scanner scanner;

		try {
			FileReader f = new FileReader(filePath);
			scanner = new Scanner(f);

			// On boucle sur chaque champ detect�
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				Scanner scannerChamps = new Scanner(line);

				// L'expression r�guli�re qui d�limite les champs
				scannerChamps.useDelimiter(Pattern.compile("[\t\n]"));

				// On boucle sur chaque champ detect�

				while (scannerChamps.hasNext()) {

					listeDesBoutons.add(new TouchButton(scannerChamps
							.nextInt(), scannerChamps.nextInt(), scannerChamps
							.nextInt(), scannerChamps.nextInt(), scannerChamps
							.next(), scannerChamps.next()));

					// Utilisation du champ...
				}

				System.out.println(line);
				// faites ici votre traitement
			}

			scanner.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			System.out.println("Fichier non trouv�");
			e.printStackTrace();
		}

	}

}
