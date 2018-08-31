import java.sql.Date;

/**
     Edward
     **/


public class ComicBooks
{
       private int BookNumber;
       private Date ReleaseDate; // Note this is an object and use the constructor that Date(int year, int month, int date);
       private String Title;
       
    public ComicBooks(int BookNumber, Date ReleaseDate, String Title)
    {
       this.BookNumber = BookNumber; 
       this.ReleaseDate = ReleaseDate;
       this.Title = Title;
    }


    public ComicBooks() {
		
	}


	public void setBookNumber(int x) {
        this.BookNumber = x;
    }
    
    public int getBookNumber() {
        return BookNumber;
    }

    public void setReleaseDate(int one, int two, int three) {
        Date date = new Date(one,two,three);
    	this.ReleaseDate = date;
    }
    
    public Date getReleaseDate() {
        return ReleaseDate;
    }

     public void setTitle(String Title) {
        this.Title = Title;
    }
    
    public String getTitle() {
        return this.Title;
    }
  }

