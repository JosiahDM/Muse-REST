package data;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import entities.Block;
import entities.Subject;
import entities.User;

@Transactional
public class UserDAO {

	@PersistenceContext
	EntityManager em;
	
	@Autowired
	BCryptPasswordEncoder pe;
	
	// All users
	public List<User> index() {
		String query = "select u from User u";
		return em.createQuery(query, User.class).getResultList();
	}
	
	// Show 1 user by id
	public User show(int id) {
		return em.find(User.class, id);
	}
	
	// Create user
	public void create(User user) {
		user.setPassword(pe.encode(user.getPassword()));
		em.persist(user);
		em.flush();
	}
	
	// update user
	public void update(User user, int id) {
		User u = em.find(User.class, id);
		u.setPassword(pe.encode(user.getPassword()));
		u.setUsername(user.getUsername());
		em.persist(u);
		em.flush();
	}
	
	// Delete user
	public void delete(int id) {
		User u = em.find(User.class, id);
		em.remove(u);
		em.flush();
	}
	
	// User login
	public User login(User user) {
		String query = "select u from User u where username = ?1";
		User u = null;
		try { 
			u = em.createQuery(query, User.class)
					.setParameter(1, user.getUsername())
					.getSingleResult();		
			if (pe.matches(user.getPassword(), u.getPassword())) {
				return u;
			}
		} catch(Exception e) {
			System.err.println("User " + user.getUsername() + " not found.");
			u = null;
		}
		return null;
	};
	
	// Add block to user - block data, user id
	public void addBlock(Block block, int id) {
		User u = em.find(User.class, id);
		Subject s = em.find(Subject.class, 0);
		block.setSubject(s);
		block.setUser(u);
		em.persist(block);
		u.addBlock(block);
		em.persist(u);
		em.flush();
	}
	
	// Show subjects for user
	public Set<Subject> showSubjects(int id) {
		User u = em.find(User.class, id);
		return u.getSubjects();
	}
	
	// Get all users blocks by subject - user id, subject name
	// return in descending order.
	public List<Block> subjectBlocks(int id, String sName) {
		String query = "select b from Block b "
					 + "join Subject s on s.id = b.subject.id "
					 + "join User u on u.id = b.user.id "
					 + "where s.name = ?1 and u.id = ?2";
		List<Block> blocks = em.createQuery(query, Block.class)
						      .setParameter(1, sName)
						      .setParameter(2, id)
						      .getResultList();
		Collections.sort(blocks, (b1, b2) -> b2.getId() - b1.getId() );
		return blocks;
	}

}
