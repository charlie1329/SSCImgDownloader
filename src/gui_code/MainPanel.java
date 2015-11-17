package gui_code;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;





import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**this panel acts as the jpanel which contains all the user interaction for the image downloader
 * 
 * @author Charlie Street
 *
 */
public class MainPanel extends JPanel
{
	private JLabel addressLabel;
	private JTextField addressEntry;
	private JLabel folderDisplay;
	private JTextField folderPath;
	private JButton selectFolder;
	private JLabel pool;
	private JTextField poolNumber;
	private JLabel extension;
	private JComboBox<String> extensionEntry;
	private JButton addExtension;
	private JScrollPane scrollPane;
	private DefaultTableModel model;
	private JTable table;
	private JButton download;
	private JButton clear;
	
	private String selectedFile;
	
	/**this constructor sets up the interface
	 * 
	 */
	public MainPanel()
	{
		super();
		setLayout(new BorderLayout());//using border layout
		this.selectedFile = "";
		
		this.addressLabel = new JLabel("Website Address: ");//setting up labels
		this.folderDisplay = new JLabel("Folder: ");
		this.pool = new JLabel("Threads: ");
		this.extension = new JLabel("File Extensions:    ");
		
		this.addressEntry = new JTextField(14);//setting up text fields
		this.poolNumber = new JTextField(2);
		this.folderPath = new JTextField(14);
		this.folderPath.setEditable(false);//dont want this one editable
		
		ImageIcon folder = new ImageIcon("directory.png");//creating image icons
		ImageIcon down = new ImageIcon("download.png");
		
		this.selectFolder = new JButton(folder);//setting up buttons (with and without icons)
		this.addExtension = new JButton("+");
		this.download = new JButton(down);
		this.clear = new JButton("Clear Screen");
		
		this.extensionEntry = new JComboBox<String>();//setting up combo box for file extensions
		this.extensionEntry.setEditable(true);//allowing editing to be done
		
		this.model = new DefaultTableModel();//model for the table (setting up the table)
		this.table = new JTable(this.model);
		this.model.addColumn("File Name");//setting the column names
		this.model.addColumn("Size");
		this.model.addColumn("Download Status");
		this.table.setShowVerticalLines(false);//should remove vertical lines from table
		this.table.setRowHeight(30);//manually setting the row height
		this.scrollPane = new JScrollPane(this.table);
		
		JPanel bottom = new JPanel();//creating separate panels to fit everything nicely on screen
		bottom.setLayout(new BorderLayout());
		bottom.add(this.clear, BorderLayout.WEST);
		bottom.add(this.download,BorderLayout.EAST);
		add(bottom,BorderLayout.SOUTH);
		
		add(this.scrollPane,BorderLayout.CENTER);//adding the main part to the centre of the panel
		
		JPanel top = new JPanel();//panel for all entry for user
		top.setLayout(new BorderLayout());
		
		JPanel top1 = new JPanel();//panel for first set of entry
		top1.setLayout(new FlowLayout(FlowLayout.LEFT));
		top1.add(this.addressLabel);
		top1.add(this.addressEntry);
		top1.add(Box.createHorizontalStrut(11));//adding gaps between things
		top1.add(this.folderDisplay);
		top1.add(Box.createHorizontalStrut(4));
		top1.add(this.folderPath);
		top1.add(this.selectFolder);
		top.add(top1, BorderLayout.NORTH);
		
		JPanel top2 = new JPanel();//second set of entry things
		top2.setLayout(new FlowLayout(FlowLayout.LEFT));
		top2.add(this.extension);
		top2.add(this.extensionEntry);
		top2.add(this.addExtension);
		top2.add(Box.createHorizontalStrut(5));
		top2.add(this.pool);
		top2.add(this.poolNumber);
		top.add(top2,BorderLayout.SOUTH);
		
		add(top,BorderLayout.NORTH);//adding to main panel
		this.model.addRow(new Object[]{"test","test2","test3"});
		this.model.addRow(new Object[]{"test","test2","test3"});
		this.model.addRow(new Object[]{"test","test2","test3"});
	}
	
}
