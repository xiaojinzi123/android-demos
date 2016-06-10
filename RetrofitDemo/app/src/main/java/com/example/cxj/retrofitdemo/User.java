package com.example.cxj.retrofitdemo;

/**
 * 用户
 * 
 * @author cxj
 *
 */
public class User {

	private String name;
	private String pass;

	public User(String name, String pass) {
		this.name = name;
		this.pass = pass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * 只有当密码和名字一样的时候才相等
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User u = (User) obj;
			if (name != null && pass != null) {
				return name.equals(u.name);
			}
		}
		return false;
	}

}
