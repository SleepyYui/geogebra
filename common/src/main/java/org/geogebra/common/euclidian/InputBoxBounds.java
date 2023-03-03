package org.geogebra.common.euclidian;

import org.geogebra.common.awt.GFont;
import org.geogebra.common.awt.GGraphics2D;
import org.geogebra.common.awt.GRectangle;
import org.geogebra.common.euclidian.draw.TextRenderer;
import org.geogebra.common.factories.AwtFactory;
import org.geogebra.common.kernel.geos.GeoInputBox;
import org.geogebra.common.util.MyMath;

public class InputBoxBounds {
	private static final int PADDING_Y = 4;
	private static final int PADDING_X = 4;
	private GRectangle bounds;
	private final GeoInputBox geoInputBox;
	private TextRenderer renderer;

	public InputBoxBounds(GeoInputBox geoInputBox) {
		this.geoInputBox = geoInputBox;
		bounds = AwtFactory.getPrototype().newRectangle();
	}

	/**
	 *
	 * @return the bounds.
	 */
	public GRectangle getBounds() {
		return bounds;
	}

	/**
	 *
	 * @param view {@link EuclidianView}
	 * @param labelTop top of the label.
	 * @param textFont font to display.
	 * @param labelDesc label description.
	 */
	public void update(EuclidianView view, double labelTop, GFont textFont, String labelDesc) {
		GGraphics2D g2 = view.getGraphicsForPen();
		bounds = renderer.measureBounds(g2, geoInputBox, textFont, labelDesc);

		if (hasWindowResized(labelTop, view.getHeight())) {
			keepBoxOffscreen(view.getHeight());
		}

		handlePaddings();
	}

	private void handlePaddings() {
		bounds.setSize((int) (bounds.getWidth() - 2 * PADDING_X),
				(int) (bounds.getHeight() - 2 * PADDING_Y));
	}

	private void keepBoxOffscreen(int viewHeight) {
		bounds.setLocation((int) bounds.getX(),
				(int) MyMath.clamp(bounds.getMinY(), 0,
						viewHeight - bounds.getHeight()));
	}

	private boolean hasWindowResized(double labelTop, int viewHeight) {
		return labelTop > 0 && labelTop < viewHeight;
	}

	/**
	 *
	 * @return y coord of the rectangle.
	 */
	public double getY() {
		return bounds.getY();
	}

	public void setRenderer(TextRenderer renderer) {
		this.renderer = renderer;
	}

	/**
	 *
	 * @param x coord.
	 * @param y coord.
	 * @return if bounds has (x, y) within.
	 */
	public boolean contains(int x, int y) {
		return bounds.contains(x, y);
	}
}
