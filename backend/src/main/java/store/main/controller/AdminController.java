package store.main.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import store.main.database.Brand;
import store.main.database.BrandRepository;
import store.main.database.Post;
import store.main.database.PostRepository;
import store.main.database.User;
import store.main.database.UserRepository;
import store.main.service.CartService;
import store.main.service.ImageService;
import store.main.service.LoaderService;

@Controller
public class AdminController {
	
	@Autowired
	PostRepository postRepository;
	
	@Autowired
	LoaderService loaderService;
	
	@Autowired
	BrandRepository brandRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private CartService cService;
	
	@Autowired
	private ImageService imgService;
	
	private void adminLoader(Model model, HttpSession session, HttpServletRequest request) {
		
		model = loaderService.userLoader(model, request);
		model = loaderService.postLoader(model);
		
		model.addAttribute("updated", false);
		model.addAttribute("deleted", false);
				
		cService.LoadNotProduct(model, session);	
	}
	
	private void adminPostLoader(Model model, Post post) {

		LinkedList<Integer> images = new LinkedList<>();
		
		for (int i = 0; i < post.getnImg(); i++) {
			images.add(i);
		}
				
		model.addAttribute("post", post);
		model.addAttribute("found", true);
		model.addAttribute("images", images);
		model.addAttribute("edit", false);
	}
	
	private void setUpdatedPost(Post post, @RequestParam String bname,
			@RequestParam 	List<MultipartFile> imagenFile, @PathVariable Long id) throws IOException {
		
		Brand b = brandRepository.findFirstByNameIgnoreCase(bname);
		
		if (b == null) {
			b = new Brand(bname);
			brandRepository.save(b);
		}
		
		Post oldPost = postRepository.findById(id).get();
		
		User user = oldPost.getUser();
				
		post.setBrand(b);
		post.setUser(user);
		post.setComponentTag(post.getComponent());
		post.setId(oldPost.getId());
		post.setPostAddress(oldPost.getPostAddress());
		
		
		int totalImg = oldPost.getnImg();
		int numImg = 0;		
		
		for(MultipartFile mf : imagenFile) {	
			numImg++;
			if(!mf.getOriginalFilename().equals("")) {			
				if (numImg > totalImg) {
					imgService.saveImage("posts", post.getId(), mf,totalImg);
					totalImg++;
				} else {
					imgService.saveImage("posts", post.getId(), mf,numImg - 1);
				}
			} 
		} 
		
		post.setnImg(totalImg);

		postRepository.save(post);
	}

	@GetMapping("/admin")
	public String adminPage(Model model,HttpSession session, HttpServletRequest request) {
		
		adminLoader(model, session, request);
		
		model.addAttribute("found", false);
		
		model.addAttribute("updated", session.getAttribute("updated"));
		model.addAttribute("deleted", session.getAttribute("deleted")); 
						
		session.setAttribute("updated", false);
		session.setAttribute("deleted", false);
				
		return "admin-page";
	}
	
	@PostMapping("/admin/search_product")
	public String findPost(Model model, HttpServletRequest request, HttpSession session, String searchAdmin) {
				
		adminLoader(model, session, request);
		
		Post post = postRepository.findByName(searchAdmin);
		
		if (post == null) {
			model.addAttribute("found", false);
		} else {
			adminPostLoader(model, post);
		}
			
		return "admin-page";
	}
	
	@PostMapping("/admin/edit/{id}")
	public String editPost(Model model, HttpServletRequest request, HttpSession session,  @PathVariable Long id) {
		
		Post post = postRepository.findById(id).get();
				
		adminLoader(model, session, request);
		adminPostLoader(model, post);
		
		model.addAttribute("edit", true); //Override value of edit after method adminPostLoader
		
		return "admin-page";
	}
	
	@PostMapping("/admin/updated/{id}")
	public String updatePost(Model model, Post post, @RequestParam String bname, @PathVariable Long id,
			@RequestParam List<MultipartFile> imagenFile, HttpServletRequest request, HttpSession session) throws IOException {
		
		setUpdatedPost(post, bname, imagenFile, id);
		
		session.setAttribute("updated", true);

		return "redirect:/admin";
	}
	
	@PostMapping("/admin/deleted/{id}")
	public String deletePost(Model model, HttpServletRequest request, HttpSession session, @PathVariable Long id) {
		
		Post post = postRepository.findById(id).get();
		
		cService.deletePostFromBD(post);
		
		session.setAttribute("deleted", true);
		
		return "redirect:/admin";
	}
	
}
