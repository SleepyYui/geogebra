package geogebra.touch.gui.laf;

import geogebra.touch.TouchApp;
import geogebra.touch.TouchEntryPoint;
import geogebra.touch.gui.TabletGUI;
import geogebra.touch.gui.elements.StandardImageButton;
import geogebra.touch.gui.elements.header.TabletHeaderPanel;
import geogebra.touch.gui.elements.stylingbar.StylingBar;
import geogebra.touch.model.TouchModel;
import geogebra.touch.utils.OptionType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.event.shared.EventHandler;

public class DefaultLAF implements LookAndFeel {
    private TabletHeaderPanel hp;
    protected TouchApp app;

    public DefaultLAF(TouchApp app){
	this.app = app;
    }
    
    @Override
    public void buildHeader(TabletGUI gui, TouchModel touchModel) {
	this.hp = new TabletHeaderPanel(gui, this.app, touchModel);
	gui.setHeaderWidget(this.hp);
	gui.addResizeListener(this.hp);
    }

    public TouchApp getApp() {
	return this.app;
    }

    @Override
    public int getAppBarHeight() {
	return 62;
    }

    @Override
    public DefaultResources getIcons() {
	return DefaultResources.INSTANCE;
    }

    @Override
    public EventHandler getOptionalButtonHandler(final StylingBar stylingBar, final StandardImageButton standardImageButton, final OptionType type) {
	return new ClickHandler() {
	    @Override
	    public void onClick(final ClickEvent event) {
		stylingBar.onTouchStartOptionalButton(event, standardImageButton, type);
	    }
	};
    }

    @Override
    public int getPaddingLeftOfDialog() {
	return 0;
    }

    @Override
    public int getPanelsHeight() {
	return 122;
    }

    @Override
    public Type getStylBarEventType() {
	return ClickEvent.getType();
    }

    @Override
    public EventHandler getStyleBarButtonHandler(final StylingBar stylingBar, final StandardImageButton newButton, final String process) {
	return new ClickHandler() {
	    @Override
	    public void onClick(final ClickEvent event) {
		stylingBar.onTouchStartStyleBarButton(event, newButton, process);
		DefaultLAF.this.getApp().setUnsaved();
		TouchEntryPoint.getLookAndFeel().updateUndoSaveButtons();
	    }
	};
    }

    @Override
    public TabletHeaderPanel getTabletHeaderPanel() {
	return this.hp;
    }

    @Override
    public int getToolBarHeight() {
	return 75;
    }

    @Override
    public boolean isMouseDownIgnored() {
	return false;
    }

    @Override
    public void setApp(TouchApp app) {
	this.app = app;
    }

    @Override
    public void setTitle(String title) {
	this.hp.setTitle(title);
    }

    @Override
    public void stateChanged(boolean b) {
	if (this.getTabletHeaderPanel() != null) {
	    this.getTabletHeaderPanel().enableDisableButtons();
	}
    }

    @Override
    public boolean supportsShare() {
	return false;
    }

    @Override
    public void updateUndoSaveButtons() {
	if (this.getTabletHeaderPanel() != null) {
	    this.getTabletHeaderPanel().enableDisableButtons();
	}

    }
}
