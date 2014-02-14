package com.tieto.princegame.persia;

import java.util.List;

import com.tieto.princegame.persia.LevelMap.MapField;
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
import cz.tieto.princegame.common.gameobject.Obstacle;
import cz.tieto.princegame.common.gameobject.Prince;

public class Utils {

    public static boolean isAlive(Obstacle obstacle) {
        return obstacle != null && !Boolean.parseBoolean(obstacle.getProperty("dead"));
    }

    public static boolean isAliveEnemy(Obstacle obstacle) {
        return obstacle != null && isEnemy(obstacle) && isAlive(obstacle);
    }

    public static boolean isDeadEnemy(Obstacle obstacle) {
        return obstacle != null && isEnemy(obstacle) && !isAlive(obstacle);
    }

    public static int getHealth(Obstacle obstacle) {
        return obstacle != null ? Integer.parseInt(obstacle.getProperty("health")) : 0;
    }

    public static Equipment getEquipment(Prince prince, EEquipment equipment) {
        return getEquipment(prince, equipment.getName());
    }

    public static Equipment getEquipment(Prince prince, String name) {
        for (Equipment eq : prince.getInventory()) {
            if (eq.getName().equals(name)) {
                return eq;
            }
        }
        return null;
    }

    public static int getAttack(Obstacle obstacle, int distance) {
        return getAttack(EObstacle.valueOf(obstacle), distance);
    }

    public static int getAttack(EObstacle obstacle, int distance) {
        return obstacle != null ? obstacle.getAttack(distance) : 0;
    }

    public static int getAttackRange(Obstacle obstacle) {
        return getAttackRange(EObstacle.valueOf(obstacle));
    }

    public static int getAttackRange(EObstacle obstacle) {
        return obstacle != null ? obstacle.getAttackRange() : 0;
    }

    public static int getWeaponAttack(Equipment weapon) {
        return getWeaponAttack(EEquipment.valueOf(weapon));
    }

    public static int getWeaponAttack(EEquipment weapon) {
        return weapon != null ? weapon.getAttack() : 0;
    }

    public static Equipment getBestWeapon(Prince prince, Obstacle obstacle) {
        return getEquipment(prince, EEquipment.SWORD);
    }

    public static boolean isSafeToMoveFast(TurnStrategy turnStrategy, int jumpLength) {
        return isSafeToMoveFast(turnStrategy.getGame(), turnStrategy.getStepDirection(), jumpLength);
    }

    public static boolean isSafeToMoveFast(Game game, EDirection direction, int jumpLength) {
        int playerPos = game.getPrincePos();
        LevelMap levelMap = game.getLevelMap();

        int dir = (EDirection.FWD == direction) ? 1 : -1;

        for (int i = 1; i <= jumpLength; i++) {
            MapField mapField = levelMap.getMapField(playerPos + i * dir);
            if (mapField == null || mapField.getGameField().getEquipment() != null) {
                return false;
            }
            if (mapField.getGameField().getObstacle() != null) {
                Obstacle obstacle = mapField.getGameField().getObstacle();
                if (!EObstacle.isDisabled(obstacle)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isEnemy(Obstacle obstacle) {
        return isEnemy(EObstacle.valueOf(obstacle));
    }

    public static boolean isEnemy(EObstacle obstacle) {
        return obstacle.getAttackRange() > 0;
    }

    public static boolean isSafeToHealHere(TurnStrategy turnStrategy) {
        LevelMap levelMap = turnStrategy.getGame().getLevelMap();
        int princePos = turnStrategy.getGame().getPrincePos();
        if (getAttackingEnemyDirection(turnStrategy) != null) {
            return false;
        }

        Integer damage = levelMap.getDamageAt(princePos);
        return damage == null || damage <= 0;
    }

    public static Obstacle getEnemyToAttack(Game game, int pos, int attackRadius) {
        List<Obstacle> enemies = game.getLevelMap().findEnemyNear(pos, attackRadius);
        return getEnemyWithLowestHealth(enemies);
    }

    private static Obstacle getEnemyWithLowestHealth(List<Obstacle> enemies) {
        int minHealth = Integer.MAX_VALUE;
        Obstacle minHealthEnemy = null;
        for (Obstacle obstacle : enemies) {
            int enemyHealth = getHealth(obstacle);
            if (enemyHealth < minHealth) {
                minHealth = enemyHealth;
                minHealthEnemy = obstacle;
            }
        }
        return minHealthEnemy;
    }

    public static EDirection getEnemyDirection(Game game, Obstacle enemy) {
        return game.getLevelMap().getEnemyDirection(enemy, game.getPrincePos());
    }

    public static EDirection getAttackingEnemyDirection(TurnStrategy turnStrategy) {
        LevelMap levelMap = turnStrategy.getGame().getLevelMap();
        int princePos = turnStrategy.getGame().getPrincePos();
        for (Obstacle enemy : levelMap.getAllEnemies()) {
            int distance = levelMap.getEnemyDistance(enemy, princePos);
            if (Math.abs(distance) < getAttackRange(enemy)) {
                return distance > 0 ? EDirection.FWD : EDirection.BKW;
            }
        }

        return null;
    }

    public static void updatePrincePossition(Game game, Action action) {
        game.setPrincePos(getNewPrincePossition(game.getPrincePos(), action));
    }

    public static int getNewPrincePossition(int currentPossition, Action action) {
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

    public static Action getBestRetreatAction(TurnStrategy turnStrategy, EDirection retreatDirection) {
        return getBestRetreatResult(turnStrategy, retreatDirection).getGame().getAction();
    }

    public static TurnStrategy getBestRetreatResult(TurnStrategy turnStrategy, EDirection retreatDirection) {
        TurnStrategy retreatResultWithoutJumping = getRetreatResult(turnStrategy, retreatDirection, false);
        int retreatPosWithoutJumping = getRetreatPossition(retreatResultWithoutJumping, retreatResultWithoutJumping.getGame().getPrincePos());
        Integer damageWithoutJumping = retreatResultWithoutJumping.getGame().getLevelMap().getDamageAt(retreatPosWithoutJumping);

        if (damageWithoutJumping != null && damageWithoutJumping == 0) {
            return retreatResultWithoutJumping;
        }

        TurnStrategy retreatResultWithJumping = getRetreatResult(turnStrategy, retreatDirection, true);
        int retreatPosWithJumping = getRetreatPossition(retreatResultWithJumping, retreatResultWithJumping.getGame().getPrincePos());
        Integer damageWithJumping = retreatResultWithJumping.getGame().getLevelMap().getDamageAt(retreatPosWithJumping);

        if (damageWithJumping == null) {
            return retreatResultWithoutJumping;
        }

        if (damageWithoutJumping == null) {
            return retreatResultWithJumping;
        }

        return damageWithoutJumping < damageWithJumping ? retreatResultWithoutJumping : retreatResultWithJumping;
    }

    private static TurnStrategy getRetreatResult(TurnStrategy turnStrategy, EDirection retreatDirection, boolean allowJumping) {
        Game retreatGame = turnStrategy.getGame().clone(true);
        retreatGame.setRetreat(true);
        retreatGame.setAllowJumping(allowJumping);
        TurnStrategy retreatStrategy = new TurnStrategy(turnStrategy.getPrince(), retreatGame, PersiaStrategy.retreatActionStrategies);
        retreatStrategy.setStepDirection(retreatDirection);
        Action retreatAction = retreatStrategy.evaluate();
        retreatGame.setAction(retreatAction);
        return retreatStrategy;
    }

    public static int getRetreatPossition(TurnStrategy retreatResult, int currPlayerPos) {
        return Utils.getNewPrincePossition(retreatResult.getGame().getPrincePos(), retreatResult.getGame().getAction());
    }

    public static boolean isHeal(Action bestRetreatAction) {
        return bestRetreatAction != null && (bestRetreatAction instanceof Heal);
    }

}
