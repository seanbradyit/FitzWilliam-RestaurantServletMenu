/*
 */
/**
 * @author seanb
 * Sean Brady 2022 - Project 1 - April 1st - First attempt for review...
 */

import javax.servlet.*; // class imported for servlet & runtime environment communication
import javax.servlet.http.*; // class imported for servlet & HTTP communication
import java.io.*; // class imported for standard I/O functionality
import javax.servlet.annotation.WebServlet; // class imported for servlet declaration and processing
import java.text.*; // class imported for data processing
import java.sql.*; // class imported for JDBC API sync and SQL language processing
import java.util.Properties; // class imported to accomodate stream parameters

@WebServlet(name="restaurant",urlPatterns={"/restaurant"}) // servlet name declaration and URL path

public class RestaurantMenu extends HttpServlet { // class created which uses the HttpServlet functionality
    
    private PreparedStatement Puller; // container created to contain and transport SQL statement
    private Connection connection; // object instantiated to establish connection to database
    
    public void init( ServletConfig config ) throws ServletException // initialising the servlet and it's configuration 
    {
        Properties properties = new Properties(); // object created to contain and transport parameters
        
        properties.put("user","root"); // collecting and attaching username parameter for DB connection
        properties.put("password","admin"); // collecting and attaching password parameter for DB connection
        
        try{
            Class.forName("com.mysql.jdbc.Driver"); // declaring driver for JDBC API
            
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/menu_schema?", properties); // establishing connection to database with credentials
            Puller = connection.prepareStatement("select * from menu order by id;"); // declaring SQL statement to use
            //----------------------
            // 2 minute update to this DB - created in MySQL Workbench via the following... 
            // CREATE EVENT IF NOT EXISTS datapull ON SCHEDULE EVERY 2 MINUTE DO select * from menu order by id;
            //----------------------
            
        }catch(Exception e){
            System.out.println("Exception occured "+e); // catch all for any fault experienced...
        }
    }
    
    protected void doPost( HttpServletRequest request, 
            HttpServletResponse response) // prepare the post statement with the servlet and IO exception being thrown and caught...
                throws ServletException, IOException
    {
            response.setContentType( "text/html" ); // declaring the type of content to write
           
            PrintWriter out = response.getWriter(); // Output writer for servlet response
            
            DecimalFormat twoDigits = new DecimalFormat("0.00"); // creating number presentation style
            twoDigits.setMaximumFractionDigits(2); // limiting new style to two decimal points only

            // START -- Creating required HTML top portion
            out.println( "<?xml version = \"1.0\"?>");
            out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD" + 
                    "XHTML 1.0 Strict//EN\" \"http://w3.org" + 
                    "/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
            out.println("<html xmlns = \"http://www.w3.org/1999/xhtml\">");
            out.println("<head>"); 
            // END -- Creating required HTML top portion
             
            try {
                ResultSet queue = Puller.executeQuery(); // create queue object to pull and show the results from SQL statement
                
                // START -- Creating required HTML mid portion
                out.println("<title>Today's Menu</title>");
                
                // ID HTML CSS entry/slot - can be included if required but not in this instance.
                //out.println("<style type=\"text/css\">div.a { \n" + "position: absolute;\n" + "left: 150px;\n" + "top: 150px; \n" + "}" + "</style>");
                
                // Menu Item Description HTML CSS entry/slot
                out.println("<style type=\"text/css\">div.b { \n" + "position: absolute;\n" + "color: blue;\n" + "left: 200px;\n" + "top: 150px; \n" + "}" + "</style>");
                
                // Menu Item Product HTML CSS entry/slot
                out.println("<style type=\"text/css\">div.c { \n" + "position: absolute;\n" + "left: 350px;\n" + "top: 150px; \n" + "}" + "</style>");
                
                // Menu Item Price HTML CSS entry/slot
                out.println("<style type=\"text/css\">div.d { \n" + "position: absolute;\n" + "left: 450px;\n" + "top: 150px; \n" + "}" + "</style>");
                
                out.println("</head>");
                out.println("<body>");
                out.print("<br>" );
                // END -- Creating required HTML mid portion
                
                // String containers to collect each entry
                // ID can be sourced for display if required... StringBuilder idbuilder = new StringBuilder();
                StringBuilder pricebuilder = new StringBuilder();
                StringBuilder descriptionbuilder = new StringBuilder();
                StringBuilder productbuilder = new StringBuilder();
                
                int counter = 0;// variable created to hold delimiter for data pulled and presented. Removing null/empty entries in the menu listing.

                        while(queue.next() && counter <= 26){// While the next entry in the queue is not NULL... Respectively process this entry and...
                                //int id = queue.getInt("id");
                                //idbuilder.append(id + "<br>");
                                counter++; // data pull delimiter variable increment
                                float pr = queue.getFloat("menu_price");                // create a local float to store the current value associated with price field
                                String desc = queue.getString("menu_description");      // create a local string to store the current value associated with description field
                                String prod = queue.getString("menu_product");          // create a local string to store the current value associated with product field
                                descriptionbuilder.append(desc + "<br>");          // Append the entry into their respective string builder object
                                productbuilder.append(prod + "<br>");              // Append the entry into their respective string builder object
                                pricebuilder.append(twoDigits.format(pr) + "<br>");// Append the entry into their respective string builder object
                        }
                        
                // START -- Creating required HTML bottom portion
                //out.println("<div class=\"a\">ID: <br>" + idbuilder.toString() + "</div>");
                out.println("<div class=\"b\">Description: <br>" + descriptionbuilder.toString() + "</div>"); // show list of product types offered
                out.println("<div class=\"c\">Product: <br>" + productbuilder.toString() + "</div>"); // show list of individal items offered
                out.println("<div class=\"d\">Price: <br>" + pricebuilder.toString() + "</div>"); // show list of item prices
                
                out.println("<br>Does the customer want to make a reservation?");           // User prompt to enter reservation system
                out.println("<form action = \"/Reservation.jsp\" method = \"post\">");      // Redirect to jsp file/page housing reservation system
                out.println("<label><input type = \"submit\" value = \"Yes\" /></label>");  // Button to initialise the transfer...
                out.println("</form>");                                                     // End of prompt
                out.println("</pre><body></html>"); // end HTML doc
                out.close(); // close I/O post response writer
                // END -- Creating required HTML bottom portion
                //<form action = "/RestaurantProject1-Servlet-Menu/Menu" method = "post">
                //<!-- Greeting for the employee -->
                //    Does the customer want to hear our Menu options?
                //<label><input type = "submit" value = "Yes" /></label>
                //</form>
                
            }
            
            catch(Exception se){// Any errors experienced, print out generic respective info for further troubleshooting.
                se.printStackTrace();
                System.out.println("Exception occured "+se);
            }
            
            
            
    }
    
    public void destroy() // Method utilised to cleanup all open/lingering established connections...
            {
                try{
                    Puller.close();
                }
                catch(SQLException s){
                    s.printStackTrace();
                }
            }
}



// Manually adjusted ports to be used in apache config
// Manually URL mapping for overall servlet to force 'restaurant entry'