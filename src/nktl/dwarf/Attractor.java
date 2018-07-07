package nktl.dwarf;

import nktl.math.geom.Vec3i;

public interface Attractor {
    Vec3i position();
    double distanceTo(Vec3i pos);
}
