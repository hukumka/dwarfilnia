package nktl.math;

import java.util.LinkedList;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 29.11.2017.
 */
public class Analysis {

    public static double[] differentiate(double dx, double[] y){
        assert dx != 0 && y != null && y.length > 0;
        int len = y.length-1;
        double[] res = new double[len];

        for (int i = 0, j = 1; i < len; i = j++)
            res[i] = (y[j]-y[i])/dx;

        return res;
    }

    public static double[] integrate(double dx, double C, double[] y){
        assert dx != 0 && y != null && y.length > 0;
        int len = y.length+1;
        double[] res = new double[len];
        res[0] = C;

        for (int i = 0, j = 1; i < y.length; i = j++)
            res[j] = res[i] + dx*y[i];

        return res;
    }

    public static int[] zeros(double[] data){
        LinkedList<Integer> list = new LinkedList<>();
        int len = data.length-1;
        for (int i = 0; i < len; i++){
            if (data[i] == 0){
                list.addLast(i);
                continue;
            }
            if (data[i]*data[i+1] < 0)
                if (Math.abs(data[i]) < Math.abs(data[i+1]))
                    list.addLast(i);
                else
                    list.addLast(i+1);
        }
        return list.stream().mapToInt(i -> i).toArray();
    }



}