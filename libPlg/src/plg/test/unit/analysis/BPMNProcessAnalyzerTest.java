package plg.test.unit.analysis;

import org.junit.Before;
import org.junit.Test;
import plg.analysis.BPMNProcessAnalyzer;
import plg.analysis.bpmeter.model.AnalysisResult;
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

    BPMNProcessAnalyzer processAnalyzer;
    File inFile;

    @Before
    public void setUp(){
        processAnalyzer = new BPMNProcessAnalyzer();
        inFile = new File(FILE_PATH + "\\" + MODEL_NAME);
    }

    @Test
    public void testAnalyzeFile(){
        AnalysisResult analysisResult = processAnalyzer.analyzeModel(inFile);
        Metric numActivities = analysisResult.findMetric(MetricType.NUM_ACTIVITIES.getName());
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

        List<AnalysisResult> analysisResults = processAnalyzer.analyzeModels(modelFiles);

        int numActivitiesModel1 = (int) analysisResults.get(0).findMetric(MetricType.NUM_ACTIVITIES.getName()).getValues().get(0).getValue();
        int numActivitiesModel2 = (int) analysisResults.get(1).findMetric(MetricType.NUM_ACTIVITIES.getName()).getValues().get(0).getValue();
        assert analysisResults!=null;
        assert numActivitiesModel1 == 42 || numActivitiesModel1 == 1;
        assert numActivitiesModel2 == 1 || numActivitiesModel2 == 42;
    }

    @Test
    public void testExportAnalysisResultsToCsv(){
        List<AnalysisResult> analysisResults = new LinkedList<>();
        AnalysisResult analysisResult = processAnalyzer.analyzeModel(inFile);
        analysisResults.add(analysisResult);

        processAnalyzer.exportAnalysisResultsToCsv(FILE_PATH, CSV_FILE_NAME, analysisResults);

        //Then
        File resultsFile = new File(FILE_PATH + "\\" + CSV_FILE_NAME);
        assert resultsFile.exists();

        //Cleanup
        resultsFile.delete();
    }


}

