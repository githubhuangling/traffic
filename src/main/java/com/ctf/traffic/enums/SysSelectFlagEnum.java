package com.ctf.traffic.enums;

public enum SysSelectFlagEnum {
    
	SYS_SELECT_DEVTYPE("设备类型","input-devType"),
	SYS_SELECT_DEVSTATE("设备状态","input-devState"),
	SYS_SELECT_TASK_STATE("任务状态","task-state"),
	SYS_SELECT_TASK_DEGREE("紧急程度","task-degree");
	
	private String name;
	private String value;
	
	
	private SysSelectFlagEnum(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
