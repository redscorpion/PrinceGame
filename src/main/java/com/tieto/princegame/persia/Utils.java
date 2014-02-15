package com.tieto.princegame.persia;

import java.util.logging.Logger;

import com.tieto.princegame.persia.domain.EDirection;
import com.tieto.princegame.persia.domain.EEquipment;
import com.tieto.princegame.persia.domain.EObstacle;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.Heal;
import cz.tieto.princegame.common.action.JumpBackward;
import cz.tieto.princegame.common.action.JumpForward;
import cz.tieto.princegame.common.action.MoveBackward;
import cz.tieto.princegame.common.action.MoveForward;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Obstacle;
import cz.tieto.princegame.common.gameobject.Prince;

public class Utils {

    private static final Logger Log = Logger.getLogger(Utils.class.getName());

    public static Equipment getEquipment(final Prince prince, final EEquipment equipment) {
        return getEquipment(prince, equipment.getName());
    }

    public static Equipment getEquipment(final Prince prince, final String name) {
        for (final Equipment eq : prince.getInventory()) {
            if (eq.getName().equals(name)) {
                return eq;
            }
        }
        return null;
    }

    public static Equipment getBestWeapon(final Prince prince, final Obstacle obstacle, final int distance) {
        int maxAttack = 0;
        Equipment bestWeapon = null;
        for (final Equipment equipment : prince.getInventory()) {
            final int attack = EEquipment.getAttack(equipment, distance);
            if (attack > maxAttack) {
                maxAttack = attack;
                bestWeapon = equipment;
            }
        }

        return bestWeapon;
    }

    public static boolean isSafeToJump(final StepStrategy stepStrategy) {
        return isSafeToJump(stepStrategy.getGame(), stepStrategy.getStepDirection(), 2);
    }

    private static boolean isSafeToJump(final Game game, final EDirection direction, final int jumpLength) {
        final int playerPos = game.getPrincePos();
        final GameMap gameMap = game.getGameMap();

        final int dir = (EDirection.FWD == direction) ? 1 : -1;

        for (int i = 1; i <= jumpLength; i++) {
            final Field gameField = gameMap.getGameField(playerPos + i * dir);
            if (gameField == null || gameField.getEquipment() != null) {
                return false;
            }
            if (gameField.getObstacle() != null && !EObstacle.isDisabled(gameField.getObstacle())) {
                return false;
            }
        }

        return true;
    }

    public static boolean isSafeToHealHere(final StepStrategy stepStrategy) {
        final GameMap gameMap = stepStrategy.getGame().getGameMap();
        final int princePos = stepStrategy.getGame().getPrincePos();
        if (getAttackingEnemyDirection(stepStrategy) != null) {
            return false;
        }

        final Integer damage = gameMap.getDamageAt(princePos);
        return damage == null || damage <= 0;
    }

    public static Obstacle getEnemyToAttack(final Game game) {
        double minKillability = Double.MAX_VALUE;
        Obstacle enemyToKill = null;
        for (final Obstacle obstacle : game.getGameMap().getAllEnemies()) {
            final int distance = getEnemyDistance(game, obstacle);
            if (Integer.MAX_VALUE == distance) {
                continue;
            }

            final Equipment bestWeapon = getBestWeapon(game.getPrince(), obstacle, distance);
            if (bestWeapon != null) {
                final double attack = EEquipment.getAttack(bestWeapon, distance);
                final double health = EObstacle.getHealth(obstacle);
                final double damage = EObstacle.getAttack(obstacle, -distance);
                double killability = health / attack;
                if (damage > 0) {
                    killability /= damage;
                }
                if (killability < minKillability) {
                    minKillability = killability;
                    enemyToKill = obstacle;
                }
            }
        }

        return enemyToKill;
    }

    public static EDirection getEnemyDirection(final int distance) {
        if (Integer.MAX_VALUE == distance) {
            return null;
        }

        return EDirection.valueOf(distance);
    }

    public static int getEnemyDistance(final Game game, final Obstacle enemy) {
        return game.getGameMap().getEnemyDistance(game.getPrincePos(), enemy);
    }

    public static EDirection getAttackingEnemyDirection(final StepStrategy stepStrategy) {
        final GameMap levelMap = stepStrategy.getGame().getGameMap();
        final int princePos = stepStrategy.getGame().getPrincePos();
        for (final Obstacle enemy : levelMap.getAllEnemies()) {
            final int distance = levelMap.getEnemyDistance(princePos, enemy);
            if (Math.abs(distance) <= EObstacle.getAttackRange(enemy)) {
                return EDirection.valueOf(distance);
            }
        }

        return null;
    }

    public static void updatePrincePossition(final Game game, final Action action) {
        game.setPrincePos(getNewPrincePossition(game.getPrincePos(), action));
    }

    public static int getNewPrincePossition(final int currentPossition, final Action action) {
        int newPossition = currentPossition;
        if (action instanceof JumpForward) {
            newPossition += 2;
        } else if (action instanceof JumpBackward) {
            newPossition -= 2;
        } else if (action instanceof MoveForward) {
            newPossition += 1;
        } else if (action instanceof MoveBackward) {
            newPossition -= 1;
        }
        return newPossition;
    }

    public static Action getBestRetreatAction(final StepStrategy stepStrategy, final EDirection retreatDirection) {
        return getBestRetreatResult(stepStrategy, retreatDirection).getGame().getStepAction();
    }

    public static StepStrategy getBestRetreatResult(final StepStrategy stepStrategy, final EDirection retreatDirection) {
        Log.fine("- retreat analysis");
        try {
            final StepStrategy retreatResultWithoutJumping = getRetreatResult(stepStrategy, retreatDirection, false);
            final int retreatPosWithoutJumping = getRetreatPossition(retreatResultWithoutJumping, retreatResultWithoutJumping.getGame().getPrincePos());
            final Integer damageWithoutJumping = retreatResultWithoutJumping.getGame().getGameMap().getDamageAt(retreatPosWithoutJumping);

            if (damageWithoutJumping != null && damageWithoutJumping == 0) {
                return retreatResultWithoutJumping;
            }

            final StepStrategy retreatResultWithJumping = getRetreatResult(stepStrategy, retreatDirection, true);
            final int retreatPosWithJumping = getRetreatPossition(retreatResultWithJumping, retreatResultWithJumping.getGame().getPrincePos());
            final Integer damageWithJumping = retreatResultWithJumping.getGame().getGameMap().getDamageAt(retreatPosWithJumping);

            if (damageWithJumping == null) {
                return retreatResultWithoutJumping;
            }

            if (damageWithoutJumping == null) {
                return retreatResultWithJumping;
            }

            return damageWithoutJumping < damageWithJumping ? retreatResultWithoutJumping : retreatResultWithJumping;
        } finally {
            Log.fine("- end retreat analysis");
        }
    }

    private static StepStrategy getRetreatResult(final StepStrategy stepStrategy, final EDirection retreatDirection, final boolean allowJumping) {
        final StepStrategy retreatStrategy = stepStrategy.clone(PersiaStrategy.retreatActionStrategies);
        retreatStrategy.setStepDirection(retreatDirection);

        final Game retreatGame = retreatStrategy.getGame();
        retreatGame.setRetreat(true);
        retreatGame.setAllowJumping(allowJumping);

        final Action retreatAction = retreatStrategy.evaluate();
        retreatGame.setStepAction(retreatAction);

        return retreatStrategy;
    }

    public static int getRetreatPossition(final StepStrategy retreatResult, final int currPlayerPos) {
        return Utils.getNewPrincePossition(retreatResult.getGame().getPrincePos(), retreatResult.getGame().getStepAction());
    }

    public static boolean isHeal(final Action bestRetreatAction) {
        return bestRetreatAction != null && (bestRetreatAction instanceof Heal);
    }

}
