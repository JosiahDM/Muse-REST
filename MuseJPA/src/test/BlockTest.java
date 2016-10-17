package test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import entities.Block;

public class BlockTest {

	EntityManagerFactory emf;
	EntityManager em;

	@Before
	public void setUp() throws Exception {
		emf = Persistence.createEntityManagerFactory("MuseJPA");
	    em = emf.createEntityManager();
	}

	
	@Test
	public void test() {
		Block b = em.find(Block.class, 1);
		assertEquals("Gonna work for 2 hours on this hard problem...here goes", b.getPreNotes());
		assertEquals(1, b.getUser().getId());
	}
	
	
	@After
	public void tearDown() throws Exception {
		em.close();
		emf.close();
	}

}
