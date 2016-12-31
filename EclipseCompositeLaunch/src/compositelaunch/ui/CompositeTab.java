package compositelaunch.ui;

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
import org.eclipse.ui.plugin.AbstractUIPlugin;

import compositelaunch.activator.Activator;

/**
 * The CompositeTab class for tab with configuration's list for launch
 *
 * @version 0.0.3 dated Dec 31, 2016
 */
public class CompositeTab extends AbstractLaunchConfigurationTab {

	private static final String NAME_OF_TAB = "Group";
	private static final String ICON_TAB = "icons/launch.png";

	private Button addConfiguration;
	private Button deleteConfiguration;

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
				System.out.println("You push button Add...");
			}
		});

		deleteConfiguration = new Button(composite, SWT.NONE);
		deleteConfiguration.setText("Delete");
		deleteConfiguration.setLayoutData(gridData);

	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub
	}

	/*
	 * Initialize the components
	 */
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		// TODO Auto-generated method stub
	}

	/*
	 * Processing pressing the Apply button
	 */
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub
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

}