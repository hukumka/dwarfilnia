package nktl.writer.tests;

import nktl.writer.RMIClient;

import java.io.IOException;
import java.rmi.NotBoundException;

public class TestCommand {

    public static void main(String[]args) throws IOException, NotBoundException {
        var process = new RMIClient().getProcess();

        process.write("fill -2172 63 1 -2172 63 1 minecraft:stone_stairs[half=bottom,facing=east]\n");



    }

}
