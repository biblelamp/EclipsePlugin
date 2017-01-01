package compositelaunch.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import compositelaunch.activator.Activator;
import compositelaunch.data.LaunchConfiguration;

/**
 * The CompositeTab class for tab with list of configurations for launch,
 * overrides the general functionality of the tab
 *
 * @author Sergey Iryupin
 * @version 0.0.4 dated Jan 1, 2017
 */
public class CompositeTab extends AbstractLaunchConfigurationTab {

	private static final String NAME_OF_TAB = "Group";
	private static final String ICON_TAB = "icons/launch.png";

	private LaunchConfiguration launchConfiguration = new LaunchConfiguration();

	private Button addConfiguration;
	private Button deleteConfiguration;

	Menu menuChoiceConfiguration;

	/*
	 * Add the desired components on the tab
	 */
	@Override
	public void createControl(Composite parent) {

		Composite composite = new Group(parent, SWT.BORDER);
		setControl(composite);
		composite.setLayout(new GridLayout(2, false));

		GridData gridData = new GridData();
		gridData.widthHint = 80;

		addConfiguration = new Button(composite, SWT.NONE);
		addConfiguration.setText("Add...");
		addConfiguration.setLayoutData(gridData);
		addConfiguration.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				choiceConfigurationToAdd();
				menuChoiceConfiguration.setVisible(true);
			}
		});

		deleteConfiguration = new Button(composite, SWT.NONE);
		deleteConfiguration.setText("Delete");
		deleteConfiguration.setLayoutData(gridData);

	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	/*
	 * Initialize the components
	 */
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		launchConfiguration.setConfiguration(configuration);
		System.out.println("initializeFrom");
	}

	/*
	 * Processing pressing the Apply button
	 */
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		System.out.println("performApply");
	}

	// Name of tab
	@Override
	public String getName() {
		return NAME_OF_TAB;
	}

	// Image for tab
	@Override
	public Image getImage() {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, ICON_TAB).createImage();
	}

	// Choice configuration for adding in table
	void choiceConfigurationToAdd() {

		if (menuChoiceConfiguration != null)
			menuChoiceConfiguration.dispose();
		menuChoiceConfiguration = new Menu(addConfiguration);
		try {
			for (ILaunchConfiguration config : DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations()) {
				String configType = config.getType().getName();

				MenuItem menuItem = null;
				for (MenuItem item : menuChoiceConfiguration.getItems()) {
					if (configType.equals(item.getText())) {
						menuItem = item;
						break;
					}
				}

				if (menuItem == null) {
					menuItem = new MenuItem(menuChoiceConfiguration, SWT.CASCADE);
					menuItem.setText(configType);

					Menu subMenuItem = new Menu(menuItem);
					menuItem.setMenu(subMenuItem);
				}

				MenuItem menuItem2 = new MenuItem(menuItem.getMenu(), SWT.NONE);
				menuItem2.setText(config.getName());
				menuItem2.setData(config);
			}
		} catch (CoreException ex) {
			ex.printStackTrace();
		}

		if (menuChoiceConfiguration.getItemCount() == 0) {
			MenuItem menuItem = new MenuItem(menuChoiceConfiguration, SWT.NONE);
			menuItem.setText("No configurations that can be added");
		}
	}

}