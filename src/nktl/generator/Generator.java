package nktl.generator;

import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;

import java.util.LinkedList;
import java.util.Random;

public class Generator {
    
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
    private int minLenBeforeTurn = 3;
    private double
            threshold_2_way,
            threshold_3_way;
    private Random random = new Random(0);

    public Generator(){
        setRandomSeed();
        setWayNumRelation(100, 30, 8);
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

    public Generator setSeed(long seed) {
        this.seed = seed;
        this.random.setSeed(seed);
        return this;
    }

    public Generator setWayNumRelation(int oneWay, int twoWays, int threeWays){
        int all = oneWay + twoWays + threeWays;
        threshold_2_way = ((double)oneWay)/all;
        threshold_3_way = ((double)oneWay + twoWays)/all;
        return this;
    }

    public Generator setLenBeforeTurn(int minLen, int maxLen){
        if (minLen > maxLen) {
            int temp = minLen;
            minLen = maxLen;
            maxLen = temp;
        }
        this.minLenBeforeTurn = minLen;
        this.maxLenBeforeTurn = maxLen;
        return this;
    }

    public DwarfMap generateMap(int dx, int dy, int layers) throws GeneratorException{
        System.out.println("Generating map with no start points...");
        return generateMap(dx, dy, layers, new DwarfCube[0]);
    }

    public DwarfMap generateMap(int dx, int dy, int layers, DwarfCube... startCubes) throws GeneratorException{
        if (dx <= 0 || dy <= 0 || layers <= 0) throw new GeneratorException();
        DwarfMap map = new DwarfMap(new Vec3i(dx, dy, layers));

        for (int i = 0; i < layers; i++) {
            generateLevel(map, i, startCubes);
        }
        map.setCubeTypes();
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

    private void generateLevel(DwarfMap map, int level, DwarfCube... startCubes) throws GeneratorException {
        LinkedList<Way> ways = new LinkedList<>();
        if (startCubes != null && startCubes.length > 0){
            for (DwarfCube cube : startCubes){
                if (!map.has(cube.position)) continue;
                double dirVal = random();
                Way way = new Way(cube.position,
                        dirVal < 0.25 ? Direction.NORTH :
                                dirVal < 0.5 ? Direction.SOUTH :
                                        dirVal < 0.75 ? Direction.EAST : Direction.WEST);
                if (ways.isEmpty()) map.setPointAsGenBounds(way);
                ways.add(way);
                DwarfCube dc = cube.copy();
                dc.position.z = level;
                map.placeCube(dc);
            }
        }
        if (ways.isEmpty()){
            Way startWay = new Way(map.getRandomPosition(level, random), Direction.NORTH);
            map.setPointAsGenBounds(startWay);
            ways.add(startWay);
            map.createCubeAt(startWay);
        }

        while (!ways.isEmpty() || !map.genBoundsMatchMax()) {
            if (!ways.isEmpty())
                ways = genNewWays(map, ways);
            else {
                if (map.genBoundsX.max() < map.rangeX.max() && map.lastEastExpander!= null)
                    ways.add(new Way(map.lastEastExpander.position, Direction.EAST));
                if (map.genBoundsY.max() < map.rangeY.max() && map.lastNorthExpander!= null)
                    ways.add(new Way(map.lastNorthExpander.position, Direction.NORTH));

                if (map.genBoundsX.min() > map.rangeX.min() && map.lastWestExpander!= null)
                    ways.add(new Way(map.lastWestExpander.position, Direction.WEST));
                if (map.genBoundsY.min() > map.rangeY.min() && map.lastSouthExpander!= null)
                    ways.add(new Way(map.lastSouthExpander.position, Direction.SOUTH));
                if (ways.isEmpty()) break;
                ways = genNewWays(map, ways);
            }
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
        int minLen = minLenBeforeTurn - 1;
        int numBlocks = minLen + (int) (random() * (maxLenBeforeTurn - minLen));

        for (int i = 0; i < numBlocks; i++) {
            //System.out.println("Direction - " + way.dir + ". Incrementing position...");
            switch (way.dir) {
                case NORTH: way.y++; break;
                case SOUTH: way.y--; break;
                case EAST: way.x++; break;
                case WEST: way.x--; break;
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


}
