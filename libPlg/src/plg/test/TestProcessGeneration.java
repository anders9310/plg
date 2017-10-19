package plg.test;

import plg.analysis.BPMNProcessAnalyzer;
import plg.analysis.bpmeter.model.AnalysisResult;
import plg.generator.process.*;
import plg.io.exporter.BPMNExporter;
import plg.model.Process;
import plg.utils.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TestProcessGeneration {

    private static final int NUM_GENERATED_MODELS = 100;
    private static final String PROJECT_ROOT_FOLDER = System.getProperty("user.dir");
    private static final String PROJECT_TEST_FOLDER = PROJECT_ROOT_FOLDER + "\\src\\plg\\test";
    public static final String MODEL_FILES_FOLDER = PROJECT_TEST_FOLDER + "\\modelfiles";
    public static final String RESULT_FILE_FOLDER = PROJECT_TEST_FOLDER + "\\analysisresults";
    public static final String RESULT_FILE_NAME = "testmodels_results.csv";
    private static BPMNProcessAnalyzer processAnalyzer = new BPMNProcessAnalyzer();

    public static void main(String[] args) throws Exception {
        generateAnalyzeExportResults();
        //analyzeExportResults();
        cleanUpModelFiles();
    }

    private static void generateAnalyzeExportResults(){
        List<File> exportedFiles = generateProcessModelFiles();
        List<AnalysisResult> analysisResults = processAnalyzer.analyzeModels(exportedFiles);
        processAnalyzer.exportAnalysisResultsToCsv(RESULT_FILE_FOLDER, RESULT_FILE_NAME, analysisResults);
    }

    private static List<File> generateProcessModelFiles(){
        BPMNExporter e = new BPMNExporter();
        List<File> exportedFiles = new LinkedList<>();
        String baseFileName = "bpmnmodel_";
        String bpmnExtension = ".bpmn";
        for(int i = 0; i< NUM_GENERATED_MODELS; i++){
            Process p =new Process("test" );
            ObligationsProcessGenerator.randomizeProcess(p, new ParameterRandomizationConfiguration(0, 0, 5, 5));

            String modelNumber = String.valueOf(i);
            String path = MODEL_FILES_FOLDER + "\\" + baseFileName + modelNumber + bpmnExtension;
            e.exportModel(p, path);
            File processFile = new File(path);
            exportedFiles.add(processFile);
        }
        return exportedFiles;
    }

    private static void analyzeExportResults(){
        List<File> generatedFiles = Arrays.asList(new File(MODEL_FILES_FOLDER).listFiles());
        List<AnalysisResult> analysisResults = processAnalyzer.analyzeModels(generatedFiles);
        processAnalyzer.exportAnalysisResultsToCsv(RESULT_FILE_FOLDER, RESULT_FILE_NAME, analysisResults);
    }

    private static void cleanUpModelFiles(){
        File[] modelFiles = new File(MODEL_FILES_FOLDER).listFiles();
        if(modelFiles!=null){
            for(File f : Arrays.asList(modelFiles)){
                if(!f.delete()){
                    Logger.instance().error("Could not delete file " + f.getName());
                }
            }
        }
    }
}
