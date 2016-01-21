package model;

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
	 */
	public Environment<E> load(){
		Environment<E> env = new Environment<E>();
		
		return env;
	}

	/**
	 * Une méthode statique pour charger un Environnement d'Integer
	 * @param path
	 * @return
	 */
	public static Environment<Integer> loadInteger(String path){
		Environment<Integer> env = new Environment<Integer>();
		
		return env;
	}
	
	/**
	 * une méthode statique pour charger un environnement de Double
	 * @param path
	 * @return
	 */
	public static Environment<Double> loadDouble(String path){
		Environment<Double> env = new Environment<Double>();
		
		return env;
	}
}
