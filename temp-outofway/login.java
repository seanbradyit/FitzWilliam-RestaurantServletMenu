/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author seanb
 */
import javax.faces.bean.ManagedBean;  
import javax.faces.bean.RequestScoped;  
@ManagedBean  
@RequestScoped

public class login{  
    String name;  
    String email;  
    String password; 
    String address;  
    
        public String getName() {  
            return name;  
        }  
        public void setName(String name) {  
            this.name = name;  
        }  
        public String getEmail() {  
            return email;  
        }  

        public void setEmail(String email) {  
            this.email = email;  
        }  
        public String getPassword() {  
            return password;  
        }  
        public void setPassword(String password) {  
            this.password = password;  
        }  
        public String getAddress() {  
            return address;  
        }  
        public void setAddress(String address) {  
            this.address = address;  
        }      
}  
