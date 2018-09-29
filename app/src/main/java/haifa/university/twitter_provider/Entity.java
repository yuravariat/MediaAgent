package haifa.university.twitter_provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yura on 24/02/2016.
 */
class Entity {
    private static final int START_INDEX = 0;
    private static final int END_INDEX = 1;
    public final List<Integer> indices;

    public Entity(int start, int end) {
        ArrayList temp = new ArrayList(2);
        temp.add(0, Integer.valueOf(start));
        temp.add(1, Integer.valueOf(end));
        this.indices = Collections.unmodifiableList(temp);
    }

    public int getStart() {
        return ((Integer)this.indices.get(0)).intValue();
    }

    public int getEnd() {
        return ((Integer)this.indices.get(1)).intValue();
    }
}