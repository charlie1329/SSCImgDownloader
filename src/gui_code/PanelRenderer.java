package gui_code;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.JPanel;

/**this class will allow me to add jpanels to my jtable
 * 
 * @author Charlie Street
 *
 */
public class PanelRenderer implements TableCellRenderer
{

	/**just need to return the jpanel to work
	 * if i don't implement this method then the table doesn't know how to render a jpanel
	 * @param table the table being worked on
	 * @param comp the component being rendered
	 * @param selected not of any particular use here
	 * @param focus not of any particular use here
	 * @param row the table row
	 * @param column the table column
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object comp,
			boolean selected, boolean focus, int row, int column) 
	{
		TableCellRenderer cellRenderer = table.getCellRenderer(row,column-1);//getting the current colour for that row
		Component rendererComponent = cellRenderer.getTableCellRendererComponent(table, null, false, true, row, column-1);
		Color cellColor = rendererComponent.getBackground();
		
		JPanel panel = (JPanel) comp;//getting the panel
		
		panel.setOpaque(true);//make opaque so i can adjust the colour of the background
		
		if(row % 2 == 0)//if I dont have this statement white won't get set
		{
			panel.setBackground(Color.WHITE);
		}
		else
		{
			panel.setBackground(cellColor);//the grey-ish colour on screen
		}
		
		return panel;
	}

}
