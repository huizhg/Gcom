package se.umu.cs.gcom.GCom;

import se.umu.cs.gcom.Communication.Communication;
import se.umu.cs.gcom.MessageOrdering.Ordering;

import java.io.Serializable;

public class Group implements Serializable {
    private static final long serialVersionUID = -1443744741548249800L;
    private final String groupName;
    private Communication communicationModule;
    private Ordering orderingModule;

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public Communication getCommunicationModule() {
        return communicationModule;
    }

    public Ordering getOrderingModule() {
        return orderingModule;
    }

    public void setCommunicationModule(Communication communicationModule) {
        this.communicationModule = communicationModule;
    }

    public void setOrderingModule(Ordering orderingModule) {
        this.orderingModule = orderingModule;
    }

    @Override
    public Group clone() throws CloneNotSupportedException {
        Group clone = (Group) super.clone();
        clone.setCommunicationModule(this.communicationModule);
        clone.setOrderingModule(this.orderingModule);
        return clone;
    }
}
