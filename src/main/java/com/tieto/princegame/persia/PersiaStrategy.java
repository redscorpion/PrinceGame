package com.tieto.princegame.persia;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.tieto.princegame.persia.actionstrategies.AttackEnemyStrategy;
import com.tieto.princegame.persia.actionstrategies.ChopperStrategy;
import com.tieto.princegame.persia.actionstrategies.EnterGateStrategy;
import com.tieto.princegame.persia.actionstrategies.GrabEquipmentStrategy;
import com.tieto.princegame.persia.actionstrategies.HealStrategy;
import com.tieto.princegame.persia.actionstrategies.PitfallStrategy;
import com.tieto.princegame.persia.actionstrategies.RetreatStrategy;
import com.tieto.princegame.persia.actionstrategies.SimpleMoveStrategy;
import com.tieto.princegame.persia.actionstrategies.ThornbushStrategy;

import cz.tieto.princegame.common.GameStrategy;
import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Prince;

public class PersiaStrategy implements GameStrategy {

    private static final Logger Log = Logger.getLogger(PersiaStrategy.class.getName());

    public interface ActionStrategy {

        Action getAction(Prince prince, TurnStrategy context);

    }

    public static List<ActionStrategy> actionStrategies = new ArrayList<ActionStrategy>();
    public static List<ActionStrategy> retreatActionStrategies = new ArrayList<ActionStrategy>();

    static {
        setupLogger();

        actionStrategies.add(new EnterGateStrategy());
        actionStrategies.add(new RetreatStrategy());
        actionStrategies.add(new HealStrategy());
        actionStrategies.add(new GrabEquipmentStrategy());
        actionStrategies.add(new AttackEnemyStrategy());
        actionStrategies.add(new ThornbushStrategy());
        actionStrategies.add(new PitfallStrategy());
        actionStrategies.add(new ChopperStrategy());
        actionStrategies.add(new SimpleMoveStrategy());

        retreatActionStrategies.add(new EnterGateStrategy());
        retreatActionStrategies.add(new ThornbushStrategy());
        retreatActionStrategies.add(new PitfallStrategy());
        retreatActionStrategies.add(new ChopperStrategy());
        retreatActionStrategies.add(new SimpleMoveStrategy());
    }

    private int step;

    private Game gameStrategy;

    public PersiaStrategy() {
        step = 0;
        gameStrategy = new Game();
    }

    public Action step(Prince prince) {
        Log.info("STEP " + ++step);
        Log.info("---------");
        Log.info("-- prince health: " + prince.getHealth());

        TurnStrategy turnStrategy = gameStrategy.newTurnStrategy(prince, step, actionStrategies);

        Action action = turnStrategy.evaluate();
        Log.info("-- turn action: " + action.getClass().getSimpleName());

        gameStrategy.setAction(action);
        gameStrategy = gameStrategy.clone(true);
        gameStrategy.getHistory().add(turnStrategy);
        Utils.updatePrincePossition(gameStrategy, action);

        Log.info("---");

        return action;
    }

    private static void setupLogger() {
        Logger logger = Logger.getLogger(PersiaStrategy.class.getPackage().getName());
        logger.setLevel(Level.FINE);

        Formatter consoleFormatter = new SimpleFormatter() {
            @Override
            public String format(LogRecord record) {
                return record.getMessage() + System.getProperty("line.separator");
            }
        };

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(consoleFormatter);
        consoleHandler.setLevel(Level.FINE);
        consoleHandler.setFilter(null);

        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
    }

}