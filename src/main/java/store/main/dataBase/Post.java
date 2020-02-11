package store.main.dataBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;



@Entity
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private int nImg = 0;

	private int component; // Motherboard, Storage device,..
	@ElementCollection
	private List<String> tags;
	private int price;
	private String details;
	private String features;

	private String postAddress;

	@ManyToOne
	private Brand brand;

	@ManyToOne
	private User user;

	public Post() {
		tags=new LinkedList<String>();
	}

	public Post(int component, int nImg, int price, String details, String features, Brand brand,
			String postAddress , String... tags) {
		this.component = component;
		this.setnImg(nImg);
		this.tags = new ArrayList<>(Arrays.asList(tags));
		this.price = price;
		this.details = details;
		this.features = features;
		this.brand = brand;
		this.postAddress = postAddress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getComponent() {
		return component;
	}

	public void setComponent(int component) {
		this.component = component;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPostAddress() {
		return postAddress;
	}

	public void setPostAddress(String postAddress) {
		this.postAddress = postAddress;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", component=" + component + ", tags=" + tags + ", price=" + price + ", details="
				+ details + ", features=" + features + ", postAddress=" + postAddress + ", brand=" + brand + ", user="
				+ user + "]";
	}

	public int getnImg() {
		return nImg;
	}

	public void setnImg(int nImg) {
		this.nImg = nImg;
	}

}