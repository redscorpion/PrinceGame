package com.tieto.princegame.persia;

import java.io.PrintWriter;
import java.io.StringWriter;
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

        Action getAction(Prince prince, StepStrategy context);

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

    private final Game game;

    public PersiaStrategy() {
        Log.fine("************");
        Log.fine("* NEW GAME *");
        Log.fine("************");
        game = new Game();
    }

    public Action step(final Prince prince) {
        final StepStrategy stepStrategy = game.newStep(prince, actionStrategies);

        final Action action = stepStrategy.evaluate();
        game.setStepAction(action);

        return action;
    }

    private static void setupLogger() {
        final Logger logger = Logger.getLogger(PersiaStrategy.class.getPackage().getName());
        logger.setLevel(Level.FINE);

        final Formatter consoleFormatter = new SimpleFormatter() {
            String format = "%1$s%2$s%n";

            @Override
            public String format(final LogRecord record) {
                final String message = formatMessage(record);
                String throwable = "";
                if (record.getThrown() != null) {
                    final StringWriter sw = new StringWriter();
                    final PrintWriter pw = new PrintWriter(sw);
                    pw.println();
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    throwable = sw.toString();
                }
                return String.format(format, message, throwable);
            }
        };

        final ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(consoleFormatter);
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFilter(null);

        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
    }

}