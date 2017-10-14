package plg.analysis.bpmeter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import plg.utils.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class BPMeterWrapper {

    private final int MAX_FILES_PER_REQUEST = 50;

    public String analyzeFiles(List<File> inFiles) {
        if(inFiles.isEmpty()){
            return "[]";
        }

        String responseString = "[";

        List<File> currentFiles = new LinkedList<>();
        int i = 0;
        while(i<inFiles.size()){
            currentFiles.add(inFiles.get(i));
            if(i % MAX_FILES_PER_REQUEST == 0 && i>0 || i==inFiles.size()-1){
                String callResponse = callApi(currentFiles);
                responseString = responseString.concat(removeOuterBrackets(callResponse));

                if(i!=inFiles.size()-1){
                    responseString = responseString.concat(",");
                }
                currentFiles = new LinkedList<>();
            }
            i++;
        }

        return responseString.concat("]");
    }

    /**
     * Adapted from javatutorial.net
     */
    private String callApi(List<File> inFiles){
        String responseString;

        FileInputStream fis = null;
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

            // server back-end URL
            HttpPost httppost = new HttpPost("http://benchflow.inf.usi.ch/bpmeter/api/");
            MultipartEntity entity = new MultipartEntity();
            for(File inFile : inFiles){
                fis = new FileInputStream(inFile);
                entity.addPart("models", new InputStreamBody(fis, inFile.getName()));
            }
            httppost.setEntity(entity);

            HttpResponse response = httpclient.execute(httppost);

            int statusCode = response.getStatusLine().getStatusCode();
            Logger.instance().debug("Status code for BPMeter analysis request: " + statusCode);
            if(statusCode!=200){
                throw new RuntimeException("Status code for BPMeter analysis request: " + statusCode);
            }

            HttpEntity responseEntity = response.getEntity();
            responseString = EntityUtils.toString(responseEntity, "UTF-8");

        } catch (ClientProtocolException e) {
            Logger.instance().error("Unable to make connection: " + e.toString());
            responseString = "";
        } catch (FileNotFoundException e) {
            Logger.instance().error(e.toString());
            responseString = "";
        } catch (IOException e) {
            Logger.instance().error(e.toString());
            responseString = "";
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException e) {
                Logger.instance().error("Could not close FileInputStream");
                responseString = "";
            }
        }
        return responseString;
    }

    private String removeOuterBrackets(String stringWithOuterBrackets){
        String str = stringWithOuterBrackets;
        return str.substring(1).substring(0,str.length()-2);
    }
}
