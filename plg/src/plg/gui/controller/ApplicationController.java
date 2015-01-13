package plg.gui.controller;

import java.io.IOException;

import plg.gui.config.ConfigurationSet;
import plg.gui.config.UIConfiguration;
import plg.gui.window.MainFrame;
import plg.gui.window.MainWindow;

/**
 * This class represents the application controller, and is in charge of
 * managing the entire application workflow.
 *
 * @author Andrea Burattin
 */
public class ApplicationController {

	private static ApplicationController controller = new ApplicationController();
	
	private MainFrame mainFrame;
	private MainWindow mainWindow;
	private ConfigurationSet configuration;
	
	/**
	 * This method returns the available instance of the application controller.
	 * 
	 * @return an application controller
	 */
	public static ApplicationController instance() {
		return controller;
	}
	
	/**
	 * Private class constructor. Access the application controller through the
	 * {@link #instance()} method.
	 */
	private ApplicationController() {
		configuration = UIConfiguration.master();
		mainWindow = new MainWindow(this);
		mainFrame = new MainFrame(this);
	}
	
	/**
	 * This method returns the {@link MainFrame} built.
	 * 
	 * @return the main frame available
	 */
	public MainFrame getMainFrame() {
		return mainFrame;
	}
	
	/**
	 * This method returns the {@link MainWindow} built.
	 * 
	 * @return the main window available
	 */
	public MainWindow getMainWindow() {
		return mainWindow;
	}
	
	/**
	 * This method closes the application controller (to be invoked just before
	 * exiting the application)
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		UIConfiguration.save();
	}
	
	/**
	 * This method returns the general configuration
	 * 
	 * @return the general configuration
	 */
	public ConfigurationSet getGeneralConfiguration() {
		return configuration;
	}
	
	/**
	 * This method returns the configuration associated to the provided root
	 * 
	 * @param root the root of the configuration
	 * @return the configuration associated to the provided root
	 */
	public ConfigurationSet getConfiguration(String root) {
		return configuration.getChild(root);
	}
}