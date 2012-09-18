package geogebra.web.gui.view.spreadsheet;

import geogebra.common.awt.GPoint;
import geogebra.common.euclidian.EuclidianConstants;
import geogebra.common.gui.view.spreadsheet.MyTable;
import geogebra.common.gui.view.spreadsheet.RelativeCopy;
import geogebra.common.kernel.Kernel;
import geogebra.common.main.SpreadsheetTableModel;
import geogebra.web.main.AppW;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;

public class SpreadsheetMouseListener implements
	MouseDownHandler, MouseUpHandler, MouseMoveHandler {

	protected String selectedCellName;
	protected String prefix0, postfix0;

	private AppW app;
	private SpreadsheetView view;
	private Kernel kernel;
	private MyTableW table;
	private SpreadsheetTableModel model;
	//private MyCellEditor editor;

	private RelativeCopy relativeCopy;

	private boolean mouseIsDown = false;

	/*************************************************
	 * Constructor
	 */
	public SpreadsheetMouseListener(AppW app, MyTableW table) {

		this.app = app;
		this.kernel = app.getKernel();
		this.table = table;
		this.view = (SpreadsheetView)table.getView();
		this.model = table.getModel();
		//TODO this.editor = table.editor;

		this.relativeCopy = new RelativeCopy(kernel);
	}

	/*TODO public void mouseClicked(MouseEvent e) {

		boolean doubleClick = (e.getClickCount() != 1);

		GPoint point = table.getIndexFromPixel(e.getX(), e.getY());
		if (point != null) {

			if (doubleClick) {

				// auto-fill down if dragging dot is double-clicked
				if (table.isOverDot) {
					handleAutoFillDown();
					return;
				}

				// otherwise, doubleClick edits cell

				if (!(table.getOneClickEditMap().containsKey(point) && view
						.allowSpecialEditor())) {
					table.setAllowEditing(true);
					table.editCellAt(table.getSelectedRow(),
							table.getSelectedColumn());

					// workaround, see
					// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4192625
					final JTextComponent f = (JTextComponent) table
							.getEditorComponent();
					if (f != null) {
						f.requestFocus();
						f.getCaret().setVisible(true);
					}

					table.setAllowEditing(false);
				}
			}
		}

		if (editor.isEditing()) {
			String text = editor.getEditingValue();
			if (text.startsWith("=")) {
				point = table.getIndexFromPixel(e.getX(), e.getY());
				if (point != null) {
					int column = point.getX();
					int row = point.getY();
					GeoElement geo = RelativeCopy.getValue(app, column, row);
					if (geo != null) {
						e.consume();
					}
				}
			}
			selectedCellName = null;
			prefix0 = null;
			table.isDragging2 = false;
			table.repaint();
		} else if (app.getMode() != EuclidianConstants.MODE_SELECTION_LISTENER) {
			int row = table.rowAtPoint(e.getPoint());
			int col = table.columnAtPoint(e.getPoint());
			GeoElement geo = (GeoElement) model.getValueAt(row, col);
			// let euclidianView know about the click
			AbstractEvent event = geogebra.euclidian.event.MouseEvent
					.wrapEvent(e);
			app.getActiveEuclidianView().clickedGeo(geo, event);
			event.release();
		}

		// else
		// { // !editor.isEditing()
		// int row = rowAtPoint(e.getPoint());
		// int col = columnAtPoint(e.getPoint());
		// GeoElement geo = (GeoElement) getModel().getValueAt(row, col);
		//
		// // copy description into input bar when a cell is clicked on
		// copyDefinitionToInputBar(geo);
		// selectionChanged();
		// }
	}

	// automatic fill down from the dragging dot
	public void handleAutoFillDown() {
		int col = table.getSelectedColumn();
		int row = table.maxSelectionRow;
		if (model.getValueAt(row, col) != null) {
			// count nonempty cells below selection
			// if no cells below, count left ... if none on the left, count
			// right
			while (row < table.getRowCount() - 1
					&& model.getValueAt(row + 1, col) != null)
				row++;
			if (row - table.maxSelectionRow == 0 && col > 0)
				while (row < table.getRowCount() - 1
						&& model.getValueAt(row + 1, col - 1) != null)
					row++;
			if (row - table.maxSelectionRow == 0
					&& table.maxSelectionColumn <= table.getColumnCount() - 1)
				while (row < table.getRowCount() - 1
						&& model.getValueAt(row + 1,
								table.maxSelectionColumn + 1) != null)
					row++;
			int rowCount = row - table.maxSelectionRow;

			// now fill down
			if (rowCount != 0) {
				boolean succ = relativeCopy.doDragCopy(
						table.minSelectionColumn, table.minSelectionRow,
						table.maxSelectionColumn, table.maxSelectionRow,
						table.minSelectionColumn, table.maxSelectionRow + 1,
						table.maxSelectionColumn, table.maxSelectionRow
								+ rowCount);
				if (succ)
					app.storeUndoInfo();
			}
			table.isDragingDot = false;
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}*/

	public void onMouseDown(MouseDownEvent e) {

		//if (!view.hasViewFocus())
		//	((LayoutW) app.getGuiManager().getLayout()).getDockManager()
		//			.setFocusedPanel(App.VIEW_SPREADSHEET);

		mouseIsDown = true;
		e.preventDefault();

		boolean rightClick = (e.getNativeButton() == NativeEvent.BUTTON_RIGHT);

		// tell selection listener about click on GeoElement
		if (!rightClick
				&& app.getMode() == EuclidianConstants.MODE_SELECTION_LISTENER) {
			/*TODO int row = table.rowAtPoint(e.getPoint());
			int col = table.columnAtPoint(e.getPoint());
			GeoElement geo = (GeoElement) model.getValueAt(row, col);

			// double click or empty geo
			if (e.getClickCount() == 2 || geo == null) {
				//TODO table.requestFocusInWindow();
			} else {
				// tell selection listener about click
				app.geoElementSelected(geo, false);
				e.consume();
				return;
			}*/
		}

		if (!rightClick) {

			// memory testing
			// Application.debug("", true, true, 0);

			if (table.getSelectionType() != MyTable.CELL_SELECT) {
				table.setSelectionType(MyTable.CELL_SELECT);
			}

			// force column selection
			if (view.isColumnSelect()) {
				GPoint point = table.getIndexFromPixel(e.getX(), e.getY());
				if (point != null) {
					int column = point.getX();
					table.setColumnSelectionInterval(column, column);
				}
			}

			/*
			 * if (MyTable.this.getSelectionModel().getSelectionMode() !=
			 * ListSelectionModel.SINGLE_INTERVAL_SELECTION) {
			 * setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			 * setColumnSelectionAllowed(true); setRowSelectionAllowed(true); }
			 */

			GPoint point1 = table.getMaxSelectionPixel();
			if (point1 == null)
				return;

			// Handle click in another cell while editing a cell:
			// if the edit string begins with "=" then the clicked cell name
			// is inserted into the edit text
			/*TODO if (editor.isEditing()) {
				String text = editor.getEditingValue();
				if (text.startsWith("=")) {
					GPoint point = table.getIndexFromPixel(e.getX(), e.getY());
					if (point != null) {
						int column = point.getX();
						int row = point.getY();
						GeoElement geo = RelativeCopy
								.getValue(app, column, row);
						if (geo != null) {

							// get cell name
							String name = GeoElementSpreadsheet
									.getSpreadsheetCellName(column, row);
							if (geo.isGeoFunction())
								name += "(x)";
							selectedCellName = name;

							// get prefix/post substrings for current text caret
							// position
							int caretPos = editor.getCaretPosition();
							prefix0 = text.substring(0, caretPos);
							postfix0 = text.substring(caretPos, text.length());

							table.isDragging2 = true;
							table.minColumn2 = column;
							table.maxColumn2 = column;
							table.minRow2 = row;
							table.maxRow2 = row;

							// insert the geo label into the editor string
							editor.addLabel(name);

							e.consume();
							table.repaint();
						}
						e.consume();
					}
				} else {

					// if text does not start with "=" then stop the editor
					// and allow it to create/redefine a geo here
					editor.setAllowProcessGeo(true);
					editor.stopCellEditing();
					editor.setAllowProcessGeo(false);
				}
			} else if (table.isOverDot) {
				table.isDragingDot = true;
				e.consume();
			}*/
		}

		// MyTable's default listeners follow, they should be simulated in Web e.g. here

		GPoint p = table.getIndexFromPixel(e.getClientX(), e.getClientY());

		// change selection if right click is outside current selection
		if (p.getY() != table.anchorSelectionRow
			|| p.getX() != table.anchorSelectionColumn) {
			// switch to cell selection mode

			if (table.getSelectionType() != MyTable.CELL_SELECT) {
				table.setSelectionType(MyTable.CELL_SELECT);
			}

			// now change the selection
			table.changeSelection(p.getY(), p.getX(), false, false);
			table.repaint();
		}
	}

	public void onMouseUp(MouseUpEvent e) {

		mouseIsDown = false;
		e.preventDefault();

		boolean rightClick = (e.getNativeButton() == NativeEvent.BUTTON_RIGHT);

		if (table.getTableMode() == MyTable.TABLE_MODE_AUTOFUNCTION) {
			table.stopAutoFunction();
			return;
		}

		if (!rightClick) {
			/*TODO if (editor.isEditing()) {
				String text = editor.getEditingValue();
				if (text.startsWith("=")) {
					GPoint point = table.getIndexFromPixel(e.getX(), e.getY());
					if (point != null) {
						int column = point.getX();
						int row = point.getY();
						if (column != editor.column || row != editor.row) {
							e.consume();
						}
					}
				}
				selectedCellName = null;
				prefix0 = null;
				postfix0 = null;
				table.isDragging2 = false;
				table.repaint();
			}

			if (table.isOverDot) {
				// prevent UI manager from changing selection when mouse
				// is in a neighbor cell but is still over the dot region
				e.consume();
			}

			if (table.isDragingDot) {
				if (table.dragingToColumn == -1 || table.dragingToRow == -1)
					return;
				int x1 = -1;
				int y1 = -1;
				int x2 = -1;
				int y2 = -1;
				// -|1|-
				// 2|-|3
				// -|4|-
				if (table.dragingToColumn < table.minSelectionColumn) { // 2
					x1 = table.dragingToColumn;
					y1 = table.minSelectionRow;
					x2 = table.minSelectionColumn - 1;
					y2 = table.maxSelectionRow;
				} else if (table.dragingToRow > table.maxSelectionRow) { // 4
					x1 = table.minSelectionColumn;
					y1 = table.maxSelectionRow + 1;
					x2 = table.maxSelectionColumn;
					y2 = table.dragingToRow;
				} else if (table.dragingToRow < table.minSelectionRow) { // 1
					x1 = table.minSelectionColumn;
					y1 = table.dragingToRow;
					x2 = table.maxSelectionColumn;
					y2 = table.minSelectionRow - 1;
				} else if (table.dragingToColumn > table.maxSelectionColumn) { // 3
					x1 = table.maxSelectionColumn + 1;
					y1 = table.minSelectionRow;
					x2 = table.dragingToColumn;
					y2 = table.maxSelectionRow;
				}

				// copy the cells
				boolean succ = relativeCopy.doDragCopy(
						table.minSelectionColumn, table.minSelectionRow,
						table.maxSelectionColumn, table.maxSelectionRow, x1,
						y1, x2, y2);
				if (succ) {
					app.storeUndoInfo();
				}

				// extend the selection to include the drag copy selection
				table.setSelection(Math.min(x1, table.minSelectionColumn),
						Math.min(y1, table.minSelectionRow),
						Math.max(x2, table.maxSelectionColumn),
						Math.max(y2, table.maxSelectionRow));

				// reset flags and cursor
				table.isOverDot = false;
				table.isDragingDot = false;
				table.dragingToRow = -1;
				table.dragingToColumn = -1;
				setTableCursor();

				// prevent UI manager from changing selection
				e.consume();

				table.repaint();

			}*/
		}

		// Alt click: copy definition to input field
		/*TODO if (!table.isEditing() && e.isAltDown() && app.showAlgebraInput()) {
			int row = table.rowAtPoint(e.getPoint());
			int col = table.columnAtPoint(e.getPoint());
			GeoElement geo = (GeoElement) model.getValueAt(row, col);

			if (geo != null) {
				// F3 key: copy definition to input bar
				app.getGlobalKeyDispatcher().handleFunctionKeyForAlgebraInput(
						3, geo);
				return;
			}
		}*/

		// handle right click
		if (rightClick) {
			if (!((AppW) kernel.getApplication()).letShowPopupMenu())
				return;

			GPoint p = table.getIndexFromPixel(e.getClientX(), e.getClientY());

			// change selection if right click is outside current selection
			if (p.getY() < table.minSelectionRow
					|| p.getY() > table.maxSelectionRow
					|| p.getX() < table.minSelectionColumn
					|| p.getX() > table.maxSelectionColumn) {
				// switch to cell selection mode

				if (table.getSelectionType() != MyTable.CELL_SELECT) {
					table.setSelectionType(MyTable.CELL_SELECT);
				}

				// now change the selection
				table.changeSelection(p.getY(), p.getX(), false, false);
			}

			// create and show context menu
			/*TODO SpreadsheetContextMenu popupMenu = new SpreadsheetContextMenu(
					table, e.isShiftDown());
			popupMenu.show(e.getComponent(), e.getX(), e.getY());*/
		}

		// MyTable's default listeners follow, they should be simulated in Web e.g. here

		GPoint p = table.getIndexFromPixel(e.getClientX(), e.getClientY());

		// change selection if right click is outside current selection
		if (p.getY() != table.leadSelectionRow
			|| p.getX() != table.leadSelectionColumn) {
			// switch to cell selection mode

			if (table.getSelectionType() != MyTable.CELL_SELECT) {
				table.setSelectionType(MyTable.CELL_SELECT);
			}

			// now change the selection
			table.changeSelection(p.getY(), p.getX(), false, true);
			table.repaint();
		}
	}

	public void onMouseMove(MouseMoveEvent e) {

		e.preventDefault();

		// TODO: move the content of the mouseMoved method here

		// DRAG

		if (mouseIsDown) {

			if (table.getTableMode() == MyTable.TABLE_MODE_AUTOFUNCTION
					|| table.getTableMode() == MyTable.TABLE_MODE_DROP) {
				// System.out.println("drop is dragging ");
				return;
			}

			// handle editing mode drag
			/*TODO if (editor.isEditing()) {
				GPoint point = table.getIndexFromPixel(e.getX(), e.getY());
				if (point != null && selectedCellName != null) {
					int column2 = point.getX();
					int row2 = point.getY();

					MatchResult matcher = GeoElementSpreadsheet.spreadsheetPattern
							.exec(selectedCellName);
					int column1 = GeoElementSpreadsheet
							.getSpreadsheetColumn(matcher);
					int row1 = GeoElementSpreadsheet.getSpreadsheetRow(matcher);

					if (column1 > column2) {
						int temp = column1;
						column1 = column2;
						column2 = temp;
					}
					if (row1 > row2) {
						int temp = row1;
						row1 = row2;
						row2 = temp;
					}
					String name1 = GeoElementSpreadsheet.getSpreadsheetCellName(
							column1, row1);
					String name2 = GeoElementSpreadsheet.getSpreadsheetCellName(
							column2, row2);
					if (!name1.equals(name2)) {
						name1 += ":" + name2;
					}

					name1 = prefix0 + name1 + postfix0;
					editor.setLabel(name1);
					table.minColumn2 = column1;
					table.maxColumn2 = column2;
					table.minRow2 = row1;
					table.maxRow2 = row2;
					table.repaint();
				}
				e.consume();
				return;
			}

			// handle dot drag
			if (table.isDragingDot) {

				e.consume();
				int mouseX = e.getX();
				int mouseY = e.getY();
				GPoint mouseCell = table.getIndexFromPixel(mouseX, mouseY);

				// save the selected cell position so it can be re-selected if
				// needed
				CellRange oldSelection = table.getSelectedCellRanges().get(0);

				if (mouseCell == null) { // user has dragged outside the table, to
										// left or above
					table.dragingToRow = -1;
					table.dragingToColumn = -1;
				} else {
					table.dragingToRow = mouseCell.getY();
					table.dragingToColumn = mouseCell.getX();
					Rectangle selRect = table.getSelectionRect(true);

					// increase size if we're at the bottom of the spreadsheet
					if (table.dragingToRow + 1 == table.getRowCount()
							&& table.dragingToRow < Kernel.MAX_SPREADSHEET_ROWS) {
						model.setRowCount(table.getRowCount() + 1);
					}

					// increase size if we go beyond the right edge
					if (table.dragingToColumn + 1 == table.getColumnCount()
							&& table.dragingToColumn < Kernel.MAX_SPREADSHEET_COLUMNS) {
						model.setColumnCount(table.getColumnCount() + 1);
						view.getColumnHeader().revalidate();
						// Java's addColumn method will clear selection, so
						// re-select our cell
						table.setSelection(oldSelection);
					}

					// scroll to show "highest" selected cell
					table.scrollRectToVisible(table.getCellRect(mouseCell.y,
							mouseCell.x, true));

					if (!selRect.contains(e.getPoint())) {

						int rowOffset = 0, colOffset = 0;

						// get row distance
						if (table.minSelectionRow > 0
								&& table.dragingToRow < table.minSelectionRow) {
							rowOffset = mouseY - selRect.y;
							if (-rowOffset < 0.5 * table.getCellRect(
									table.minSelectionRow - 1,
									table.minSelectionColumn, true).height)
								rowOffset = 0;
						} else if (table.maxSelectionRow < Kernel.MAX_SPREADSHEET_ROWS
								&& table.dragingToRow > table.maxSelectionRow) {
							rowOffset = mouseY - (selRect.y + selRect.height);
							if (rowOffset < 0.5 * table.getCellRect(
									table.maxSelectionRow + 1,
									table.maxSelectionColumn, true).height)
								rowOffset = 0;
						}

						// get column distance
						if (table.minSelectionColumn > 0
								&& table.dragingToColumn < table.minSelectionColumn) {
							colOffset = mouseX - selRect.x;
							if (-colOffset < 0.5 * table.getCellRect(
									table.minSelectionRow,
									table.minSelectionColumn - 1, true).width)
								colOffset = 0;
						} else if (table.maxSelectionColumn < Kernel.MAX_SPREADSHEET_COLUMNS
								&& table.dragingToColumn > table.maxSelectionColumn) {
							colOffset = mouseX - (selRect.x + selRect.width);
							if (colOffset < 0.5 * table.getCellRect(
									table.maxSelectionRow,
									table.maxSelectionColumn + 1, true).width)
								colOffset = 0;
						}

						if (rowOffset == 0 && colOffset == 0) {
							table.dragingToColumn = -1;
							table.dragingToRow = -1;
						} else if (Math.abs(rowOffset) > Math.abs(colOffset)) {
							table.dragingToRow = mouseCell.y;
							table.dragingToColumn = (colOffset > 0) ? table.maxSelectionColumn
									: table.minSelectionColumn;
						} else {
							table.dragingToColumn = mouseCell.x;
							table.dragingToRow = (rowOffset > 0) ? table.maxSelectionRow
									: table.minSelectionRow;
						}
						table.repaint();
					}

					// handle ctrl-select dragging of cell blocks
					else {
						if (e.isControlDown()) {
							table.handleControlDragSelect(e);
						}
					}
				}
			}*/


			// MyTable's default listeners follow, they should be simulated in Web e.g. here

			GPoint p = table.getIndexFromPixel(e.getClientX(), e.getClientY());

			// change selection if right click is outside current selection
			if (p.getY() != table.leadSelectionRow
				|| p.getX() != table.leadSelectionColumn) {
				// switch to cell selection mode

				if (table.getSelectionType() != MyTable.CELL_SELECT) {
					table.setSelectionType(MyTable.CELL_SELECT);
				}

				// now change the selection
				table.changeSelection(p.getY(), p.getX(), false, true);
				table.repaint();
			}
		}
	}

	/**
	 * Shows tool tip description of geo on mouse over
	 */
	/*public void mouseMoved(MouseEvent e) {
		if (table.isEditing())
			return;

		// get GeoElement at mouse location
		int row = table.rowAtPoint(e.getPoint());
		int col = table.columnAtPoint(e.getPoint());
		GeoElement geo = (GeoElement) model.getValueAt(row, col);

		// set tooltip with geo's description
		if (geo != null & view.getAllowToolTips()) {
			app.setTooltipFlag();
			table.setToolTipText(geo.getLongDescriptionHTML(true, true));
			app.clearTooltipFlag();
		} else
			table.setToolTipText(null);

		// check if over the dragging dot and update accordingly
		GPoint maxPoint = table.getMaxSelectionPixel();
		GPoint minPoint = table.getMinSelectionPixel();

		if (maxPoint != null) {
			int dotX = maxPoint.getX();
			int dotY = maxPoint.getY();
			int s = MyTableD.DOT_SIZE + 2;
			Rectangle dotRect = new Rectangle(dotX - s / 2, dotY - s / 2, s, s);
			boolean overDot = dotRect.contains(e.getPoint());
			if (table.isOverDot != overDot) {
				table.isOverDot = overDot;
				setTableCursor();
				table.repaint();
			}
		}

		// check if over the DnD region and update accordingly
		GPoint testPoint = table.getMinSelectionPixel();
		if (testPoint != null) {
			int minX = minPoint.getX();
			int minY = minPoint.getY();
			int maxX = maxPoint.getX();
			int w = maxX - minX;
			Rectangle dndRect = new Rectangle(minX, minY - 2, w, 4);
			boolean overDnD = dndRect.contains(e.getPoint());
			if (table.isOverDnDRegion != overDnD) {
				table.isOverDnDRegion = overDnD;
				setTableCursor();
			}
		}

	}*/

	/**
	 * Sets table cursor
	 */
	/*private void setTableCursor() {

		if (table.isOverDot)
			table.setCursor(table.crossHairCursor);

		// else if(table.isOverDnDRegion)
		// table.setCursor(table.grabCursor);

		else
			table.setCursor(table.defaultCursor);

	}*/

}
