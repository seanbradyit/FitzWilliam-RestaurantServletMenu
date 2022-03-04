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
    
    private PreparedStatement Puller, Product ,Price;
    private Connection connection;
    
    public void init( ServletConfig config ) throws ServletException
    {
        Properties properties = new Properties();
        
        properties.put("user","postgres");
        properties.put("password","admin");
        
        try{
            Class.forName("org.postgresql.Driver");
            
            connection = DriverManager.getConnection("jdbc:postgresql:postgres", properties);
            
            //updateVotes = connection.prepareStatement("UPDATE surveyresults SET votes = votes + 1" + "WHERE id = ?");
            //Description = connection.prepareStatement("SELECT description, price, product, id \" + \"FROM menu ORDER by id");
            Puller = connection.prepareStatement("select * from menu order by id");
            //totalVotes = connection.prepareStatement("SELECT sum(votes) FROM surveyresults");
            //results = connection.prepareStatement("SELECT surveyoption, votes, id " + "FROM surveyresults ORDER by id");
                    
        }catch(Exception e){
            System.out.println("Exception occured "+e);
        }
    }
    
    protected void doPost( HttpServletRequest request, 
            HttpServletResponse response)
                throws ServletException, IOException
    {
            response.setContentType( "text/html" );
            PrintWriter out = response.getWriter();
            
            DecimalFormat twoDigits = new DecimalFormat("0.00");
            twoDigits.setMaximumFractionDigits(2);
            
            //System.out.println(twoDigits.format(decimalNumber));
            
            out.println( "<?xml version = \"1.0\"?>");
            
            out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD" + 
                    "XHTML 1.0 Strict//EN\" \"http://w3.org" + 
                    "/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
            
            out.println("<html xmlns = \"http://www.w3.org/1999/xhtml\">");
            
            out.println("<head>");
            
            
            //int value = Integer.parseInt( request.getParameter("animal"));
            
            try {
                
                //updateVotes.setInt(1, value);
                //updateVotes.executeUpdate();
                
                //ResultSet totalRS = totalVotes.executeQuery();
                //totalRS.next();
                //int total = totalRS.getInt(1);
                
                //ResultSet resultsRS = results.executeQuery();
                
                ResultSet queue = Puller.executeQuery();
                //int idnum = desc.getInt(0);
                
                out.println("<title>Thank you!</title>");
                
                out.println("<style type=\"text/css\">div.a { \n" + "position: absolute;\n" + "left: 150px;\n" + "top: 150px; \n" + "}" + "</style>");
                out.println("<style type=\"text/css\">div.b { \n" + "position: absolute;\n" + "left: 200px;\n" + "top: 150px; \n" + "}" + "</style>");
                
                out.println("</head>");
                
                out.println("<body>");

                out.print("<br>" );
                
                StringBuilder idbuilder = new StringBuilder();
                StringBuilder pricebuilder = new StringBuilder();

                        while(queue.next()){
                            int id = queue.getInt("id");
                            float pr = queue.getFloat("price");
                            //String descriptionpull = queue.getString("description");
                            //String productpull = queue.getString("product");
                            //String pricepull = queue.getString("price");
                            ///String fixeddes = "%15s %n" + des; 
                            // System.out.printf( "%-15s %15s %n", heading1, heading2);
                            //String pro = desc.getString("Product");
                            //String pri = desc.getString("Price");
                            // out.print(id + " " + des + " " + pro + " " + pri);
                            // out.println(fixeddes); 
                            //String des = desc.getString("Description");
                            //out.print(des + "<br>");
                        //    build.append(id);
                            //build.append(descriptionpull);
                            //build.append(productpull);
                            //build.append(pricepull);
                        //    out.print(build.toString() + "<br>");
                            //out.print(descriptionpull + "<br>");
                            //out.print(productpull + "<br>");
                            //for(int i = 0; i < 28; i++){
                            //    out.print(i + "<br>" + descriptionpull);
                            //}
                            idbuilder.append(id + "<br>");
                            pricebuilder.append(twoDigits.format(pr) + "<br>");
                            //pricebuilder.append(pr + "<br>");
                        }
                
                //out.println("ID: " + idbuilder.toString());
                out.println("<div class=\"a\">ID: <br>" + idbuilder.toString() + "</div>");
                out.println("<div class=\"b\">Price: <br>" + pricebuilder.toString() + "</div>");
                out.println("</pre><body></html>");
                //desc.close();
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
                    Puller.close();
                    //Product.close();
                    //Price.close();
                    //connection.close();
                }
                catch(SQLException s){
                    s.printStackTrace();
                }
            }
}
