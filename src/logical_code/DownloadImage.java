package logical_code;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**this class implements Runnable and it will represent the downloading of a single image
 * to be run in a thread pool
 * @author Charlie Street
 *
 */
public class DownloadImage implements Runnable
{
	private JPanel statusPanel;//the panel with the status on it
	private String fileLink;
	private long fileSize;
	private JProgressBar progress;
	private String folder;
	/**constructor essentially initialises all attributes
	 * 
	 * @param status the panel displaying the download status
	 * @param fileLink the file link to be downloaded
	 * @param fileSize the size of the file being downloaded
	 * @param folder the location to store the file
	 */
	public DownloadImage(JPanel status, String fileLink,String folder, long fileSize)
	{
		this.statusPanel = status;//now I have control of the panel and what goes inside it
		this.fileLink = fileLink;
		this.fileSize = fileSize;
		this.folder = folder + this.fileLink.substring(this.fileLink.lastIndexOf("/")+1, this.fileLink.length());//getting store location
		
		this.progress = new JProgressBar(0,(int)this.fileSize);//the progress bar
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
						int currentProgress = progress.getValue();//getting current value
						progress.setValue(currentProgress+1);//incrementing it
					}
				});
			}
			
			//finally I want to get rid of the progress bar and set it to the tick picture sice completed
			SwingUtilities.invokeLater(new Runnable(){
				public void run()
				{
					statusPanel.removeAll();
					ImageIcon icon = new ImageIcon("jar_and_icons/tick.png");//getting image
					JLabel tick = new JLabel(icon);
					statusPanel.add(tick);//add to panel
				}
			});
			
		}
		catch(IOException e)//if it all goes wrong
		{
			JOptionPane.showMessageDialog(this.statusPanel.getParent(), "Can't retreive image at: " + this.fileLink,
					"Can't get image",JOptionPane.ERROR_MESSAGE);//displaying faulty link
		}
		finally//closing streams
		{
			try//try to close
			{
				in.close();
				out.close();
			} catch (IOException e)
			{
				//nothing can be done in this situation so just ignore and move on
			}
		}

	}

}
