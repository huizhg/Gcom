package se.umu.cs.gcom.MessageOrdering;

import se.umu.cs.gcom.Communication.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VectorClock implements Comparable<VectorClock> {
    private HashMap<UUID, Integer> clock;

    public VectorClock() {
        clock = new HashMap<>();
    }

    public VectorClock(HashMap<UUID, Integer> clock) {
        this.clock = clock;
    }

    public void initialize(User user) {
        clock.put(user.getUserID(), 0);
    }

    public void increment(User user) {
        int oldValue = clock.get(user.getUserID());
        int newValue = oldValue + 1;
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

        Set<UUID> knownUsers = this.clock.keySet();
        // Expand the users set, because the vector clock from a received message may contain the clock of other users
        // that the current clock has not seen before.
        knownUsers.addAll(otherClock.clock.keySet());
        for (UUID userID : knownUsers
        ) {
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
        return (VectorClock) this.clone();
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
}