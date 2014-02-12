package strategy;

import java.util.List;

import landscape.GameScene;

import org.junit.Test;

import com.mycompany.princeextreme.PersiaStrategy;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.EnterGate;
import cz.tieto.princegame.common.action.Grab;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class MiAlStrategyTest {

	private static final int CYCLE = 40;

	public void testRunner(List<Field> fields, int position) {

		PersiaStrategy ps = new PersiaStrategy();
		
		for (int i = 0; i < CYCLE; i++) {
			Prince prince = GameScene.getPrinceOnPosition(position);

			Action action = ps.step(prince);

			System.out.println(action.getClass().getSimpleName() + " position " + position);
			
			if (action instanceof EnterGate) {
				System.out.println("VICTORY");
				break;
			}
			
			if (action instanceof Grab) {
				System.out.println("Grab");
				hasSword = true;
			}
				
		
			// correct position
			position = GameScene.updateGameScene(action);
		}
		
		System.out.println("game over");
	}

	@Test
	public void testPitfall() {

		testRunner(GameScene.generateFields("PATH", "PATH", "PITFALL", "PATH",
				"GATE", "PATH"), 1);

	}
	
	@Test
	public void testKnight() {

		testRunner(GameScene.generateFields("PATH","SWORD", "PITFALL", "PATH", "PITFALL", "PATH","KNIGHT",
				"GATE", "PATH"), 3);

	}

}
