package com.beyondtech.tvpss.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

	@Autowired
	private SessionFactory sessionFactory;

	@GetMapping("/test-db")
	@ResponseBody
	public String testConnection() {
		try {
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			session.createNativeQuery("SELECT 1", Integer.class).uniqueResult();
			session.getTransaction().commit();
			session.close();
			return "Database connection successful!";
		} catch (Exception e) {
			return "Database connection failed: " + e.getMessage();
		}
	}
}