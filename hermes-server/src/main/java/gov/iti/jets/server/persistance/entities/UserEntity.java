
package gov.iti.jets.server.persistance.entities;

import java.sql.Date;

public class UserEntity {

	public String phone;
	public String name;
	public String email;
	public String password;
	public String image; // to be implemented later
	public boolean gender;
	public Date dob;
	public String country;
	public String bio;

	// full constructor that should be used
	public UserEntity(String phone, 
	String password,
	String name, 
	String email, 
	 String image,
	  boolean gender, 
	  Date dob,
			String country, String bio) {
		this.phone = phone;
		this.password = password;
		this.name = name;
		this.email = email;
		this.image = image;
		this.gender = gender;
		this.dob = dob;
		this.country = country;
		this.bio = bio;
	}

	public UserEntity() {
	}

	public UserEntity(String phone,
			String password,
			String name,
			String email,
			boolean gender,
			Date dob,
			String country,
			String bio) {
		this.phone = phone;
		this.password = password;
		this.name = name;
		this.email = email;
		// this.image = image;
		this.gender = gender;
		this.dob = dob;
		this.country = country;
		this.bio = bio;
	}

	public UserEntity(String phone, String password) {
		this.phone = phone;
		this.password = password;
	}

	@Override
	public String toString() {
		return name + " : " + phone;
	}
}
