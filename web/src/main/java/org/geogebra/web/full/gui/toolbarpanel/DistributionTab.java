package org.geogebra.web.full.gui.toolbarpanel;

import org.geogebra.web.full.gui.toolbarpanel.tableview.StickyProbabilityTable;
import org.geogebra.web.full.gui.view.probcalculator.DistributionPanelSuite;
import org.geogebra.web.full.gui.view.probcalculator.ProbabilityCalculatorViewW;

public class DistributionTab extends ToolbarPanel.ToolbarTab {

	private final ToolbarPanel toolbarPanel;
	private DistributionPanelSuite distrPanel;

	/**
	 * Constructor
	 * @param toolbarPanel - parent toolbar panel
	 */
	public DistributionTab(ToolbarPanel toolbarPanel, StickyProbabilityTable table) {
		super(toolbarPanel);
		this.toolbarPanel = toolbarPanel;
		createContent(toolbarPanel, table);
	}

	private void createContent(ToolbarPanel toolbarPanel, StickyProbabilityTable table) {
		ProbabilityCalculatorViewW view = (ProbabilityCalculatorViewW) toolbarPanel.getApp()
				.getGuiManager().getProbabilityCalculator();
		distrPanel = new DistributionPanelSuite(view, toolbarPanel.getApp().getLocalization());
		distrPanel.setLabels();
		view.setDistributionPanel(distrPanel);
		view.setTable(new ProbabilityTableAdapter(table,
				toolbarPanel.getApp(), view));
		distrPanel.updateGUI(); // make sure the correct interval is selected
		add(distrPanel);
	}

	@Override
	protected void onActive() {
		// to do fill
	}

	@Override
	public void setLabels() {
		distrPanel.setLabels();
	}

	@Override
	public void open() {
		// to do fill
	}

	@Override
	public void close() {
		toolbarPanel.close(false);
	}
}
