package se.umu.cs.gcom.MessageOrdering;

import java.util.HashMap;
import java.util.UUID;

public class VectorClock {
    private HashMap<UUID,Integer> clock;

    public VectorClock(){
        clock = new HashMap<>();
    }

}
