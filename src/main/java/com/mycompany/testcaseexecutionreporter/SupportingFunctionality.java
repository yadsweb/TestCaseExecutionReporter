package com.mycompany.testcaseexecutionreporter;

/**
 *
 * @author ystoyanov
 */
public class SupportingFunctionality extends ConfigurationRetriever {

    public static String testCasesKeysExtractor(String[][] data) {

        String result = "[";
        for (String[] data1 : data) {
            result = result + " \"" + data1[0] + "\",";
        }

        result = result.substring(0, result.length() - 1) + "]";
        return result;
    }

}
