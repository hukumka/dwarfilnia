package nktl.generator;

import java.util.LinkedList;

public class DwarfList {
    private LinkedList<DwarfCube> cubes5x5 = new LinkedList<>();
    private LinkedList<DwarfCube> cubes7x7 = new LinkedList<>();

    public LinkedList<DwarfCube> get5x5() {
        return cubes5x5;
    }

    public LinkedList<DwarfCube> get7x7() {
        return cubes7x7;
    }
}
