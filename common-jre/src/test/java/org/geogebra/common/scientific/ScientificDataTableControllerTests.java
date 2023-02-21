package org.geogebra.common.scientific;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.gui.view.table.ScientificDataTableController;
import org.geogebra.common.gui.view.table.TableValuesView;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.main.App;
import org.junit.Before;
import org.junit.Test;

public final class ScientificDataTableControllerTests extends BaseUnitTest {

	private TableValuesView tableValuesView;
	private ScientificDataTableController controller;

	@Before
	public void setUp() {
		App app = getApp();
		Kernel kernel = app.getKernel();
		kernel.setUndoActive(true);
		kernel.initUndoInfo();

		tableValuesView = new TableValuesView(kernel);
		kernel.attach(tableValuesView);

		controller = new ScientificDataTableController(kernel);
		controller.setup(tableValuesView, true);
	}

	@Test
	public void testInitialSetup() {
		assertNull(controller.getDefinitionOfF());
		assertFalse(controller.isFDefined());
		assertNull(controller.getDefinitionOfG());
		assertFalse(controller.isGDefined());
		assertEquals(0, getUndoHistorySize());
	}

	@Test
	public void testDefineFunctions() {
		// define f
		assertTrue(controller.defineFunctions("x", null));
		assertFalse(controller.hasFDefinitionErrorOccurred());
		assertEquals("x", controller.getDefinitionOfF());
		assertEquals(1, getUndoHistorySize());

		// define g
		assertTrue(controller.defineFunctions("x", "ln(x)"));
		assertFalse(controller.hasGDefinitionErrorOccurred());
		assertEquals("ln(x)", controller.getDefinitionOfG());
		assertEquals(2, getUndoHistorySize());
	}

	@Test
	public void testRedefineF() {
		// define f
		controller.defineFunctions("x", null);
		assertEquals(1, getUndoHistorySize());

		// redefine f using a different definition
		assertTrue(controller.defineFunctions("sqrt(x)", null));
		assertFalse(controller.hasFDefinitionErrorOccurred());
		assertEquals("sqrt(x)", controller.getDefinitionOfF());
		assertEquals(2, getUndoHistorySize());

		// redefine f using the same definition, no undo point should be created
		assertTrue(controller.defineFunctions("sqrt(x)", null));
		assertFalse(controller.hasFDefinitionErrorOccurred());
		assertEquals("sqrt(x)", controller.getDefinitionOfF());
		assertEquals(2, getUndoHistorySize());
	}

	@Test
	public void testInvalidInput() {
		controller.defineFunctions("abc", null);
		assertTrue(controller.hasFDefinitionErrorOccurred());
		assertFalse(controller.hasGDefinitionErrorOccurred());
		assertEquals(0, getUndoHistorySize());
	}

	private int getUndoHistorySize() {
		return getKernel().getConstruction().getUndoManager().getHistorySize();
	}
}
