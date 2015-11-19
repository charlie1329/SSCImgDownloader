package gui_code;

import java.awt.BorderLayout;

import java.awt.FlowLayout;

import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import logical_code.FileExtensionFilter;
import logical_code.PoolDownloader;
import logical_code.RetrieveLinks;

/**this panel acts as the jpanel which contains all the user interaction for the image downloader
 * 
 * @author Charlie Street
 *
 */
public class MainPanel extends JPanel
{
	
	private static final long serialVersionUID = 1L;//default value
	
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
	
	
	/**this constructor sets up the interface
	 * 
	 */
	public MainPanel()
	{
		super();
		
		initialiseComponents();//initialising attributes
		positionComponents();//positioning
		addListeners();//add all listeners
		
	}
	
	/**this method initialises and sets up all fields/components
	 * 
	 */
	private void initialiseComponents()
	{
		setLayout(new BorderLayout());//using border layout
		
		this.addressLabel = new JLabel("Website Address: ");//setting up labels
		this.folderDisplay = new JLabel("Folder: ");
		this.pool = new JLabel("Threads: ");
		this.extension = new JLabel("File Extensions:    ");
		
		this.addressEntry = new JTextField(14);//setting up text fields
		this.poolNumber = new JTextField(2);
		this.folderPath = new JTextField(15);
		this.folderPath.setEditable(false);//dont want this one editable
		
		ImageIcon folder = new ImageIcon("directory.png");//creating image icons
		ImageIcon down = new ImageIcon("download.png");
		
		this.selectFolder = new JButton(folder);//setting up buttons (with and without icons)
		this.addExtension = new JButton("+");
		this.download = new JButton(down);
		this.clear = new JButton("Clear Screen");
		
		this.extensionEntry = new JComboBox<String>();//setting up combo box for file extensions
		this.extensionEntry.setEditable(true);//allowing editing to be done
		
		this.model = new NonEditableModel();//my model for the table (setting up the table)
		this.model.addColumn("File Name");//setting the column names
		this.model.addColumn("File Type");
		this.model.addColumn("Size (kb)");
		this.model.addColumn("Download Status");
		this.table = new JTable(this.model);
		
		this.table.setShowVerticalLines(false);//should remove vertical lines from table
		this.table.setRowSelectionAllowed(false);//table properties
		this.table.getTableHeader().setReorderingAllowed(false);//stop column switching
		this.table.setRowHeight(30);//manually setting the row height
		this.table.getColumn("Download Status").setCellRenderer(new PanelRenderer());//setting panel renderer to column three
		
		this.scrollPane = new JScrollPane(this.table);
	}
	
	/**this method positions all of the components on the gui
	 * 
	 */
	private void positionComponents()
	{
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
		top2.add(Box.createHorizontalStrut(5));//adding an empty gap
		top2.add(this.pool);
		top2.add(this.poolNumber);
		top.add(top2,BorderLayout.SOUTH);
		
		add(top,BorderLayout.NORTH);//adding to main panel
	}
	
	/**this method will allow the selection of a target folder
	 * 
	 */
	private void selectFolder()
	{
		JFileChooser fileChooser = new JFileChooser((String)null);//new chooser
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//only want folder selection
		fileChooser.showDialog(this, "Select");
		File chosenFile = fileChooser.getSelectedFile();
		
		if(chosenFile != null)//i.e user selected something
		{
			final String path = chosenFile.getAbsolutePath();
			SwingUtilities.invokeLater(new Runnable(){//updating gui so invoke later
				public void run()
				{
					folderPath.setText(path);//setting the path on screen
				}
			});
		}
	}
	
	/**this method will add a new user specified file extension
	 * 
	 */
	private void addExtension()
	{
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				String entered = extensionEntry.getSelectedItem().toString();//getting already entered term
				if(entered != null && !getExtensions().contains(entered))//ie new extension added
				{
					extensionEntry.addItem(entered);//adding to displayed extensions
				}
			}
		});
	}
	
	/**this method will attempt user download
	 * 
	 */
	private void attemptDownload()
	{
		String webAddress = this.addressEntry.getText();//getting all entry data
		String folder = this.folderPath.getText();
		ArrayList<String> extensions = this.getExtensions();
		String threads = this.poolNumber.getText();
		
		final int realThreads;
		try//try and cast threads to an int as necessary
		{
			realThreads = Integer.parseInt(threads);
			
			if(webAddress.equals("") || folder.equals("") || realThreads < 0)//if data incorrectly entered
			{
				JOptionPane.showMessageDialog(this, "Either you haven't entered some data or you have an invalid thread number."
						+ "Please try again.","Invalid Data",JOptionPane.ERROR_MESSAGE);//display error message
			}
			else
			{
				Runnable backgroundDownload = new Runnable(){//creating a runnable for the worker thread about to be started
					public void run()
					{
						FileExtensionFilter filter = new FileExtensionFilter(extensions);//setting up filter
						PoolDownloader pool = new PoolDownloader(realThreads,model,folder);//setting up pool
						RetrieveLinks retriever = new RetrieveLinks(webAddress,filter,pool);//setting up retriever
						retriever.connectAndGetLinks();//starting the download process
						pool.shutDown();//shutdown after completion
					}	
				};
				
				Thread worker = new Thread(backgroundDownload);//worker thread for downloads
				worker.start();//starting my worker thread
			}
		}
		catch(ClassCastException e)//if threads not an integer
		{
			JOptionPane.showMessageDialog(this, "Threads has to be an integer. Please enter again",
					"Number Of Threads",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**this method will get all extensions from the extension list
	 * 
	 * @return all extensions in an array list
	 */
	private ArrayList<String> getExtensions()
	{
		ArrayList<String> extension = new ArrayList<String>();//array list to store extensions
		
		int count = this.extensionEntry.getComponentCount();//number of components
		
		for(int i = 0; i < count; i++)
		{
			extension.add(this.extensionEntry.getItemAt(i));//getting item of combo box
		}
		
		return extension;
	}
	
	/**this method adds all the necessary listeners to my interface
	 * 
	 */
	private void addListeners()
	{
		this.selectFolder.addActionListener(e -> selectFolder());//adding folder listener
		this.addExtension.addActionListener(e -> addExtension());//adding file extension
		this.download.addActionListener(e -> attemptDownload());//for downloading
	}
	
}
