package plg.generator.process;

public enum Metric {
    NUM_ACTIVITIES,
    NUM_GATEWAYS,
    NUM_AND_GATES,
    NUM_XOR_GATES,
    DIAMETER,
    COEFFICIENT_OF_NETWORK_CONNECTIVITY,
    CONTROL_FLOW_COMPLEXITY;

    public boolean isRatioBased(){
        switch (this){
            case COEFFICIENT_OF_NETWORK_CONNECTIVITY:
                return true;
            default:
                return false;
        }
    }
}
