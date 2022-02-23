/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author seanb
 */

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import javax.servlet.annotation.WebServlet;
import java.text.*;
import java.sql.*;
import java.util.Properties;

@WebServlet(name="RestaurantProject1-Servlet-Menu",urlPatterns={"/RestaurantProject1-Servlet-Menu"})

public class RestaurantMenu extends HttpServlet {
    
    private PreparedStatement updateVotes, totalVotes, results;
    private Connection connection;
    
    public void init( ServletConfig config ) throws ServletException
    {
        Properties properties = new Properties();
        
        properties.put("user","postgres");
        properties.put("password","admin");
        
        try{
            Class.forName("org.postgresql.Driver");
            
            connection = DriverManager.getConnection("jdbc:postgresql:postgres", properties);
            
            updateVotes = connection.prepareStatement("UPDATE surveyresults SET votes = votes + 1" + "WHERE id = ?");
            totalVotes = connection.prepareStatement("SELECT sum(votes) FROM surveyresults");
            results = connection.prepareStatement("SELECT surveyoption, votes, id " + "FROM surveyresults ORDER by id");
                    
        }catch(Exception e){
            System.out.println("Exception occured "+e);
        }
    }
    
    protected void doPost( HttpServletRequest request, 
            HttpServletResponse response)
                throws ServletException, IOException
    {
        String firstName = request.getParameter( "firstName" );
            response.setContentType( "text/html" );
            PrintWriter out = response.getWriter();
            //DecimalFormat twoDigits = new DecimalFormat("0.00");
            //twoDigits.setMaximumFractionDigits(2);
            //System.out.println(twoDigits.format(decimalNumber));
            
            out.println( "<?xml version = \"1.0\"?>");
            
            out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD" + 
                    "XHTML 1.0 Strict//EN\" \"http://w3.org" + 
                    "/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
            
            out.println("<html xmlns = \"http://www.w3.org/1999/xhtml\">");
            
            out.println("<head>");
            
            
            int value = Integer.parseInt( request.getParameter("animal"));
            
            try {
                
                updateVotes.setInt(1, value);
                updateVotes.executeUpdate();
                
                ResultSet totalRS = totalVotes.executeQuery();
                totalRS.next();
                int total = totalRS.getInt(1);
                
                ResultSet resultsRS = results.executeQuery();
                out.println("<title>Thank you!</title>");
                out.println("</head>");
                
                out.println("<body>");
                out.println("<p>Thank you for participating.");
                
                resultsRS.close();
                
                out.print("Total responses:" );
                out.print(total);
                
                out.println("</pre><body></html>");
                out.close();
            }
            
            catch(Exception se){
                se.printStackTrace();
                System.out.println("Exception occured "+se);
            }
            
            
            
    }
    
    public void destroy()
            {
                try{
                    updateVotes.close();
                    totalVotes.close();
                    results.close();
                    connection.close();
                }
                catch(SQLException s){
                    s.printStackTrace();
                }
            }
}
