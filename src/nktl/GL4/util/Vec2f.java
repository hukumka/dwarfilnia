package nktl.GL4.util;

import java.util.Objects;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 05.09.2016.
 */
public class Vec2f {
    public float x, y;

    public Vec2f() {}

    public Vec2f(float value){
        x = y = value;
    }

    public Vec2f(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec2f vec2f = (Vec2f) o;
        return Float.compare(vec2f.x, x) == 0 &&
                Float.compare(vec2f.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
