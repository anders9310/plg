package plg.analysis.bpmeter;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import plg.analysis.bpmeter.model.RawAnalysisResult;
import plg.utils.Logger;

import java.util.List;

public class BPMeterDataModelHandler {

    private static Gson gson = new Gson();

    public static List<RawAnalysisResult> convertJsonResultToDataModel(String analysisResultsAsJson){
        String json = analysisResultsAsJson;

        List<RawAnalysisResult> result;
        try{
            result = gson.fromJson(analysisResultsAsJson, new TypeToken<List<RawAnalysisResult>>(){}.getType());
        } catch(JsonParseException e){
            Logger.instance().error("Error while deserializing JSON: " + "\n" +
                    "Error message" + e.toString());
            result = null;
        }

        return result;
    }
}
