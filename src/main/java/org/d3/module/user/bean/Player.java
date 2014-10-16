package org.d3.module.user.bean;

public class Player implements Comparable{
	
	private long 	id;
	private String 	name;
	private int 	title;
	private int		icon;
	private int		sex;
	
	public Player(){}
	
	public Player(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTitle() {
		return title;
	}
	public void setTitle(int title) {
		this.title = title;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", title=" + title
				+ ", icon=" + icon + ", sex=" + sex + "]";
	}

	public int compareTo(Object o) {
		Player user = (Player) o;
		return this.name.compareTo(user.getName());
	}
	
}
