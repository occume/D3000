package org.d3.codec.test;

public class MemcachedResponse {
	private byte 	magic;
	private byte 	opCode;
	private byte 	dataType;
	private short 	status;
	private int 	id;
	private long 	cas;
	private int 	flags;
	private int 	expires;
	private String 	key;
	private String 	data;
	
	public MemcachedResponse(byte magic, byte opCode, byte dataType, short status,
				int id, long cas, int flags, int expires,
				String key, String data){
		this.magic = magic;
		this.opCode = opCode;
		this.dataType = dataType;
		this.status = status;
		this.id = id;
		this.cas = cas;
		this.flags = flags;
		this.expires = expires;
		this.key = key;
		this.data = data;
	}
	
	public byte magic() {
		return magic;
	}
	public byte opCode() {
		return opCode;
	}
	public byte dataType() {
		return dataType;
	}
	public short status() {
		return status;
	}
	public int id() {
		return id;
	}
	public long cas() {
		return cas;
	}
	public int flags() {
		return flags;
	}
	public int expires() {
		return expires;
	}
	public String key() {
		return key;
	}
	public String data() {
		return data;
	}
}
