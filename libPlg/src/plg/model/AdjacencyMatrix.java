package plg.model;

import plg.model.sequence.Sequence;

import java.util.List;

public class AdjacencyMatrix {
    private List<FlowObject> nodes;
    private List<Sequence> vertices;

    private boolean[][] adjacencyMatrix;

    public AdjacencyMatrix(List<FlowObject> nodes, List<Sequence> vertices){
        this.nodes = nodes;
        this.vertices = vertices;
        computeAdjacencyMatrix();
    }

    private void computeAdjacencyMatrix() {
        adjacencyMatrix = new boolean[nodes.size()][nodes.size()];
        for(Sequence seq : vertices){
            int sourceIndex = nodes.indexOf(seq.getSource());
            int sinkIndex = nodes.indexOf(seq.getSink());
            addEdge(sourceIndex, sinkIndex);
        }
    }
    private void addEdge(int i, int j) {
        adjacencyMatrix[i][j] = true;
    }
    private void removeEdge(int i, int j) {
        adjacencyMatrix[i][j] = false;
    }

    public boolean[][] getAdjacencyMatrix(){
        return adjacencyMatrix;
    }
}
