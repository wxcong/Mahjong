package org.mahjong.client;


public class Tile implements Comparable<Tile>{
	private int domain;
	private String pre;
	private String post;
	private int index = -1;
	public Tile(int domain, String pre, String post){
		this.domain = domain;
		this.pre = pre;
		this.post = post;
	}
	
	public Tile(Tile tile) {
		domain = tile.domain;
		pre = tile.pre;
		post = tile.post;
		index = tile.index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getValue(){
		return pre+post;
	}
	
	public int getDomain() {
		return domain;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Tile)) return false;
		Tile t = (Tile) o;
		return (domain == t.domain) && (pre.equals(t.pre)) && (post.equals(t.post));
	}
	
	public String getPre() {
		return pre;
	}
	
	public int getPost() {
		return Integer.valueOf(post);
	}
	public boolean isEmpty() {
		return this.pre==null&&this.post==null;
	}
	
	@Override
	public int hashCode() {
		int hashCode = domain;
		hashCode = hashCode + pre.toString().hashCode()
			+ post.toString().hashCode();
		return hashCode;
	}
	
	@Override
	public int compareTo(Tile t) {
		if(domain < t.getDomain()) return 1;
		else if(domain > t.getDomain()) return -1;
		else{
			if(domain != 3) {
				if(getPost() < t.getPost()) return 1;
				else if(getPost() > t.getPost()) return -1;
				else return 0;
			}else {
				return getPriority(getPre()) - getPriority(t.getPre());
			}
		}
	}
	@Override
	public String toString() {
		return "(" + pre + post + "-" + domain + ")";
	}
	
	private int getPriority(String tileName) {
		switch (tileName) {
		case "Eas": return 0;
		case "Nor": return 1;
		case "Wes": return 2;
		case "Sou": return 3;
		case "Red": return 4;
		case "Whi": return 5;
		case "Gre": return 6;
		default: return -1;
		}
	}
 }
