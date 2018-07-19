package nktl.dwarf;

import nktl.math.geom.Vec3i;

import java.util.Random;

public class DwarfSet {

    /*
        Переменные
     */

    // Размер карты
    Vec3i dimensions = new Vec3i(20, 20, 20);

    // Рандом
    long seed = 0;
    Random random = new Random();

    // Соотношение количества путей
    private double prob_way_1, prob_way_2, prob_way_3, prob_way_4, prob_way_5;

    // Соотношение двусторонних и односторонних путей
    private double prob_one_way = 0.3;

    // Вероятность диагональных лестниц
    private double prob_stairs = 0.1;
    private double prob_spiral_stairs = 0.2;

    // Вероятность самозамыкания узла
    private double prob_node_loop = 0.5;

    // Длины

    private int minLength = 1, lengthDelta = 5 - minLength + 1;

    /*
        PUBLIC
     */
    // Setters

    // Устанавливает соотношение количества путей
    public DwarfSet setWayRatio(double one, double two, double three,
                                double four, double five, double six) {
        double total = one + two + three + four + five + six;
        double top = one;
        prob_way_1 = top/total;
        top += two;
        prob_way_2 = top/total;
        top += three;
        prob_way_3 = top/total;
        top += four;
        prob_way_4 = top/total;
        top += five;
        prob_way_5 = top/total;
        return this;
    }

    // Задает сид рандомайзера
    public DwarfSet setSeed(long seed) {
        this.seed = seed;
        random.setSeed(seed);
        return this;
    }

    public DwarfSet setDimensions(int x, int y, int z){
        this.dimensions.copy(x, y, z);
        return this;
    }

    // Getters
    public long getSeed(){
        return seed;
    }

    /*
        Для генератора
     */
    DwarfSet(){
        setSeed(new Random().nextLong());

        double e0 = 1;
        double e1 = e0/2;
        double e2 = e1/2;
        double e3 = e2/2;
        double e4 = e3/2;
        double e5 = e4/2;
        //System.out.println(String.format("%f %f %f %f %f %f", e0, e1, e2, e3, e4, e5));
        setWayRatio(e0, e1, e2, e3, e4, e5);
    }

    double random() {
        return random.nextDouble();
    }

    void resetSeed(){
        random.setSeed(seed);
    }

    int numWaysOfMax(int max) throws GeneratorException {
        if (max > 6) throw new GeneratorException("Логическая ошибка при выборе количества путей. См. DwarfSet");
        if (max < 2) return max;
        double rand = random();
        switch (max) {
            case 2: rand *= prob_way_2;
                break;
            case 3: rand *= prob_way_3;
                break;
            case 4: rand *= prob_way_4;
                break;
            case 5: rand *= prob_way_5;
                break;
        }
        if (rand < prob_way_1) return 1;
        if (rand < prob_way_2) return 2;
        if (rand < prob_way_3) return 3;
        if (rand < prob_way_4) return 4;
        if (rand < prob_way_5) return 5;
        return 6;
    }

    boolean makeOneWay(){
        return random() < prob_one_way;
    }

    boolean makeStairs(){
        return random() < prob_stairs;
    }

    boolean makeSpiralStairs(){
        return random() < prob_spiral_stairs;
    }

    boolean makeNodeLoop() {
        return random() < prob_node_loop;
    }

    int getLength(){
        double rand = random();
        int add = (int) Math.floor(rand*lengthDelta);
        if (rand == 1) --add;

        return minLength + add;
    }

}
