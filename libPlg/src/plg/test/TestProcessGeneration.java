package plg.test;

import plg.analysis.BPMNProcessAnalyzer;
import plg.analysis.bpmeter.model.AnalysisResult;
import plg.generator.process.*;
import plg.io.exporter.BPMNExporter;
import plg.model.Process;
import plg.utils.Logger;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class TestProcessGeneration {

    private static final int NUM_GENERATED_MODELS = 500;
    public static final String basePath = "C:\\Users\\ander_000\\IdeaProjects\\plg\\libPlg\\src\\plg\\test\\modelfiles";
    public static final String resultsBasePath = "C:\\Users\\ander_000\\IdeaProjects\\plg\\libPlg\\src\\plg\\test\\analysisresults";
    public static final String csvFileName = "testmodels_results.csv";
    private static BPMNProcessAnalyzer processAnalyzer = new BPMNProcessAnalyzer();
    private static final String PROJECT_ROOT_FOLDER = System.getProperty("user.dir");


    public static void main(String[] args) throws Exception {
        BPMNExporter e = new BPMNExporter();
        String baseFileName = "bpmnmodel_";
        String bpmnExtension = ".bpmn";
        List<AnalysisResult> analysisResults = new LinkedList<>();
        for(int i = 0; i< NUM_GENERATED_MODELS; i++){
            Process p =new Process("test" );
            ObligationsProcessGenerator.randomizeProcess(p, new ParameterRandomizationConfiguration(10, 5));

            String modelNumber = String.valueOf(i);
            String path = basePath + "\\" + baseFileName + modelNumber + bpmnExtension;
            e.exportModel(p, path);

            File inFile = new File(path);
            AnalysisResult analysisResult = processAnalyzer.analyzeModel(inFile);
            if(analysisResult!=null){
                analysisResults.add(analysisResult);
            }else{
                Logger.instance().info("Analysis returned null-result for " + path + ". Not exporting results for this model.");
            }

        }

        processAnalyzer.exportAnalysisResultsToCsv(resultsBasePath, csvFileName, analysisResults);
    }
}
