package plg.test.unit.analysis;

import org.junit.Before;
import org.junit.Test;
import plg.analysis.BPMNProcessAnalyzer;
import plg.analysis.model.bpmeter.AnalysisResult;
import plg.analysis.model.bpmeter.Metric;
import plg.analysis.model.bpmeter.MetricType;

import java.io.File;

public class BPMNProcessAnalyzerTest {
    BPMNProcessAnalyzer processAnalyzer;
    File inFile;

    @Before
    public void setUp(){
        processAnalyzer = new BPMNProcessAnalyzer();
        inFile = new File("D:\\Users\\Anders\\Documents\\MasterThesis\\PlgModels\\bpmnmodel_0.bpmn");
    }

    @Test
    public void testAnalyzeFile(){
        AnalysisResult analysisResult = processAnalyzer.analyzeFile(inFile);
        Metric numActivities = analysisResult.findMetric(MetricType.NUM_ACTIVITIES.getName());
        double numActivitiesValue = numActivities.getValues().get(0).getValue();
        assert numActivitiesValue == 30;
    }
}

