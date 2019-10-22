package com.works.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import model.Admin;
import model.Product;
import model.Customer;
import util.HibernateUtil;
import util.Util;

@Controller
@RequestMapping("/admin")
public class DashboardController {

	SessionFactory sf = HibernateUtil.getSessionFactory();
	int editID = 0; 
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashBoard( HttpServletRequest req, Model model ) {
		model.addAttribute("ls", dataResult());
		return Util.control(req, "dashboard");
	}
	
	
	@RequestMapping(value = "/customerInsert", method = RequestMethod.POST)
	public String customerInsert(HttpServletRequest req, Customer cus) { //--> Dependency injection
		
		Session sesi = sf.openSession();
		Transaction tr = sesi.beginTransaction();
		//cus.setCpass(Util.MD5(cus.getCpass()));
		/*cus.setCname(cus.getCname());
		cus.setCmail(cus.getCmail());
		cus.setCpassword(cus.getCpassword());
		cus.setCphone(cus.getCphone());
		cus.setCaddress(cus.getCaddress());
		long start = System.currentTimeMillis();
		System.out.println("start : " + start );*/
		int id =  (int) sesi.save(cus);
		System.out.println("insert id : " + id);
		tr.commit(); // işlem başarılı kayıt yap
		long end = System.currentTimeMillis();
		System.out.println("end : " + end );
		//long bettwen =  end - start;
		//System.out.println("bettwen : " + bettwen );
		//tr.rollback(); // işlem hatası yukarıdakileri geri al
		
		return Util.control(req, "redirect:/admin/dashboard");
	}
	
	
	
	public List<Customer> dataResult() {
		Session sesi = sf.openSession();
		List<Customer> ls = sesi.createQuery("from Customer").getResultList();
		return ls;
	}
	
	// delete customer
	@RequestMapping(value = "/deleteCustomer/{cid}")
	public String deleteCustomer(@PathVariable int cid,HttpServletRequest req) {
		
		Session sesi = sf.openSession();
		Transaction tr = sesi.beginTransaction();
		Customer cus = sesi.load(Customer.class, cid);
		sesi.delete(cus);
		tr.commit();
		
		return Util.control(req, "redirect:/admin/dashboard");
	}
	
	@RequestMapping(value = "/updateCustomer/{cid}", method = RequestMethod.GET)
	public String findCustomer(@PathVariable int cid,HttpServletRequest req, Model model) {
		editID = cid;
		Session sesi = sf.openSession();
		Customer cus = sesi.load(Customer.class, cid);
		model.addAttribute("cus", cus);
		return Util.control(req, "redirect:/");
		//return Util.control(req, "redirect:/admin/dashboard");
	}
	
	@RequestMapping(value = "/updateCustomer/update", method = RequestMethod.POST)
	public String updateCustomer(Customer cus,HttpServletRequest req) {
		Session sesi = sf.openSession();
		Transaction tr = sesi.beginTransaction();
		cus.setCid(editID);
		sesi.update(cus);
		tr.commit();
		//return Util.control(req, "redirect:/");
		return Util.control(req, "redirect:/admin/dashboard");
	}
	
	
	
	
	
	
	
}
