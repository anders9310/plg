package plg.model;

public class PlaceholderComponent extends FlowObject {

    /**
     * This method creates a new component and tries to register it to the
     * given process owner. This constructor is also responsible for the
     * generation of the component identifier.
     *
     * @param owner the process owner of this component
     */
    public PlaceholderComponent(Process owner) {
        super(owner);
    }

    @Override
    public String getComponentName() {
        return "Unknown Component";
    }
    public int getComponentId() {
        return componentId;
    }
}
