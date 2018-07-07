package nktl.dwarf.cubes;

import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;

import java.util.ArrayList;

public class Corridor extends BaseCube {
    public enum FeatureType{
        SEWER,
        DOOR,
        ENTRANCE,
        WAY,
        CAGE
    }

    public static class Feature{
        public FeatureType type;
        public Direction direction;

        Feature(FeatureType type, Direction direction){
            this.type = type;
            this.direction = direction;
        }
    }

    private ArrayList<Feature> features;

    Corridor(Vec3i pos){
        super(pos);
    }

    void addFeature(Feature f){
        this.features.add(f);
    }

    public ArrayList<Feature> features(){
        return this.features;
    }
}
