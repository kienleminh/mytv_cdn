/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.vnpt.jwt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Nguyen Nhu Son
 */
public class UserRoles {
    
    public static final List<String> PUBLIC_PATH = Arrays.asList("hello");
    public static final List<String> ECOSYSTEM_ALLOW_PATH = Arrays.asList("ecosystem", "ecosystem2");
    public static final List<String> ADMIN_ALLOW_PATH = Arrays.asList("admin");
    
    public static final List<String> ECOSYSTEM_ALLOW_IP = Arrays.asList("127.0.0.1");
    
    public static final List<String> VERTICAL_USERS = Arrays.asList("movie", "news", "music", "health", "clip", "mytv");
    
    private static boolean checkPrefixStartWith(HashSet<String> allowPaths, String resourcePath) {
        for (String allowPath : allowPaths) {
            if (resourcePath.startsWith(allowPath)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean checkAllowIPs(List<String> ips, String ipRequest) {
        if (ips == null) {
            return true;
        }
        return ips.contains(ipRequest);
    }
    
    public static boolean checkRolesByUser(User user) {
        if (user.roles == null) {
            return false;
        }
        if (user.username == null) {
            return false;
        }
        if (!VERTICAL_USERS.contains(user.username)) {
            return false;
        }
        return user.roles.contains(Role.ROLE_CDN);
    }
}
