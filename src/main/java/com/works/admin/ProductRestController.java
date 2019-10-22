package com.works.admin;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import model.Product;

@RestController
public class ProductRestController {
	
SessionFactory sf = util.HibernateUtil.getSessionFactory();
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/allProducts", method = RequestMethod.GET)
	public HashMap<String, Object> allProducts() {
		
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("statu", true);
		hm.put("message", "Products are served successfully!");
		
		Session sesi = sf.getCurrentSession();
		Transaction tr = sesi.beginTransaction();
		List<Product> ls = sesi.createQuery("from Product").getResultList();
		hm.put("products", ls);
		tr.commit();
		sesi.close();
		return hm;

	}

}
