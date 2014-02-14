package landscape;

import cz.tieto.princegame.common.gameobject.Obstacle;

public class ThornBush implements Obstacle {

	Boolean burnt = false;
	
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void burn(){
		System.out.println(" -- BUSH burning !!");
		burnt = true;
	}

	public String getName() {
		return "thornbush";
	}

	public String getProperty(String arg0) {
		if(arg0.equals("burnt")){
			return burnt.toString();
		}
		return null;
	}

}
