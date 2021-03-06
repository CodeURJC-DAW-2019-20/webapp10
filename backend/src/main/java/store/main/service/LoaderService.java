package store.main.service;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import store.main.database.Post;
import store.main.database.PostRepository;
import store.main.database.User;
import store.main.database.UserRepository;

@Service
public class LoaderService {

	@Autowired
	private PostRepository postRepository; // Repository of posts

	@Autowired
	private UserRepository userRepository; // Repository of users

	/**
	 * Loads the Home Page
	 * 
	 * @param model
	 * @return model with attributes
	 */
	public Model modelLoader(Model model) {

		// list with all product order by descending price
		List<Post> postListPriceDesc = postRepository.OrderByPriceDesc();

		// list with all product order by ascending price
		List<Post> postListPriceAsc = postRepository.OrderByPriceAsc();

		// list order by new arrivals
		List<Post> postListIdDesc = postRepository.OrderByIdDesc();

		boolean load = !postListIdDesc.isEmpty();
		model.addAttribute("load", load);
		// load the three most expensive products
		loadPosts(model, postListPriceDesc, "1", 3);

		// load cheapest products
		loadPosts(model, postListPriceAsc, "2", 8);

		// load newly arrived products
		loadPosts(model, postListIdDesc, "3", 8);

		if (load) {
			model.addAttribute("post", postListPriceDesc.get(0)); // The most expensive product
		}
		return model;
	}

	public Model userLoader(Model model, HttpServletRequest request) {

		if (request.isUserInRole("USER")) {

			User user = userRepository.findByEmail(request.getUserPrincipal().getName());

			model.addAttribute("logged", true);
			model.addAttribute("user", user);

		} else {
			model.addAttribute("logged", false);
		}

		if (request.isUserInRole("ADMIN")) {
			model.addAttribute("isAdmin", true);
		} else {
			model.addAttribute("isAdmin", false);
		}

		return model;
	}

	/**
	 * @param model
	 * @param list
	 * @param tag   mustache list
	 * @param n     first products to show
	 */
	private void loadPosts(Model model, List<Post> list, String tag, int n) {

		if (list.size() > n) {
			List<Post> auxList = new LinkedList<Post>();
			for (int i = 0; i < n; i++) {
				auxList.add(list.get(i));
			}
			model.addAttribute("list" + tag, auxList);
		} else {
			model.addAttribute("list" + tag, list);
		}
	}

	public Model postLoader(Model model) {
		model.addAttribute("postList", postRepository.findAll());
		return model;

	}
}
