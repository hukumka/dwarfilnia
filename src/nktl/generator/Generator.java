package nktl.generator;

import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;
import nktl.math.graph.Graph;

import java.util.LinkedList;
import java.util.Random;

public class Generator {
    
    static class Way extends Vec3i{
        Direction dir = Direction.NORTH;
        Graph<DwarfCube>.Node node = null;

        Way(Graph<DwarfCube>.Node node){
            this.node = node;
        }

        Way(Graph<DwarfCube>.Node node, Vec3i pos, Direction dir) {
            this(node);
            this.copy(pos.x, pos.y, pos.z);
            this.dir = dir;
        }

        Way getLeft(){ return new Way(this.node, this, this.dir.getLeft()); }
        Way getRight(){ return new Way(this.node, this, this.dir.getRight()); }
        Way getFront(){ return new Way(this.node, this, this.dir); }
        Way getBack() { return new Way(this.node, this, this.dir.getBack()); }
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

        if (startCubes != null && startCubes.length > 0){
            for (int layer = 0; layer < layers-1; layer++){
                for (DwarfCube cube : startCubes) {
                    Vec3i pos = cube.getPosition();
                    pos.z = layer;
                    Vec3i below = pos.copy();
                    below.z++;
                    var n1 = map.get(pos).node;
                    var n2 = map.get(below).node;
                    map.graph.connect(n1, n2);
                    map.connections.add(new Connection(n1, n2, map.get(below)));
                }
            }
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

    private void generateLevel(DwarfMap map, int level, DwarfCube... startCubes) throws GeneratorException {


        LinkedList<Way> ways = new LinkedList<>();
        if (startCubes != null && startCubes.length > 0){
            for (DwarfCube cube : startCubes){
                DwarfCube dc = cube.copy();
                dc.position.z = level;
                if (!map.has(dc.position)) continue;
                Direction direction;
                if (dc.typeIs(DwarfCube.TYPE_DIAGONAL_LADDER))
                    direction = dc.enumDirection();
                else if (dc.direction == 0) {
                    double dirVal = random();
                    direction =
                            dirVal < 0.25 ? Direction.NORTH :
                                    dirVal < 0.5 ? Direction.SOUTH :
                                            dirVal < 0.75 ? Direction.EAST : Direction.WEST;
                } else direction = dc.enumDirection();
                Graph<DwarfCube>.Node node = map.graph.newNode(dc);
                Way way = new Way(node, dc.position, direction);
                if (ways.isEmpty()) map.setPointAsGenBounds(way);
                ways.add(way);
                dc.setNode(node);
                dc.position.z = level;
                map.placeCube(dc);
            }
        }
        if (ways.isEmpty()){
            Vec3i position = map.getRandomPosition(level, random);
            map.setPointAsGenBounds(position);
            DwarfCube cube = map.createCubeAt(position, true);
            Graph.Node node = map.graph.newNode(cube);
            cube.setNode(node);
            Way startWay = new Way(map.graph.newNode(cube), position, Direction.NORTH);
            ways.add(startWay);
        }

        while (!ways.isEmpty() || !map.genBoundsMatchMax()) {
            if (!ways.isEmpty()) {
                ways = genNewWays(map, ways);
            } else {
                if (map.genBoundsX.max() < map.rangeX.max() && map.eastEdge != null)
                    ways.add(new Way(map.eastEdge.node, map.eastEdge.position, Direction.EAST));
                if (map.genBoundsY.max() < map.rangeY.max() && map.northEdge != null)
                    ways.add(new Way(map.northEdge.node, map.northEdge.position, Direction.NORTH));

                if (map.genBoundsX.min() > map.rangeX.min() && map.westEdge != null)
                    ways.add(new Way(map.westEdge.node, map.westEdge.position, Direction.WEST));
                if (map.genBoundsY.min() > map.rangeY.min() && map.southEdge != null)
                    ways.add(new Way(map.southEdge.node, map.southEdge.position, Direction.SOUTH));
                if (ways.isEmpty()) break;
                ways = genNewWays(map, ways);
            }
        }
    }

    private LinkedList<Way> genNewWays(DwarfMap map, LinkedList<Way> ways){
        LinkedList<Way> newWays = new LinkedList<>();
        for (Way way : ways) {
            Way[] ws;
            DwarfCube cube = map.get(way);
            if (cube.typeIs(DwarfCube.TYPE_DIAGONAL_LADDER)) {
                Direction dir = cube.enumDirection();
                boolean hasBlockInFront = map.hasBlockAtDirection(way, dir);
                boolean hasBlockBehind = map.hasBlockAtDirection(way, dir.getBack());
                ws = new Way[2];
                if (!hasBlockInFront) ws[0] = way.getFront();
                if (!hasBlockBehind) ws[1] = way.getBack();
            } else {
                int combination = waysToGo();
                switch (combination) {
                    case LEFT: ws = new Way[]{way.getLeft()}; break;
                    case RIGHT: ws = new Way[]{way.getRight()}; break;
                    case FORWARD: ws = new Way[]{way.getFront()}; break;
                    case FORWARD_LEFT: ws = new Way[]{way.getFront(), way.getLeft()}; break;
                    case FORWARD_RIGHT: ws = new Way[]{way.getFront(), way.getRight()}; break;
                    case LEFT_RIGHT: ws = new Way[]{way.getLeft(), way.getRight()}; break;
                    default: ws = new Way[]{
                            way.getLeft(), way.getRight(), way.getFront()
                    }; break;
                }
            }

            for (Way w : ws) {
                if (w == null) continue;
                //System.out.print("Before : " + map.get(30, 10, way.z).type);
                Way nextWay = generateDaWay(map, w);
                //System.out.println("; After : " + map.get(30, 10, way.z).type);
                if (nextWay != null) newWays.add(nextWay);
            }
        }
        ways.clear();
        return newWays;
    }

    private Way generateDaWay(DwarfMap map, Way way) {

        int minLen = minLenBeforeTurn - 1;
        int numBlocks = minLen + (int) (random() * (maxLenBeforeTurn - minLen));
        if (map.get(way).getType() == DwarfCube.TYPE_VERTICAL_LADDER && numBlocks < 2)
            numBlocks = 2;
        for (int i = 0; i < numBlocks; i++) {
            //System.out.println("Direction - " + way.dir + ". Incrementing position...");
            switch (way.dir) {
                case NORTH: way.y++; break;
                case SOUTH: way.y--; break;
                case EAST: way.x++; break;
                case WEST: way.x--; break;
            }
            if (!map.has(way)) return null;
            if (map.get(way) != null) return null;
            //System.out.println("Map has new position.");
            if (map.hasVerticalLaddersAtCorners(way))
                return null;

            if (map.hasBlocksAroundAtLevel(way, way.dir)){
                //System.out.println("Found block on the way.");
                //*
                Graph<DwarfCube>.Node node = map.anotherNodeAround(way, way.node);
                if (node != null) {
                    if (map.graph.isConnected(node, way.node)) return null;
                    DwarfCube cube = createCubeWithNode(map, way);
                    map.graph.connect(node, way.node);
                    map.connections.add(new Connection(node, way.node, cube));
                } else /**/
                if (random() < loopProbability) createCubeWithNode(map, way);
                return null;
            } else createCubeWithNode(map, way);
            //System.out.println("No blocks around. Can continue.");
        }
        return way;
    }

    private DwarfCube createCubeWithNode(DwarfMap map, Way way){
        DwarfCube cube = map.createCubeAt(way);
        cube.setNode(way.node);
        return cube;
    }

    private double random(){
        return random.nextDouble();
    }


}
