package nktl.generator;

import nktl.math.RangeInt;
import nktl.math.geom.Vec3i;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Generator {
    
    enum Direction {
        NORTH, SOUTH, EAST, WEST;

        Direction getLeft() {
            switch (this) {
                case NORTH:
                    return WEST;
                case WEST:
                    return SOUTH;
                case SOUTH:
                    return EAST;
                default:
                    return NORTH;
            }
        }

        Direction getRight() {
            switch (this) {
                case NORTH:
                    return EAST;
                case EAST:
                    return SOUTH;
                case SOUTH:
                    return WEST;
                default:
                    return NORTH;
            }
        }
    }

    static class Way extends Vec3i{
        Direction dir = Direction.NORTH;

        Way(){}

        Way(Vec3i pos, Direction dir) {
            this.copy(pos.x, pos.y, pos.z);
            this.dir = dir;
        }

        Way getLeft(){
            return new Way(this, this.dir.getLeft());
        }

        Way getRight(){
            return new Way(this, this.dir.getRight());
        }

        Way getForward(){
            return new Way(this, this.dir);
        }
    }

    private static final RangeInt RANGE6 = new RangeInt(0, 6);

    private static final byte
            LEFT = 0, RIGHT = 1, FORWARD = 2,
            FORWARD_LEFT = 3, FORWARD_RIGHT = 4, LEFT_RIGHT = 5,
            EVERYWHERE = 6;

    private static final double THIRD = 1./3.;
    private static final double TWO_THIRDS = 2*THIRD;
    
    // 

    private double
            threshold_2_way,
            threshold_3_way;
    Random random;

    public Generator(long seed, double one_way_probability){
        random = new Random(seed);
        generateThresholds(one_way_probability/(1 - one_way_probability));
    }

    public byte waysToGo(){
        double variety = random();
        if (variety > threshold_3_way) return EVERYWHERE;
        double variant = random();
        boolean oneWay = variety < threshold_2_way;
        if (variant < THIRD){
            return oneWay ? LEFT : FORWARD_LEFT;
        } else
        if (variant < TWO_THIRDS){
            return oneWay ? RIGHT : FORWARD_RIGHT;
        } else {
            return oneWay ? FORWARD : LEFT_RIGHT;
        }
    }

    private void generateThresholds(double b){
        double a = 1./(1 + 2*b + b*b);
        threshold_2_way = a*b + a*b*b;
        threshold_3_way = threshold_2_way + a*b;
    }

    public DwarfMap generateBooleanMap(int dx, int dy, int layers) throws GeneratorException{
        if (dx <= 0 || dy <= 0 || layers <= 0) throw new GeneratorException();
        DwarfMap map = new DwarfMap(new Vec3i(dx, dy, layers));

        for (int i = 0; i < layers; i++) {
            generateLevel(map, i);
        }
        return map;
    }

    public void generateLevel(DwarfMap map, int level) throws GeneratorException {
        Way startWay = new Way(map.getRandomPosition(level, random), Direction.NORTH);

        LinkedList<Way> ways = new LinkedList<>();
        ways.add(startWay);

        while (!ways.isEmpty()) {
            ways = genNewWays(map, ways);
        }
    }

    public LinkedList<Way> genNewWays(DwarfMap map, LinkedList<Way> ways){
        LinkedList<Way> newWays = new LinkedList<>();
        for (Way way : ways) {
            int combination = RangeInt.from01(random(), RANGE6);
            Way[] ws;
            switch (combination) {
                case LEFT: ws = new Way[]{way.getLeft()}; break;
                case RIGHT: ws = new Way[]{way.getRight()}; break;
                case FORWARD: ws = new Way[]{way.getForward()}; break;
                case FORWARD_LEFT: ws = new Way[]{way.getForward(), way.getLeft()}; break;
                case FORWARD_RIGHT: ws = new Way[]{way.getForward(), way.getRight()}; break;
                case LEFT_RIGHT: ws = new Way[]{way.getLeft(), way.getRight()}; break;
                default: ws = new Way[]{
                        way.getLeft(), way.getRight(), way.getForward()
                }; break;
            }
            for (Way w : ws) {
                Way nextWay = generateDaWay(map, w);
                if (nextWay != null) newWays.add(nextWay);
            }
        }
        ways.clear();
        return newWays;
    }

    public Way generateDaWay(DwarfMap map, Way way) {
        int numBlocks = random() > 0.5 ? 3 : 2;

        for (int i = 0; i < numBlocks; i++) {
            switch (way.dir) {
                case NORTH: way.x++; break;
                case SOUTH: way.x--; break;
                case EAST: way.y++; break;
                case WEST: way.y--; break;
            }
            if (!map.has(way)) return null;
            if (map.hasBlocksAroundAtLevel(way)){
                if (random()>0.5) map.createCubeAt(way);
                return null;
            } else map.createCubeAt(way);
        }
        return way;
    }

    public double random(){
        return random.nextDouble();
    }

}
