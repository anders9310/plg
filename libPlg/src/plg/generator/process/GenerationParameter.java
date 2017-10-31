package plg.generator.process;

public enum GenerationParameter {
    NUM_ACTIVITIES,
    NUM_GATEWAYS,
    NUM_AND_GATES,
    NUM_XOR_GATES,
    COEFFICIENT_OF_NETWORK_CONNECTIVITY;

    public boolean isRatioBased(){
        switch (this){
            case COEFFICIENT_OF_NETWORK_CONNECTIVITY:
                return true;
            default:
                return false;
        }
    }
}
