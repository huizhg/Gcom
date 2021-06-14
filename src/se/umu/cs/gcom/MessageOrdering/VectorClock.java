package se.umu.cs.gcom.MessageOrdering;

import se.umu.cs.gcom.GroupManagement.User;

import java.io.Serializable;
import java.util.*;

public class VectorClock implements Comparable<VectorClock>, Serializable {
    private static final long serialVersionUID = -1764420974559772373L;
    private HashMap<UUID, Integer> clock;

    public VectorClock() {
        clock = new HashMap<>();
    }

    public VectorClock(HashMap<UUID, Integer> clock) {
        this.clock = clock;
    }

//    public void initialize(User user) {
//        clock.put(user.getUserID(), 0);
//    }

    public void increment(User user) {
//        System.out.println("User ID = "+user.getUserID());
        Integer oldValue = clock.get(user.getUserID());
        if (oldValue == null){
            oldValue = 0;
        }
        Integer newValue = oldValue + 1;
//        System.out.println("Old value = "+oldValue);
//        System.out.println("new value = "+newValue);
        clock.put(user.getUserID(), newValue);
    }

    public void remove(User user) {
        clock.remove(user.getUserID());
    }

    /**
     * @param otherClock When a process receive a message and it's vector clock from another process, compare the vector
     *                   clock of current process with the received clock and update the clock of current process.
     */
    public void update(VectorClock otherClock) {
//        System.out.println("In update.");
        ArrayList<UUID> knownUsers = new ArrayList<>(this.clock.keySet());
//        System.out.println("Known user - "+knownUsers.toString());
//        System.out.println("Incoming - "+otherClock.clock.keySet());
//        System.out.println(knownUsers.addAll(otherClock.clock.keySet()));
        // Expand the users set, because the vector clock from a received message may contain the clock of other users
        // that the current clock has not seen before.
        knownUsers.addAll(otherClock.clock.keySet());
//        System.out.println("Before loop");
        for (UUID userID : knownUsers
        ) {
//            System.out.println("In loop");
            clock.putIfAbsent(userID, 0);
            otherClock.clock.putIfAbsent(userID, 0);
            this.clock.put(userID, Math.max(this.clock.get(userID), otherClock.clock.get(userID)));
        }
    }

    public boolean lessThan(VectorClock otherClock) {
        HashSet<UUID> ourSet = new HashSet<>(this.clock.keySet());
        ourSet.addAll(otherClock.clock.keySet());

        for (UUID userID : ourSet
        ) {
            clock.putIfAbsent(userID, 0);
            otherClock.clock.putIfAbsent(userID, 0);
            if (clock.get(userID) > otherClock.clock.get(userID)) {
                return false;
            }

        }
        return true;

    }
    public VectorClock copy() throws CloneNotSupportedException {
//        System.out.println("Cloneing!");
        HashMap<UUID, Integer> copyMap = new HashMap<UUID, Integer>();
        copyMap.putAll(this.clock);
        VectorClock newClock = new VectorClock(copyMap);
        return newClock;
    }

    @Override
    public int compareTo(VectorClock otherClock) {
        if (this.lessThan(otherClock)) {
            return -1;
        } else if (otherClock.lessThan(this)) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return clock.toString();
    }
}
