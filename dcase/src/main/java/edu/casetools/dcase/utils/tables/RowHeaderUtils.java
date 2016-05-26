/*
 * Copyright 2015 @author Unai Alegre 
 * 
 * This file is part of R-CASE (Requirements for Context-Aware Systems Engineering), a module 
 * of Modelio that aids the requirements elicitation phase of a Context-Aware System (C-AS). 
 * 
 * R-CASE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * R-CASE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Modelio. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package edu.casetools.dcase.utils.tables;

import java.awt.Dimension;
import java.awt.FontMetrics;

import javax.swing.JLabel;
import javax.swing.JTable;

import edu.casetools.dcase.extensions.tables.headers.RowHeadersTableModel;

// TODO: Auto-generated Javadoc
/**
 * TalbeRowUtilities. Utility for adding a row column to a JTable
 * 
 * @author Oliver Watkins modifiied by @author Unai Alegre
 */
public class RowHeaderUtils {

    /**
     * The Enum ROW_HEADER.
     */
    public enum ROW_HEADER {
	CONTAINER, DEPENDENCY
    };

    private static RowHeaderUtils instance = null;

    /**
     * Gets the single instance of RowHeaderUtils.
     *
     * @return single instance of RowHeaderUtils
     */
    public static RowHeaderUtils getInstance() {
	if (instance == null) {
	    instance = new RowHeaderUtils();
	}
	return instance;
    }

    /**
     * Adjust row header width.
     *
     * @param rowHeadersTable
     *            the row headers table
     * @param label
     *            the label
     */
    public void adjustRowHeaderWidth(final JTable rowHeadersTable, final JLabel label) {

	label.setFont(rowHeadersTable.getFont());
	label.setOpaque(true);
	label.setHorizontalAlignment(JLabel.CENTER);

	RowHeadersTableModel tm = (RowHeadersTableModel) rowHeadersTable.getModel();

	label.setText(tm.getMaxValue());
	FontMetrics fontMetrics = label.getFontMetrics(label.getFont());

	int widthFactor;

	if (null != fontMetrics && null != label.getText()) {
	    widthFactor = fontMetrics.stringWidth(label.getText());

	    rowHeadersTable.setPreferredScrollableViewportSize(new Dimension(widthFactor + 25, 100)); // height
	    // is
	    // ignored
	    rowHeadersTable.repaint();
	}
    }

}
