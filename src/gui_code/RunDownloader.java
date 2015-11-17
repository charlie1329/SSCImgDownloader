package gui_code;

import javax.swing.SwingUtilities;

/**this class will run the image downloader
 * 
 * @author Charlie Street
 *
 */
public class RunDownloader 
{
	/**main method posts the creation og the main frame to the event queue
	 * 
	 * @param args n/a in this program
	 */
	public static void main(String[] args)
	{
		//posting to queue using invoke later
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				CreateDownloaderFrame download = new CreateDownloaderFrame();//adding to event queue
			}
		});
	}
}
