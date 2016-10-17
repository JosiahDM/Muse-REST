package test;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import entities.Subject;
import entities.User;

public class SubjectTest {
	EntityManagerFactory emf;
	EntityManager em;

	@Before
	public void setUp() throws Exception {
		emf = Persistence.createEntityManagerFactory("MuseJPA");
	    em = emf.createEntityManager();
	}

	
	@Test
	public void test() {
		Subject s = em.find(Subject.class, 1);
		assertEquals("Java", s.getName());
	}
	
	
	@After
	public void tearDown() throws Exception {
		em.close();
		emf.close();
	}

}
