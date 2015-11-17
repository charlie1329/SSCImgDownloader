package gui_code;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**this class will create a jframe which will add to it the appropriate downloader panel
 * 
 * @author Charlie Street
 *
 */
public class CreateDownloaderFrame
{
	private JFrame frame;
	//private DownloadPanel panel;
	
	public CreateDownloaderFrame()
	{
		this.frame = new JFrame("Image Downloader");//setting standard properties
		this.frame.setSize(600,400);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//setting to nimbus look and feel
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    //dont do anything i.e keep default layout
		}
		
		//this.panel = new DownloadPanel();//adding panel to frame
		//this.frame.add(this.panel);
		this.frame.setResizable(false);
		this.frame.setLocationRelativeTo(null);//centering on screen
		this.frame.setVisible(true);//displaying frame
	}
}
