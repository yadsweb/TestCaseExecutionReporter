package com.mycompany.testcaseexecutionreporter;

/**
 *
 * @author ystoyanov
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class JiraGenericConnector extends ConfigurationRetriever {

    private final HttpClient client = new HttpClient();
    String credentials = ConfigurationRetriever.JiraUsername + ":" + ConfigurationRetriever.JiraPassword;
    String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

    public List<String> jiraGetMethod(String ResourcePath) throws HttpException, IOException {

        List<String> result = new ArrayList();
        GetMethod method = new GetMethod(JiraUrl + ResourcePath);
        method.setRequestHeader("Authorization", " Basic " + encodedCredentials);
        method.addRequestHeader("content-type", "application/json");
        try {
            int statusCode = client.executeMethod(method);

            String responseAsString = "";
            BufferedReader BR = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
            String readLine;
            while (((readLine = BR.readLine()) != null)) {
                responseAsString = responseAsString + readLine;
            }
            log.info("Get request to: " + JiraUrl + ResourcePath + " return status code: " + statusCode);
            log.info("Response body is: " + responseAsString);
            result.add(Integer.toString(statusCode));
            result.add(responseAsString);
        } catch (HttpException HttpEx) {
            log.error("Exception appear when trying to execute get request to: " + JiraUrl + ResourcePath);
            throw HttpEx;
        } catch (IOException IO) {
            log.error("Exception appear when trying to execute get request to: " + JiraUrl + ResourcePath);
            throw IO;
        }
        return result;
    }

    public List<String> jiraPostMethod(String ResourcePath, String RequestBody, String Type) throws IOException {
        System.out.println(credentials);
        List<String> result = new ArrayList();
        PostMethod method = new PostMethod(JiraUrl + ResourcePath);
        method.setRequestHeader("Authorization", " Basic " + encodedCredentials);
        method.addRequestHeader("content-type", "application/json");
        StringRequestEntity requestEntity;
        try {
            requestEntity = new StringRequestEntity(RequestBody, Type, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            log.error("Exception appear when trying create request entity with paramerers: RequestBody='" + RequestBody + "' and Type='" + Type + "'" + "\n" + ex);
            return result;
        }
        log.info("The post request contains this json data : " + requestEntity.getContent());

        method.setRequestEntity(requestEntity);

        try {
            int statusCode = client.executeMethod(method);
            String responseAsString = "";
            BufferedReader BR = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
            String readLine;
            while (((readLine = BR.readLine()) != null)) {
                responseAsString = responseAsString + readLine;
            }
            log.info("Post request to: " + JiraUrl + ResourcePath + " return status code: " + statusCode);
            log.info("Response body is: " + responseAsString);
            result.add(Integer.toString(statusCode));
            result.add(responseAsString);
        } catch (HttpException HttpEx) {
            log.error("Exception appear when trying to execute post request to: " + JiraUrl + ResourcePath);
            throw HttpEx;
        } catch (IOException IO) {
            log.error("Exception appear when trying to execute post request to: " + JiraUrl + ResourcePath);
            throw IO;
        }
        return result;

    }

    public List<String> jiraPutMethod(String ResourcePath, String RequestBody, String Type) throws IOException {

        List<String> result = new ArrayList();
        PutMethod method = new PutMethod(JiraUrl + ResourcePath);
        method.setRequestHeader("Authorization", " Basic " + encodedCredentials);
        method.addRequestHeader("content-type", "application/json");
        StringRequestEntity requestEntity;
        try {
            requestEntity = new StringRequestEntity(RequestBody, Type, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            log.error("Exception appear when trying create request entity with paramerers: RequestBody='" + RequestBody + "' and Type='" + Type + "'" + "\n" + ex);
            return result;
        }
        log.info("The put request contains this json data : " + requestEntity.getContent());

        method.setRequestEntity(requestEntity);

        try {
            int statusCode = client.executeMethod(method);
            String responseAsString = "";
            BufferedReader BR = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
            String readLine;
            while (((readLine = BR.readLine()) != null)) {
                responseAsString = responseAsString + readLine;
            }
            log.info("Put request to: " + JiraUrl + ResourcePath + " return status code: " + statusCode);
            log.info("Response body is: " + responseAsString);
            result.add(Integer.toString(statusCode));
            result.add(responseAsString);
        } catch (HttpException HttpEx) {
            log.error("Exception appear when trying to execute put request to: " + JiraUrl + ResourcePath);
            throw HttpEx;
        } catch (IOException IO) {
            log.error("Exception appear when trying to execute put request to: " + JiraUrl + ResourcePath);
            throw IO;
        }
        return result;

    }

}
