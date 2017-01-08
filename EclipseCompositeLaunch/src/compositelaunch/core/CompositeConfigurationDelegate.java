package compositelaunch.core;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.IProcess;

/**
 * The CompositeConfigurationDelegate class for launching
 *
 * @author Sergey Iryupin
 * @version 0.0.3 dated Jan 8, 2017
 */
public class CompositeConfigurationDelegate implements ILaunchConfigurationDelegate {

	private final String LINK_ITSELF = " - link itself, launching banned.";
	private final String RENAMED_OR_DELETED = " - was renamed or deleted, launching banned.";
	private final String STARTED = " started.";
	
	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {

		String configType = configuration.getType().getName(); // get type of current configuration

		List<String> items = CompositeTools.getConfigurations(configuration);
		SubMonitor launchMonitor = SubMonitor.convert(monitor, configuration.getName(), items.size());

		for (String item : items) {

			if (monitor.isCanceled())
				break;

			String itemType = CompositeTools.getTypeConfigurationByName(item);

			if (itemType.equals(configType)) { // avoid looping
				System.out.println(item + LINK_ITSELF);
				continue;
			}

			if (itemType.equals(CompositeTools.getInvalidType())) { // configuration was renamed or deleted
				System.out.println(item + RENAMED_OR_DELETED);
				continue;
			}

			launchMonitor.setTaskName(item);

			ILaunchConfiguration config = CompositeTools.getConfigurationByName(item);

			// launch next configuration from the list
			ILaunch configurationLaunch = config.launch(mode, launchMonitor.newChild(1));

			// adding all objects IDebugTarget
			for (IDebugTarget debugTarget : configurationLaunch.getDebugTargets())
				launch.addDebugTarget(debugTarget);

			// adding all process
			for (IProcess process : configurationLaunch.getProcesses())
				launch.addProcess(process);

			System.out.println(item + STARTED);
		}
		monitor.done();
	}

}