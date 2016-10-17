package test;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import entities.Block;
import entities.User;

public class UserTest {
	EntityManagerFactory emf;
	EntityManager em;

	@Before
	public void setUp() throws Exception {
		emf = Persistence.createEntityManagerFactory("MuseJPA");
	    em = emf.createEntityManager();
	}

	
	@Test
	public void test() {
		User u = em.find(User.class, 1);
		assertEquals("herbderpison", u.getUsername());
		assertEquals(1, u.getSubjects().size());
	}
	
	
	@After
	public void tearDown() throws Exception {
		em.close();
		emf.close();
	}

}
