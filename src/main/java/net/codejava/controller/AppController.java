package net.codejava.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import net.codejava.entities.User;
import net.codejava.repository.UserRepository;

@Controller
public class AppController {

	@Autowired
	private UserRepository repository;
	
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
	@GetMapping("/register")
	public String showSignUpForm(Model model) {
		model.addAttribute("user", new User());
		return "signup_form";
	}
	
	@PostMapping("/process_register")
	public String processRegistration(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		repository.save(user);
		return "registration_success";
	}
	
	@GetMapping("/list_users")
	public String ViewUsersList(Model model) {
		List<User> listUsers = repository.findAll();
		model.addAttribute("listUsers", listUsers);
		return "users";
	}
	
	@GetMapping("/edit/{id}")
	public String editUser(@PathVariable("id") Long id, Model model) {
		User user = repository.findById(id)
			      .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		model.addAttribute("user", user);
		return "edit";
	}
	
	@PostMapping("/process_edit/{id}")
	public String editPage(@PathVariable Long id, @Validated User user) {
		
		User user2 = repository.findById(id).get();
		String pass = user2.getPassword();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(pass);
		user.setPassword(encodedPassword);
		repository.save(user);
		
		
		return "redirect:/list_users";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Long id, Model model) {
		User user = repository.findById(id)
			      .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
			    repository.delete(user);
		return "redirect:/list_users";
	}
}




















