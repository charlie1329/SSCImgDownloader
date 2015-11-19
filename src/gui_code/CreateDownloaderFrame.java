package gui_code;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**this class will act as a jframe which will add to it the appropriate downloader panel
 * 
 * @author Charlie Street
 *
 */
public class CreateDownloaderFrame extends JFrame
{
	
	private static final long serialVersionUID = 1L;//default value
	private MainPanel panel;
	
	public CreateDownloaderFrame()
	{
		super("Image Downloader");//setting standard properties
		setSize(600,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
		
		//adding the panel to the frame
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				panel = new MainPanel();//adding panel to frame
				add(panel);
			}
		});
		
		setResizable(false);
		setLocationRelativeTo(null);//centering on screen
		setVisible(true);//displaying frame
	}
}
