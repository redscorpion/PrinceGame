package com.mycompany.princeextreme;

import java.util.List;

import com.mycompany.princeextreme.LevelMap.MapField;

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
        int playerPos = game.getPricePos();
        LevelMap levelMap = game.getLevelMap();

        int dir = (EDirection.FWD == direction) ? 1 : -1;

        for (int i = 1; i <= jumpLength; i++) {
            MapField mapField = levelMap.getMapField(playerPos + i * dir);
            if (mapField == null || mapField.getGameField().getEquipment() != null) {
                return false;
            }
            if (mapField.getGameField().getObstacle() != null) {
                Obstacle obstacle = mapField.getGameField().getObstacle();
                if (!isEnemy(obstacle) || isAlive(obstacle)) {
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
        return EObstacle.KNIGHT == obstacle || EObstacle.DRAGON == obstacle;
    }

    public static boolean isSafeToHealHere(TurnStrategy turnStrategy) {
        Game gameStrategy = turnStrategy.getGame();

        EObstacle[] values = EObstacle.values();
        for (EObstacle eObstacle : values) {
            if (isEnemy(eObstacle)) {
                if (gameStrategy.getLevelMap().isEnemyNear(eObstacle, gameStrategy.getPricePos(), eObstacle.getAttackRange())) {
                    return false;
                }
            }
        }

        Integer damage = gameStrategy.getLevelMap().getDamageAt(gameStrategy.getPricePos());
        return damage == null || damage <= 0;
    }

    public static Obstacle getNearestEnemy(Game game, int pos, int radius) {
        List<Obstacle> enemies = game.getLevelMap().findEnemyNear(pos, radius);
        return enemies.size() > 0 ? enemies.get(0) : null;
    }

    public static EDirection getEnemyDirection(Game game, Obstacle enemy) {
        return game.getLevelMap().getEnemyDirection(enemy, game.getPricePos());
    }

    public static EDirection getAttackingEnemyDirection(TurnStrategy turnStrategy) {
        Game game = turnStrategy.getGame();

        EObstacle[] values = EObstacle.values();
        for (EObstacle eObstacle : values) {
            if (isEnemy(eObstacle)) {
                EDirection enemyDirection = game.getLevelMap().getAttackingEnemyDirection(eObstacle, game.getPricePos(), eObstacle.getAttackRange());
                if (enemyDirection != null) {
                    return enemyDirection;
                }
            }
        }

        return null;
    }

    public static void updatePrincePossition(Game game, Action action) {
        game.setPlayerPos(getNewPrincePossition(game.getPricePos(), action));
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

    public static Integer getSmallestRetreatDamage(TurnStrategy turnStrategy, EDirection retreatDirection) {
        TurnStrategy retreatResult = getBestRetreatResult(turnStrategy, retreatDirection);
        int retreatPos = getRetreatPossition(retreatResult, retreatResult.getGame().getPricePos());
        return retreatResult.getGame().getLevelMap().getDamageAt(retreatPos);
    }

    public static Action getBestRetreatAction(TurnStrategy turnStrategy, EDirection retreatDirection) {
        return getBestRetreatResult(turnStrategy, retreatDirection).getGame().getAction();
    }

    private static TurnStrategy getBestRetreatResult(TurnStrategy turnStrategy, EDirection retreatDirection) {
        TurnStrategy retreatResultWithoutJumping = getRetreatResult(turnStrategy, retreatDirection, false);
        int retreatPosWithoutJumping = getRetreatPossition(retreatResultWithoutJumping, retreatResultWithoutJumping.getGame().getPricePos());
        Integer damageWithoutJumping = retreatResultWithoutJumping.getGame().getLevelMap().getDamageAt(retreatPosWithoutJumping);

        if (damageWithoutJumping != null && damageWithoutJumping == 0) {
            return retreatResultWithoutJumping;
        }

        TurnStrategy retreatResultWithJumping = getRetreatResult(turnStrategy, retreatDirection, true);
        int retreatPosWithJumping = getRetreatPossition(retreatResultWithJumping, retreatResultWithJumping.getGame().getPricePos());
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
        TurnStrategy retreatStrategy = new TurnStrategy(turnStrategy.getPrince(), retreatGame, PersiaStrategy.retreatStrategies);
        retreatStrategy.setStepDirection(retreatDirection);
        Action retreatAction = retreatStrategy.evaluate();
        retreatGame.setAction(retreatAction);
        return retreatStrategy;
    }

    private static int getRetreatPossition(TurnStrategy retreatResult, int currPlayerPos) {
        return Utils.getNewPrincePossition(retreatResult.getGame().getPricePos(), retreatResult.getGame().getAction());
    }

    public static boolean isHeal(Action bestRetreatAction) {
        return bestRetreatAction != null && (bestRetreatAction instanceof Heal);
    }

}
