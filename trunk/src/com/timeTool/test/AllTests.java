package com.timeTool.test;

import com.timeTool.actions.AdjustTimeKeyHandlerTest;
import com.timeTool.ResourceAutomationTest;
import com.timeTool.ui.CommonDialogTest;

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
		junit.textui.TestRunner.run(suite());
	}

	/**
    * Run the main test suite
    * @return Test returns the test
	*/
	public static Test suite()
	{
		final TestSuite suite = new TestSuite("Test for timeTool");
		suite.addTestSuite(CommonDialogTest.class);
		suite.addTestSuite(ResourceAutomationTest.class);
        suite.addTestSuite(TaskModelTest.class);
        suite.addTestSuite(AdjustTimeKeyHandlerTest.class);
        suite.addTestSuite(TimeToolTest.class);
		suite.addTestSuite(TaskTest.class);
		suite.addTestSuite(ExportOptionsTest.class);
		suite.addTestSuite(TaskModelIteratorTest.class); 
		return suite;
	}

}
