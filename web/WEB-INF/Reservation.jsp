<?xml version = "1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict/dtd">

<html xmlns = "http://www.w3.org/1999/xhtml">
<%-- META data for HTML head and formatting --%> 

<%-- overall import of SQL functionality for JSP file and usage, primarily added as a catch all --%> 
<%@page import="java.sql.*"%>

<%-- Import of Java linked SQL functionality involving creating and handling connections --%> 
<%@page import="java.sql.Connection"%>

<%-- Import of Java linked SQL functionality for loading and preparing of API drivers and associated resources --%> 
<%@page import="java.sql.DriverManager"%>

<%-- Import of Java linked SQL functionality to accommodate packaging and deploying of SQL commands --%> 
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Statement"%>

<%-- Import of Java linked SQL functionality to accommodate returning data from packaged SQL commands --%>
<%@page import="java.sql.ResultSet"%>

<%-- Import of Java linked SQL functionality to allow the processing of errors or exceptions experienced through computation --%> 
<%@page import="java.sql.SQLException"%>

<%-- Import of Java linked SQL functionality to branch communication between java and mysql --%> 
<%@page import="com.mysql.jdbc.Driver"%>

<%-- Import of Java time, date and I/O data handling classes for formatting and displaying with partial comparison operations --%> 
<%@page import="java.util.Date,java.util.Calendar,java.io.*,java.util.Enumeration"%>
<%@page import="java.util.concurrent.TimeUnit"%>
<%@page import="java.text.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>

		<%-- Establishing connection to the database with constructors initialised --%> 
		<%
			String userDB = "root";
			String pwdDB = "admin";
			String DBconnectURL = "jdbc:mysql://localhost:3307/menu_schema?";
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection DBconnection = null;
			PreparedStatement ResPrep = null;
			PreparedStatement ResPutter = null;
			ResultSet ResResult = null;
		%>

<%-- META data for HTML head and formatting, plus page title with CSS text size formatting --%> 
	<head>
		<meta http-equiv = "refresh" content = "60" />
		
		<title> Customer Reservations </title>
		
		<style type = "text/css">
			.big { }
		</style>
	</head>
	
	<%-- START of body... the option to view the menu is presented to the user --%> 
	<body>
		<form action = "/RestaurantMenu/Menu" method = "post">
						If you would would like to return to our menu...
						<label><input type = "submit" value = "Show the menu" /></label>
		</form>
		
		<%
			// variables created to store user entries during login attempt with comparison variables created also...
			String name = request.getParameter("userName");
			String gatekeeper = request.getParameter("master");
			// if the username and password is not exactly as 'admin' and 'admin' then login will not be permitted
			String user = "admin";
			String password = "admin";
			
			// Establishing connection to database using properties supplied
			DBconnection = DriverManager.getConnection(DBconnectURL, userDB, pwdDB);
			// Basic query for pulling data for menu - packaged for use by mySQL
			ResPrep = DBconnection.prepareStatement("select * from reservations order by id;");
			// Execution of the query for pulling of data to be use for use from running memory during session
			ResResult = ResPrep.executeQuery();
			
			// Stub entries created and initialised for all data entered by user when making reservation
			String putterfirstname = "Free";
			String putterlastname = "Free";
			String putteraddress = "Stub Address";
			String putterTelephone = "5551234";
			String putterParty = "1";
			String putterTable = "1";
			String TestDate = " ";
			String Bookdate = " ";
			
			// Stub entries created and initialised for the purpose of data clearance per table respectively
			String putterfirstnameClear = "...";
			String putterlastnameClear = "...";
			String putteraddressClear = "...";
			String putterTelephoneClear = "...";
			String putterPartyClear = "...";
			String putterDateClear = "...";
		
			// Get string values of data entered by user through provided form
			putterfirstname = request.getParameter("customerFirstName");
			putterlastname = request.getParameter("customerLastName");
			putteraddress = request.getParameter("customerAddress");
			putterTelephone = request.getParameter("customerTelephone");
			putterParty = request.getParameter("customerParty");
			putterTable = request.getParameter("customerTable");
			
			// Created a current time, date and calendar entry for respective use 
			Date Nowdate = Calendar.getInstance().getTime(); 	// Created current date and time object
			Calendar NowdateCal = Calendar.getInstance(); 		// Created calendar object
			NowdateCal.setTime(Nowdate);						// Tied in time for the Calendar object
			
			// Contains data entered into date field as string and sets to base value if null until later entry
			TestDate = request.getParameter("customerDate");
			if(TestDate==null){TestDate = "1111-11-11";}
			
			// Creates a new date display format for new reservation date and applies accordingly to reservation date object
			DateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");	// Created display format for date/time/etc...
			Date Bookingdate = NowdateCal.getTime();					// Created reservation date and time object
			Bookdate = myFormat.format(Bookingdate);					// Applied format for display
			if(Bookdate==null){Bookdate = "1111-11-11";}				// Sets to base value if null until later entry
			
			//Left this line in... when it should have been removed... Apologies "Date ActualBookingdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bookdate);"
			
			Date ScheduledDay = new SimpleDateFormat("yyyy-MM-dd").parse(TestDate); // Created new Date Object to capture date user entered and then applied the correct date and time display format 
			String strDate = myFormat.format(ScheduledDay); // Converted over to string for display
			String putterDate = strDate; // Swapped over to variable used for SQL data updating...
			
			// Two string commands created to accommodate either Updating of a database entry or Updating of a database entry with no data for clearance
			String sqlUpdateTable = "update reservations set Firstname = '" + putterfirstname + "', Lastname = '" + putterlastname + "', Address = '" + putteraddress + "', Telephone = '" + putterTelephone +  "', Date = '" + putterDate + "', NumberInParty = '" + putterParty + "' where id = '" + putterTable + "';";
			String sqlUpdateClear = "update reservations set Firstname = '" + putterfirstnameClear + "', Lastname = '" + putterlastnameClear + "', Address = '" + putteraddressClear + "', Telephone = '" + putterTelephoneClear +  "', Date = '" + putterDateClear + "', NumberInParty = '" + putterPartyClear + "' where id = '" + putterTable + "';";
			
			// If the date booked within the database is beyond today then update the database
			if(ScheduledDay.compareTo(Nowdate) > 0){
				ResPutter = DBconnection.prepareStatement(sqlUpdateTable);
				ResPutter.executeUpdate();
			}else{ // If this is not the case then clear the entry within the database
				ResPutter = DBconnection.prepareStatement(sqlUpdateClear);
				ResPutter.executeUpdate();
			}
			
			// if there is an entry in the login fields and the admin creds are entered correctly then load the reservation system...
			if(name!=null && name.equals(user) && gatekeeper.equals(password)){
		%>
				<%-- Initial reservations are shown in table format --%>
				<table border="1">
				<tr>
					<td>First name</td>
					<td>Last name</td>
					<td>Address</td>
					<td>Telephone</td>
					<td>Date</td>
					<td>NumberInParty</td>
					<td>Table No.</td>
				</tr>
				<p>Current Time: <%= new java.util.Date() %></p>
				<%
				
				while(ResResult.next()){%>
					<tr>
						<td><%=ResResult.getString("Firstname") %></td>
						<td><%=ResResult.getString("Lastname") %></td>
						<td><%=ResResult.getString("Address") %></td>
						<td><%=ResResult.getString("Telephone") %></td>
						<td><%=ResResult.getString("Date") %></td>
						<td><%=ResResult.getString("NumberInParty") %></td>
						<td><%=ResResult.getString("theTable") %></td>
					</tr>
				<%}
				
				%>
				
				<%-- Form for reservation entry created and presented to the user with appropriate date input sanitisation where applicable --%>
				<p>Would you like to create a reservation?</p>
				<form action = "Reservation.jsp" method = "post">
					<p>	The first name <input type = "text" name="customerFirstName" /><br>
					    The last name <input type = "text" name="customerLastName" /><br>
						The address <input type = "text" name="customerAddress" /><br>
						The telephone <input type = "text" name="customerTelephone" /><br>
						The booking date 'YYYY-MM-DD' <input type="text" minlength="10" maxlength="10" name="customerDate" placeholder="YYYY-MM-DD" required pattern="(?:19|20)[0-9]{2}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-9])|(?:(?!02)(?:0[1-9]|1[0-2])-(?:30))|(?:(?:0[13578]|1[02])-31))" title="YYYY-MM-DD"/><br>
						The size of party <input type = "text" name="customerParty" /><br>
						The table <input type = "text" name="customerTable" /><br>
						<input type = "submit" value = "Save"> <button type="reset" value="reset">Clear</button>
					</p>
				
				</form>
				
				<p>Please review current bookings and make a new one if required...</p>
		
		<%
			}
			else{ // redirect back to login screen 
		%>
				<form action = "Reservation.jsp" method = "post">
					<p>	Username <input type = "text" name="userName" />
					    Password <input type = "text" name="master" /><br/>
						<input type = "submit" value = "Login">
					</p>
				
				</form>
		<%
			}
			
			// close SQL connection objects for memory management purposes
			ResPrep.close();
			ResPutter.close();
			
		%>
	</body>
</html>