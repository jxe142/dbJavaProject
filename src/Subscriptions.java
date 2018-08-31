
import java.util.ArrayList;

public class Subscriptions
{
	private String Name;
	private String Phone_Number;
	private ArrayList<String> Title = new ArrayList<String>();
	
	// All the Sets
	
	public void setName(String N)
	{
		Name = N;
	}
	
	public void setPhone_Number(String PN)
	{
		Phone_Number = PN;
	}
	
	public void setTitle(String T)
	{
		Title.add(T);
	}
	
	// All the Gets
	
	public String getName()
	{
		return Name;
	}
	
	public String getPhone_Number()
	{
		return Phone_Number;
	}
	
	public ArrayList getAllTitles()
	{
		return Title;
	}
	
	public String getSpecficTitle(String title){
		if(Title.contains(title)){ //if it is in the list
			return Title.get(Title.indexOf(title));
		} 
		
		return "Not in List";
			
		
	}

}
