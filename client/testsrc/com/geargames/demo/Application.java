package com.geargames.demo;

/**
 * This class contain ...
 *
 * @author andrey.barakov@exactprosystems.com
 *
 */

//import javax.swing.UIManager;

//import java.io.File;
//
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;

// EditorMain, MatrixApplication, MatrixApp
public class Application {
	
//	private static final Logger logger = Logger.getLogger( GeneratorMain.class );
	
	public Application()
	{
		ApplicationFrame appFrame = new ApplicationFrame();
		appFrame.setVisible(true);
	 }

	 /**Main method*/
	public static void main(String[] args)
	{
//		try
//		{
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		new Application();
		
//		if(!readConfig())
//		{
//			System.out.println( "Error - cannot read properties file." );
//			return ;
//		}		
//		
//		try
//		{
//			AbstractTemplateMatrixProcessor amp = 
//				TemplateMatrixProcessorFactoryImpl.getInstance().getMatrixProcessor();
//			
//			if(null != amp)
//			{
//				amp.process();
//			}
//			else
//			{
//				System.out.println( "Error: cannot obtain matrix processor for setup conditions. Check settings in [config.xml] file" );
//			}
//		}
//		catch( Exception e )
//		{
//			System.out.println( "Error:" );
//			e.printStackTrace();
//		}

	}
	
//	private static boolean readConfig()
//	{
//		try
//		{
//			String baseDir = (((System.getProperty("basedir") == null) ? "." : System.getProperty("basedir")));
//			String propertyFile = baseDir + File.separator + "testdata" + File.separator + "log.properties";
//			
//			if(!(new File(propertyFile)).exists())
//			{
//				System.out.println(String.format( "File [%s] does not exists.", propertyFile ));
//				return false;
//			}
//			
//			PropertyConfigurator.configure(propertyFile);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			return false;			
//		}
//		return true;		
//	}

}
