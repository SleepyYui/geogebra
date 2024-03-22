package org.geogebra.web.full.gui.toolbar.mow.toolbox.components;

import static org.geogebra.common.euclidian.EuclidianConstants.MODE_SHAPE_RECTANGLE;

import java.util.List;
import java.util.function.Consumer;

import org.geogebra.web.html5.gui.GPopupPanel;
import org.geogebra.web.html5.main.AppW;
import org.gwtproject.user.client.ui.FlowPanel;
import org.gwtproject.user.client.ui.Widget;

public class CategoryPopup extends GPopupPanel {
	private Consumer<Integer> updateParentCallback;
	private IconButton lastSelectedButton;
	private FlowPanel contentPanel;

	/**
	 * Constructor
	 * @param app - application
	 * @param tools - list of tools
	 * @param updateParentCallback - callback to update anchor
	 */
	public CategoryPopup(AppW app, List<Integer> tools, Consumer<Integer> updateParentCallback) {
		super(app.getAppletFrame(), app);
		setAutoHideEnabled(true);
		this.updateParentCallback = updateParentCallback;

		addStyleName("categoryPopup");
		buildBaseGui(tools);
	}

	public void addContent(Widget widget) {
		contentPanel.add(widget);
	}

	private void buildBaseGui(List<Integer> tools) {
		contentPanel = new FlowPanel();
		for (Integer mode : tools) {
			IconButton button = new IconButton(mode, (AppW) app);
			if (tools.get(0) == mode) {
				app.setMode(mode);
				updateButtonSelection(button);
			}
			button.addFastClickHandler(source -> {
				app.setMode(mode);
				updateParentCallback.accept(mode);
				updateButtonSelection(button);
				hide();
			});
			contentPanel.add(button);
		}
		add(contentPanel);
	}

	private void updateButtonSelection(IconButton newSelectedButton) {
		if (lastSelectedButton != null) {
			lastSelectedButton.deactivate();
		}

		lastSelectedButton = newSelectedButton;
		lastSelectedButton.setActive(true,
				((AppW) app).getGeoGebraElement().getDarkColor(((AppW) app).getFrameElement()));
	}

	public Integer getLastSelectedMode() {
		return lastSelectedButton == null ? -1 : lastSelectedButton.getMode();
	}
}
