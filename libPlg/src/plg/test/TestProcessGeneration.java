package plg.test;

import plg.generator.process.*;
import plg.io.exporter.BPMNExporter;
import plg.model.Process;

public class TestProcessGeneration {

    private static final int NUM_GENERATED_MODELS = 500;

    public static void main(String[] args) throws Exception {
        BPMNExporter e = new BPMNExporter();
        String basePath = "D:\\Users\\Anders\\Documents\\MasterThesis\\PlgModels";
        String baseFileName = "bpmnmodel_";
        String bpmnExtension = ".bpmn";
        for(int i = 0; i< NUM_GENERATED_MODELS; i++){
            Process p =new Process("test" );
            ObligationsProcessGenerator.randomizeProcess(p, new ParameterRandomizationConfiguration(10, 5));

            String modelNumber = String.valueOf(i);
            String path = basePath + "\\" + baseFileName + modelNumber + bpmnExtension;
            e.exportModel(p, path);
        }

    }
}
