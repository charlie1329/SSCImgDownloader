package logical_code;

import java.util.ArrayList;

/**this class will go about storing a list of file extensions set by the user
 * it can then be formatted into a string such that it can be used as a filter when
 * getting the images
 * @author Charlie Street
 *
 */
public class FileExtensionFilter 
{
	private ArrayList<String> extensions;
	
	/**constructor will simply initialise the array list to empty
	 * 
	 */
	public FileExtensionFilter()
	{
		this.extensions = new ArrayList<String>();
	}
	
	/**this method simply adds a new file extension to the array list
	 * 
	 * @param newExtension the new file extension to be allowed
	 */
	public void addExtension(String newExtension)
	{
		this.extensions.add(newExtension);
	}
	
	/**this method is a wrapper for get in the array list class
	 * it gets the extension at index
	 * @param index the index to retrieve from
	 * @return the associated extension
	 */
	private String getExtension(int index)
	{
		return this.extensions.get(index);
	}
	
	/**this method takes all file extensions and converts it into a single string
	 * to be used by regex with JSoup when retrieving the image links
	 * @return the extensions formatted as a string
	 */
	public String getRegexExtensions()
	{
		String expression = "";//where result is stored
		
		for(int i = 0; i < this.extensions.size()-1; i++)//looping round all but last element
		{
			expression += this.getExtension(i) + "|";// | means or in this context
		}
		
		expression += this.getExtension(this.extensions.size()-1);//adding final item, just without the | included
		
		expression = "(" +expression + ")";//need to add parentheses for final result
		
		return expression;
	}
}
