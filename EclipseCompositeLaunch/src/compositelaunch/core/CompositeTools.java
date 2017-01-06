package compositelaunch.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * The CompositeTools class contains several useful methods:
 * - String getConfigListName()
 * - String getTypeOfConfiguration(String name)
 * - ILaunchConfiguration getConfiguration(String name)
 * - List<ILaunchConfiguration> getConfigurations(ILaunchConfiguration configuration)
 *
 * @author Sergey Iryupin
 * @version 0.0.1 dated Jan 6, 2017
 */
public class CompositeTools {

	private static final String CONGIF_LIST = "ConfigurationList";
	private static List<String> configs;

	// Get the configuration list attribute name
	public static String getConfigListName() {
		return CONGIF_LIST;
	}

	// Get type of configuration by the name
	public static String getTypeOfConfiguration(String name) {
		String type = "<unknown>";
		try {
			for (ILaunchConfiguration config : DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations()) {
				if (name.equals(config.getName())) {
					type = config.getType().getName();
					break;
				}
			}
		} catch (CoreException ex) {
			ex.printStackTrace();
		}
		return type;
	}
	
	// Get ILaunchConfiguration by its name  
	public static ILaunchConfiguration getConfiguration(String name) {
		ILaunchConfiguration configuration = null;
		try {
			for (ILaunchConfiguration config : DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations()) {
				if (name.equals(config.getName())) {
					configuration  = config;
				}
			}
		} catch (CoreException ex) {
			ex.printStackTrace();
		}
		return configuration;
	}

	// Get a list of configuration names
	public static List<ILaunchConfiguration> getConfigurations(ILaunchConfiguration configuration) {
		List<ILaunchConfiguration> listConfiguration = new ArrayList<>();
		try {
			if (configuration.hasAttribute(CONGIF_LIST)) {
				configs = configuration.getAttribute(CONGIF_LIST, (List<String>)null);
				for(String item : configs) {
					listConfiguration.add(getConfiguration(item));
				}
			}
		} catch (CoreException ex) {
			ex.printStackTrace();
		}
		return listConfiguration;
	}

}