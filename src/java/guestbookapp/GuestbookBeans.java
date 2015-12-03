/*
 * (Guestbook Application) Create a JSF web app that allows users to sign and view a guest-book. 
Use the Guestbook database to store guestbook entries. [Note: A SQL script to create the
Guestbook database is provided in the examples directory for this chapter.] The Guestbook database
has a single table, Messages, which has four columns: Date, Name, Email and Message. 
The database already contains a few sample entries. Using the AddressBook app in S
ection 30.2 as your guide, cre-ate two Facelets pages and a managed bean. 
The index.xhtml page should show the Guestbook en-tries in tabular format and 
should provide a button to add an entry to the Guestbook. 
When the user clicks this button, display an addentry.xhtml page. 
Provide h:inputText elements for the userâ€™s name and email address, 
an h:inputTextarea for the message and a Sign Guestbook button to 
sub-mit the form. When the form is submitted, you should store in the 
Guestbook database a new entry containing the userâ€™s input and the date of the entry.


CIS5200 Fall 2015
Project 12
Nikkita Hirayama
 */
package guestbookapp;


import java.text.SimpleDateFormat;
import java.util.Date;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import javax.faces.bean.ManagedBean;

import javax.sql.rowset.CachedRowSet;

@ManagedBean( name="guestbookapp" )
public class GuestbookBeans
{
   // instance variables that represent a guest
   private String date;
   private String name;
   private String email;
   private String message;
  

   // allow the server to inject the Data
  // @Resource( name="jdbc/guestbook" )
   //DataSource dataSource;
     private String URL = "jdbc:derby://localhost:1527/GuestBook" ;
    private String USERNAME = "APP" ;
    private String PASSWORD = "APP" ;
   
   // get the date
   public String getDate()
   {
      return date;
   } // end method getDate

   // set the date
   public void setDate(String date)
   {
       this.date = date;
      
   } // end method setDate

   // get the name
   public String getName()
   {
      return name;
   } // end method getName

   // set the  name
   public void setName( String name )
   {
      this.name = name;
   } // end method setName

   // get the email
   public String getEmail()
   {
      return email;
   } // end method getEmail

   // set the email
   public void setEmail( String email )
   {
      this.email = email;
   } // end method setEmail

   // get the message
   public String getMessage()
   {
      return message;
   } // end method getMessage

   // set the message
   public void setMessage( String message )
   {
      this.message = message;
   } // end method setMessage

   
   // save a new guest entry
   public String save() throws SQLException, ParseException
   {
       
       //obtain a connection
       Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    

      
      // check whether connection was successful
      if ( connection == null )
         throw new SQLException( "Unable to connect to DataSource" );

      try
      {
         // create a PreparedStatement to insert a new guest entry
         PreparedStatement addEntry =
            connection.prepareStatement( "INSERT INTO MESSAGES " +
               "(DATE, NAME, EMAIL, MESSAGE)" +
               "VALUES ( ?, ?, ?, ? )" );
         
        SimpleDateFormat outputformatter = new SimpleDateFormat("DD/MM/YY");
       
       // Date today = (Date)Calendar.getInstance().getTime();
       Date today = new Date(); 
       //String saveDate = outputformatter.format(date);
        
         // specify the PreparedStatement's arguments
        
         addEntry.setString( 1, new SimpleDateFormat("MM/dd/yyyy").format(today));
         addEntry.setString( 2, getName() );
         addEntry.setString( 3, getEmail() );
         addEntry.setString( 4, getMessage() );

         addEntry.executeUpdate(); // insert the entry
         return "index"; // go back to index.xhtml page
      } // end try
      finally
      {
         connection.close(); // return this connection to pool
      } // end finally
   } // end method save

   // return a ResultSet of entries
   public ResultSet getGuests() throws SQLException
   {
     

      // obtain a connection from the connection 
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
      // check whether connection was successful
      if ( connection == null )
         throw new SQLException( "Unable to connect to DataSource" );

      try
      {
         // create a PreparedStatement to insert a new address book entry
         PreparedStatement getGuests = connection.prepareStatement(
            "SELECT DATE, NAME, EMAIL, MESSAGE " +
            "FROM MESSAGES ORDER BY DATE" );

         CachedRowSet rowSet = new com.sun.rowset.CachedRowSetImpl();
         rowSet.populate( getGuests.executeQuery() );
         return rowSet; 
      } // end try
      finally
      {
         connection.close(); // return this connection to pool
      } // end finally
   } // end method 
} // end class 