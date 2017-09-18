package plg.test.unit.analysis;

import org.junit.Before;
import org.junit.Test;
import plg.analysis.BPMeterWrapper;

import java.io.File;

public class BPMeterWrapperTest {
    BPMeterWrapper bpMeter;
    File inFile;

    @Before
    public void setUp(){
        bpMeter = new BPMeterWrapper();
        inFile = new File("D:\\Users\\Anders\\Documents\\MasterThesis\\PlgModels\\bpmnmodel_0.bpmn");
    }

    @Test
    public void testFileAnalysis(){
        String resultJson = bpMeter.analyzeFile(inFile);
        System.out.println(resultJson);
    }
}
