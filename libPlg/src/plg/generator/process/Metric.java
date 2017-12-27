package plg.generator.process;

public enum Metric {
    //Size
    NUM_NODES,
    NUM_ACTIVITIES,
    NUM_GATEWAYS,
    NUM_AND_GATES,
    NUM_XOR_GATES,
    //Density
    //DENSITY, //No support
    COEFFICIENT_OF_NETWORK_CONNECTIVITY,
    AVG_DEGREE_OF_CONNECTORS,
    //Partitionability
    SEQUENTIALITY,
    //Connector Interplay
    CONNECTOR_HETEROGENEITY,
    CONTROL_FLOW_COMPLEXITY,
    //Cyclicity
    NUMBER_OF_CYCLES,
    //Concurrency
    TOKEN_SPLIT;

}
