package compositelaunch.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.swt.graphics.Image;

/**
 * The CompositeTools class contains several useful methods:
 * - void setConfigType(String configType)
 * - String getConfigType()
 * - String getConfigListName()
 * - String getInvalidType()
 * - boolean isConfigurationEmptyOrBanned(String name)
 * - String getTypeConfigurationByName(String name)
 * - Image getImageConfigurationByName(String name)
 * - ILaunchConfiguration getConfigurationByName(String name)
 * - List<String> getConfigurations(ILaunchConfiguration configuration)
 *
 * @author Sergey Iryupin
 * @version 0.0.3 dated Jan 8, 2017
 */
public class CompositeTools {

	private static String CONFIG_TYPE = null; //"Composite Launch";
	private static final String CONGIF_LIST = "ConfigurationList";
	private static final String NO_TYPE = "<Renamed or deleted>";

	// Set the configuration self type
	public static void setConfigType(String configType) {
		if (CONFIG_TYPE == null)
			CONFIG_TYPE = configType;
	}

	// Get the configuration self type
	public static String getConfigType() {
		return CONFIG_TYPE;
	}

	// Get the configuration list attribute name
	public static String getConfigListName() {
		return CONGIF_LIST;
	}

	// Get String constant NO_TYPE
	public static String getInvalidType() {
		return NO_TYPE;
	}

	// Get boolean status of configuration by its name
	public static boolean isConfigurationEmptyOrBanned(String name) {
		if (NO_TYPE.equals(getTypeConfigurationByName(name))) // if configuration was renamed or deleted
			return true;
		if (CONFIG_TYPE.equals(getTypeConfigurationByName(name))) // if .launch file was corrected
			return true;
		return false;
	}

	// Get type of configuration by its name
	public static String getTypeConfigurationByName(String name) {
		String type = NO_TYPE;
		try {
			for (ILaunchConfiguration config : DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations())
				if (name.equals(config.getName())) {
					type = config.getType().getName();
					break;
				}
		} catch (CoreException ex) {
			ex.printStackTrace();
		}
		return type;
	}

	// Get image of configuration by its name
	public static Image getImageConfigurationByName(String name) throws CoreException {
		ILaunchConfiguration configuration = getConfigurationByName(name);
		if (configuration != null)
			return DebugUITools.getImage(configuration.getType().getIdentifier());
		return null;
	}

	// Get ILaunchConfiguration by its name  
	public static ILaunchConfiguration getConfigurationByName(String name) {
		ILaunchConfiguration configuration = null;
		try {
			for (ILaunchConfiguration config : DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations())
				if (name.equals(config.getName())) {
					configuration = config;
					break;
				}
		} catch (CoreException ex) {
			ex.printStackTrace();
		}
		return configuration;
	}

	// Get a list of configurations from my configuration
	public static List<String> getConfigurations(ILaunchConfiguration configuration) {
		List<String> listConfiguration = new ArrayList<>();
		try {
			if (configuration.hasAttribute(CONGIF_LIST))
				for(String item : configuration.getAttribute(CONGIF_LIST, (List<String>)null))
					listConfiguration.add(item);
		} catch (CoreException ex) {
			ex.printStackTrace();
		}
		return listConfiguration;
	}

}