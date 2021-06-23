package se.umu.cs.gcom.MessageOrdering;

import se.umu.cs.gcom.GCom.User;

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

    public HashMap<UUID, Integer> getClock() {
        return clock;
    }

    public void initialize(UUID userID) {
        clock.put(userID, 0);
    }

    public void increment(User user) {
        Integer oldValue = clock.get(user.getUserID());
        if (oldValue == null){
            oldValue = 0;
        }
        Integer newValue = oldValue + 1;
        clock.put(user.getUserID(), newValue);
    }

    public void remove(User user) {
        clock.remove(user.getUserID());
    }

    public Integer getUserClock(UUID userID){
        return this.clock.get(userID);
    }
    /**
     * @param otherClock When a process receive a message and it's vector clock from another process, compare the vector
     *                   clock of current process with the received clock and update the clock of current process.
     */
    public void update(VectorClock otherClock) {
        ArrayList<UUID> knownUsers = new ArrayList<>(this.clock.keySet());
        knownUsers.addAll(otherClock.clock.keySet());
        for (UUID userID : knownUsers) {
            clock.putIfAbsent(userID, 0);
            otherClock.clock.putIfAbsent(userID, 0);
            this.clock.put(userID, Math.max(this.clock.get(userID), otherClock.clock.get(userID)));
        }
    }

    public boolean lessThan(VectorClock otherClock) {
        HashSet<UUID> ourSet = new HashSet<>(this.clock.keySet());
        ourSet.addAll(otherClock.clock.keySet());
        
        for (UUID userID : ourSet){
            clock.putIfAbsent(userID, 0);
            otherClock.clock.putIfAbsent(userID, 0);
        }

        for (UUID userID : ourSet) {
            if (clock.get(userID) > otherClock.clock.get(userID)) {
                return false;
            }else if (clock.get(userID) < otherClock.clock.get(userID)){
                return true;
            }
        }
        return true;

    }
    public VectorClock copy() throws CloneNotSupportedException {
        HashMap<UUID, Integer> copyMap = new HashMap<UUID, Integer>();
        copyMap.putAll(this.clock);
        return new VectorClock(copyMap);
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
