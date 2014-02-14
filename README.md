TestCaseExecutionReporter
==================

This tool use simple file named "Environment.properties" which contain all relevant properties to connect to zephyr for jira api and send results from automated test execution with substeps.

In Jira it actualy first create new test cycle then add tests to test cycle and finally update the status of each test to relevant status taken from substeps report file.

The file or file path is send as parameter to the main method.


It contains: 
==================

Substeps.report = (the path to substeps report file 'report_data.json')

TestingEnvironment = (simple string which will be used as environment for the test cycle)

Jira.url = (jira url)

Jira.username = (jira username)

Jira.password = (jira password)

Jira.projectId = (jira project id which is the id of project for which we want to create test cycle and add tests and execution results to it)

Jira.buildNumber = (build number which will be reported to the test cycle and used as prefix to the test cycle name)

Jira.initialCycleName = (the actual test cycle name)
