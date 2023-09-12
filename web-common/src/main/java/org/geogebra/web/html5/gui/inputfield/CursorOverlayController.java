package org.geogebra.web.html5.gui.inputfield;

import org.geogebra.common.awt.GColor;
import org.geogebra.common.awt.GFont;
import org.geogebra.common.kernel.geos.properties.HorizontalAlignment;
import org.geogebra.common.util.debug.Log;
import org.geogebra.gwtutil.NavigatorUtil;
import org.geogebra.web.html5.Browser;
import org.geogebra.web.html5.gui.util.CancelEventTimer;
import org.geogebra.web.html5.gui.util.Dom;
import org.geogebra.web.html5.gui.util.LongTouchManager;
import org.geogebra.web.html5.gui.util.LongTouchTimer;
import org.geogebra.web.html5.main.AppW;
import org.geogebra.web.html5.util.keyboard.KeyboardManagerInterface;
import org.gwtproject.dom.client.Element;
import org.gwtproject.event.dom.client.KeyDownEvent;
import org.gwtproject.event.dom.client.KeyPressEvent;
import org.gwtproject.user.client.ui.FlowPanel;

import com.himamis.retex.editor.share.util.GWTKeycodes;

import elemental2.dom.DomGlobal;
import elemental2.dom.Event;
import elemental2.dom.Touch;
import elemental2.dom.TouchEvent;
import jsinterop.base.Js;

public class CursorOverlayController implements TextFieldController,
		LongTouchTimer.LongTouchHandler {

	private final AppW app;
	private final AutoCompleteTextFieldW textField;
	private final FlowPanel main;
	private CursorOverlay cursorOverlay;
	private double blinkHandler;

	public CursorOverlayController(AutoCompleteTextFieldW textField, FlowPanel main) {
		this.app = textField.getApplication();
		this.textField = textField;
		this.main = main;
		enableForTextField();
	}

	/**
	 * Hide keyboard and reset the keyaord field
	 * @param app application
	 */
	public static void hideKeyboard(AppW app) {
		if (CancelEventTimer.cancelKeyboardHide()) {
			return;
		}
		KeyboardManagerInterface kbManager = app.getKeyboardManager();
		if (app.hasPopup() && kbManager != null) {
			kbManager.setOnScreenKeyboardTextField(null);
			return;
		}
		app.hideKeyboard();
	}

	private void enableForTextField() {
		cursorOverlay = new CursorOverlay();
		textField.addFocusHandler(evt -> addCursor());
		textField.addBlurHandler(evt -> removeCursor());
		textField.updateInputBoxAlign();
		final Element element = textField.getInputElement();
		app.getGlobalHandlers().addEventListener(element, "touchstart",
				this::preventNativeSelection);

		app.getGlobalHandlers().addEventListener(main.getElement(),"touchstart",
				this::unselectOverlay);

		app.getGlobalHandlers().addEventListener(main.getElement(), "touchstart",
				e -> {
					if (cursorOverlay.hasFakeSelection()) {
						return;
					}
					TouchEvent touchEvent = Js.uncheckedCast(e);
					if (touchEvent.touches.length > 0) {
						Touch touch = touchEvent.touches.item(0);
						double x = touch.clientX;
						double y = touch.clientY;
						Log.debug("(" + x + "," + y + ")");
						LongTouchManager.getInstance().scheduleTimer(this, (int) x, (int) y, 200);
					}
				} );

		app.getGlobalHandlers().addEventListener(main.getElement(), "touchend",
				e -> {
						CancelEventTimer.cancelMouseEvent();
						LongTouchManager.getInstance().cancelTimer();

				} );

	}

	private void startBlinking() {
		blinkHandler = DomGlobal.setInterval(event -> update(), 200);
	}

	private void preventNativeSelection(Event event) {
		event.preventDefault();
		if (cursorOverlay.hasFakeSelection()) {
			addCursor();
		}
	}

	private void unselectOverlay(Event event) {
		event.preventDefault();
		event.stopPropagation();
		if (isSelected()) {
			unselectAll();
		}
	}

	public void update() {
		cursorOverlay.update(textField.getCursorPos(), textField.getText());
	}

	public void selectAll() {
		stopBlinking();
		cursorOverlay.addFakeSelection();
	}

	@Override
	public void addCursor() {
		main.add(cursorOverlay);
		main.addStyleName("withCursorOverlay");
		app.showKeyboard(textField, true);
		update();
	}

	@Override
	public void removeCursor() {
		if (!cursorOverlay.isAttached()) {
			return;
		}
		cursorOverlay.removeFromParent();
		main.removeStyleName("withCursorOverlay");
		hideKeyboard(app);
	}

	private void stopBlinking() {
		DomGlobal.clearTimeout(blinkHandler);
	}

	@Override
	public void setFont(GFont font) {
		Dom.setImportant(cursorOverlay.getElement().getStyle(), "font-size",
				font.getSize() + "px");
	}

	public void setHorizontalAlignment(HorizontalAlignment alignment) {
		cursorOverlay.setHorizontalAlignment(alignment);
	}

	public void unselectAll() {
		cursorOverlay.removeFakeSelection();
	}

	@Override
	public void setForegroundColor(GColor color) {
		cursorOverlay.getElement().getStyle().setColor(GColor.getColorString(color));
	}

	@Override
	public void handleKeyboardEvent(KeyDownEvent e) {
		int keyCode = e.getNativeKeyCode();
		if (keyCode == 0 && NavigatorUtil.isiOS()) {
			int arrowType = Browser.getIOSArrowKeys(e.getNativeEvent());
			if (arrowType != -1) {
				keyCode = arrowType;
			}
		}
		switch (keyCode) {
		case GWTKeycodes.KEY_BACKSPACE:
			textField.onBackSpace();
			break;
		case GWTKeycodes.KEY_LEFT:
			textField.onArrowLeft();
			break;
		case GWTKeycodes.KEY_RIGHT:
			textField.onArrowRight();
			break;
		case GWTKeycodes.KEY_UP:
			textField.handleUpArrow();
			break;
		case GWTKeycodes.KEY_DOWN:
			textField.handleDownArrow();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean shouldBeKeyPressInserted(KeyPressEvent event) {
		return event.getNativeEvent().getKeyCode() != GWTKeycodes.KEY_BACKSPACE
				&& event.getNativeEvent().getKeyCode() != 0;
	}

	@Override
	public int getSelectionStart() {
		return 0;
	}

	@Override
	public int getSelectionEnd() {
		return textField.getText().length();
	}

	public boolean isSelected() {
		return cursorOverlay.hasFakeSelection();
	}

	@Override
	public void handleLongTouch(int x, int y) {
		Log.debug("longTouch");
		CancelEventTimer.touchEventOccured();
		selectAll();
	}
}
