package compositelaunch.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * The CompositeTools class contains several useful methods:
 * - boolean isConfigurationUnknown(String name)
 * - String getConfigListName()
 * - String getTypeConfigurationByName(String name)
 * - ILaunchConfiguration getConfigurationByName(String name)
 * - List<ILaunchConfiguration> getConfigurations(ILaunchConfiguration configuration)
 *
 * @author Sergey Iryupin
 * @version 0.0.2 dated Jan 7, 2017
 */
public class CompositeTools {

	private static final String CONGIF_LIST = "ConfigurationList";
	private static final String NO_TYPE = "<Renamed or deleted>";

	// Get the configuration list attribute name
	public static String getConfigListName() {
		return CONGIF_LIST;
	}

	// Get boolean status of configuration by its name
	public static boolean isConfigurationUnknown(String name) {
		return NO_TYPE.equals(getTypeConfigurationByName(name));
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

	// Get ILaunchConfiguration by its name  
	public static ILaunchConfiguration getConfigurationByName(String name) {
		ILaunchConfiguration configuration = null;
		try {
			for (ILaunchConfiguration config : DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations())
				if (name.equals(config.getName())) {
					configuration  = config;
					break;
				}
		} catch (CoreException ex) {
			ex.printStackTrace();
		}
		return configuration;
	}

	// Get a list of configurations from my configuration
	public static List<ILaunchConfiguration> getConfigurations(ILaunchConfiguration configuration) {
		List<ILaunchConfiguration> listConfiguration = new ArrayList<>();
		try {
			if (configuration.hasAttribute(CONGIF_LIST)) {
				for(String item : configuration.getAttribute(CONGIF_LIST, (List<String>)null))
					listConfiguration.add(getConfigurationByName(item));
			}
		} catch (CoreException ex) {
			ex.printStackTrace();
		}
		return listConfiguration;
	}

}