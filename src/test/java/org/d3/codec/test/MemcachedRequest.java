package org.d3.codec.test;

import java.util.Random;

public class MemcachedRequest {
	private static final Random rand = new Random();
	private int 	magic = 0x80;
	private byte 	opCode;
	private String 	key;
	private int 	flags = 0xdeadbeef;
	private int 	expires;
	private String 	body;
	private int 	id = rand.nextInt();
	private long 	cas;
	private boolean hasExtras;
	
	public MemcachedRequest(byte opcode, String key, String value){
		this.opCode = opcode;
		this.key = key;
		this.body = value == null ? "" : value;
		this.hasExtras = opcode == Opcode.SET;
	}
	
	public MemcachedRequest(byte opcode, String key){
		this(opcode, key, null);
	}
	
	public int magic() {
		return magic;
	}
	
	public int opCode() {
		return opCode;
	}
	
	public String key() {
		return key;
	}
	
	public int flags() {
		return flags;
	}
	
	public int expires() {
		return expires;
	}
	
	public String body() {
		return body;
	}
	
	public int id() {
		return id;
	}
	
	public long cas() {
		return cas;
	}
	
	public boolean hasExtras() {
		return hasExtras;
	}
}
