package logical_code;

import java.io.IOException;
import java.util.ArrayList;

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
	
	/**this constructor will set up all fields such that the links can be retrieved by the user
	 * 
	 * @param webPage the user selected web page
	 * @throws IOException will be caught by gui if necessary
	 */
	public RetrieveLinks(String webPage,FileExtensionFilter filter) throws IOException
	{
		this.webPage = setUpAddress(webPage);
		this.filter = filter;
		this.links = connectAndGetLinks();
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
	 * @throws IOException will be caught by gui
	 * @return the arraylist of links
	 */
	private ArrayList<String> connectAndGetLinks() throws IOException
	{
		ArrayList<String> linkStrings = new ArrayList<String>();
		
		Document selectedWebPage = Jsoup.connect(this.webPage).get();//getting the document
		
		String filter = this.filter.getRegexExtensions();//getting user defined filters
		
		String fullSelector = "img[src~=(?i)\\." + filter + "]";//full selector for document
		
		Elements allLinks = selectedWebPage.select(fullSelector);//getting all links
		
		for(Element img : allLinks)//going through all links
		{
			String current = img.attr("src");
			
			if(current.indexOf(this.webPage) == -1)//i.e not fully qualified
			{
				current = this.webPage + current;
			}
			
			linkStrings.add(current);
		}
		
		return linkStrings;
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
