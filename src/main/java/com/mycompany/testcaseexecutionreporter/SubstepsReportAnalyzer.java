package com.mycompany.testcaseexecutionreporter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ystoyanov
 */
public class SubstepsReportAnalyzer extends ConfigurationRetriever {

    public String[][] getTestCaseDetails() throws JSONException, IOException {

        FileInputStream FileIS;
        InputStreamReader InputSR;
        BufferedReader BR;
        String[][] result;

        try {
            FileIS = new FileInputStream(SubstepsReportFile);
            InputSR = new InputStreamReader(FileIS);
            BR = new BufferedReader(InputSR);
            StringBuilder SB = new StringBuilder();
            String textinLine;

            while (true) {
                textinLine = BR.readLine();
                if (textinLine == null) {
                    break;
                }
                SB.append(textinLine);
            }

            log.info("Removing '" + SB.toString().substring(0, SB.toString().indexOf("{")) + "' from the information readed from substeps report file.");
            String JsonData = SB.toString().replace(SB.toString().substring(0, SB.toString().indexOf("{")), "");
            log.info("Reading test data, from the information readed from substeps report file.");
            try {
                JSONObject jsonObject = new JSONObject(JsonData);
                JSONArray jsonArrayFeatures = jsonObject.getJSONArray("children").getJSONObject(0).getJSONArray("children");
                result = new String[jsonArrayFeatures.length()][2];
                for (int counter = 0; counter < jsonArrayFeatures.length(); counter++) {
                    JSONArray temp = jsonArrayFeatures.getJSONObject(counter).getJSONArray("children");
                    JSONObject testRawData = temp.getJSONObject(0);
                    String issue_title = testRawData.getJSONObject("data").getString("title");
                    String issue_status_img = testRawData.getJSONObject("data").getString("icon");
                    String issue_status = issue_status_img.substring(issue_status_img.indexOf("/") + 1, issue_status_img.indexOf("."));

                    log.info("Found test case with title: '" + issue_title + "' and status '" + issue_status + "'");
                    log.info("Adding issue key: '" + issue_title.substring(issue_title.lastIndexOf("_") + 1) + "' to temporary list!");
                    result[counter][0] = issue_title.substring(issue_title.lastIndexOf("_") + 1);

                    switch (issue_status) {
                        case "PASSED": {
                            log.info("Adding integer issue status: '1' to temporary list!");
                            result[counter][1] = "1";
                            break;
                        }
                        case "FAILED": {
                            log.info("Adding integer issue status: '2' to temporary list!");
                            result[counter][1] = "2";
                            break;
                        }
                        default: {
                            log.info("Adding integer issue status: '3' to temporary list!");
                            result[counter][1] = "3";
                            break;
                        }
                    }
                }
            } catch (JSONException ex) {
                log.error("Error when trying to create json object from data\n" + JsonData + " \n");
                throw ex;
            }

            FileIS.close();
            InputSR.close();
            BR.close();

        } catch (IOException ex) {
            log.error("Exception appear when trying to read the substep report file. \n");
            throw ex;
        }
        return result;
    }

}
