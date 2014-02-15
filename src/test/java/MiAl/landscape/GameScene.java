package MiAl.landscape;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.Grab;
import cz.tieto.princegame.common.action.JumpBackward;
import cz.tieto.princegame.common.action.JumpForward;
import cz.tieto.princegame.common.action.MoveBackward;
import cz.tieto.princegame.common.action.MoveForward;
import cz.tieto.princegame.common.action.Use;
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
    public static String DRAGON = "DRAGON";
    public static String THORN_BUSH = "THORN_BUSH";
    public static String MATCHES = "MATCHES";

    public static List<Field> fields = null;

    public static int currentPosition;

    static Prince prince = null;

    static List<Equipment> equipments = new ArrayList<Equipment>();

    public static List<Field> generateFields(final String... field_idents) {
        fields = new ArrayList<Field>(field_idents.length);
        for (final String field_ident : field_idents) {
            final Field field = mock(Field.class);
            fields.add(field);

            if (field_ident.equals(SWORD)) {
                final Equipment equipment = new Sword();
                when(field.getEquipment()).thenReturn(equipment);
            }

            if (field_ident.equals(MATCHES)) {
                final Equipment equipment = new Matches();
                when(field.getEquipment()).thenReturn(equipment);
            }

            if (field_ident.equals(PITFALL)) {
                final Obstacle obstacle = mock(Obstacle.class);
                when(obstacle.getName()).thenReturn("pitfall");
                when(field.getObstacle()).thenReturn(obstacle);
            }

            if (field_ident.equals(KNIGHT)) {
                final Obstacle obstacle = new Knight(3);
                when(field.getObstacle()).thenReturn(obstacle);
            }

            if (field_ident.equals(DRAGON)) {
                final Obstacle obstacle = new Dragon(3);
                when(field.getObstacle()).thenReturn(obstacle);
            }

            if (field_ident.equals(THORN_BUSH)) {
                final Obstacle obstacle = new ThornBush();
                when(field.getObstacle()).thenReturn(obstacle);
            }

            if (field_ident.equals(GATE)) {
                when(field.isGate()).thenReturn(true);
            }

        }

        return fields;
    }

    public static Prince getPrinceOnPosition(final int position) {
        currentPosition = position;

        if (prince == null) {
            prince = mock(Prince.class);
        }

        final Field currentField = fields.get(position);
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

    public static int updateGameScene(final Action action) {
        if (action instanceof Use) {
            final Use u = (Use) action;
            if (u.getEquipment() instanceof Matches && u.getObstacle() instanceof ThornBush) {
                final ThornBush bush = (ThornBush) u.getObstacle();
                bush.burn();
            }

            if (u.getEquipment() instanceof Sword && u.getObstacle() instanceof Knight) {
                final Knight knight = (Knight) u.getObstacle();
                knight.fight();
            }
        }

        if (action instanceof Grab) {
            System.out.println("Grab");
            final Equipment equipment = fields.get(currentPosition).getEquipment();
            if (equipment != null && equipment instanceof Sword) {
                equipments.add(equipment);
                when(prince.getInventory()).thenReturn(equipments);
            }
            if (equipment != null && equipment instanceof Matches) {
                equipments.add(equipment);
                when(prince.getInventory()).thenReturn(equipments);
            }
            when(fields.get(currentPosition).getEquipment()).thenReturn(null);
            System.out.println("GRABBED : " + equipment);
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
            final int jump = candidatePosition - currentPosition;
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

    private static void testTargetPosition(final int candidatePosition) {
        final Field candField = fields.get(candidatePosition);

        if (candField.getObstacle() != null) {
            final Obstacle o = candField.getObstacle();
            if (o.getName().equals("pitfall")) {
                Assert.fail("Dead - pitfall");
            }

            if (o.getName().equals("knight") && (o.getProperty("dead").equals("false"))) {
                Assert.fail("Dead - knight");
            }

            if (o.getName().equals("dragon") && (o.getProperty("dead").equals("false"))) {
                Assert.fail("Dead - dragon");
            }
        }
    }

    private static void testOverPosition(final int candidatePosition) {
        final Field candField = fields.get(candidatePosition);

        if (candField.getObstacle() != null) {
            final Obstacle o = candField.getObstacle();

            if (o.getName().equals("knight") && (o.getProperty("dead").equals("false"))) {
                Assert.fail("Dead - knight");
            }

            if (o.getName().equals("dragon") && (o.getProperty("dead").equals("false"))) {
                Assert.fail("Dead - dragon");
            }
        }
    }
}

class Sword implements Equipment {
    public String getProperty(final String arg0) {
        if ("name".equals(arg0)) {
            return getName();
        }
        return null;
    }

    public String getName() {
        return "sword";
    }

    public int getId() {
        return System.identityHashCode(this);
    }
}

class Matches implements Equipment {
    public String getProperty(final String arg0) {
        if ("name".equals(arg0)) {
            return getName();
        }
        return null;
    }

    public String getName() {
        return "matches";
    }

    public int getId() {
        return System.identityHashCode(this);
    }
}