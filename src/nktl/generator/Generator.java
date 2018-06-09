package nktl.generator;

import nktl.math.RangeInt;
import nktl.math.geom.Vec3i;

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

    private static final byte
            LEFT = 0, RIGHT = 1, FORWARD = 2,
            FORWARD_LEFT = 3, FORWARD_RIGHT = 4, LEFT_RIGHT = 5,
            EVERYWHERE = 6;

    private static final double THIRD = 1./3.;
    private static final double TWO_THIRDS = 2*THIRD;
    
    // Переменные для генерации

    private long seed = 0;
    private double loopProbability = 0.5;
    private int maxLenBeforeTurn = 5;
    private double
            threshold_2_way,
            threshold_3_way;
    private Random random = new Random(0);

    public Generator(){
        setRandomSeed();
        try {
            setOneWayProbability(0.7);
        } catch (Exception e) {e.printStackTrace();}
    }

    // PUBLIC
    public long setRandomSeed(){
        setSeed((long) (Math.random()*2*Long.MAX_VALUE));
        random.setSeed(seed);
        return seed;
    }

    public Generator setLoopProbability(double probability) throws GeneratorException {
        if (probability < 0 || probability > 1)
            throw new GeneratorException("Wrong loop probability : " + probability);
        this.loopProbability = probability;
        return this;
    }

    public Generator setOneWayProbability(double probability) throws GeneratorException {
        if (probability < 0 || probability > 1)
            throw new GeneratorException("Wrong one way probability : " + probability);

        generateThresholds(probability/(1 - probability));
        return this;
    }

    public Generator setSeed(long seed) {
        this.seed = seed;
        this.random.setSeed(seed);
        return this;
    }

    public Generator setMaxLenBeforeTurn(int length){
        this.maxLenBeforeTurn = length;
        return this;
    }

    public DwarfMap generateMap(int dx, int dy, int layers) throws GeneratorException{
        if (dx <= 0 || dy <= 0 || layers <= 0) throw new GeneratorException();
        DwarfMap map = new DwarfMap(new Vec3i(dx, dy, layers));

        for (int i = 0; i < layers; i++) {
            generateLevel(map, i);
        }
        return map;
    }

    // PRIVATE

    private byte waysToGo(){
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

    private void generateLevel(DwarfMap map, int level) throws GeneratorException {
        Way startWay = new Way(map.getRandomPosition(level, random), Direction.NORTH);

        LinkedList<Way> ways = new LinkedList<>();
        ways.add(startWay);
        map.createCubeAt(startWay);

        while (!ways.isEmpty()) {
            ways = genNewWays(map, ways);
        }
    }

    private LinkedList<Way> genNewWays(DwarfMap map, LinkedList<Way> ways){
        LinkedList<Way> newWays = new LinkedList<>();
        for (Way way : ways) {
            int combination = waysToGo();
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

    private Way generateDaWay(DwarfMap map, Way way) {
        int numBlocks = 1 + (int) (random() * (maxLenBeforeTurn - 1));

        for (int i = 0; i < numBlocks; i++) {
            //System.out.println("Direction - " + way.dir + ". Incrementing position...");
            switch (way.dir) {
                case NORTH: way.x++; break;
                case SOUTH: way.x--; break;
                case EAST: way.y++; break;
                case WEST: way.y--; break;
            }
            if (!map.has(way)) return null;
            //System.out.println("Map has new position.");
            if (map.hasBlocksAroundAtLevel(way, way.dir)){
                //System.out.println("Found block on the way.");
                if (random() < loopProbability) map.createCubeAt(way);
                return null;
            } else map.createCubeAt(way);
            //System.out.println("No blocks around. Can continue.");
        }
        return way;
    }

    private double random(){
        return random.nextDouble();
    }

    private void generateThresholds(double b){
        double a = 1./(1 + 2*b + b*b);
        threshold_2_way = a*b + a*b*b;
        threshold_3_way = threshold_2_way + a*b;
    }


}
