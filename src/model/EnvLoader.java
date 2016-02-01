package model;

import java.io.*;

/**
 * 
 * Load and generates environments
 *
 */
public class EnvLoader<E extends Number>{
	private String path;
	
	public EnvLoader(String path){
		this.path = path;
	}
	
	/**
	 * Cette méthode permet de charger un environnement de n'importe quel type.
	 * Pour se faire, il faut construire un objet avec le type paramétré et appeler 
	 * cette fonction auquelle on passe le chemin du fichier à charger.
	 * @return
	 * @throws IOException 
	 * @throws MultiplePlace 
	 * @throws UnknownPlace 
	 */
	public Environment<E> load() throws IOException, MultiplePlace, UnknownPlace{
		Environment<E> env = new Environment<E>();
		
		InputStream ips=new FileInputStream(path); 
		InputStreamReader ipsr=new InputStreamReader(ips);
		BufferedReader br=new BufferedReader(ipsr);

		String line = null;
		
		//On boucle tant qu'on est sur des définitions de place (des labels)
		while ((line=br.readLine())!=null && line != "" && !line.isEmpty()){
			matrix.addPlace(line);
		}
		
		//On sort de la boucle, donc soit on passe aux liens, soit on on est à la fin du fichier
		if(line != null){
			
			//Numéro de la place dont on définit les successeurs
			int index = 0;
			//tant qu'on tombe sur des lignes remplies, on crée des relations
			while ((line=br.readLine())!=null && line != "" && !line.isEmpty()){
				String[] weights = line.split(" ");
				
				for (int i = 0; i < weights.length; i++) {
					E weight = (E)((Number)Integer.parseInt(weights[i]));
					
					//Si le poids est de 0, alors on traduit cela par un poids infini (valeur max d'un int)
					weight = (weight == (Integer) 0) ? (E)(Integer)Integer.MAX_VALUE : weight;
					matrix.addLink(index, i, weight);
				}
				
				index++;
			}
		}
		
		return env;
	}

	/**
	 * Une méthode statique pour charger un Environnement d'Integer
	 * @param path
	 * @return
	 * @throws IOException 
	 * @throws MultiplePlace 
	 * @throws UnknownPlace 
	 */
	public static Environment<Integer> loadInteger(String path) throws IOException, MultiplePlace, UnknownPlace{
		EnvLoader<Integer> loader = new EnvLoader<>(path);
		
		return loader.load();
	}
	
	/**
	 * une méthode statique pour charger un environnement de Double
	 * @param path
	 * @return
	 * @throws IOException 
	 * @throws MultiplePlace 
	 * @throws UnknownPlace 
	 */
	public static Environment<Double> loadDouble(String path) throws IOException, MultiplePlace, UnknownPlace{
		EnvLoader<Double> loader = new EnvLoader<>(path);
		
		return loader.load();
	}
}
