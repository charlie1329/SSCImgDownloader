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
	
	/**this constructor will set up all fields such that the links can be retrieved by the user
	 * 
	 * @param webPage the user selected web page
	 */
	public RetrieveLinks(String webPage,FileExtensionFilter filter) 
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
	private ArrayList<String> connectAndGetLinks()
	{
		try
		{
			Document selectedWebPage = Jsoup.connect(this.webPage).get();//getting the document
		
			String filter = this.filter.getRegexExtensions();//getting user defined filters
			ArrayList<String> images = getLinks("img","src",filter,selectedWebPage);//getting images
			ArrayList<String> other = getLinks("a","href",filter,selectedWebPage);//getting all other files
			images.addAll(other);//appending lists
			System.out.println(images.size());
			for(int i = images.size()-1; i >=0; i--)//removing any duplicates due to files and images pointing to same place
			{
				int firstIndex = images.indexOf(images.get(i));//last index of item
				if(firstIndex < i)//ie if another version
				{
					images.remove(i);//removing duplicate
				}
			}
			System.out.println(images.size());
			return images;
		}
		catch(IOException e)//if failure to connect to web page
		{
			JOptionPane.showMessageDialog(null,"Unable to connect to webpage: "+ this.webPage + ". Please try again");//showing problem
			return new ArrayList<String>();//default value if something goes wrong
		}
		
	}
	
	/**this method will get the links in the web page based upon a tag and attribute
	 * 
	 * @param tag the html tag to look for
	 * @param attr the attribute within said tag
	 * @param filter the filter statement set by the user
	 * @param selectedWebPage the webpage to search
	 * @return the array list of links
	 */
	private ArrayList<String> getLinks(String tag, String attr, String filter, Document selectedWebPage)
	{
		ArrayList<String> myLinks = new ArrayList<String>();
		
		String fullSelector = tag+"["+attr+"~=(?i)\\." + filter + "]";
		Elements allLinks = selectedWebPage.select(fullSelector);//getting all links
		for(Element img : allLinks)//going through all links
		{
			String current = img.attr(attr);
			
			if(current.indexOf("http://") == -1 && current.indexOf("https://") == -1)//i.e not fully qualified if it has http:// it is a valid web address
			{
				current = this.webPage + current;
			}
			
			myLinks.add(current);
			//at this stage here add the thing to the table
		}
		
		return myLinks;
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
