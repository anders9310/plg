package plg.analysis;

import plg.analysis.bpmeter.BPMeterDataModelHandler;
import plg.analysis.bpmeter.BPMeterWrapper;
import plg.analysis.bpmeter.model.RawAnalysisResult;
import plg.utils.Logger;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class BPMNProcessAnalyzer {
    CSVManager csvManager;
    BPMeterWrapper bpMeter = new BPMeterWrapper();

    public RawAnalysisResult analyzeModel(File bpmnModelFile){
        List<File> files = new LinkedList<>();
        files.add(bpmnModelFile);

        List<RawAnalysisResult> rawAnalysisResult = analyzeModels(files);
        return rawAnalysisResult.get(0);
    }

    public List<RawAnalysisResult> analyzeModels(List<File> bpmnModelFiles){
        Logger.instance().info("Starting process analysis for list of files");

        String analysisResultJson = bpMeter.analyzeFiles(bpmnModelFiles);
        List<RawAnalysisResult> result = BPMeterDataModelHandler.convertJsonResultToDataModel(analysisResultJson);

        Logger.instance().info("Process analysis complete for list of files");
        return result;
    }

    public void exportAnalysisResultsToCsv(String filePath, String fileName, List<RawAnalysisResult> rawAnalysisResult){
        Logger.instance().info("Starting process analysis result exportation: " + fileName);
        csvManager = new CSVManager();
        csvManager.writeCsv(filePath, fileName, rawAnalysisResult);
        Logger.instance().info("Process analysis result exportation finished: " + fileName);
    }
}
