package com.mycompany.testcaseexecutionreporter;

import java.io.IOException;
import org.json.JSONException;

public class App {

    public static void main(String[] args) throws JSONException, IOException, Exception {

        int cycleid;
        String[][] executionData;

        System.setProperty("jsse.enableSNIExtension", "false");
        System.out.println("Usage with parameters:");
        System.out.println("First parameter is the path and file name of the configuration file (for example the Environment.properties file)");
        System.out.println("Second parameter is the build number, if not provided the one from configuration file will be used");

        String ConfigurationFile;
        if (args[0].length() > 0 && args[0].contains(".properties")) {
            ConfigurationFile = args[0];
        } else {
            throw new Exception("Error: Invalid properties file.");
        }

        ConfigurationRetriever CR = new ConfigurationRetriever(ConfigurationFile);

        CR.initializeProperties();

        try {
            if (args[1].length() > 0) {
                ConfigurationRetriever.JiraBuildNumber = args[1];
            }
        } catch (Exception e) {
            System.out.println("No build number provided as parameter to main method so the one from properties file will be used.");
        }

        SubstepsReportAnalyzer SRA = new SubstepsReportAnalyzer();
        JiraFunctionality jiraFunc = new JiraFunctionality();

        executionData = SRA.getTestCaseDetails();

        cycleid = jiraFunc.createTestCycle();

        jiraFunc.addTestsToTestCycle(SupportingFunctionality.testCasesKeysExtractor(executionData), cycleid);

        jiraFunc.updateTestsExecutionStatuses(jiraFunc.retriveExecutionIds(executionData, cycleid));

    }
}
