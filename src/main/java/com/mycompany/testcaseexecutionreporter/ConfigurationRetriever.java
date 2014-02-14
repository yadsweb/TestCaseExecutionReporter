package com.mycompany.testcaseexecutionreporter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author ystoyanov
 */
public class ConfigurationRetriever {

    static String JiraUsername;
    static String JiraPassword;
    static String JiraUrl;
    static String SubstepsReportFile;
    static String JiraProjectId;
    static String JiraBuildNumber;
    static String TestingEnvironment;
    static String JiraInitialCycleName;
    static String ConfigurationFile;
    static Properties propertiesContainer;
    static Logger log = Logger.getLogger(JiraGenericConnector.class.getName());

    ConfigurationRetriever() {
    }

    ConfigurationRetriever(String ConfigurationFile) {

        ConfigurationRetriever.ConfigurationFile = ConfigurationFile;

    }

    public void initializeProperties() throws IOException {
        
            propertiesContainer = read(ConfigurationFile);
            JiraUrl = propertiesContainer.getProperty("Jira.url");
            JiraUsername = propertiesContainer.getProperty("Jira.username");
            JiraPassword = propertiesContainer.getProperty("Jira.password");
            SubstepsReportFile = propertiesContainer.getProperty("Substeps.report");
            JiraProjectId = propertiesContainer.getProperty("Jira.projectId");
            JiraBuildNumber = propertiesContainer.getProperty("Jira.buildNumber");
            TestingEnvironment = propertiesContainer.getProperty("TestingEnvironment");
            JiraInitialCycleName = propertiesContainer.getProperty("Jira.initialCycleName");
        
    }
    
    public Properties read(String filePath) throws IOException {
        Properties properties = new Properties();
        FileInputStream in;
        try{
        in = new FileInputStream(filePath);
        properties.load(in);
        in.close();
        }
        catch(IOException ex){
        log.error("Configuration file not found! \n" );
        throw ex;
        }
        return properties;
    }

}
