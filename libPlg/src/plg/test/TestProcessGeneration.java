package plg.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import plg.analysis.BPMNProcessAnalyzer;
import plg.analysis.bpmeter.model.RawAnalysisResult;
import plg.generator.process.*;
import plg.io.exporter.BPMNExporter;
import plg.model.Process;
import plg.utils.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TestProcessGeneration {

    private static final int NUM_GENERATED_MODELS = 100;
    private static final String PROJECT_ROOT_FOLDER = System.getProperty("user.dir");
    private static final String PROJECT_TEST_FOLDER = PROJECT_ROOT_FOLDER + "\\src\\plg\\test";
    public static final String MODEL_FILES_FOLDER = PROJECT_TEST_FOLDER + "\\modelfiles";
    public static final String RESULT_FILE_FOLDER = PROJECT_TEST_FOLDER + "\\analysisresults";
    public static final String GENERATION_RESULTS_FOLDER = PROJECT_TEST_FOLDER + "\\generationresults";
    public static final String ANALYSE_RESPONSE = PROJECT_TEST_FOLDER + "\\experimentresults";
    public static final String RESULT_FILE_NAME = "testmodels_results.csv";
    public static final String GENERATION_RESULTS_FILE_NAME = "generation_results.csv";
    private static BPMNProcessAnalyzer processAnalyzer = new BPMNProcessAnalyzer();
    private static List<Map<String, Map<String, Double>>> generationResults = new LinkedList<>();

    public static void main(String[] args) throws Exception {
        generateProcessModelFilesAndWriteResultsToCsv();
        //generateAnalyzeExportResults();
        //analyzeExportResults();
        cleanUpModelFiles();
    }

    private static void generateAnalyzeExportResults(){
        List<File> exportedFiles = generateProcessModelFilesAndWriteResultsToCsv();
        List<RawAnalysisResult> rawAnalysisResult = processAnalyzer.analyzeModels(exportedFiles);
        processAnalyzer.exportAnalysisResultsToCsv(RESULT_FILE_FOLDER, RESULT_FILE_NAME, rawAnalysisResult);
    }

    private static List<File> generateProcessModelFilesAndWriteResultsToCsv(){
        BPMNExporter e = new BPMNExporter();
        List<File> exportedFiles = new LinkedList<>();
        String baseFileName = "bpmnmodel_";
        String bpmnExtension = ".bpmn";
        long startTime = System.currentTimeMillis();
        for(int i = 0; i< NUM_GENERATED_MODELS; i++){
            Process p =new Process("test" );

            Map<Metric, Double> inputs = createInputMetrics();
            DynamicProcessGenerator generator = new DynamicProcessGenerator(p, inputs);
            generator.randomizeProcess();

            generationResults.add(generator.getGenerationResults());

            String modelNumber = String.valueOf(i);
            String path = MODEL_FILES_FOLDER + "\\" + baseFileName + modelNumber + bpmnExtension;
            e.exportModel(p, path);
            File processFile = new File(path);
            exportedFiles.add(processFile);
            Logger.instance().info("Generated " + baseFileName + modelNumber + bpmnExtension);
        }
        long endTime = System.currentTimeMillis();
        Logger.instance().info("EXECUTION TIME: " + (endTime-startTime)/1000.0 + " s");
        Logger.instance().info("AVG TIME PER MODEL: " + (endTime-startTime)/1000.0/NUM_GENERATED_MODELS + " s");
        writeGenerationResultsToCsv(GENERATION_RESULTS_FOLDER, GENERATION_RESULTS_FILE_NAME);
        return exportedFiles;
    }

    private static List<Process> generateProcessModels(int numberOfModels){
        List<Process> generatedProcesses = new LinkedList<>();

        for(int i = 0; i< numberOfModels; i++){
            Process p = new Process("test" );

            Map<Metric, Double> inputs = new HashMap<>();
            inputs.put(Metric.NUM_NODES, 40.0);
            inputs.put(Metric.CONTROL_FLOW_COMPLEXITY, 10.0);

            DynamicProcessGenerator generator = new DynamicProcessGenerator(p, inputs);
            generator.randomizeProcess();
        }

        return generatedProcesses;
    }

    private static Map<Metric, Double> createInputMetrics(){
        Map<Metric, Double> inputs = new HashMap<>();
        //Test 1
        //inputs.put(Metric.NUM_AND_GATES, 10.0);
        //inputs.put(Metric.NUM_XOR_GATES, 10.0);
        //inputs.put(Metric.NUM_ACTIVITIES, 30.0);

        //Test 2
        //inputs.put(Metric.NUM_NODES, 40.0);
        //inputs.put(Metric.CONTROL_FLOW_COMPLEXITY, 10.0);
        //inputs.put(Metric.CONNECTOR_HETEROGENEITY, 0.75);

        //Test 3
        //inputs.put(Metric.NUM_NODES, 50.0);

        //Test 4: One ratio-based metric
        //inputs.put(Metric.NUM_NODES, 40.0);
        //inputs.put(Metric.CONNECTOR_HETEROGENEITY, 0.75);

        //Test 5: ratio-based metrics
        //inputs.put(Metric.NUM_NODES, 30.0);
        //inputs.put(Metric.CONNECTOR_HETEROGENEITY, 0.75);
        //inputs.put(Metric.AVG_DEGREE_OF_CONNECTORS, 1.25);
        //inputs.put(Metric.SEQUENTIALITY, 0.46);

        //Time complexity tests
        //Test: Time - low size
        //inputs.put(Metric.NUM_NODES, 10.0);
        //Test: Time - highsize
        //inputs.put(Metric.NUM_NODES, 100.0);
        //Test: Time - simple metrics
        //inputs.put(Metric.NUM_NODES, 50.0);
        //inputs.put(Metric.NUM_ACTIVITIES, 35.0);
        //Test: Time - complex metrics
        inputs.put(Metric.NUM_NODES, 32.0);
        //inputs.put(Metric.NUMBER_OF_CYCLES, 5.0);

        //inputs.put(Metric.CONNECTOR_HETEROGENEITY, 0.75);
        //inputs.put(Metric.AVG_DEGREE_OF_CONNECTORS, 1.25);
        //inputs.put(Metric.SEQUENTIALITY, 0.46);

        //inputs.put(Metric.NUM_NODES, 60.0);
        //inputs.put(Metric.NUM_AND_GATES, 10.0);
        //inputs.put(Metric.NUM_XOR_GATES, 10.0);
        //inputs.put(Metric.NUM_ACTIVITIES, 30.0);
        //inputs.put(Metric.NUMBER_OF_CYCLES, 2.0);
        //inputs.put(Metric.NUM_GATEWAYS, 10.0);
        //inputs.put(Metric.CONTROL_FLOW_COMPLEXITY, 15.0);

        //inputs.put(Metric.CONNECTOR_HETEROGENEITY, 0.28);
        //inputs.put(Metric.AVG_DEGREE_OF_CONNECTORS, 1.25);
        //inputs.put(Metric.TOKEN_SPLIT, 1.82);
        //inputs.put(Metric.SEQUENTIALITY, 0.46);

        //inputs.put(Metric.NUM_NODES, 40.0);
        //inputs.put(Metric.CONTROL_FLOW_COMPLEXITY, 10.0);
        //inputs.put(Metric.CONNECTOR_HETEROGENEITY, 0.75);

        //inputs.put(Metric.NUMBER_OF_CYCLES, 3.0);
        //inputs.put(Metric.NUM_ACTIVITIES, 20.0);

        //inputs.put(Metric.COEFFICIENT_OF_NETWORK_CONNECTIVITY, 0.96);
        //inputs.put(Metric.DENSITY, 0.09);
        return inputs;
    }

    private static void analyzeExportResults(){
        List<File> generatedFiles = Arrays.asList(new File(MODEL_FILES_FOLDER).listFiles());
        List<RawAnalysisResult> rawAnalysisResult = processAnalyzer.analyzeModels(generatedFiles);
        processAnalyzer.exportAnalysisResultsToCsv(RESULT_FILE_FOLDER, RESULT_FILE_NAME, rawAnalysisResult);
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

    private static void writeGenerationResultsToCsv(String filePath, String fileName){
        final String NEW_LINE_SEPARATOR = "\n";
        final String CURRENT_VALUE = "Current value";
        final String TARGET_VALUE = "Target value";
        final String DIFF = "Diff";
        final Object [] FILE_HEADER = {"Process number", "Metric name", CURRENT_VALUE, TARGET_VALUE, DIFF};

        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

        try{
            fileWriter = new FileWriter(filePath + "\\" + fileName);
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord(FILE_HEADER);

            int modelNumber = 0;
            for(Map<String, Map<String, Double>> result : generationResults){
                for(String metricName : result.keySet()){
                    List<String> generationResultList = new LinkedList<>();
                    generationResultList.add(String.valueOf(modelNumber));
                    generationResultList.add(metricName);
                    generationResultList.add(result.get(metricName).get(CURRENT_VALUE).toString());
                    generationResultList.add(result.get(metricName).get(TARGET_VALUE).toString());
                    generationResultList.add(result.get(metricName).get(DIFF).toString());
                    csvFilePrinter.printRecord(generationResultList);
                }
                modelNumber++;
            }
        }catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        }finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
                e.printStackTrace();
            }
        }

    }
}
