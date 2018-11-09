package com.bairock.eleMonitor.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LineTemFormGroup {

	private long id;
	private String name;
	
	private List<LineTemForm> listLineTemForm = new ArrayList<>();
	
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

	public List<LineTemForm> getListLineTemForm() {
		return listLineTemForm;
	}

	public void setListLineTemForm(List<LineTemForm> listLineTemForm) {
		this.listLineTemForm = listLineTemForm;
	}

	public void addLineTemDevice(Device dev) {
		for(LineTemForm line : listLineTemForm) {
			if(line.getPlace().equals(dev.getPlace())) {
				addToLineTemForm(line, dev);
				return;
			}
		}
		//程序走到这就说明listLineTemForm中没有对应的对象, 创建新对象
		LineTemForm line = new LineTemForm();
		line.setPlace(dev.getPlace());
		addToLineTemForm(line, dev);
		listLineTemForm.add(line);
	}
	
	private void addToLineTemForm(LineTemForm line, Device dev) {
		if(dev.getName().contains("A")) {
			line.setDeviceA(dev);
		}else if(dev.getName().contains("B")) {
			line.setDeviceB(dev);
		}else if(dev.getName().contains("C")) {
			line.setDeviceC(dev);
		}
	}
	
	public void sort() {
		Collections.sort(listLineTemForm);
	}
}
