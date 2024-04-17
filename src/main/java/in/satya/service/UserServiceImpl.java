package in.satya.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.satya.Repo.CityRepo;
import in.satya.Repo.CountryRepo;
import in.satya.Repo.StateRepo;
import in.satya.Repo.UserRepo;
import in.satya.Utils.EmailUtils;
import in.satya.dto.LoginDto;
import in.satya.dto.QuoteDto;
import in.satya.dto.RegisterDto;
import in.satya.dto.ResetPwdDto;
import in.satya.dto.UserDto;
import in.satya.entity.CityEntity;
import in.satya.entity.CountryEntity;
import in.satya.entity.StateEntity;
import in.satya.entity.UserEntity;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private CountryRepo countryRepo;
	@Autowired
	private StateRepo stateRepo;
	@Autowired
	private CityRepo cityRepo;
	@Autowired
	private EmailUtils emailUtils;

	@Override
	public Map<Integer, String> getCountries() {
		Map<Integer, String> countryMap = new HashMap<>();
		List<CountryEntity> countryList = countryRepo.findAll();

		countryList.forEach(c -> {
			countryMap.put(c.getCountryId(), c.getCountryName());
		});
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer cid) {
		Map<Integer, String> stateMap = new HashMap<>();
		List<StateEntity> stateList = stateRepo.getStates(cid);
		stateList.forEach(s -> {
			stateMap.put(s.getStateId(), s.getStateName());
		});

		return stateMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer sid) {
		Map<Integer, String> cityMap = new HashMap<>();
		List<CityEntity> citiesList = cityRepo.getCities(sid);
		citiesList.forEach(c -> {
			cityMap.put(c.getCityId(), c.getCityName());
		});
		return cityMap;
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepo.findByEmail(email);
		System.out.println(userEntity);
		if (userEntity == null) {
			return null;
		}
		UserDto userDto2 = new UserDto();
		BeanUtils.copyProperties(userEntity, userDto2);


		return userDto2;
	}

	@Override
	public boolean registerUser(RegisterDto regDto) {
		ModelMapper mapper = new ModelMapper();
		UserEntity entity = mapper.map(regDto, UserEntity.class);

		CountryEntity countryId = countryRepo.findById(regDto.getCountryId()).orElseThrow();
		StateEntity stateId = stateRepo.findById(regDto.getStateId()).orElseThrow();
		CityEntity cityId = cityRepo.findById(regDto.getCityId()).orElseThrow();
		entity.setCountry(countryId);
		entity.setState(stateId);
		entity.setCity(cityId);
		entity.setPwd(generateRandom());
		System.out.println(entity);
		entity.setPwdUpdated("No");
		String subject = "User Registration";
		String body = "Your temporary password is:" + entity.getPwd();
		emailUtils.sendEmail(regDto.getEmail(), subject, body);
		UserEntity saveEntity = userRepo.save(entity);
		return saveEntity.getUserId() != null;
	}

	@Override
	public UserDto getUser(LoginDto loginDto) {
		UserEntity userEntity = userRepo.findByEmailAndPwd(loginDto.getEmail(), loginDto.getPwd());
		if (userEntity == null) {
			return null;
		}
		
//		UserDto userDto = new UserDto();
//      BeanUtils.copyProperties(userEntity, userDto);
//		return userDto;

		ModelMapper mapper = new ModelMapper();

		return mapper.map(userEntity, UserDto.class);
	}

	@Override
	public boolean resetPwd(ResetPwdDto pwdDto) {
		UserEntity userEntity = userRepo.findByEmailAndPwd(pwdDto.getEmail(), pwdDto.getOldPwd());

		if (userEntity != null) {
			userEntity.setPwd(pwdDto.getNewPwd());
			userEntity.setPwdUpdated("Yes");
			userRepo.save(userEntity);
			return true;
		}
		return false;
	}

	@Override
	public String getQuote() {
		QuoteDto[] value = null;
		String url = "https://type.fit/api/quotes";

		RestTemplate rest = new RestTemplate();
		ResponseEntity<String> entity = rest.getForEntity(url, String.class);
		String body = entity.getBody();
		ObjectMapper mapper = new ObjectMapper();
		try {
			value = mapper.readValue(body, QuoteDto[].class);

		} catch (Exception e) {

			e.printStackTrace();
		}
		Random r = new Random();
		int i = r.nextInt(value.length - 1);
		return value[i].getText();
	}

	private static String generateRandom() {
		String aToZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		Random rand = new Random();
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			int randIndex = rand.nextInt(aToZ.length() - 1);
			res.append(aToZ.charAt(randIndex));
		}
		return res.toString();
	}

}
