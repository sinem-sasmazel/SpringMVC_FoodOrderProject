package com.works.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import model.Customer;
import util.DB;
import util.Util;

@Controller
@RequestMapping("/user")
public class UserLoginController {
	
	DB db = new DB();

	// @RequestMapping("/user") -> all subdomains follow the /user root

	// login page create
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String login() {
		return "user/login";
	}

	// admin login fnc
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String userLogin( 
			Customer cus, 
			Model model, 
			HttpServletRequest req, 
			@RequestParam(defaultValue = "off") String remember,
			HttpServletResponse res) {
		try {
			String query = "select * from customer where cmail = ? and cpassword = ?";
			PreparedStatement pre = db.connect(query);
			pre.setString(1, cus.getCmail());
			pre.setString(2, Util.MD5(cus.getCpassword()));
			ResultSet rs = pre.executeQuery();
			if (rs.next()) {
				// user can log-in.
				// session is creating
				cus.setCid(rs.getInt("cid"));
				cus.setCname(rs.getString("cname"));
				req.getSession().setAttribute("cid", cus);
				
				// remember check ?
				if(remember.equals("on")) {
					Cookie cookie = new Cookie("user_cookie", ""+rs.getInt("cid"));
					cookie.setMaxAge(60*60*24);
					res.addCookie(cookie);
				}
				
				
				return "redirect:/user/dashboard";
			} else {
				model.addAttribute("error", "User name or password is invalid!");
			}
		} catch (Exception e) {
			System.err.println("login error : " + e);
			model.addAttribute("error", "System error!");
		}
		return "user/login";
	}
	
	
	// exit
	@RequestMapping(value = "/exit", method = RequestMethod.GET)
	public String exit(HttpServletRequest req, HttpServletResponse res) {
		
		Cookie cookie = new Cookie("user_cookie", "");
		cookie.setMaxAge(0);
		res.addCookie(cookie);
		
		// all sessions remove
		req.getSession().invalidate();
		// single session remove
		req.getSession().removeAttribute("cid");
		return "redirect:/user/";
	}
	
	

}
