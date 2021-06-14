package se.umu.cs.gcom.GroupManagement;

import se.umu.cs.gcom.Communication.Communication;
import se.umu.cs.gcom.MessageOrdering.Ordering;

import java.io.Serializable;

public class Group implements Serializable {
    private static final long serialVersionUID = -1443744741548249800L;
    private final String groupName;
    private Communication communicationMethod;
    private Ordering orderingMethod;

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public Communication getCommunicationMethod() {
        return communicationMethod;
    }

    public Ordering getOrderingMethod() {
        return orderingMethod;
    }

    public void setCommunicationMethod(Communication communicationMethod) {
        this.communicationMethod = communicationMethod;
    }

    public void setOrderingMethod(Ordering orderingMethod) {
        this.orderingMethod = orderingMethod;
    }

    @Override
    public Group clone() throws CloneNotSupportedException {
        Group clone = (Group) super.clone();
        clone.setCommunicationMethod(this.communicationMethod);
        clone.setOrderingMethod(this.orderingMethod);
        return clone;
    }
}
