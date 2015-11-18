package gui_code;

import javax.swing.table.DefaultTableModel;

/**this class extends a default table model such that the cells are non editable
 * 
 * @author Charlie Street
 *
 */
public class NonEditableModel extends DefaultTableModel
{
	/**regardless return false as I don't want editable cells
	 * @param row the table row
	 * @param column the table column
	 * @return false always
	 */
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
}
