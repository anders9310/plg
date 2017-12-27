package plg.test.unit.analysis;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import plg.analysis.BPMNProcessAnalyzer;
import plg.analysis.bpmeter.model.RawAnalysisResult;
import plg.analysis.bpmeter.model.Metric;
import plg.analysis.bpmeter.model.MetricType;
import plg.utils.Logger;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class BPMNProcessAnalyzerTest {
    private static final String FILE_PATH = "C:\\Users\\ander_000\\IdeaProjects\\plg\\libPlg\\src\\plg\\test\\unit\\analysis";
    private static final String MODEL_NAME = "bpmnmodel0.bpmn";
    private static final String CSV_FILE_NAME = "bpmnmodel_analysisresults.csv";

    private BPMNProcessAnalyzer processAnalyzer;
    private File inFile;

    @Before
    public void setUp(){
        processAnalyzer = new BPMNProcessAnalyzer();
        inFile = new File(FILE_PATH + "\\" + MODEL_NAME);
    }

    @Test
    public void testAnalyzeFile(){
        RawAnalysisResult rawAnalysisResult = processAnalyzer.analyzeModel(inFile);
        Metric numActivities = rawAnalysisResult.findMetric(MetricType.NUM_ACTIVITIES.getName());
        double numActivitiesValue = numActivities.getValues().get(0).getValue();
        assert numActivitiesValue == 42;
    }

    @Test
    public void testAnalyzeFiles(){
        final String modelFileBaseName = "bpmnmodel";
        final String modelFileType = ".bpmn";
        List<File> modelFiles = new LinkedList<>();
        for(int i = 0; i<2; i++){
            File file = new File(FILE_PATH + "\\" + modelFileBaseName + i + modelFileType);
            modelFiles.add(file);
        }

        List<RawAnalysisResult> rawAnalysisResult = processAnalyzer.analyzeModels(modelFiles);

        int numActivitiesModel1 = (int) rawAnalysisResult.get(0).findMetric(MetricType.NUM_ACTIVITIES.getName()).getValues().get(0).getValue();
        int numActivitiesModel2 = (int) rawAnalysisResult.get(1).findMetric(MetricType.NUM_ACTIVITIES.getName()).getValues().get(0).getValue();
        assert rawAnalysisResult !=null;
        assert numActivitiesModel1 == 42 || numActivitiesModel1 == 1;
        assert numActivitiesModel2 == 1 || numActivitiesModel2 == 42;
    }

    @Test
    public void testExportAnalysisResultsToCsv(){
        List<RawAnalysisResult> rawAnalysisResultList = new LinkedList<>();
        RawAnalysisResult rawAnalysisResult = processAnalyzer.analyzeModel(inFile);
        rawAnalysisResultList.add(rawAnalysisResult);

        processAnalyzer.exportAnalysisResultsToCsv(FILE_PATH, CSV_FILE_NAME, rawAnalysisResultList);

        //Then
        File resultsFile = new File(FILE_PATH + "\\" + CSV_FILE_NAME);
        assert resultsFile.exists();

        //Cleanup
        resultsFile.delete();
    }

    @Ignore //Takes a fairly long time
    @Test
    public void testAnalyzeManyFiles(){
        final String modelFileBaseName = "bpmnmodel";
        final String modelFileType = ".bpmn";
        List<File> modelFiles = new LinkedList<>();
        File file = new File(FILE_PATH + "\\" + modelFileBaseName + 0 + modelFileType);
        int numFiles = 500;
        for(int i = 0; i<numFiles; i++){
            modelFiles.add(file);
        }

        long startMillis = System.currentTimeMillis();
        List<RawAnalysisResult> rawAnalysisResult = processAnalyzer.analyzeModels(modelFiles);

        long durationSecs = (System.currentTimeMillis()-startMillis)/1000;
        Logger.instance().debug("Analysis of " + numFiles + " files took " + durationSecs + " seconds.");

        assert rawAnalysisResult.size() == numFiles;
    }


}

