package com.works.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import model.Product;
import util.HibernateUtil;
import util.Util;

@Controller
@RequestMapping("/admin")
public class ProductController {
	
	SessionFactory sf = HibernateUtil.getSessionFactory();
	int editID = 0; 

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public String products( HttpServletRequest req, Model model ) {
		model.addAttribute("ls", prodResult());
		return Util.control(req, "products");
	}
	
	
	@RequestMapping(value = "/productInsert", method = RequestMethod.POST)
	public String customerInsert(HttpServletRequest req, Product prod) { //--> Dependency injection
		
		Session sesi = sf.openSession();
		Transaction tr = sesi.beginTransaction();
		int id =  (int) sesi.save(prod);
		System.out.println("insert id : " + id);
		tr.commit(); // işlem başarılı kayıt yap
		//tr.rollback(); // işlem hatası yukarıdakileri geri al
		
		return Util.control(req, "redirect:/admin/products");
	}
	
	public List<Product> prodResult() {
		Session sesi = sf.openSession();
		List<Product> ls = sesi.createQuery("from Product").getResultList();
		return ls;
	}
	
	// delete customer
	@RequestMapping(value = "/deleteProduct/{pid}")
	public String deleteCustomer(@PathVariable int pid,HttpServletRequest req) {
		
		Session sesi = sf.openSession();
		Transaction tr = sesi.beginTransaction();
		Product prod = sesi.load(Product.class, pid);
		sesi.delete(prod);
		tr.commit();
		
		return Util.control(req, "redirect:/admin/products");
	}
	
	@RequestMapping(value = "/updateProduct/{pid}", method = RequestMethod.GET)
	public String findCustomer(@PathVariable int pid,HttpServletRequest req, Model model) {
		editID = pid;
		Session sesi = sf.openSession();
		Product prod = sesi.load(Product.class, pid);
		model.addAttribute("prod", prod);
		//return Util.control(req, "dashboard");
		return Util.control(req, "redirect:/");
		//return Util.control(req, "redirect:/admin");
	}
	
	@RequestMapping(value = "/updateProduct/updateProd", method = RequestMethod.POST)
	public String updateCustomer(Product prod,HttpServletRequest req) {
		Session sesi = sf.openSession();
		Transaction tr = sesi.beginTransaction();
		prod.setPid(editID);
		sesi.update(prod);
		tr.commit();
		//return Util.control(req, "redirect:/");
		return Util.control(req, "redirect:/admin/products");
	}
	
}
