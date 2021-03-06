package plg.test.unit.analysis;

import org.junit.Before;
import org.junit.Test;
import plg.analysis.bpmeter.BPMeterWrapper;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class BPMeterWrapperTest {
    BPMeterWrapper bpMeter;
    File inFile;
    private static final String FILE_PATH = "C:\\Users\\ander_000\\IdeaProjects\\plg\\libPlg\\src\\plg\\test\\unit\\analysis\\bpmnmodel0.bpmn";

    @Before
    public void setUp(){
        bpMeter = new BPMeterWrapper();
        inFile = new File(FILE_PATH);
    }

    @Test
    public void testFileAnalysis(){
        List<File> fileList = new LinkedList<>();
        fileList.add(inFile);

        String resultJson = bpMeter.analyzeFiles(fileList);

        assert !resultJson.equals("");
    }

    @Test
    public void testMultipleFilesAnalysis(){
        List<File> fileList = new LinkedList<>();
        int numFiles = 5;
        for(int i=0; i<numFiles; i++){
            fileList.add(inFile);
        }

        String resultJson = bpMeter.analyzeFiles(fileList);

        assertCorrectFormat(resultJson);
    }

    @Test
    public void testMoreThanMaxFilesAnalysis(){
        List<File> fileList = new LinkedList<>();
        int numFiles = 150;
        for(int i=0; i<numFiles; i++){
            fileList.add(inFile);
        }

        String resultJson = bpMeter.analyzeFiles(fileList);

        assertCorrectFormat(resultJson);
    }

    @Test
    public void testNoFilesAnalysis(){
        List<File> fileList = new LinkedList<>();

        String resultJson = bpMeter.analyzeFiles(fileList);

        assert resultJson.equals("[]");
    }

    private void assertCorrectFormat(String resultJson){
        assert !resultJson.equals("");
        assert !resultJson.equals("[]");
        assert resultJson.charAt(0)=='[';
        assert resultJson.charAt(resultJson.length()-1)==']';
        assert resultJson.charAt(resultJson.length()-2)!=',';
        assert resultJson.length()>2;
    }
}
