package com.bairock.eleMonitor.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	//登录账号
	@NotNull
	private String username;
	//用户姓名
	private String personName = "";
	@NotNull
	private String password;
	private String tel = "";
	
	//关联的站点
	@ManyToMany
	@JsonManagedReference("user_station")
	private List<Station> listStation = new ArrayList<>();
	
	//角色集合
	@ManyToMany
    private List<SysRole> roles = new ArrayList<>();
	
	//是否启用
	private boolean enable = false;
	
	//备注
	private String remark="";
	
	//建档时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public List<Station> getListStation() {
		return listStation;
	}
	public void setListStation(List<Station> listStation) {
		this.listStation = listStation;
	}
	public List<SysRole> getRoles() {
		return roles;
	}
	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public void addStation(Station station) {
		if(null != station) {
			boolean haved = false;
			for(Station s : listStation) {
				if(s.getId() == station.getId()) {
					haved = true;
					break;
				}
			}
			if(!haved) {
				station.addUser(this);
				listStation.add(station);
			}
		}
	}
	
	public void removeStation(Station station) {
		station.removeUser(this);
		listStation.remove(station);
	}
}
