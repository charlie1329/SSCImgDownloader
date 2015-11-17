package gui_code;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

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
		
		this.selectedFile = "            ";
		
		this.addressLabel = new JLabel("Website: ");//setting up labels
		this.folderDisplay = new JLabel("Folder: "+this.selectedFile);
		this.pool = new JLabel("Threads: ");
		this.extension = new JLabel("File extensions: ");
		
		this.addressEntry = new JTextField(10);//setting up text fields
		this.poolNumber = new JTextField(3);
		
		ImageIcon folder = new ImageIcon("jars_and_icons/directory.png");//creating image icons
		ImageIcon down = new ImageIcon("jars_and_icons/download.png");
		
		this.selectFolder = new JButton(folder);//setting up buttons
		this.addExtension = new JButton("+");
		this.download = new JButton();
		this.download.setIcon(down);
		this.clear = new JButton("Clear Screen");
		
		this.extensionEntry = new JComboBox<String>();//setting up combo box for file extensions
		this.extensionEntry.setEditable(true);//allowing editing to be done
		
		this.model = new DefaultTableModel();//model for the table (setting up the table)
		this.table = new JTable(this.model);
		this.model.addColumn("File Name");//setting the column names
		this.model.addColumn("Size");
		this.model.addColumn("Download Status");
		this.table.setShowVerticalLines(false);//should remove vertical lines from table
		this.scrollPane = new JScrollPane(this.table);
		
		JPanel bottom = new JPanel();//creating separate panels to fit everything nicely on screen
		bottom.setLayout(new BorderLayout());
		bottom.add(this.clear, BorderLayout.WEST);
		bottom.add(this.download,BorderLayout.EAST);
		add(bottom,BorderLayout.SOUTH);
		
		add(this.scrollPane,BorderLayout.CENTER);
		
		JPanel top = new JPanel();//panel for all entry for user
		top.setLayout(new FlowLayout(FlowLayout.LEFT));
		top.add(this.addressLabel);
		top.add(this.addressEntry);
		top.add(this.folderDisplay);
		top.add(this.selectFolder);
		top.add(this.pool);
		top.add(this.poolNumber);
		top.add(this.extension);
		top.add(this.extensionEntry);
		top.add(this.addExtension);
		add(top,BorderLayout.NORTH);//adding to main panel
		this.model.addRow(new Object[]{"test","test2","test3"});
		this.model.addRow(new Object[]{"test","test2","test3"});
		this.model.addRow(new Object[]{"test","test2","test3"});
	}
	
}
