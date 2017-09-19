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
import java.io.IOException;
import java.util.List;

public class BPMeterWrapper {

    public String analyzeFiles(List<File> inFiles) {
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

            HttpEntity responseEntity = response.getEntity();
            responseString = EntityUtils.toString(responseEntity, "UTF-8");

        } catch (ClientProtocolException e) {
            Logger.instance().error("Unable to make connection: " + e.toString());
            responseString = "";
        } catch (Exception e){
            Logger.instance().error(e.toString());
            responseString = "";
        }
        finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException e) {
                Logger.instance().error("Could not close FileInputStream");
            }
        }
        return responseString;
    }

    /**
     * Adapted from javatutorial.net
     */
    public String analyzeFile(File inFile) {
        String responseString;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(inFile);
            DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

            // server back-end URL
            HttpPost httppost = new HttpPost("http://benchflow.inf.usi.ch/bpmeter/api/");
            MultipartEntity entity = new MultipartEntity();
            // set the file input stream and file name as arguments
            entity.addPart("models", new InputStreamBody(fis, inFile.getName()));
            httppost.setEntity(entity);
            // execute the request
            HttpResponse response = httpclient.execute(httppost);

            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity responseEntity = response.getEntity();
            responseString = EntityUtils.toString(responseEntity, "UTF-8");

        } catch (ClientProtocolException e) {
            Logger.instance().error("Unable to make connection: " + e.toString());
            responseString = "";
        } catch (Exception e){
            Logger.instance().error(e.toString());
            responseString = "";
        }
        finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException e) {
                Logger.instance().error("Could not close FileInputStream");
            }
        }
        return removeOuterBrackets(responseString);
    }

    private String removeOuterBrackets(String stringWithOuterBrackets){
        String str = stringWithOuterBrackets;
        return str.substring(1).substring(0,str.length()-2);
    }
}
