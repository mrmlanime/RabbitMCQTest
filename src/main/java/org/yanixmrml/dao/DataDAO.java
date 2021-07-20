package org.yanixmrml.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.yanixmrml.model.Data;

public class DataDAO {

	public SessionFactory sessionFactory = new Configuration().configure("data.cfg.xml")
								.addAnnotatedClass(Data.class).buildSessionFactory();
	public DataDAO() {
		
	}
	
	public void saveData(Data data) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.save(data);
		session.close();
	}
	
	@SuppressWarnings("unchecked")
	public List<Data> getData() {
		List<Data> dataList = new ArrayList<Data>();
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		dataList = session.createQuery("FROM data").getResultList();
		session.close();
		return dataList;
	}
	
}
