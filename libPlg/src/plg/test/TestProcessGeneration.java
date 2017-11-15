package plg.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import plg.analysis.BPMNProcessAnalyzer;
import plg.analysis.bpmeter.model.AnalysisResult;
import plg.generator.process.*;
import plg.io.exporter.BPMNExporter;
import plg.model.Process;
import plg.utils.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TestProcessGeneration {

    private static final int NUM_GENERATED_MODELS = 2;
    private static final String PROJECT_ROOT_FOLDER = System.getProperty("user.dir");
    private static final String PROJECT_TEST_FOLDER = PROJECT_ROOT_FOLDER + "\\src\\plg\\test";
    public static final String MODEL_FILES_FOLDER = PROJECT_TEST_FOLDER + "\\modelfiles";
    public static final String RESULT_FILE_FOLDER = PROJECT_TEST_FOLDER + "\\analysisresults";
    public static final String GENERATION_RESULTS_FOLDER = PROJECT_TEST_FOLDER + "\\generationresults";
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
        List<AnalysisResult> analysisResults = processAnalyzer.analyzeModels(exportedFiles);
        processAnalyzer.exportAnalysisResultsToCsv(RESULT_FILE_FOLDER, RESULT_FILE_NAME, analysisResults);
    }

    private static List<File> generateProcessModelFilesAndWriteResultsToCsv(){
        BPMNExporter e = new BPMNExporter();
        List<File> exportedFiles = new LinkedList<>();
        String baseFileName = "bpmnmodel_";
        String bpmnExtension = ".bpmn";
        for(int i = 0; i< NUM_GENERATED_MODELS; i++){
            Process p =new Process("test" );
            ObligationsProcessGenerator generator = new ObligationsProcessGenerator(p, new ParameterRandomizationConfiguration(p, 15, 0, 0, 0, 0, 0));
            generator.randomizeProcess();

            generationResults.add(generator.getGenerationResults());

            String modelNumber = String.valueOf(i);
            String path = MODEL_FILES_FOLDER + "\\" + baseFileName + modelNumber + bpmnExtension;
            e.exportModel(p, path);
            File processFile = new File(path);
            exportedFiles.add(processFile);
        }
        writeGenerationResultsToCsv(GENERATION_RESULTS_FOLDER, GENERATION_RESULTS_FILE_NAME);
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

    private static void writeGenerationResultsToCsv(String filePath, String fileName){
        final String NEW_LINE_SEPARATOR = "\n";
        final String CURRENT_VALUE = "Current value";
        final String TARGET_VALUE = "Target value";
        final String MEAN = "Mean";
        final Object [] FILE_HEADER = {"Process number", "Metric name", CURRENT_VALUE, TARGET_VALUE, MEAN};

        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

        try{
            fileWriter = new FileWriter(filePath + "\\" + fileName);
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord(FILE_HEADER);

            for(Map<String, Map<String, Double>> result : generationResults){
                for(String metricName : result.keySet()){
                    List<String> generationResultList = new LinkedList<>();
                    generationResultList.add(String.valueOf(generationResults.indexOf(result)));
                    generationResultList.add(metricName);
                    generationResultList.add(result.get(metricName).get(CURRENT_VALUE).toString());
                    generationResultList.add(result.get(metricName).get(TARGET_VALUE).toString());
                    generationResultList.add(result.get(metricName).get(MEAN).toString());
                    csvFilePrinter.printRecord(generationResultList);
                }
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
