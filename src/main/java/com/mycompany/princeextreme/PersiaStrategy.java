package com.mycompany.princeextreme;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.princeextreme.actionstrategies.AttackEnemyStrategy;
import com.mycompany.princeextreme.actionstrategies.ChopperStrategy;
import com.mycompany.princeextreme.actionstrategies.EnterGateStrategy;
import com.mycompany.princeextreme.actionstrategies.GrabEquipmentStrategy;
import com.mycompany.princeextreme.actionstrategies.HealStrategy;
import com.mycompany.princeextreme.actionstrategies.PitfallStrategy;
import com.mycompany.princeextreme.actionstrategies.SimpleMoveStrategy;

import cz.tieto.princegame.common.GameStrategy;
import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Prince;

public class PersiaStrategy implements GameStrategy {

    public interface ActionStrategy {

        Action getAction(Prince prince, TurnStrategy context);

    }

    private List<ActionStrategy> strategies = new ArrayList<ActionStrategy>();

    {
        strategies.add(new EnterGateStrategy());
        strategies.add(new HealStrategy());
        strategies.add(new GrabEquipmentStrategy());
        strategies.add(new AttackEnemyStrategy());
        strategies.add(new PitfallStrategy());
        strategies.add(new ChopperStrategy());
        strategies.add(new SimpleMoveStrategy());
    }

    private int steps = 0;

    private com.mycompany.princeextreme.GameStrategy gameStrategy = new com.mycompany.princeextreme.GameStrategy();

    public Action step(Prince prince) {
        System.out.println("STEP " + ++steps);
        System.out.println("----");
        System.out.println("princeHealth:" + prince.getHealth());

        TurnStrategy turnStrategy = gameStrategy.newStep(prince, steps, strategies);
        Action action = turnStrategy.invokeFirst(prince, turnStrategy);
        turnStrategy.setAction(action);

        System.out.println("action:" + action.getClass().getSimpleName());

        gameStrategy = gameStrategy.clone();
        gameStrategy.getHistory().add(turnStrategy);

        System.out.println("---");
        System.out.println("");
        return action;
    }

}