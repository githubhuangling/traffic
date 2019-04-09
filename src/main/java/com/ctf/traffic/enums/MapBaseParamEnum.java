package com.ctf.traffic.enums;

public enum MapBaseParamEnum {
    
	MAPPARA1("测试1",1),
	MAPPARA2("测试2",2),
	MAPPARA3("测试3",3);;
	
	//百度地图javascript API接口密钥
	public static final String MAPKEY = "d8qR2fRYpIgLMLh0oFEFMCdlkFCiA5Ht";
	private String name;
	private Integer value;
	
	
	private MapBaseParamEnum(String name, Integer value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	
	
}
