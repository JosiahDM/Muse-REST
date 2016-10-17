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

import data.BlockDAO;
import entities.Block;

@RestController
public class BlockController {
	
	@Autowired
	BlockDAO blockDao;

	// List all blocks in database. Will not be used by users in finished app.
	@RequestMapping(path="/block", method=RequestMethod.GET)
	public List<Block> index() {
		return blockDao.index();
	}
	
	// List all blocks by user, get ID from JSON
	@RequestMapping(path="/block/user/{id}", method=RequestMethod.GET)
	public Set<Block> index(@PathVariable int id) {
		Set<Block> blocks = blockDao.index(id);
		return blocks;
	}
	
	// View a single block by the block's ID
	@RequestMapping(path="/block/{id}", method=RequestMethod.GET)
	public Block show(@PathVariable int id) {
		return blockDao.show(id);
	}
	
	// Create a block
	// might want to return the created block to display it immediately?
	@RequestMapping(path="/block", method=RequestMethod.POST)
	public void create(@RequestBody String userJSON, HttpServletResponse response) {
		ObjectMapper mapper = new ObjectMapper();
		Block block = null;
		try {
			block = mapper.readValue(userJSON, Block.class);
			blockDao.create(block);
			response.setStatus(201);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(400);
		}
	}
	
	// Update a block
	@RequestMapping(path="/block/{id}", method=RequestMethod.PUT)
	public void update(@PathVariable int id, 
			@RequestBody String userJSON, HttpServletResponse response) {
		ObjectMapper mapper = new ObjectMapper();
		Block block = null;
		try {
			block = mapper.readValue(userJSON, Block.class);
			blockDao.update(id, block);
			response.setStatus(201);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(400);
		}
	}
	
	// Delete
	@RequestMapping(path="block/{id}", method=RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		blockDao.delete(id);
	}
	
}
