package com.timeTool.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Run all the tests
 */
public class AllTests 
{
	/**
    * Main entry point
    * @param  args  arguments
	*/
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(AllTests.suite());
	}

	/**
    * Run the main test suite
    * @return Test returns the test
	*/
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for timeTool");
		//$JUnit-BEGIN$
		suite.addTestSuite(TimeToolTest.class);
		suite.addTestSuite(TaskTest.class);
		suite.addTestSuite(TestFileIO.class);
		suite.addTestSuite(ExportOptionsTest.class);
		suite.addTestSuite(TaskModelIteratorTest.class); 
		//$JUnit-END$
		return suite;
	}

}
