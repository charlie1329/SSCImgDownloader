package logical_code;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**this class carries out most of the downloading work by setting up a thread pool and
 * adding downloads to the queue
 * @author Charlie Street
 *
 */
public class PoolDownloader
{
	private ExecutorService threadPool;
	private ArrayList<String> links;
	private DefaultTableModel tableModel;//allows me to update the interface (in a thread safe way)
	private String folderPath;
	
	/**this constructor initialises the attributes and then starts the thread pool
	 * 
	 * @param retriever the object wrapping around all the links
	 * @param howManyThreads the number of threads selected by the user
	 * @param tableModel the table to update
	 */
	public PoolDownloader(RetrieveLinks retriever, int howManyThreads, DefaultTableModel tableModel, String folderPath)
	{
		this.threadPool = Executors.newFixedThreadPool(howManyThreads);
		this.links = retriever.getLinks();//retrieving all links to download 
		this.tableModel = tableModel;
		this.folderPath = folderPath;
		this.startDownload();
		this.threadPool.shutdown();//shutdown after jobs carried out
	}
	
	/**this method will start to add tasks to the thread pool
	 * just before it is added to the queue, its associated jtable row will be created displaying a queue status
	 */
	private void startDownload()
	{
		for(int i = 0; i < this.links.size(); i++)
		{
			try
			{
				final String currentLink = this.links.get(i);//so i can pass in to anonymous class
				URL webPage = new URL(currentLink);
				
				int fileSizeB = webPage.openConnection().getContentLength();//for progress bar
				final double fileSizeKb = Math.round((double)(100*fileSizeB/1024))/100;//getting file size to display
		
				final String fileType = this.links.get(i).substring(currentLink.lastIndexOf(".")+1);//getting file type
				final JPanel statusPanel = new JPanel();//the panel to show status
				
				SwingUtilities.invokeLater(new Runnable(){//adding new row to table in AWT dispatch thread so thread safe
					public void run()
					{
						ImageIcon queue = new ImageIcon("in_queue.png");
						JLabel qLabel = new JLabel(queue);//label with image
						statusPanel.add(qLabel);//adding starting image to panel
						
						tableModel.addRow(new Object[]{currentLink.substring(currentLink.lastIndexOf("/")+1),
								fileType,fileSizeKb,statusPanel});//adding new row
					}
				});

				DownloadImage newFile = new DownloadImage(statusPanel,currentLink,this.folderPath,fileSizeB);//create task
				this.threadPool.submit(newFile);//submit to job queue
				
			}
			catch(IOException e)//if one file can't be downloaded
			{
				JOptionPane.showMessageDialog(null,
						"Unable to download: " + this.links.get(i).substring(this.links.get(i).lastIndexOf("/")+1) + ", sorry for any inconvenience",
						"Unable To Download File",JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
}
