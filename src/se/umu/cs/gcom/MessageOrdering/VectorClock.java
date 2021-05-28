package se.umu.cs.gcom.MessageOrdering;

import se.umu.cs.gcom.GroupManagement.User;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VectorClock implements Comparable<VectorClock> {
    private HashMap<String, Integer> clock;

    public VectorClock() {
        clock = new HashMap<>();
    }

    public VectorClock(HashMap<String, Integer> clock) {
        this.clock = clock;
    }

    public void initialize(User user) throws RemoteException {
        clock.put(user.getId(), 0);
    }

    public void increment(User user) throws RemoteException {
        int oldValue = clock.get(user.getId());
        int newValue = oldValue + 1;
        clock.put(user.getId(), newValue);
    }

    public void remove(User user) throws RemoteException {
        clock.remove(user.getId());
    }

    /**
     * @param otherClock When a process receive a message and it's vector clock from another process, compare the vector
     *                   clock of current process with the received clock and update the clock of current process.
     */
    public void update(VectorClock otherClock) {

        Set<String> knownUsers = this.clock.keySet();
        // Expand the users set, because the vector clock from a received message may contain the clock of other users
        // that the current clock has not seen before.
        knownUsers.addAll(otherClock.clock.keySet());
        for (String userID : knownUsers
        ) {
            clock.putIfAbsent(userID, 0);
            otherClock.clock.putIfAbsent(userID, 0);
            this.clock.put(userID, Math.max(this.clock.get(userID), otherClock.clock.get(userID)));
        }
    }

    public boolean lessThan(VectorClock otherClock) {
        HashSet<String> ourSet = new HashSet<>(this.clock.keySet());
        ourSet.addAll(otherClock.clock.keySet());

        for (String userID : ourSet
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