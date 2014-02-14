package com.mycompany.testcaseexecutionreporter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ystoyanov
 */
public class JiraFunctionality extends JiraGenericConnector {

    public int createTestCycle() throws IOException, JSONException, Exception {

        Date date = new Date();
        DateFormat dateFormatDescription = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        DateFormat dateFormatStartDate = new SimpleDateFormat("dd/MMM/yyyy");
        String startDate = dateFormatStartDate.format(date);
        String description = "Ran on: " + dateFormatDescription.format(date);

        String requestBody = "{\"name\":\"" + JiraInitialCycleName
                + "\"," + "\"build\":\"" + JiraBuildNumber + "\"," + "\"environment\":\"" + TestingEnvironment + "\","
                + "\"description\":\"" + description + "\"," + "\"startDate\":\""
                + startDate + "\"," + "\"projectId\":\"" + JiraProjectId + "\","
                + "\"versionId\":\"-1\"}";

        List<String> FullResponse = jiraPostMethod("/rest/zephyr/latest/cycle", requestBody, "application/json");

        if (FullResponse.get(0).equals("200")) {
            try {
                JSONObject jsonResponse = new JSONObject(FullResponse.get(1));
                int newlyCreatedTestCycleID = Integer.parseInt(jsonResponse.getString("id"));
                log.info("The id of newly created cycle is: " + newlyCreatedTestCycleID);
                return newlyCreatedTestCycleID;
            } catch (JSONException ex) {
                log.error("Error when trying to extract the newly created cycle's id from the response body of successful request!");
                throw ex;
            }
        } else {
            log.error("The request for creating test cycle faled, returned status code is: " + FullResponse.get(0));
            log.error("Request body is:\n" + FullResponse.get(1));
            throw new Exception("The request for creating test cycle returned status code different then 200");
        }
    }

    public void addTestsToTestCycle(String keys, int cycle_id) throws IOException, JSONException, Exception {

        String requestBody = "{\"issues\":" + keys + ","
                + "\"versionId\":\"-1\","
                + "\"cycleId\":\"" + cycle_id + "\","
                + "\"projectId\":\"" + JiraProjectId + "\","
                + "\"method\":\"1\"}";
        List<String> FullResponse = jiraPostMethod("/rest/zephyr/latest/execution/addTestsToCycle", requestBody, "application/json");

        if (FullResponse.get(0).equals("200")) {

            log.info("Test with keys : " + keys + " added successful to test cycle with id: " + cycle_id);

        } else {
            log.error("The request for adding test cases: " + keys + " to cycle with id " + cycle_id + " returned code: " + FullResponse.get(0));
            log.error("Request body is:\n" + FullResponse.get(1));
            throw new Exception("The request for adding test to test cycle returned status code different then 200");
        }

    }

    public String[][] retriveExecutionIds(String[][] data, int cycleID) throws IOException, JSONException, Exception {

        String[][] result = new String[data.length][3];

        List<String> FullResponse = jiraGetMethod("/rest/zephyr/latest/execution?cycleId=" + cycleID);

        if (FullResponse.get(0).equals("200")) {

            log.info("The request for extracting issue executions from cycle with id " + cycleID + " returned status code 200 success.");

            JSONObject responseBody = new JSONObject(FullResponse.get(1));
            JSONArray allExecutions = responseBody.getJSONArray("executions");
            for (int counter1 = 0; counter1 < data.length; counter1++) {
                for (int counter2 = 0; counter2 < allExecutions.length(); counter2++) {
                    if (data[counter1][0].equals(allExecutions.getJSONObject(counter2).getString("issueKey"))) {

                        result[counter1][0] = data[counter1][0];
                        result[counter1][1] = data[counter1][1];
                        result[counter1][2] = allExecutions.getJSONObject(counter2).getString("id");
                        log.info("Executin id " + allExecutions.getJSONObject(counter2).getString("id") + " for issue key " + data[counter1][0] + " with status " + data[counter1][1] + " added successful to the result.");

                        break;
                    }
                }
            }

        } else {
            log.error("The request for extracting test cases executing id's from cycle with id " + cycleID + " returned code: " + FullResponse.get(0));
            log.error("Request body is:\n" + FullResponse.get(1));
            throw new Exception("The request for extracting test cases executing id's returned status code different then 200");
        }

        return result;
    }

    public void updateTestsExecutionStatuses(String[][] data) {

        for (String[] tempData : data) {
            try {

                List<String> FullResponse = jiraPutMethod("/rest/zephyr/latest/execution/" + tempData[2] + "/execute", "{\"status\":\"" + tempData[1] + "\"}", "application/json");

                if (FullResponse.get(0).equals("200")) {

                    log.info("The request for updating execution status of test with key " + tempData[0] + " returned status code 200 success, execution status is set to \"" + tempData[1] + "\".");
                } else {
                    log.error("The request for updating execution status of test with key " + tempData[0] + " returned code: " + FullResponse.get(0));
                    log.error("Request body is:\n" + FullResponse.get(1));
                    throw new Exception("The request for updating execution status of test with key " + tempData[0] + " returned status code different then 200");
                }
            } catch (Exception e) {
                log.error("Error when trying to update the execution of test with key " + tempData[0] +", execution id "+tempData[2]+" and status "+tempData[1]+"\n "+e);
            }
        }
    }
}
