import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

public class Creation {
    static ArrayList<ComicBooks> ComicBooks =  new ArrayList<ComicBooks>();
    static ArrayList<Series> Series =  new ArrayList<Series>(); 
    static ArrayList<Subscriptions> Subscription =  new ArrayList<Subscriptions>();
    static ArrayList<Customers> Customers =  new ArrayList<Customers>();
    static DBConnector connector = new DBConnector("db-s17-group4", "k^4KddL&");

    
    public static void main(String[] args){
        readAndCreateComics();//read works for comics
        readAndCreateCustomers();//read and work on customers
        //System.out.println("Books "+ComicBooks.size());
        //System.out.println("Series " +Series.size());
        //System.out.println("Customers " +Customers.size());
        //System.out.println("Sub " +Subscription.size());
       // databaseInput();
     // deleteDatabase();
       // printList(ComicBooks);
        //queryControl(); 
        masterControl();
        
        System.out.println("Done with everything!");
        
        
        
    }
    
    public static void readAndCreateComics(){
        //Holders for all the difffent objects 
        int count =0;
        
        try{
            File text =  new File("completedata.txt");
            BufferedReader reader = new BufferedReader(new FileReader(text.getName())); //used so that we can go throguht the file
            String line = ""; //start with a black line
            line = reader.readLine(); //get the first line
            //System.out.println(line); //DBUG
            
            
            if(line.equals("Comics:")){ //this is for the comic section
                line = reader.readLine(); 
                while(!line.equals("Customers:") ){ //making sure not at end of file and in the Customers section
                //  System.out.println(line); //DEBUG
                    if(line.charAt(0) == ('\t') && line.charAt(1) != ('\t')){ //if there is only one tab for the program
                    	line = line.trim();
                        Series currentSeries = new Series(); //make the objects that we need and add the titile to these objects 
                        currentSeries.setTitle(line); 
                        line = reader.readLine(); // move to next line
                        
                        while(line.charAt(1) == ('\t')){  //see if there are two tabs 
                        	ComicBooks currentBook = new ComicBooks();
                            currentBook.setTitle(currentSeries.getTitle());
                            if(Character.isLetter(line.charAt(2))){ // checks it see if the first part of the word is a Letter meanig a publisher
                            	line = line.trim();
                                currentSeries.setPublisher(line); //add to object
                                Series.add(currentSeries);
                                line = reader.readLine();
                            } else { //we are working with the dates in the comicbook number 
                                if(line.length() == 13){
                                    line = line.substring(2, 13); //get rid of the first 2 tabs 
                                } else
                                    line = line.substring(2, 12); //get rid of the first 2 tabs 
                            //  System.out.println(line); //DEBUG
                                
                                for(String holder : line.split("\t")){
                                    if(holder.length() == 2 || holder.length() ==3 || holder.length() == 1 ){ //we are working with a booknumber
                                    //  System.out.println(holder); DEBUG
                                        currentBook.setBookNumber(Integer.parseInt(holder)); //add the comicbook object 
                                    } else{ //we are working with the date
                                    //  System.out.println(holder);
                                        int one = 0;
                                        int two = 0;
                                        int three =0;
                                        count = 0;
                                        for(String datePart: holder.split("\\/")){ //split each part of the date and send them off
                                            //System.out.println(datePart);
                                            count++;
                                            if(count == 1){
                                                one = Integer.parseInt(datePart);
                                            }
                                            if(count ==2)
                                                two = Integer.parseInt(datePart);
                                            if(count == 3){
                                                count = 0;
                                                three = Integer.parseInt(datePart)+100;
                                                currentBook.setReleaseDate(three,one-1,two); //send date values to the books and make them sql dates
                                                //System.out.println(currentBook.getReleaseDate());
                                     		  // System.out.println(currentBook.getTitle() + " " + currentBook.getBookNumber() + " " + currentBook.getReleaseDate());
                                               ComicBooks.add(currentBook); //put the book in the list
                                            //  System.out.println(three);
                                            }
                                        }
                                    }
                                    
                                }
                                line = reader.readLine();
                            }
            
                        }
                        
                    }
                }

            } 
            
        } catch (Exception e){
            
        }
    }
    
    public static void readAndCreateCustomers(){
        try{
            File text =  new File("completedata.txt");
            BufferedReader reader = new BufferedReader(new FileReader(text.getName())); //used so that we can go throguht the file
            String line = ""; //start with a black line
            line = reader.readLine(); //get the first line
            //System.out.println(line); //DBUG
        
            while(!line.equals("Customers:")){
                line = reader.readLine();
            }
        
            if (line.equals("Customers:")) { //we are in the customer section of the file 
                line = reader.readLine(); //get the next line
                while(line != null){
                    if(Character.isAlphabetic(line.charAt(0))){
                        Customers currentCustomer = new Customers(); //make the objects we are going to work with
                        Subscriptions currentSub = new Subscriptions(); 
                        currentCustomer.setName(line);
                        currentSub.setName(line);
                        line = reader.readLine(); // get next line
                
                        if(line.charAt(0) == '\t'){
                        	line = line.trim();
                            currentCustomer.setPhone_Number(line); //add the phone numbers to both 
                            currentSub.setPhone_Number(line);
                            line = reader.readLine(); //get next line
                            line = line.trim();
                            currentCustomer.setAddress(line); //add the address
                            Customers.add(currentCustomer); //add to the database we are done with cusotmers
                            line = reader.readLine();
                            int num=1;
                            
                            while( line != null && line.charAt(1) == '\t'){
                                line = line.trim();
                            	currentSub.setTitle(line);
                                line = reader.readLine();
                            }
                            
                            Subscription.add(currentSub);
                        }
                    
                    } 
                
                }
            }
        } catch (Exception e){      
    }
        
        
} 
    
   public static void printList(ArrayList<ComicBooks> list){
	   for(ComicBooks book : list){
		   System.out.println(book.getTitle() + " " + book.getBookNumber() + " " + book.getReleaseDate());
	   }
	   
   }
    
   public static void deleteDatabase(){
      // DBConnector connector = new DBConnector("db-s17-group4", "k^4KddL&");
      // connector.changingQuery("DELETE FROM [Comic Books];"); USED for schools sever
       connector.changingQuery("DELETE FROM ComicBooks;"); //Personal Server
       connector.changingQuery("DELETE FROM Subscriptions;");
       connector.changingQuery("DELETE FROM Customers;");
       connector.changingQuery("DELETE FROM Series;");


   }
    
    
    
   public static void databaseInput()
   {
      // DBConnector connector = new DBConnector("db-s17-group4", "k^4KddL&");
       
       System.out.println("Writting Series");
       for(Series current : Series)
       {
           String query = "INSERT INTO Series VALUES('"+current.getPublisher()+"' , '"+current.getTitle()+"');";
           connector.changingQuery(query);
        }
       System.out.println("Done ");
       
       System.out.println("Writting Customers");
        for(Customers current : Customers)
       {
        	current.setAddress(current.getAddress().replaceAll("'", "''"));
        	current.setName(current.getName().replaceAll("'", "''"));
        	
           String query = "INSERT INTO Customers VALUES('"+current.getName()+"' , '"+current.getAddress()+"' , '"+current.getPhone_Number()+"');";
           connector.changingQuery(query);
        }
        System.out.println("Done");
        
        System.out.println("Writting ComicBooks");
         for(ComicBooks current : ComicBooks)
       {
          // String query = "INSERT INTO [Comic Books] VALUES('"+current.getBookNumber()+"' , '"+current.getReleaseDate()+"' , '"+current.getTitle()+"');"; SCHOOL Sever 
           String query = "INSERT INTO ComicBooks VALUES('"+current.getBookNumber()+"' , '"+current.getReleaseDate()+"' , '"+current.getTitle()+"');";   //Personal Sever         
           connector.changingQuery(query);
        }
        System.out.println("Done");
        
        System.out.println("Writting Subscrtiptions");
        for(Subscriptions current : Subscription){
            ArrayList<String> sublist = current.getAllTitles();
        	current.setName(current.getName().replaceAll("'", "''"));
            for(String currentTitle : sublist)
            {
            String query = "INSERT INTO Subscriptions VALUES('"+current.getName()+"' , '"+current.getPhone_Number()+"' , '"+currentTitle+"');";
            connector.changingQuery(query);
            }
       }
        System.out.println("Done"); 
   }
 
   
   public static void masterControl(){
	   Scanner in = new Scanner(System.in);
	   String prompt ="\nWelcome please choose the action you would like to preform \n"
	   		+ "1: Search \n"
	   		+ "2: Add \n"
	   		+ "3: Update \n"
	   		+ "4: Delete \n"
	   		+ "5: Expert Users commands \n"
	   		+ "-1: Exit";
	   System.out.println(prompt);
	   int control = in.nextInt();
	   while(control != -1){
		   switch (control){
      		case 1: searchContorl();
      			System.out.println(prompt);
      			control = in.nextInt();
      			break;
      		case 2: addControl();
      			System.out.println(prompt);
      			control = in.nextInt();
      			break;
      		case 3: 
      			updateControl();
      			System.out.println(prompt);
      			control = in.nextInt();
      			break;
      		case 4: 
      			deleteControl();
      			System.out.println(prompt);
      			control = in.nextInt();
      			break;
      		default: System.out.println("You didn't enter a vaild number please try again \n"+ prompt);
      			control = in.nextInt();
      			break;
      		
      	}
	   }
	   
   }
   
   
//------------------SEARCH QUEIRES-----------------------------------------------------------------------------------   
   
   public static void searchContorl(){ //used to change the behavior of the apps
      // DBConnector connector = new DBConnector("db-s17-group4", "k^4KddL&"); //Start the conection
	   Scanner in = new Scanner(System.in);
	   String prompt ="Welcome please choose the action you would like to preform \n"
	   		+ "1: Find a customers subscriptions \n"
	   		+ "2: Find a customer based on thier phone number \n"
	   		+ "3: Find comicbooks based on the series name \n"
	   		+ "4: Find comicbooks from a specfifc date \n"
	   		+ "5: Find comicbooks from each publisher \n"
	   		+ "6: Find customers that are subsriced to a series \n"
	   		+ "7: Find comicbooks that need to be ordered on a specific date \n"
	   		+ "-1: Exit";
	   System.out.println(prompt);
	   int control = in.nextInt();
	   
	   while(control != -1){
		   switch (control){
      		case 1:
      			search1();
      			System.out.println(prompt);
      			control = in.nextInt();
      			break;
      		case 2:
      			search2();
      			System.out.println(prompt);
      			control = in.nextInt();
      			break;
      		case 3: 
      			search3();
      			System.out.println(prompt);
      			control = in.nextInt();
      			break;
      		case 4: 
      			search4();
      			System.out.println(prompt);
      			control = in.nextInt();
      			break;
      		case 5:
      			search5();
      			System.out.println(prompt);
      			control = in.nextInt();
      			break;
      		case 6: 
      			search6();
      			System.out.println(prompt);
      			control = in.nextInt();
      			break;
      		case 7:
      			search7();
      			System.out.println(prompt);
      			control = in.nextInt();
      			break;
      		default: System.out.println("You didn't enter a vaild number please try again \n"+ prompt);
      			control = in.nextInt();
      			break;
      		
      	}
	   }
	   
   }
   
  public static void search1(){
	   Scanner in = new Scanner(System.in);
	   System.out.println("Who would you like to search for?");
		String value = in.nextLine();
		value = value.replaceAll("'", "''");
		String query = "SELECT Title FROM Subscriptions WHERE Name= '"+value+"';";
		try {
			ResultSet myRs= connector.query(query);
			System.out.println("\n*************Results******************");   
				while(myRs.next()){
					System.out.println(myRs.getString("Title"));   
				   }
				System.out.println("*************End Results***************\n");
			} catch (SQLException e) {
				e.printStackTrace();
			}
  }
  
  public static void search2(){
	   Scanner in = new Scanner(System.in);
	   System.out.println("Enter a phone number *Note format: 555-555-5555* ?");
		String value = in.nextLine();
		value = value.replaceAll("'", "''");
		String query = "SELECT Name FROM Customers WHERE PhoneNumber= '"+value+"';";
		try {
			ResultSet myRs= connector.query(query);
			System.out.println("\n*************Results******************");   
				while(myRs.next()){
					System.out.println(myRs.getString("Name"));   
				   }
				System.out.println("*************End Results***************\n");
			} catch (SQLException e) {
				e.printStackTrace();
			}
 }
  
  public static void search3(){
	   Scanner in = new Scanner(System.in);
	   System.out.println("Enter a series name: ");
		String value = in.nextLine();
		value = value.replaceAll("'", "''");
		String query = "SELECT ComicBooks.Title, Book_number, Release_Date  FROM ComicBooks, Series WHERE Series.Title= '"+value+"' AND ComicBooks.Title= '"+value+"';"; //AND Series.Title= ComicBooks.Title
		try {
			ResultSet myRs= connector.query(query);
			System.out.println("\n*************Results******************");   
				while(myRs.next()){
					System.out.println(myRs.getString("Title") + " " + myRs.getString("Book_number") +" "+  myRs.getString("Release_Date") );   
				   }
				System.out.println("*************End Results***************\n");
			} catch (SQLException e) {
				e.printStackTrace();
			}
}
  
  public static void search4(){
	   Scanner in = new Scanner(System.in);
	   System.out.println("Enter a comicbook date: *Note format: Year-Month-Day* ");
		String value = in.nextLine();
		value.replaceAll("'", "''");
		String query = "SELECT ComicBooks.Title, Book_number  FROM ComicBooks WHERE Release_Date= '"+value+"';";
		try {
			ResultSet myRs= connector.query(query);
			System.out.println("\n*************Results******************");   
				while(myRs.next()){
					System.out.println(myRs.getString("Title") + " " + myRs.getString("Book_number"));   
				   }
				System.out.println("*************End Results***************\n");
			} catch (SQLException e) {
				e.printStackTrace();
			}
}
  
  public static void search5(){
	   Scanner in = new Scanner(System.in);
	   System.out.println("Enter a publisher: ");
		String value = in.nextLine();
		value = value.replaceAll("'", "''");
		String query = "SELECT ComicBooks.Title, Book_number, Release_Date FROM ComicBooks,Series WHERE Series.Publisher= '"+value+"' AND ComicBooks.Title = Series.Title;";
		try {
			ResultSet myRs= connector.query(query);
			System.out.println("\n*************Results******************");   
				while(myRs.next()){
					System.out.println(myRs.getString("Title") + " " + myRs.getString("Book_number") +" "+  myRs.getString("Release_Date"));   
				   }
				System.out.println("*************End Results***************\n");
			} catch (SQLException e) {
				e.printStackTrace();
			}
}
  
  public static void search6(){
	   Scanner in = new Scanner(System.in);
	   System.out.println("Enter a series to find subscribers: ");
		String value = in.nextLine();
		value = value.replaceAll("'", "''");
		String query = "SELECT Customers.Name,PhoneNumber FROM Customers,Subscriptions WHERE Subscriptions.Title= '"+value+"' AND Subscriptions.Name = Customers.Name;";
		try {
			ResultSet myRs= connector.query(query);
			System.out.println("\n*************Results******************");   
				while(myRs.next()){
					System.out.println(myRs.getString("Name") + " " + myRs.getString("PhoneNumber"));   
				   }
				System.out.println("*************End Results***************\n");
			} catch (SQLException e) {
				e.printStackTrace();
			}
}  
  
  public static void search7(){
	   Scanner in = new Scanner(System.in);
	   System.out.println("Enter a date to see which books need to be ordered and how many: *Note format: Year-Month-Day* ");
		String value = in.nextLine();
		value = value.replaceAll("'", "''");
		String query = "SELECT ComicBooks.Title, ComicBooks.Book_Number, Count(Subscriptions.Name) AS Count FROM ComicBooks,Subscriptions WHERE ComicBooks.Release_Date= '"+value+"' AND Subscriptions.Title=ComicBooks.Title GROUP BY(ComicBooks.Title);";
		try {
			ResultSet myRs= connector.query(query);
			System.out.println("\n*************Results******************");   
				while(myRs.next()){
					System.out.println(myRs.getString("Title") + " " + myRs.getString("Book_Number") + " Number of books to order: " + myRs.getString("Count") );   
				   }
				System.out.println("*************End Results***************\n");
			} catch (SQLException e) {
				e.printStackTrace();
			}
}
   
//---------------------------------------------------------Add Queries -----------------------------------------------------------------------------
  public static void addControl(){
	   Scanner in = new Scanner(System.in);
	   String prompt ="Welcome please choose the action you would like to preform \n"
	   		+ "1: Add new Customers \n"
	   		+ "2: Add new Subscriptions for existing customers \n"
	   		+ "-1: Exit";
	   System.out.println(prompt);
	   int control = in.nextInt();
	   while(control != -1){
		   switch (control){
     		case 1: 
     			add1();
     			System.out.println(prompt);
     			control = in.nextInt();
     			break;
     		case 2: 
     			add2();
     			System.out.println(prompt);
     			control = in.nextInt();
     			break;
     		default: System.out.println("You didn't enter a vaild number please try again \n"+ prompt);
     			control = in.nextInt();
     			break;
     		
     	}
	   }
	   
  }
   
  public static void add1(){
	   Scanner in = new Scanner(System.in);
	   System.out.println("Enter the customers name: ");
		String name = in.nextLine();
		name = name.replaceAll("'", "''");
		System.out.println("Enter the customers address: ");
		String address = in.nextLine();
		address = address.replaceAll("'", "''");
		System.out.println("Enter the customers phone number: *Note in format 555-555-5555* ");
		String phone = in.nextLine();
		phone = phone.replaceAll("'", "''");
		
		String query = "INSERT INTO Customers VALUES('"+name+"', '"+address+"', '"+phone+"' );";
		connector.changingQuery(query);
		System.out.println(name+" has been added to the database");
}
  
  public static void add2(){
	   Scanner in = new Scanner(System.in);
	   System.out.println("Enter the customers name: ");
		String name = in.nextLine();
		name = name.replaceAll("'", "''");
		System.out.println("Enter the customers phone number: *Note in format 555-555-5555* ");
		String phone = in.nextLine();
		phone = phone.replaceAll("'", "''");
		System.out.println("Enter the Title of the new subscription: ");
		String title = in.nextLine();
		title = title.replaceAll("'", "''");
		
		String query = "INSERT INTO Subscriptions(Name,Phone_Number,Title) SELECT Customers.Name, Customers.PhoneNumber,Series.Title FROM Customers,Series WHERE Customers.Name='"+name+"' AND Customers.PhoneNumber='"+phone+"' AND Series.Title ='"+title+"';";
		connector.changingQuery(query);
		System.out.println(name+" has been added to the database \n");
}

//---------------------------------------------------------Update Queries -----------------------------------------------------------------------------
  public static void updateControl(){
	   Scanner in = new Scanner(System.in);
	   String prompt ="Welcome please choose the action you would like to preform \n"
	   		+ "1: Update customer name \n"
	   		+ "2: Update customer phone number\n"
	   		+ "3: Update customer address\n"
	   		+ "4: Update comic book release date\n"
	   		+ "-1: Exit";
	   System.out.println(prompt);
	   int control = in.nextInt();
	   while(control != -1){
		   switch (control){
    		case 1: 
    			update(control);
    			System.out.println(prompt);
    			control = in.nextInt();
    			break;
    		case 2: 
    			update(control);
    			System.out.println(prompt);
    			control = in.nextInt();
    			break;
    		case 3:
    			update(control);
    			System.out.println(prompt);
    			control = in.nextInt();
    			break;
    		case 4:
    			update2();
    			System.out.println(prompt);
    			control = in.nextInt();
    			break;	
    		default: System.out.println("You didn't enter a vaild number please try again \n"+ prompt);
    			control = in.nextInt();
    			break;
    		
    	}
	   }
	   
 }
  
  public static void update(int selection){
	    Scanner in = new Scanner(System.in);
	    System.out.println("Enter the customers name you would like to change: ");
		String oldName = in.nextLine();
		oldName = oldName.replaceAll("'", "''");
		System.out.println("Enter the customers phone number: *Note in format 555-555-5555* ");
		String oldPhone = in.nextLine();
		oldPhone = oldPhone.replaceAll("'", "''");
		
	   if(selection ==1){
		   System.out.println("Enter the customers new name: *Note first and last* ");
			String name = in.nextLine();
			name = name.replaceAll("'", "''");
			String query = "UPDATE Customers SET Name='"+name+"' WHERE Customers.Name='"+oldName+"' AND Customers.PhoneNumber='"+oldPhone+"';";
			connector.changingQuery(query);
			System.out.println(name+" has been updated in the database \n");
			
	   } else if(selection ==2){
		   System.out.println("Enter the customers new phone number: *Note in format 555-555-5555* ");
			String phone = in.nextLine();
			phone = phone.replaceAll("'", "''");
			String query = "UPDATE Customers SET PhoneNumber='"+phone+"' WHERE Customers.Name='"+oldName+"' AND Customers.PhoneNumber='"+oldPhone+"';";
			connector.changingQuery(query);
			System.out.println("Phone number "+phone+" has been updated in the database \n");
			
	   } else if(selection ==3){
		    System.out.println("Enter the customers new address");
			String address = in.nextLine();
			address= address.replaceAll("'", "''");
			String query = "UPDATE Customers SET Adderss='"+address+"' WHERE Customers.Name='"+oldName+"' AND Customers.PhoneNumber='"+oldPhone+"';";
			connector.changingQuery(query);
			System.out.println("Address "+address+" has been updated in the database \n");
	   }
} 
  
public static void update2(){
	 Scanner in = new Scanner(System.in);
	    System.out.println("Enter the comicbook Title you would like to change");
		String oldBook = in.nextLine();
		oldBook = oldBook.replaceAll("'", "''");
		System.out.println("Enter the release date for the comic: *Note in the format year-month-day*");
		String oldDate = in.nextLine();
		oldDate = oldDate.replaceAll("'", "''");
		System.out.println("Enter the book number for the comic");
		String Number = in.nextLine();
		int oldNumber = Integer.parseInt(Number); 
		System.out.println("Enter the new date for the comic book: *Note in the format year-month-day*");
		String newDate = in.nextLine();
		
		String query = "UPDATE ComicBooks SET Release_Date='"+newDate+"' WHERE Title='"+oldBook+"' AND Book_Number='"+oldNumber+"' AND Release_Date='"+oldDate+"';";
		connector.changingQuery(query);
		System.out.println("The date hase been chagned from "+oldDate+" to "+newDate+" for "+oldBook+" "+oldNumber+"\n\n");
	
}

//---------------------------------------------------------Delete Queries -----------------------------------------------------------------------------
public static void deleteControl(){
	   Scanner in = new Scanner(System.in);
	   String prompt ="Welcome please choose the action you would like to preform \n"
	   		+ "1: Delete a Customer \n"
	   		+ "2: Delete a customers subscription \n"
	   		+ "-1: Exit";
	   System.out.println(prompt);
	   int control = in.nextInt();
	   while(control != -1){
		   switch (control){
  		case 1: 
  			delete1();
  			System.out.println(prompt);
  			control = in.nextInt();
  			break;
  		case 2: 
  			delete2();
  			System.out.println(prompt);
  			control = in.nextInt();
  			break;
  		default: System.out.println("You didn't enter a vaild number please try again \n"+ prompt);
  			control = in.nextInt();
  			break;
  		
  	}
	   }
	   
}
 
public static void delete1(){
	 Scanner in = new Scanner(System.in);
	    System.out.println("Enter the customers name you would like to delete: ");
		String Name = in.nextLine();
		Name = Name.replaceAll("'", "''");
		System.out.println("Enter the customers phone number: *Note in format 555-555-5555* ");
		String Phone = in.nextLine();
		Phone = Phone.replaceAll("'", "''");
		
		String query = "DELETE FROM Customers WHERE Name='"+Name+"' AND PhoneNumber='"+Phone+"';";
		connector.changingQuery(query);
		System.out.println(Name+" has been deleted from the database");
	
}

public static void delete2(){
	 Scanner in = new Scanner(System.in);
	    System.out.println("Enter the customers name you would like to delete: ");
		String Name = in.nextLine();
		Name = Name.replaceAll("'", "''");
		System.out.println("Enter the customers phone number: *Note in format 555-555-5555* ");
		String Phone = in.nextLine();
		Phone = Phone.replaceAll("'", "''");
		System.out.println("Enter the title of the series ");
		String Title = in.nextLine();
		Title = Title.replaceAll("'", "''");
		
		String query = "DELETE FROM Subscriptions WHERE Name='"+Name+"' AND Phone_Number='"+Phone+"' AND Title='"+Title+"';";
		connector.changingQuery(query);
		System.out.println(Name+" subscription to "+Title+" has been deleted from the database");
	
}
//************************************************************************************************************************************************** 
 //															ADVANCE USERS
//************************************************************************************************************************************************** 
//************************************************************************************************************************************************** 
//************************************************************************************************************************************************** 
 
   public static void queryControl(){ //used for mor adavance users who want to have custom commands
	   Scanner in = new Scanner(System.in);
      	String prompt= "Please select which action you would like to do \n"
      			+ "1: Means select queries \n"
      			+ "2: Means add queires \n"
      			+ "3: Means remove queries \n"
      			+ "4: Means custom query \n"
      			+ "-1: Means exit";
      	System.out.println(prompt);
      	int control = in.nextInt();

       while(control != -1){
       	switch (control){
       		case 1: selectQueries();
       			System.out.println(prompt);
       			control = in.nextInt();
       			break;
       		case 2: addQueries();
       			System.out.println(prompt);
       			control = in.nextInt();
       			break;
       		case 3: deleteQueries();
       			System.out.println(prompt);
       			control = in.nextInt();
       			break;
       		case 4: CustomQuery();
       			System.out.println(prompt);
       			control = in.nextInt();
       			break;
       		default: System.out.println("You didn't enter a vaild number please try again \n"+ prompt);
       			control = in.nextInt();
       			break;
       		
       	}
       }
   }
   
   public static void selectQueries(){
      // DBConnector connector = new DBConnector("db-s17-group4", "k^4KddL&"); //Start the conection
	   Scanner in = new Scanner(System.in); 
	   System.out.println("What databse would you like to prefrom SELECT? \n"
	   		+ "1) Series 2) Customers 3) Comic Books  4) Subscriptions \n"
	   		+ "If using tables in unions please use the exit with -1 and use custom query");
	   
//------------------------------Select for Series-----------------------------------------------------------	   
	   int db = in.nextInt(); //the users choice 
	   if(db == 1){ //if they choose the 1st choice
		  System.out.println("Choose the colums you wish to preform the query on: \n"
		  		+ "1) Publisher 2) Title 3) Both");
		  int colums = in.nextInt(); //used to help figure out which colums we are working with
		  boolean answer = true; //contolr for while loop to see if the value was vaild
		  
		  do {
			  	if(colums ==1 ){ //This is for the publisher table
			  		  System.out.println("Would you like to select all? \n 1) Yes 2) No");
			  		  int all = in.nextInt();
			  		  boolean correct = true;
			  		  
			  		  do {	 
			  			  if(all == 1){
			  				  String query = "SELECT Publisher FROM Series; ";
			  				  connector.query(query);
			  				  correct = false;

			  			  } else if (all == 2){ //they didn't want to slect all s
			  				  String query = "SELECT Publisher FROM Series";
			  				  connector.query(queryCreator(query, 1));
			  				  correct = false;
			  			  } else { //The messed up
			  				  System.out.println("You didn't enter a vaild choice try again");
			  				  System.out.println("Would you like to select all? \n 1) Yes 2) No");

			  			  }
			  		  } while (correct);	  
			  	answer = false; //used to control outter loop
			  
			  	} else if (colums == 2){ // This is for the Titles 
			  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
			  		  int all = in.nextInt();
			  		  boolean correct = true;
			  		  
			  		  do {	 
			  			  if(all == 1){ //choose all
			  				  String query = "SELECT Title FROM Series; ";
			  				  connector.query(query);
			  				  correct = false;

			  			  } else if (all == 2){ //they didn't want to slect all s
			  				  String query = "SELECT Title FROM Series";
			  				  connector.query(queryCreator(query, 1));
			  				  correct = false;
			  			  } else { //messed up 
			  				  System.out.println("You didn't enter a vaild choice try again");
						  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");

			  			  }
			  		  } while (correct);	  
			  	answer = false; //used to control outter loop

			  
			  	} else if (colums == 3){
			  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
			  		  int all = in.nextInt();
			  		  boolean correct = true;
			  		  
			  		  do {	 
			  			  if(all == 1){
			  				  String query = "SELECT Publisher, Title FROM Series; ";
			  				  connector.query(query);
			  				  correct = false;

			  			  } else if (all == 2){ //they didn't want to slect all s
			  				  String query = "SELECT Publisher, Title FROM Series ";
			  				  connector.query(queryCreator(query, 1));
			  				  correct = false;
			  			  } else {
			  				  System.out.println("You didn't enter a vaild choice try again");
						  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
  
			  			  }
			  		  } while (correct);	  
			  	answer = false; //used to control outter loop
			  
			  	} else {
			  		System.out.println("You didn't enter a vaild choice try again ");
			  		System.out.println("Choose the colums you wish to preform the query on: \n"
					  		+ "1) Publisher 2) Title 3) Both");
					colums = in.nextInt();

			  	}
			 }while(answer);

			  
		   }

//------------------------------Select for Customers-----------------------------------------------------------	   	   
		  else if (db ==2){
			  System.out.println("Choose the colums you wish to preform the query on: \n"
				  		+ "1) Name 2) Adderss 3) Phone Number 4) Name and Phone Number 5)Name and Adderss 6)Address and Phone Number 7) All Three ");
				  int colums = in.nextInt(); //used to help figure out which colums we are working with
				  boolean answer = true; //contolr for while loop to see if the value was vaild
				  
				  do {
					  	if(colums ==1 ){ //This is for the publisher table
					  		  System.out.println("Would you like to select all? \n 1) Yes 2) No");
					  		  int all = in.nextInt();
					  		  boolean correct = true;
					  		  
					  		  do {	 
					  			  if(all == 1){
					  				  String query = "SELECT Name FROM Customers; ";
					  				  connector.query(query);
					  				  correct = false;

					  			  } else if (all == 2){ //they didn't want to slect all s
					  				  String query = "SELECT Name FROM Customers ";
					  				  connector.query(queryCreator(query, 1));
					  				  correct = false;
					  			  } else { //The messed up
					  				  System.out.println("You didn't enter a vaild choice try again");
					  				  System.out.println("Would you like to select all? \n 1) Yes 2) No");

					  			  }
					  		  } while (correct);	  
					  	answer = false; //used to control outter loop
					  
					  	} else if (colums == 2){ // This is for the Titles 
					  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
					  		  int all = in.nextInt();
					  		  boolean correct = true;
					  		  
					  		  do {	 
					  			  if(all == 1){ //choose all
					  				  String query = "SELECT Adderss FROM Customers; ";
					  				  connector.query(query);
					  				  correct = false;

					  			  } else if (all == 2){ //they didn't want to slect all s
					  				  String query = "SELECT Adderss FROM Customers";
					  				  connector.query(queryCreator(query, 1));
					  				  correct = false;
					  			  } else { //messed up 
					  				  System.out.println("You didn't enter a vaild choice try again");
								  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");

					  			  }
					  		  } while (correct);	  
					  	answer = false; //used to control outter loop

					  
					  	} else if (colums == 3){
					  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
					  		  int all = in.nextInt();
					  		  boolean correct = true;
					  		  
					  		  do {	 
					  			  if(all == 1){
					  				  String query = "SELECT PhoneNumber FROM Customers; ";
					  				  connector.query(query);
					  				  correct = false;

					  			  } else if (all == 2){ //they didn't want to slect all s
					  				  String query = "SELECT PhoneNumber FROM Customers ";
					  				  connector.query(queryCreator(query, 1));
					  				  correct = false;
					  			  } else {
					  				  System.out.println("You didn't enter a vaild choice try again");
								  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
		  
					  			  }
					  		  } while (correct);	  
					  	answer = false; //used to control outter loop
					  
					  	}  else if (colums == 4){
					  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
					  		  int all = in.nextInt();
					  		  boolean correct = true;
					  		  
					  		  do {	 
					  			  if(all == 1){
					  				  String query = "SELECT Name, PhoneNumber FROM Customers; ";
					  				  connector.query(query);
					  				  correct = false;

					  			  } else if (all == 2){ //they didn't want to slect all s
					  				  String query = "SELECT Name, PhoneNumber FROM Customers ";
					  				  connector.query(queryCreator(query, 1));
					  				  correct = false;
					  			  } else {
					  				  System.out.println("You didn't enter a vaild choice try again");
								  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
		  
					  			  }
					  		  } while (correct);	  
					  	answer = false; //used to control outter loop 
					  	
					  	}  else if (colums == 5){
					  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
					  		  int all = in.nextInt();
					  		  boolean correct = true;
					  		  
					  		  do {	 
					  			  if(all == 1){
					  				  String query = "SELECT Name, Adderss FROM Customers; ";
					  				  connector.query(query);
					  				  correct = false;

					  			  } else if (all == 2){ //they didn't want to slect all s
					  				  String query = "SELECT Name, Adderss FROM Customers ";
					  				  connector.query(queryCreator(query, 1));
					  				  correct = false;
					  			  } else {
					  				  System.out.println("You didn't enter a vaild choice try again");
								  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
		  
					  			  }
					  		  } while (correct);	  
					  	answer = false; //used to control outter loop
					  	
					  	}  else if (colums == 6){
					  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
					  		  int all = in.nextInt();
					  		  boolean correct = true;
					  		  
					  		  do {	 
					  			  if(all == 1){
					  				  String query = "SELECT Address, PhoneNumber FROM Customers; ";
					  				  connector.query(query);
					  				  correct = false;

					  			  } else if (all == 2){ //they didn't want to slect all s
					  				  String query = "SELECT Address, PhoneNumber FROM Customers ";
					  				  connector.query(queryCreator(query, 1));
					  				  correct = false;
					  			  } else {
					  				  System.out.println("You didn't enter a vaild choice try again");
								  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
		  
					  			  }
					  		  } while (correct);	  
					  	answer = false; //used to control outter loop
					  	
					  	}  else if (colums == 7){
					  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
					  		  int all = in.nextInt();
					  		  boolean correct = true;
					  		  
					  		  do {	 
					  			  if(all == 1){
					  				  String query = "SELECT Name,Adderss,PhoneNumber FROM Customers; ";
					  				  connector.query(query);
					  				  correct = false;

					  			  } else if (all == 2){ //they didn't want to slect all s
					  				  String query = "SELECT Name,Adderss,PhoneNumber FROM Customers ";
					  				  connector.query(queryCreator(query, 1));
					  				  correct = false;
					  			  } else {
					  				  System.out.println("You didn't enter a vaild choice try again");
								  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
		  
					  			  }
					  		  } while (correct);	  
					  	answer = false; //used to control outter loop
					  	}else {
					  		System.out.println("You didn't enter a vaild choice try again ");
					  		System.out.println("Choose the colums you wish to preform the query on: \n"
							  		+ "1) Publisher 2) Title 3) Both");
							colums = in.nextInt();

					  	}
					 }while(answer);

			  
//------------------------------Select for Comic Books-----------------------------------------------------------	   
	   } else if (db == 3){
		   System.out.println("Choose the colums you wish to preform the query on: \n"
			  		+ "1) Book Number 2) Release Date 3) Title 4) Book Number and Release Date 5)Book Number and Title 6)Book Number and Release Date 7) All Three ");
			  int colums = in.nextInt(); //used to help figure out which colums we are working with
			  boolean answer = true; //contolr for while loop to see if the value was vaild
			  
			  do {
				  	if(colums ==1 ){ //This is for the publisher table
				  		  System.out.println("Would you like to select all? \n 1) Yes 2) No");
				  		  int all = in.nextInt();
				  		  boolean correct = true;
				  		  
				  		  do {	 
				  			  if(all == 1){
				  				//  String query = "SELECT Book_Number FROM [Comic Books]; "; SCHOOL
				  				  String query = "SELECT Book_Number FROM ComicBooks; "; //Personal

				  				  connector.query(query);
				  				  correct = false;

				  			  } else if (all == 2){ //they didn't want to slect all s
				  				 // String query = "SELECT Book_Number FROM [Comic Books] "; SCHOOL
				  				  String query = "SELECT Book_Number FROM ComicBooks ";			//Personal	  				  
				  				  connector.query(queryCreator(query, 1));
				  				  correct = false;
				  			  } else { //The messed up
				  				  System.out.println("You didn't enter a vaild choice try again");
				  				  System.out.println("Would you like to select all? \n 1) Yes 2) No");

				  			  }
				  		  } while (correct);	  
				  	answer = false; //used to control outter loop
				  
				  	} else if (colums == 2){ // This is for the Titles 
				  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
				  		  int all = in.nextInt();
				  		  boolean correct = true;
				  		  
				  		  do {	 
				  			  if(all == 1){ //choose all
				  				 // String query = "SELECT Release_Date FROM [Comic Books]; "; //SCHOOL
				  				  String query = "SELECT Release_Date FROM ComicBooks; ";	//Personal			  				  
				  				  connector.query(query);
				  				  correct = false;

				  			  } else if (all == 2){ //they didn't want to slect all s
				  				 // String query = "SELECT Release_Date FROM [Comic Books] "; //SChool
				  				  String query = "SELECT Release_Date FROM ComicBooks ";	//Personal			  				  
				  				  connector.query(queryCreator(query, 1));
				  				  correct = false;
				  			  } else { //messed up 
				  				  System.out.println("You didn't enter a vaild choice try again");
							  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");

				  			  }
				  		  } while (correct);	  
				  	answer = false; //used to control outter loop

				  
				  	} else if (colums == 3){
				  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
				  		  int all = in.nextInt();
				  		  boolean correct = true;
				  		  
				  		  do {	 
				  			  if(all == 1){
				  				  //String query = "SELECT Title FROM [Comic Books] ; ";
				  				  String query = "SELECT Title FROM ComicBooks ; ";				  				  
				  				  connector.query(query);
				  				  correct = false;

				  			  } else if (all == 2){ //they didn't want to slect all s
				  	//			  String query = "SELECT Title FROM [Comic Books] ";
				  				  String query = "SELECT Title FROM ComicBooks ";				  				  
				  				  connector.query(queryCreator(query, 1));
				  				  correct = false;
				  			  } else {
				  				  System.out.println("You didn't enter a vaild choice try again");
							  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
	  
				  			  }
				  		  } while (correct);	  
				  	answer = false; //used to control outter loop
				  
				  	}  else if (colums == 4){
				  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
				  		  int all = in.nextInt();
				  		  boolean correct = true;
				  		  
				  		  do {	 
				  			  if(all == 1){
				  				//  String query = "SELECT Book_Number, Release_Date FROM [Comic Books]; ";
				  				  String query = "SELECT Book_Number, Release_Date FROM ComicBooks; ";				  				  
				  				  connector.query(query);
				  				  correct = false;

				  			  } else if (all == 2){ //they didn't want to slect all s
				  				 // String query = "SELECT Book_Number, Release_Date FROM [Comic Books] ";
				  				  String query = "SELECT Book_Number, Release_Date FROM ComicBooks ";				  				  
				  				  connector.query(queryCreator(query, 1));
				  				  correct = false;
				  			  } else {
				  				  System.out.println("You didn't enter a vaild choice try again");
							  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
	  
				  			  }
				  		  } while (correct);	  
				  	answer = false; //used to control outter loop 
				  	
				  	}  else if (colums == 5){
				  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
				  		  int all = in.nextInt();
				  		  boolean correct = true;
				  		  
				  		  do {	 
				  			  if(all == 1){
				  				 // String query = "SELECT Book_Number, Title FROM [Comic Books]; ";
				  				  String query = "SELECT Book_Number, Title FROM ComicBooks; ";				  				  
				  				  connector.query(query);
				  				  correct = false;

				  			  } else if (all == 2){ //they didn't want to slect all s
				  				 // String query = "SELECT Book_Number, Title FROM [Comic Books] ";
				  				  String query = "SELECT Book_Number, Title FROM ComicBooks";				  				  
				  				  connector.query(queryCreator(query, 1));
				  				  correct = false;
				  			  } else {
				  				  System.out.println("You didn't enter a vaild choice try again");
							  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
	  
				  			  }
				  		  } while (correct);	  
				  	answer = false; //used to control outter loop
				  	
				  	}  else if (colums == 6){
				  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
				  		  int all = in.nextInt();
				  		  boolean correct = true;
				  		  
				  		  do {	 
				  			  if(all == 1){
				  			//	  String query = "SELECT Title, Release_Date FROM [Comic Books]; ";
				  				  String query = "SELECT Title, Release_Date FROM ComicBooks; ";				  				  
				  				  connector.query(query);
				  				  correct = false;

				  			  } else if (all == 2){ //they didn't want to slect all s
				  				//  String query = "SELECT Title, Release_Date FROM [Comic Books] ";
				  				  String query = "SELECT Title, Release_Date FROM ComicBooks";				  				  
				  				  connector.query(queryCreator(query, 1));
				  				  correct = false;
				  			  } else {
				  				  System.out.println("You didn't enter a vaild choice try again");
							  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
	  
				  			  }
				  		  } while (correct);	  
				  	answer = false; //used to control outter loop
				  	
				  	}  else if (colums == 7){
				  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
				  		  int all = in.nextInt();
				  		  boolean correct = true;
				  		  
				  		  do {	 
				  			  if(all == 1){
				  				//  String query = "SELECT Book_Number, Release_Date, Title FROM [Comic Books]; ";
				  				  String query = "SELECT Book_Number, Release_Date, Title FROM ComicBooks; ";				  				  
				  				  connector.query(query);
				  				  correct = false;

				  			  } else if (all == 2){ //they didn't want to slect all s
				  			//	  String query = "SELECT Book_Number, Release_Date, Title FROM [Comic Books] ";
				  				  String query = "SELECT Book_Number, Release_Date, Title FROM ComicBooks";				  				  
				  				  connector.query(queryCreator(query, 1));
				  				  correct = false;
				  			  } else {
				  				  System.out.println("You didn't enter a vaild choice try again");
							  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
	  
				  			  }
				  		  } while (correct);	  
				  	answer = false; //used to control outter loop
				  	}else {
				  		System.out.println("You didn't enter a vaild choice try again ");
				  		System.out.println("Choose the colums you wish to preform the query on: \n"
						  		+ "1) Publisher 2) Title 3) Both");
						colums = in.nextInt();

				  	}
				 }while(answer);

//------------------------------Select for Subscriptions-----------------------------------------------------------	   

		   
	   } else if (db ==4){
		   System.out.println("Choose the colums you wish to preform the query on: \n"
			  		+ "1) Name 2) Phone Number 3) Title 4) Name and Phone Number 5) Name and Title 6) Phone Number and Title 7) All Three ");
			  int colums = in.nextInt(); //used to help figure out which colums we are working with
			  boolean answer = true; //contolr for while loop to see if the value was vaild
			  
			  do {
				  	if(colums ==1 ){ //This is for the publisher table
				  		  System.out.println("Would you like to select all? \n 1) Yes 2) No");
				  		  int all = in.nextInt();
				  		  boolean correct = true;
				  		  
				  		  do {	 
				  			  if(all == 1){
				  				  String query = "SELECT Name FROM Subscriptions; ";
				  				  connector.query(query);
				  				  correct = false;

				  			  } else if (all == 2){ //they didn't want to slect all s
				  				  String query = "SELECT Name FROM Subscriptions ";
				  				  connector.query(queryCreator(query, 1));
				  				  correct = false;
				  			  } else { //The messed up
				  				  System.out.println("You didn't enter a vaild choice try again");
				  				  System.out.println("Would you like to select all? \n 1) Yes 2) No");

				  			  }
				  		  } while (correct);	  
				  	answer = false; //used to control outter loop
				  
				  	} else if (colums == 2){ // This is for the Titles 
				  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
				  		  int all = in.nextInt();
				  		  boolean correct = true;
				  		  
				  		  do {	 
				  			  if(all == 1){ //choose all
				  				  String query = "SELECT Phone_Number FROM Subscriptions; ";
				  				  connector.query(query);
				  				  correct = false;

				  			  } else if (all == 2){ //they didn't want to slect all s
				  				  String query = "SELECT Phone_Number FROM Subscriptions ";
				  				  connector.query(queryCreator(query, 1));
				  				  correct = false;
				  			  } else { //messed up 
				  				  System.out.println("You didn't enter a vaild choice try again");
							  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");

				  			  }
				  		  } while (correct);	  
				  	answer = false; //used to control outter loop

				  
				  	} else if (colums == 3){
				  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
				  		  int all = in.nextInt();
				  		  boolean correct = true;
				  		  
				  		  do {	 
				  			  if(all == 1){
				  				  String query = "SELECT Title FROM Subscriptions ; ";
				  				  connector.query(query);
				  				  correct = false;

				  			  } else if (all == 2){ //they didn't want to slect all s
				  				  String query = "SELECT Title FROM Subscriptions ";
				  				  connector.query(queryCreator(query, 1));
				  				  correct = false;
				  			  } else {
				  				  System.out.println("You didn't enter a vaild choice try again");
							  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
	  
				  			  }
				  		  } while (correct);	  
				  	answer = false; //used to control outter loop
				  
				  	}  else if (colums == 4){
				  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
				  		  int all = in.nextInt();
				  		  boolean correct = true;
				  		  
				  		  do {	 
				  			  if(all == 1){
				  				  String query = "SELECT Name, Phone_Number FROM Subscriptions; ";
				  				  connector.query(query);
				  				  correct = false;

				  			  } else if (all == 2){ //they didn't want to slect all s
				  				  String query = "SELECT Name, Phone_Number FROM Subscriptions ";
				  				  connector.query(queryCreator(query, 1));
				  				  correct = false;
				  			  } else {
				  				  System.out.println("You didn't enter a vaild choice try again");
							  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
	  
				  			  }
				  		  } while (correct);	  
				  	answer = false; //used to control outter loop 
				  	
				  	}  else if (colums == 5){
				  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
				  		  int all = in.nextInt();
				  		  boolean correct = true;
				  		  
				  		  do {	 
				  			  if(all == 1){
				  				  String query = "SELECT Name, Title FROM Subscriptions; ";
				  				  connector.query(query);
				  				  correct = false;

				  			  } else if (all == 2){ //they didn't want to slect all s
				  				  String query = "SELECT Name, Title FROM Subscriptions ";
				  				  connector.query(queryCreator(query, 1));
				  				  correct = false;
				  			  } else {
				  				  System.out.println("You didn't enter a vaild choice try again");
							  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
	  
				  			  }
				  		  } while (correct);	  
				  	answer = false; //used to control outter loop
				  	
				  	}  else if (colums == 6){
				  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
				  		  int all = in.nextInt();
				  		  boolean correct = true;
				  		  
				  		  do {	 
				  			  if(all == 1){
				  				  String query = "SELECT Title, Phone_Number FROM Subscriptions; ";
				  				  connector.query(query);
				  				  correct = false;

				  			  } else if (all == 2){ //they didn't want to slect all s
				  				  String query = "SELECT Title, Phone_Number FROM Subscriptions ";
				  				  connector.query(queryCreator(query, 1));
				  				  correct = false;
				  			  } else {
				  				  System.out.println("You didn't enter a vaild choice try again");
							  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
	  
				  			  }
				  		  } while (correct);	  
				  	answer = false; //used to control outter loop
				  	
				  	}  else if (colums == 7){
				  		 System.out.println("Would you like to select all? \n 1) Yes 2) No");
				  		  int all = in.nextInt();
				  		  boolean correct = true;
				  		  
				  		  do {	 
				  			  if(all == 1){
				  				  String query = "SELECT Name, Phone_Number , Title FROM Subscriptions; ";
				  				  connector.query(query);
				  				  correct = false;

				  			  } else if (all == 2){ //they didn't want to slect all s
				  				  String query = "SELECT Name, Phone_Number, Title FROM Subscriptions ";
				  				  connector.query(queryCreator(query, 1));
				  				  correct = false;
				  			  } else {
				  				  System.out.println("You didn't enter a vaild choice try again");
							  	  System.out.println("Would you like to select all? \n 1) Yes 2) No");
	  
				  			  }
				  		  } while (correct);	  
				  	answer = false; //used to control outter loop
				  	}else {
				  		System.out.println("You didn't enter a vaild choice try again ");
				  		System.out.println("Choose the colums you wish to preform the query on: \n"
						  		+ "1) Publisher 2) Title 3) Both");
						colums = in.nextInt();

				  	}
				 }while(answer);

	   } else if(db == -1){
		   return;
	   } else {
		   System.out.println("You entered a invalid number please try again ");
		   selectQueries();
	   }
	   String query = in.nextLine();
	   System.out.println(query);

   
   
   }
   
   public static void addQueries(){
	   
   }
   
   public static void deleteQueries(){
	   
   }
   
   public static void CustomQuery(){
	   Scanner in = new Scanner(System.in);
	   System.out.println("Enter your custom query ");
	   String query = in.nextLine();
	   query.replaceAll("'", "''");
      // DBConnector connector = new DBConnector("db-s17-group4", "k^4KddL&"); //Start the conection
       connector.query(query);
	   
   }
   
   
   public static String queryCreator(String datbase , int action){ //1 = select 2= add 3= delete
	   Scanner in = new Scanner(System.in);
	   String query ="";
	   if(action == 1){ //we are selecting
		   System.out.println("Enter your WHERE and/or any other SQL clauses");
		   String clause = in.nextLine();
		   query = datbase + clause; 
	   } else if (action == 2){
		   
	   } else {
		   
	   }
	   
	   query = query.replaceAll("'", "''");
	   System.out.println(query);
	   return query;
   }
   
  
   
  }  
