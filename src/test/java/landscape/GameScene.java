package landscape;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.Grab;
import cz.tieto.princegame.common.action.JumpBackward;
import cz.tieto.princegame.common.action.JumpForward;
import cz.tieto.princegame.common.action.MoveBackward;
import cz.tieto.princegame.common.action.MoveForward;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Obstacle;
import cz.tieto.princegame.common.gameobject.Prince;

public class GameScene {

	public static String PATH = "PATH";
	public static String SWORD = "SWORD";
	public static String GATE = "GATE";
	public static String KNIGHT = "KNIGHT";
	public static String PITFALL = "PITFALL";

	public static List<Field> fields = null;

	public static int currentPosition;

	static boolean hasSword = false;

	public static List<Field> generateFields(String... field_idents) {
		fields = new ArrayList<Field>(field_idents.length);
		for (String field_ident : field_idents) {
			Field field = mock(Field.class);
			fields.add(field);

			if (field_ident.equals(SWORD)) {
				Equipment equipment = mock(Equipment.class);
				when(equipment.getName()).thenReturn("sword");
				when(field.getEquipment()).thenReturn(equipment);
			}

			if (field_ident.equals(PITFALL)) {
				Obstacle obstacle = mock(Obstacle.class);
				when(obstacle.getName()).thenReturn("pitfall");
				when(field.getObstacle()).thenReturn(obstacle);
			}

			if (field_ident.equals(KNIGHT)) {
				Obstacle obstacle = mock(Obstacle.class);
				when(obstacle.getName()).thenReturn("knight");
				when(field.getObstacle()).thenReturn(obstacle);

				when(obstacle.getProperty("dead")).thenReturn("false");
				when(obstacle.getProperty("health")).thenReturn("4");
				when(obstacle.getProperty("dead")).thenReturn("false");
				when(obstacle.getProperty("health")).thenReturn("2");
				when(obstacle.getProperty("dead")).thenReturn("true");
				when(obstacle.getProperty("health")).thenReturn("0");

			}

			if (field_ident.equals(GATE)) {
				when(field.isGate()).thenReturn(true);
			}

		}

		return fields;
	}

	public static Prince getPrinceOnPosition(int position) {
		currentPosition = position;

		Prince prince = mock(Prince.class);

		Field currentField = fields.get(position);
		when(prince.look(0)).thenReturn(currentField);

		Field prevField = null;
		Field nextField = null;

		if (position > 0) {
			prevField = fields.get(position - 1);
		}
		if (position < fields.size() - 1) {
			nextField = fields.get(position + 1);
		}

		when(prince.look(-1)).thenReturn(prevField);
		when(prince.look(1)).thenReturn(nextField);

		return prince;

	}

	public static int updateGameScene(Action action) {
		if (action instanceof Grab) {
			System.out.println("Grab");
			Equipment equipment = fields.get(currentPosition).getEquipment();
			if (equipment != null && equipment.getName().equals("sword")) {
				hasSword = true;
			}
		}

		int candidatePosition = currentPosition;

		if (action instanceof MoveForward) {
			candidatePosition++;
		} else if (action instanceof JumpForward) {
			candidatePosition += 2;
		}
		if (action instanceof MoveBackward) {
			candidatePosition--;
		} else if (action instanceof JumpBackward) {
			candidatePosition -= 2;
		}

		if (candidatePosition < 0) {
			candidatePosition = 0;
		}
		if (candidatePosition > fields.size() - 1) {
			candidatePosition = fields.size() - 1;
		}

		if (currentPosition != candidatePosition) {
			int jump = candidatePosition - currentPosition;
			if (Math.abs(jump) == 2) {
				if (jump > 0) {
					testOverPosition(currentPosition + 1);
				} else {
					testOverPosition(currentPosition - 1);
				}
			}
			testTargetPosition(candidatePosition);
		}

		return candidatePosition;
	}

	private static void testTargetPosition(int candidatePosition) {
		Field candField = fields.get(candidatePosition);

		if (candField.getObstacle() != null) {
			Obstacle o = candField.getObstacle();		
			if (o.getName().equals("pitfall")) {
				Assert.fail("Dead - pitfall");
			}

			if (o.getName().equals("knight")
					&& (o.getProperty("dead").equals("false"))) {
				Assert.fail("Dead - knight");
			}

			if (o.getName().equals("dragon")
					&& (o.getProperty("dead").equals("false"))) {
				Assert.fail("Dead - dragon");
			}
		}
	}
	
	private static void testOverPosition(int candidatePosition) {
		Field candField = fields.get(candidatePosition);

		if (candField.getObstacle() != null) {
			Obstacle o = candField.getObstacle();		
			
			if (o.getName().equals("knight")
					&& (o.getProperty("dead").equals("false"))) {
				Assert.fail("Dead - knight");
			}

			if (o.getName().equals("dragon")
					&& (o.getProperty("dead").equals("false"))) {
				Assert.fail("Dead - dragon");
			}
		}
	}
}
