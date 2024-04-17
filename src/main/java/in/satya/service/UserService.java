package in.satya.service;

import java.util.Map;

import in.satya.dto.LoginDto;
import in.satya.dto.RegisterDto;
import in.satya.dto.ResetPwdDto;
import in.satya.dto.UserDto;

public interface UserService {
	
	public Map<Integer,String> getCountries();

	public Map<Integer,String> getStates(Integer cid);

	public Map<Integer,String> getCities(Integer sid);

	public UserDto getUser(String email);

	public boolean registerUser(RegisterDto regDto);

	public UserDto getUser(LoginDto loginDto);

	public boolean resetPwd(ResetPwdDto pwdDto);

	public String getQuote();

}
