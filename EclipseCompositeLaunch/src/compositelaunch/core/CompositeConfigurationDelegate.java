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
 * @version 0.0.2 dated Jan 6, 2017
 */
public class CompositeConfigurationDelegate implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {

		try {

			List<ILaunchConfiguration> items = CompositeTools.getConfigurations(configuration);
			SubMonitor launchMonitor = SubMonitor.convert(monitor, configuration.getName(), items.size());

			for (ILaunchConfiguration item : items) {

				if (monitor.isCanceled())
					break;

				// launch next configuration from the list
				ILaunch configurationLaunch = item.launch(mode,launchMonitor.newChild(1));

				// adding all objects IDebugTarget
				for (IDebugTarget debugTarget : configurationLaunch.getDebugTargets())
					launch.addDebugTarget(debugTarget);

				// adding all process
				for (IProcess process : configurationLaunch.getProcesses())
					launch.addProcess(process);
			}

		} finally {
			monitor.done();
		}
	}

}