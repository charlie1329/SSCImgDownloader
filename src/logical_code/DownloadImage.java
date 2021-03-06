package logical_code;

import gui_code.MainPanel;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**this class implements Runnable and it will represent the downloading of a single image
 * to be run in a thread pool
 * @author Charlie Street
 *
 */
public class DownloadImage implements Runnable
{
	private JPanel statusPanel;//the panel with the status on it
	private String fileLink;
	private int fileSize;
	private JProgressBar progress;
	private String folder;
	private final boolean knownFileSize;
	private DefaultTableModel tableModel;
	/**constructor essentially initialises all attributes
	 * 
	 * @param status the panel displaying the download status
	 * @param fileLink the file link to be downloaded
	 * @param fileSize the size of the file being downloaded
	 * @param folder the location to store the file
	 */
	public DownloadImage(DefaultTableModel tableModel, JPanel status, String fileLink,String folder, int fileSize)
	{
		this.statusPanel = status;//now I have control of the panel and what goes inside it
		this.fileLink = fileLink;
		this.fileSize = fileSize;
		this.folder = folder + "\\" + this.fileLink.substring(this.fileLink.lastIndexOf("/")+1, this.fileLink.length());//getting store location
		
		if(this.folder.contains("?"))//question marks mess with file directories
		{
			int occur = this.folder.indexOf("?");
			this.folder = this.folder.substring(0,occur) + this.folder.substring(occur+1);
		}
		
		this.tableModel = tableModel;
		
		if(fileSize == 0 || fileSize == -1)//accounting for file size unknown
		{
			this.knownFileSize = false;
			this.progress = new JProgressBar(0,100);//won't increment the progress bar since no file size knows
			this.progress.setString("Downloading");
		}
		else
		{
			this.knownFileSize = true;
			this.progress = new JProgressBar(0,(int)this.fileSize);//the progress bar
		}
	}
	
	/**the method that runs everything, it will write the bytes of the image
	 * to the correct folder as well as updating the status of it as it runs
	 */
	@Override
	public void run()
	{
		InputStream in = null;//streams for writing image to file
		BufferedOutputStream out = null;
		
		//first step is to remove the queue icon in the panel
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				statusPanel.removeAll();//remove image
				statusPanel.add(progress);//add the progress bar
				statusPanel.repaint();
				statusPanel.revalidate();
				tableModel.fireTableDataChanged();
			}
		});
		
		try//try to get image
		{
			URL url = new URL(this.fileLink);//converting to url
			in = url.openStream();
			out = new BufferedOutputStream(new FileOutputStream(this.folder));//setting up streams
			//starting to write to file
			int buffer = in.read();
			
			while(buffer != -1)
			{
				out.write(buffer);//writing to file
				buffer = in.read();//getting next part
				
				//now updating progress bar
				SwingUtilities.invokeLater(new Runnable(){
					public void run()
					{
						if(knownFileSize)
						{
							int currentProgress = progress.getValue();//getting current value
							progress.setValue(currentProgress+1);//incrementing it
							statusPanel.repaint();
							statusPanel.revalidate();
							tableModel.fireTableDataChanged();
						}
					}
				});
			}
			
			//finally I want to get rid of the progress bar and set it to the tick picture since completed
			SwingUtilities.invokeLater(new Runnable(){
				public void run()
				{
					statusPanel.removeAll();
					ImageIcon icon = new ImageIcon("tick.png");//getting image
					JLabel tick = new JLabel(icon);
					statusPanel.add(tick);//add to panel
					statusPanel.repaint();
					statusPanel.revalidate();
					tableModel.fireTableDataChanged();
				}
			});
			
		}
		catch(IOException e)//if it all goes wrong
		{
			//the main cause of an error in this case would be if there is a link on the web page which is wrapped in something
			//meaning you can't really retrieve it; therefore it shouldn't be downloaded hence the error checking
			SwingUtilities.invokeLater(new Runnable(){//if failure to download, notify user by failed icon
				public void run()
				{
					statusPanel.removeAll();
					ImageIcon cross = new ImageIcon("cross.png");
					JLabel crossLabel = new JLabel(cross);
					statusPanel.add(crossLabel);
				}
			});
			return;
		}
		finally//closing streams
		{
			try//try to close
			{
				synchronized(MainPanel.NumberDownloaded)//potential for multiple threads to access this at same time so want to be careful
				{
					MainPanel.NumberDownloaded ++;//incrementing number counted
				}
				
				in.close();
				out.close();	
			}
			catch (IOException e)
			{
				//nothing can be done in this situation so just ignore and move on
			}
		}

	}

}
