package logical_code;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**this class is used to connect to a web page and retrieve all the image links in it
 * 
 * @author Charlie Street
 *
 */
public class RetrieveLinks 
{
	private String webPage;
	private FileExtensionFilter filter;
	private ArrayList<String> links;//will be filled by time methods have been run
	private PoolDownloader pool;
	
	/**this constructor will set up all fields such that the links can be retrieved by the user
	 * 
	 * @param webPage the user selected web page
	 */
	public RetrieveLinks(String webPage,FileExtensionFilter filter,PoolDownloader pool) 
	{
		this.webPage = setUpAddress(webPage);
		this.filter = filter;
		this.links = new ArrayList<String>();
		this.pool = pool;//allowing me to add to pool here
	}
	
	/**this method sets up the web address into the correct format for use
	 * 
	 * @param webPage the users entry for the web page
	 * @return the formatted web address
	 */
	private String setUpAddress(String webPage)
	{
		String formatted = "";//return value
		
		if(webPage.indexOf("http://") == -1 && webPage.indexOf("https://") == -1)//needs to be one of these two
		{
			formatted = "http://"+webPage;//using http:// as the default rather than https
		}
		else
		{
			formatted = webPage;
		}
		
		return formatted;
	}
	
	/**this method will connect to the web page and get the associated html document
	 * it will then proceed to get the links in the form of an arraylist of strings
	 */
	public void connectAndGetLinks()
	{
		try
		{
			Document selectedWebPage = Jsoup.connect(this.webPage).get();//getting the document
		
			String filter = this.filter.getRegexExtensions();//getting user defined filters
			getLinks("img","src",filter,selectedWebPage);//getting images
			getLinks("a","href",filter,selectedWebPage);//getting all other files
			
			
		}
		catch(IOException e)//if failure to connect to web page
		{
			JOptionPane.showMessageDialog(null,"Unable to connect to webpage: "+ this.webPage + ". Please try again");//showing problem
		}
		
	}
	
	/**this method will get the links in the web page based upon a tag and attribute
	 * 
	 * @param tag the html tag to look for
	 * @param attr the attribute within said tag
	 * @param filter the filter statement set by the user
	 * @param selectedWebPage the webpage to search
	 */
	private void getLinks(String tag, String attr, String filter, Document selectedWebPage)
	{
		
		String fullSelector = tag+"["+attr+"~=(?i)\\." + filter + "]";
		Elements allLinks = selectedWebPage.select(fullSelector);//getting all links
		for(Element img : allLinks)//going through all links
		{
			String current = img.attr(attr);
			
			if(current.indexOf("http://") == -1 && current.indexOf("https://") == -1)//i.e not fully qualified if it has http:// it is a valid web address
			{
				if(this.webPage.charAt(this.webPage.length()-1) != '/' && current.charAt(0) != '/')//if no separator
				{
					current = this.webPage + "/" +  current;
				}
				else
				{
					current = this.webPage + current;
				}
			}
			
			if(!this.links.contains(current))//i.e no duplicates
			{
				this.links.add(current);
				this.pool.startDownload(current);//starting download once link retreived
			}
		}
		
	}
	
	/**this method returns the array list of links to download from
	 * 
	 * @return the array list of links
	 */
	public ArrayList<String> getLinks()
	{
		return this.links;
	}
}
