package persia;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import persia.equipment.Sword;
import persia.obstacles.Chopper;
import persia.obstacles.Dragon;
import persia.obstacles.Knight;
import persia.obstacles.Pitfall;
import persia.simulator.TestGame;
import persia.simulator.TestGameBuilder;

import com.tieto.princegame.persia.PersiaStrategy;

import cz.tieto.princegame.common.action.Action;

@RunWith(JUnit4.class)
public class PersiaStrategyIntegrationTest {

    @Test
    public void test() {
        TestGameBuilder builder = TestGameBuilder.buildGame().withGateAt(21).withPrinceAtPossition(7).withPrinceHealth(10);

        builder.addObstacle(new Knight(5), 2);
        builder.addEquipment(new Sword(), 3);
        builder.addObstacle(new Pitfall(), 4);
        builder.addObstacle(new Chopper(), 6);
        builder.addObstacle(new Chopper(), 11);
        builder.addObstacle(new Knight(5), 13);
        builder.addObstacle(new Knight(5), 14);
        builder.addObstacle(new Dragon(5), 15);
        builder.addObstacle(new Dragon(5), 16);
        builder.addObstacle(new Pitfall(), 17);

        TestGame game = builder.build();

        PersiaStrategy strategy = new PersiaStrategy();

        do {
            Action step = strategy.step(game.getPrince());
            game = game.doStep(step);
        } while (!game.isOwer());

        if (game.getPrince().getHealth() <= 0) {
            fail("PRINCE DEAD");
        }
    }

}
