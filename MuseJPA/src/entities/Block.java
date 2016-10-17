package entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Block {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="pre_notes")
	private String preNotes;
	@Column(name="post_notes")
	private String postNotes;
	private Date start;
	private Date end;
	@Column(name="time_goal")
	private Date timeGoal;
	
	@OneToOne
	@JoinColumn(name="subject_id")
	private Subject subject;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	@JsonBackReference(value="userBlocks")
	private User user;
	
	public String getPreNotes() {
		return preNotes;
	}
	public void setPreNotes(String preNotes) {
		this.preNotes = preNotes;
	}
	public String getPostNotes() {
		return postNotes;
	}
	public void setPostNotes(String postNotes) {
		this.postNotes = postNotes;
	}
	
	public Date getTimeGoal() {
		return timeGoal;
	}
	public void setTimeGoal(Date timeGoal) {
		this.timeGoal = timeGoal;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public int getId() {
		return id;
	}
	
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Block [id=" + id + ", preNotes=" + preNotes + ", postNotes=" + postNotes + ", start=" + start + ", end="
				+ end + ", subject=" + subject + ", user=" + user + "]";
	}
	


	
	
}
