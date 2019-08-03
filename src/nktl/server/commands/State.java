package nktl.server.commands;

public interface State {

    String name();
    String value();
    Facing rotate90Y();

    enum Facing implements State {
        north, south, east, west;

        public Facing rotate90Y() {
            switch (this) {
                case west: return south;
                case south: return east;
                case east: return north;
                case north: default: return west;
            }
        }

        @Override
        public String toString() { return "facing=" + this; }
    }



}
