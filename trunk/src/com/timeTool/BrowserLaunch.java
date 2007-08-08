package com.timeTool;
/////////////////////////////////////////////////////////
//  Bare Bones Browser Launch                          //
//  Version 1.5                                        //
//  December 10, 2005                                  //
//  Supports: Mac OS X, GNU/Linux, Unix, Windows XP    //
//  Example Usage:                                     //
//     String url = "http://www.centerkey.com/";       //
//     BareBonesBrowserLaunch.openURL(url);            //
//  Public Domain Software -- Free to Use as You Like  //
/////////////////////////////////////////////////////////

import java.lang.reflect.Method;

public class BrowserLaunch 
{

   private static final String BROWSER_ERROR1 = "BrowserError1";
   private static final String BROWSER_ERROR2 = "BrowserError2";
   private final ResourceAutomation resources;


	public BrowserLaunch(ResourceAutomation resources) {
		this.resources = resources;
	}


	public void openURL(String url)
   {
	   try
	   {
		   
		   String osName = System.getProperty("os.name");
		   if (osName.startsWith("Mac OS")) 
		   {
			   Class fileMgr = Class.forName("com.apple.eio.FileManager");
			   Method openURL = fileMgr.getDeclaredMethod("openURL",
			   new Class[] {String.class});
			   openURL.invoke(null, new Object[] {url});
		   }
		   else if (osName.startsWith("Windows")) {
			   Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
		   } else { //assume Unix or Linux
			   String[] browsers = {
				   "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
			   String browser = null;
			   for (int count = 0; count < browsers.length && browser == null; count++) {
				   if (Runtime.getRuntime().exec(
					   new String[]{"which", browsers[count]}).waitFor() == 0) {
					   browser = browsers[count];
				   }
			   }
			   if (browser == null) {
				   throw new Exception(resources.getResourceString(BROWSER_ERROR1));
			   } else {
				   Runtime.getRuntime().exec(new String[]{browser, url});
			   }
		   }
	   }
	   catch (Exception e)
	   {
		   ErrorHandler.showError(null, e, resources);
	   }
   	}
}
