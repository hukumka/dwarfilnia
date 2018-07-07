package nktl.dwarf;

import java.util.Random;

public class DwarfSet {
    /*
        ПЕРЕМЕННЫЕ
     */
    // Вероятность замыкания узла на себя
    double prob_loop_open_threshold = 0.4; // Вероятность замыкания
    double prob_loop_cage_threshold = 0.5; // Вероятность замыкания с окошком
    double prob_loop_door_threshold = 0.6; // Вероятность замыкания с дверью

    // Вероятность генерации тоннеля в одну сторону
    double prob_one_way_tunnel = 0.05;

    // Вероятность генерация диагональной лестницы
    double prob_diagonal_ladder = 0.1;

    // Длина Длина тоннеля
    int min_tunnel_len = 2,
        delta_tunnel_len = 5 - min_tunnel_len;




    // random
    private long seed;
    private Random random = new Random();

    /*
        PUBLIC
     */

    public DwarfSet(){
        randomSeed();
    }

    public long getSeed(){
        return seed;
    }

    public void setSeed(long seed){
        this.seed = seed;
        random.setSeed(seed);
    }

    public double random(){
        return random.nextDouble();
    }

    public void setLoopDistribution(int open, int cage, int door, int wall) throws GeneratorException {
        if (open < 0 || cage < 0 || door < 0 || wall < 0) throw new GeneratorException("Wrong probability set");
        double all = open + cage + door + wall;
        prob_loop_open_threshold = open/all;
        prob_loop_cage_threshold = (open + cage)/all;
        prob_loop_door_threshold = (open + cage + door)/all;
    }


    /*
        PRIVATE
     */
    private void randomSeed(){
        setSeed(2*(long)((Math.random()-0.5)*Long.MAX_VALUE));
    }

    private void timeAsSeed(){
        setSeed(System.currentTimeMillis());
    }


}
