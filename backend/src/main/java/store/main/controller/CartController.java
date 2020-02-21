package store.main.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import store.main.database.*;
import store.main.service.CartService;
import store.main.service.LoaderService;

@Controller
public class CartController {

	@Autowired
	private Cart cart;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartService cService;

	@Autowired
	private LoaderService loaderService;

	private User getUserInfo(HttpServletRequest request) {
		return userRepository.findByEmail(request.getUserPrincipal().getName());
	}

	@RequestMapping("/cart")
	public String mapPost(Model model, HttpSession session, HttpServletRequest request) {

		model = loaderService.userLoader(model, request);
		model = loaderService.postLoader(model);

		cService.LoadNotProduct(model, session);

		return "cart";
	}

	@RequestMapping("/cart/removeItem-{idRemove}")
	public String mapPost(Model model, @PathVariable Long idRemove, HttpSession session, HttpServletRequest request) {

		model = loaderService.userLoader(model, request);
		model = loaderService.postLoader(model);

		cService.RemoveComponentNotProduct(model, session, idRemove);

		return "cart";
	}

	@GetMapping("/payment")
	public String checkoutPayment(Model model, HttpSession session, HttpServletRequest request) {

		// Get User
		User user = getUserInfo(request);

		String creditCard = "";

		if (user.getCreditCard() != null) {
			creditCard = user.getCreditCard();
		}

		model.addAttribute("creditcard", creditCard);
		model.addAttribute("name", user.getFirstName() + " " + user.getLastName());
		model.addAttribute("email", user.getEmail());

		model = loaderService.userLoader(model, request);
		model = loaderService.postLoader(model);

		cService.LoadNotProduct(model, session);

		return "checkout-payment";
	}

	@PostMapping("/final_review")
	public String finalReview(Model model, HttpSession session, HttpServletRequest request, String creditcard) {

		User user = getUserInfo(request);

		String creditCard = creditcard.substring(creditcard.length() - 4, creditcard.length());

		// User data to model
		model.addAttribute("phone", user.getPhone());
		model.addAttribute("firstname", user.getFirstName());
		model.addAttribute("lastname", user.getLastName());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("creditcard", creditCard);

		model = loaderService.userLoader(model, request);
		model = loaderService.postLoader(model);

		cService.LoadNotProduct(model, session);

		return "checkout-review";
	}

	@RequestMapping("/complete")
	public String completeOrder(Model model, HttpSession session, HttpServletRequest request) {

		User user = getUserInfo(request);

		List<Post> cartAux = (List<Post>) session.getAttribute("cart");

		List<User> sellerList = user.getSellers();
		
		List<Post> removeList=new LinkedList<>();
		
		for (Post p : cartAux) { // set sellers
			if (!containsSeller(sellerList, p.getUser())) {
				sellerList.add(p.getUser());
			}
			removeList.add(p);
		}
		user.setSellers(sellerList);
		userRepository.save(user);
		
		for(Post post:removeList) {
			deletePostFromBD(post);
		}

		model = loaderService.userLoader(model, request);
		model = loaderService.postLoader(model);

		// Reset cart
		session.setAttribute("cart", new LinkedList<>());
		session.setAttribute("total", (long) 0);
		session.setAttribute("empty", true);
		model.addAttribute("name", user.getFirstName());

		cService.LoadNotProduct(model, session);

		return "checkout-complete";
	}

	private boolean containsSeller(List<User> list, User user) {
		for (User u : list) {
			if (user.getFirstName().equals(u.getFirstName()) && user.getLastName().equals(u.getLastName())) {
				return true;
			}
		}
		return false;
	}
	
	//this method delete a post from database

		private void deletePostFromBD(Post p) {
			Brand b=p.getBrand();
			b.getPosts().remove(p);
			brandRepository.save(b);
			
			User u=p.getUser();
			u.getPosts().remove(p);
			userRepository.save(u);
			
			postRepository.deleteById(p.getId());
		}

}
