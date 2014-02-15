package MiAl.strategy;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import MiAl.landscape.GameScene;

import com.tieto.princegame.persia.PersiaStrategy;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.EnterGate;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class MiAlStrategyTest {

    private static final int CYCLE = 40;

    public void testRunner(final List<Field> fields, int position) {

        final PersiaStrategy ps = new PersiaStrategy();
        int i = 0;
        for (; i < CYCLE; i++) {
            final Prince prince = GameScene.getPrinceOnPosition(position);

            final Action action = ps.step(prince);

            System.out.println(">> ACTION " + action.getClass().getSimpleName() + " position " + position);

            if (action instanceof EnterGate) {
                System.out.println("VICTORY");
                break;
            }

            // correct position
            position = GameScene.updateGameScene(action);
        }
        if (i >= CYCLE) {
            Assert.fail("Infinite Loop");
        }
        System.out.println("game over");
    }

    @Test
    public void testPitfall() {

        testRunner(GameScene.generateFields("PATH", "PATH", "PITFALL", "PATH", "GATE", "PATH"), 1);

    }

    @Test
    public void testKnight() {

        testRunner(GameScene.generateFields("PATH", "SWORD", "PITFALL", "PATH", "PITFALL", "PATH", "KNIGHT", "GATE", "PATH"), 3);

    }

    @Test
    public void testBush() {

        testRunner(GameScene.generateFields("PATH", "MATCHES", "PITFALL", "PATH", "PITFALL", "PATH", "THORN_BUSH", "GATE", "PATH"), 3);

    }

}
