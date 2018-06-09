package nktl.math;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 29.11.2017.
 */
public class Test {
    public static void main(String[]args){
        Test.class.getClassLoader().setDefaultAssertionStatus(true);
        Range r = new Range();
        r.fromCenter(2.412, 0.02);
        r.fromCenter(2.412, 0.08);
        System.out.println(r);



    }
}
