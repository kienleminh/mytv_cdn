/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.vnpt.jwt;

import java.util.List;

/**
 *
 * @author Nguyen Nhu Son
 */
public class User {
    
    public String username;
    public List<String> roles;
    public List<String> allowIPs;
}
