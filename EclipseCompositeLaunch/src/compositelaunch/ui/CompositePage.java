package compositelaunch.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import compositelaunch.core.CompositeTools;

/**
 * The CompositePage class for CompositeTab with buttons, table
 * and several useful methods 
 *
 * @author Sergey Iryupin
 * @version 0.0.2 dated Jan 6, 2017
 */
public class CompositePage extends Composite {

	private final String ADD_BUTTON = "Add...";
	private final String ADD_BUTTON_TIP = "Click to choice configuration for adding to list";
	private final String DEL_BUTTON = "Delete";
	private final String DEL_BUTTON_TIP = "Click to delete configuration from the list";
	private final int NUM_OF_COLUMNS = 2; // columns for GridLayout
	private final int WITH_BUTTON = 80;
	private final String[] TABLE_TITLES = {"Name", "Type"};
	private final String NO_CONFIGURATION = "No configurations to select/add";

	private Button addConfiguration;
	private Button deleteConfiguration;
	private Table table;
	private Menu menuChoiceConfiguration;

	private String typeConfiguration;
	private List<String> configs;

	private ITableChangedListener changedListener;

	/*
	 * Constructor: buttons + table
	 */
	public CompositePage(Composite parent) {
		super(parent, SWT.MULTI);

		this.setLayout(new GridLayout(NUM_OF_COLUMNS, false));
		GridData gridButton = new GridData();
		gridButton.widthHint = WITH_BUTTON;

		addConfiguration = new Button(this, SWT.NONE); // add button
		addConfiguration.setText(ADD_BUTTON);
		addConfiguration.setToolTipText(ADD_BUTTON_TIP);
		addConfiguration.setLayoutData(gridButton);
		addConfiguration.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				choiceConfigurationToAdd();
				menuChoiceConfiguration.setVisible(true);
			}
		});

		deleteConfiguration = new Button(this, SWT.NONE); // delete button
		deleteConfiguration.setText(DEL_BUTTON);
		deleteConfiguration.setToolTipText(DEL_BUTTON_TIP);
		deleteConfiguration.setLayoutData(gridButton);
		deleteConfiguration.setEnabled(false);
		deleteConfiguration.addSelectionListener(new SelectionAdapter() { // delete listener
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionIndex() > -1) {
					table.remove(table.getSelectionIndex());
					deleteConfiguration.setEnabled(false);
					changedConfig();
				}
			}
		});

		// like createSeparator() from AbstractLaunchConfigurationTab class
		new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		GridData gridTable = new GridData(GridData.FILL_BOTH);
		gridTable.horizontalSpan = 2;

		table = new Table(this, SWT.BORDER); // table with configuration list
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(gridTable);
		for (int i = 0; i < TABLE_TITLES.length; i++) {
		      TableColumn column = new TableColumn(table, SWT.NONE);
		      column.setText(TABLE_TITLES[i]);
		      column.pack();
		}
		table.addSelectionListener(new SelectionAdapter() { // select listener
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteConfiguration.setEnabled(true);
			}
		});
	}

	/*
	 * Set my changedListener for table
	 */
	public void setChangedListener(ITableChangedListener changedListener) {
		this.changedListener = changedListener;
	}

	/*
	 * Send signal about changes for my changedListener
	 */
	public void changedConfig() {
		if (changedListener != null) changedListener.handler();
	}

	/*
	 * Expand the configuration list to the table
	 */
	public void setConfigList(ILaunchConfiguration configuration) {
		try {
			table.removeAll();
			typeConfiguration = configuration.getType().getName();
			if (configuration.hasAttribute(CompositeTools.getConfigListName())) {
				configs = configuration.getAttribute(CompositeTools.getConfigListName(), (List<String>)null);
				for(String item : configs) {
					TableItem tableItem = new TableItem(table, SWT.NULL);
					tableItem.setText(item);
					tableItem.setText(1, CompositeTools.getTypeOfConfiguration(item));
					table.getColumn(0).pack();
					table.getColumn(1).pack();
				}
			}
		} catch (CoreException ex) {
			ex.printStackTrace();
		}
	}
	
	/*
	 * Prepare configuration list for save
	 */
	public List<String> getConfigList() {
		configs = new ArrayList<String>();
		for (TableItem item : table.getItems()) configs.add(item.getText());
		return configs;
	}

	/*
	 * Make menu for selection/addition configuration to table
	 */
	private void choiceConfigurationToAdd() {

		if (menuChoiceConfiguration != null)
			menuChoiceConfiguration.dispose();

		menuChoiceConfiguration = new Menu(addConfiguration);
		try {
			for (ILaunchConfiguration config : DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations()) {
				String configType = config.getType().getName();

				if (typeConfiguration.equals(configType)) continue; // skip self configuration type

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

				MenuItem menuItemConfig = new MenuItem(menuItem.getMenu(), SWT.NONE);
				menuItemConfig.setText(config.getName());
				menuItemConfig.setData(configType);
				menuItemConfig.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						MenuItem item = MenuItem.class.cast(e.getSource());
						TableItem tableItem = new TableItem(table, SWT.NULL);
						tableItem.setText(item.getText());
						table.getColumn(0).pack();
						tableItem.setText(1, (String) item.getData());
						table.getColumn(1).pack();
						changedConfig();
					}
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});
			}
		} catch (CoreException ex) {
			ex.printStackTrace();
		}

		if (menuChoiceConfiguration.getItemCount() == 0) {
			MenuItem menuItem = new MenuItem(menuChoiceConfiguration, SWT.NONE);
			menuItem.setText(NO_CONFIGURATION);
		}
	}

}