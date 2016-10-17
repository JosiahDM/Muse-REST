package data;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import entities.Block;
import entities.Subject;
import entities.User;

@Transactional
public class BlockDAO {
	
	@PersistenceContext
	EntityManager em;
	
	// List of all blocks
	public List<Block> index() {
		String query = "select b from Block b";
		List<Block> blocks = em.createQuery(query, Block.class)
							   .getResultList();
		return blocks;
	}
	// List of all blocks by user ID
	public Set<Block> index(int id) {
		User u = em.find(User.class, id);
		return u.getBlocks();
	}
	
	
	// Return one block
	public Block show(int id) {
		return em.find(Block.class, id);
	}
	
	// Create a block
	public void create(Block block) {
		em.persist(block);
		em.flush();
	}
	
	// Update a block
	public void update(int id, Block block) {
		Block b = em.find(Block.class, id);
		Subject s = findSubject(block.getSubject().getName());
		b.setEnd(block.getEnd());
		b.setPostNotes(block.getPostNotes());
		b.setPreNotes(block.getPreNotes());
		b.setStart(block.getStart());
		b.setSubject(s);
		em.persist(b);
		em.flush();
	}
	
	// Find or create subject if not found
	public Subject findSubject(String name) {
		String query = "select s from Subject s where s.name = ?1";
		Subject s = null;
		try {
			s = em.createQuery(query, Subject.class)
					.setParameter(1, name)
					.getSingleResult();
		} catch (Exception e) {
			System.out.println(e);
			s = new Subject();
			s.setName(name);
			em.persist(s);
		}
		return s;
	}
	
	public void delete(int id) {
		Block b = em.find(Block.class, id);
		em.remove(b);
		em.flush();
	}
}
