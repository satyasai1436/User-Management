package in.satya.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	private String name;

	private String email;
	
	private String pwd;

	private String pwdUpdated;

	private Long phno;

	@ManyToOne
	@JoinColumn(name = "country_id")
	private CountryEntity country;
	@ManyToOne
	@JoinColumn(name = "state_id")
	private StateEntity state;
	@ManyToOne
	@JoinColumn(name = "city_id")
	private CityEntity city;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPwdUpdated() {
		return pwdUpdated;
	}

	public void setPwdUpdated(String pwdUpdated) {
		this.pwdUpdated = pwdUpdated;
	}

	public Long getPhno() {
		return phno;
	}

	public void setPhno(Long phno) {
		this.phno = phno;
	}

	public CountryEntity getCountry() {
		return country;
	}

	public void setCountry(CountryEntity country) {
		this.country = country;
	}

	public StateEntity getState() {
		return state;
	}

	public void setState(StateEntity state) {
		this.state = state;
	}

	public CityEntity getCity() {
		return city;
	}

	public void setCity(CityEntity city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "UserEntity [userId=" + userId + ", name=" + name + ", email=" + email + ", pwd=" + pwd + ", pwdUpdated="
				+ pwdUpdated + ", phno=" + phno + ", country=" + country + ", state=" + state + ", city=" + city + "]";
	}

}
