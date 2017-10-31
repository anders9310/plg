package plg.model;

public class MetricCalculator {
    Process p;

    public MetricCalculator(Process process) {
        this.p = process;
    }

    public double getCoefficientOfNetworkConnectivity(){
        double projectedNumArcs = p.getComponents().size() - p.getSequences().size() - p.getGateways().size() - 1 + (5.0/2.0)*p.getGateways().size() - p.getNumSkips();
        double numNodes = p.getComponents().size() - p.getSequences().size();
        if(!(projectedNumArcs>0)){
            return 0;
        }
        else if(!(numNodes>0)) {
            return Double.POSITIVE_INFINITY;
        }
        else {
            return projectedNumArcs / numNodes;
        }
    }
}
