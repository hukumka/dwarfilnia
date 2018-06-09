package nktl.generator.test;

import nktl.math.RangeInt;

public class GenTest {
    public static void main(String[]args){
        RangeInt r = new RangeInt(0, 5);

        System.out.println(RangeInt.from01(0, r));
        System.out.println(RangeInt.from01(1, r));
    }
}
