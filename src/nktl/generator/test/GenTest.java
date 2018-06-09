package nktl.generator.test;

import nktl.generator.DwarfMap;
import nktl.generator.Generator;
import nktl.generator.GeneratorException;
import nktl.math.RangeInt;

public class GenTest {
    public static void main(String[]args) throws GeneratorException {
        Generator generator = new Generator(4, 0.7);
        DwarfMap dm = generator.generateBooleanMap(100, 100, 3);
    }
}
