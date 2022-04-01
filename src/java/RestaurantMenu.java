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
    
    private PreparedStatement Puller;
    private Connection connection;
    
    public void init( ServletConfig config ) throws ServletException
    {
        Properties properties = new Properties();
        
        //properties.put("user","postgres");
        properties.put("user","root");
        properties.put("password","admin");
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/menu_schema?", properties);
            Puller = connection.prepareStatement("select * from menu order by id;");
            
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

            out.println( "<?xml version = \"1.0\"?>");
            out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD" + 
                    "XHTML 1.0 Strict//EN\" \"http://w3.org" + 
                    "/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
            out.println("<html xmlns = \"http://www.w3.org/1999/xhtml\">");
            out.println("<head>"); 
             
            try {
                ResultSet queue = Puller.executeQuery();
                
                out.println("<title>Thank you!</title>");
                
                out.println("<style type=\"text/css\">div.a { \n" + "position: absolute;\n" + "left: 150px;\n" + "top: 150px; \n" + "}" + "</style>");
                out.println("<style type=\"text/css\">div.b { \n" + "position: absolute;\n" + "left: 200px;\n" + "top: 150px; \n" + "}" + "</style>");
                out.println("<style type=\"text/css\">div.c { \n" + "position: absolute;\n" + "left: 350px;\n" + "top: 150px; \n" + "}" + "</style>");
                out.println("<style type=\"text/css\">div.d { \n" + "position: absolute;\n" + "left: 450px;\n" + "top: 150px; \n" + "}" + "</style>");
                out.println("<style type=\"text/css\">div.e { \n" + "position: absolute;\n" + "left: 150px;\n" + "top: 50px; \n" + "}" + "</style>");
                
                out.println("</head>");
                out.println("<body>");
                out.print("<br>" );
                
                StringBuilder idbuilder = new StringBuilder();
                StringBuilder pricebuilder = new StringBuilder();
                StringBuilder descriptionbuilder = new StringBuilder();
                StringBuilder productbuilder = new StringBuilder();

                        while(queue.next()){
                                int id = queue.getInt("id");
                                float pr = queue.getFloat("price");
                                String desc = queue.getString("description");
                                String prod = queue.getString("product");
                                idbuilder.append(id + "<br>");
                                descriptionbuilder.append(desc + "<br>");
                                productbuilder.append(prod + "<br>");
                                pricebuilder.append(twoDigits.format(pr) + "<br>");
                        }
                        
                out.println("<div class=\"a\">ID: <br>" + idbuilder.toString() + "</div>");
                out.println("<div class=\"b\">Description: <br>" + descriptionbuilder.toString() + "</div>");
                out.println("<div class=\"c\">Product: <br>" + productbuilder.toString() + "</div>");
                out.println("<div class=\"d\">Price: <br>" + pricebuilder.toString() + "</div>");
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
                    Puller.close();
                }
                catch(SQLException s){
                    s.printStackTrace();
                }
            }
}
