package nktl.server.commands;

public interface State {



    enum Facing {
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
