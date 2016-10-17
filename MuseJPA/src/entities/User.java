package entities;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String username;
	private String password;
	
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	@JsonManagedReference(value="userBlocks")
	private Set<Block> blocks;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="block",
			joinColumns=@JoinColumn(name="user_id"),
			inverseJoinColumns=@JoinColumn(name="subject_id"))
	@OrderBy("id ASC")
	private Set<Subject> subjects;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<Block> getBlocks() {
		return blocks;
	}
	public void setBlocks(Set<Block> blocks) {
		this.blocks = blocks;
	}
	public Set<Subject> getSubjects() {
		return subjects;
	}
	public void setSubjects(Set<Subject> subjects) {
		this.subjects = subjects;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
	}
	
	public void addBlock(Block block) {
		if (blocks == null) {
			blocks = new LinkedHashSet<>();
		}
		if (!blocks.contains(block)) {
			blocks.add(block);
			block.setUser(this);
		}
	}

	public void removeBlock(Block block) {
		if (blocks != null && blocks.contains(block)) {
			blocks.remove(block);
			block.setUser(null);
		}
	}
	
	public void addSubject(Subject subject) {
		if (subjects == null) {
			subjects = new LinkedHashSet<>();
		}
		if (!subjects.contains(subject)) {
			subjects.add(subject);
		}
	}
	
	public void removeSubject(Subject subject) {
		if (subjects != null && subjects.contains(subject)) {
			subjects.remove(subject);
		}
	}
	
}
