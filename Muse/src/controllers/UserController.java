package controllers;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import data.UserDAO;
import entities.Block;
import entities.Subject;
import entities.User;

@RestController
public class UserController {

	@Autowired
	UserDAO userDao;

	// List all users
	@RequestMapping(path="/user", method=RequestMethod.GET)
	public List<User> index() {
		return userDao.index();
	}
	
	// Get user by ID
	@RequestMapping(path="/user/{id}", method=RequestMethod.GET)
	public User index(@PathVariable int id) {
		return userDao.show(id);
	}
	
	// Create user
	@RequestMapping(path="/user", method=RequestMethod.POST)
	public void create(@RequestBody String userJSON, HttpServletResponse response) {
		ObjectMapper mapper = new ObjectMapper();
		User user = null;
		try {
			user = mapper.readValue(userJSON, User.class);
			response.setStatus(201);
			userDao.create(user);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(400);
		}
	}
	
	// Update user
	@RequestMapping(path="/user/{id}", method=RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody String userJSON, HttpServletResponse response) {
		ObjectMapper mapper = new ObjectMapper();
		User user = null;
		try {
			user = mapper.readValue(userJSON, User.class);
			response.setStatus(201);
			userDao.update(user, id);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(400);
		}
	}
	
	// Delete user
	@RequestMapping(path="/user/{id}", method=RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		userDao.delete(id);
	}
	
	// Add a block to user
	@RequestMapping(path="/user/{id}/block/", method=RequestMethod.POST)
	public Block createScore(@RequestBody String userJSON, @PathVariable int id, 
							HttpServletResponse response) {
		ObjectMapper mapper = new ObjectMapper();
		Block block = null;
		try {
			block = mapper.readValue(userJSON, Block.class);
			response.setStatus(201);
			userDao.addBlock(block, id);
			return block;
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(400);
		}
		return null;
	}
	
	// Get subjects for user by user id
	@RequestMapping(path="/user/{id}/subjects", method=RequestMethod.GET)
	public Set<Subject> indexSubjects(@PathVariable int id) {
		Set<Subject> subjects = userDao.showSubjects(id);
		return subjects;
	}
	
	// Get all users blocks by subject - user id, subject name
	@RequestMapping(path="/user/{id}/blocks/{sName}", method=RequestMethod.GET)
	public List<Block> subjectBlocks(@PathVariable int id, @PathVariable String sName) {
		return userDao.subjectBlocks(id, sName);
	}
	
}
