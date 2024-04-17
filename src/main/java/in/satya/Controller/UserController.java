package in.satya.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import in.satya.dto.LoginDto;
import in.satya.dto.RegisterDto;
import in.satya.dto.ResetPwdDto;
import in.satya.dto.UserDto;
import in.satya.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("registerDto", new RegisterDto());
		Map<Integer, String> countries = userService.getCountries();
		model.addAttribute("countries", countries.entrySet());
		return "registerView";
	}

	@GetMapping("/states/{cid}")
	@ResponseBody
	public Map<Integer, String> getStates(@PathVariable("cid") Integer cid) {

		return userService.getStates(cid);

	}

	@GetMapping("/cities/{sid}")
	@ResponseBody
	public Map<Integer, String> getCities(@PathVariable("sid") Integer sid) {

		return userService.getCities(sid);
	}

	@PostMapping("/register")
	public String register(RegisterDto regDto, Model model) {
		
		System.out.println(regDto.getEmail());
		
		UserDto userDto = userService.getUser(regDto.getEmail());
		if (userDto != null) {
			model.addAttribute("emsg", "Duplicate Email");
			return "registerView";
		}
		boolean registerUser = userService.registerUser(regDto);
		if (registerUser) {
			model.addAttribute("smsg", "User Registered Succesfully");
		} else {
			model.addAttribute("emsg", "User Registration Failed");
		}
		model.addAttribute("registerDto", new RegisterDto());
		return "registerView";
	}

	@GetMapping("/")
	public String loginPage(Model model) {
		model.addAttribute("loginDto", new LoginDto());
		return "loginView";
	}

	@PostMapping("/login")
	public String login(LoginDto loginDto, Model model) {
		UserDto user = userService.getUser(loginDto);
		System.out.println(user);
		if (user == null) {
			model.addAttribute("emsg", "Invalid Credentails");
			return "loginView";
		}
		if ("Yes".equals(user.getPwdUpdated())) {
			return "redirect:dashBoard";
		} else {
			ResetPwdDto resetDto=new ResetPwdDto();
			String email = user.getEmail();
//			Integer userId = user.getUserId();
			resetDto.setEmail(email);
//			resetDto.setUserId(userId);
			model.addAttribute("resetPwdDto",resetDto );
			return "resetPwdView";
		}

	}

	@PostMapping("/resetPwd")
	public String resetPwd(@ModelAttribute("pwdDto") ResetPwdDto pwdDto, Model model) {

		if (!(pwdDto.getNewPwd().equals(pwdDto.getConfirmPwd()))) {
			model.addAttribute("emsg", "New Pwd and Confirm Pwd Must Be same");
			return "resetPwdView";
		}
		String email = pwdDto.getEmail();
		UserDto user = userService.getUser(email);
		if (user.getPwd().equals(pwdDto.getOldPwd())) {
			boolean pwd = userService.resetPwd(pwdDto);
			if (pwd) {
				return "redirect:dashBoard";
			} else {
				model.addAttribute("emsg", "Pwd update Failed");
				return "resetPwdView";
			}

		} else {
			model.addAttribute("emsg", "Given Old Password  is Wrong");
			return "resetPwdView";
		}

	}

	@GetMapping("/dashBoard")
	public String dashboard(Model model) {
		String quote = userService.getQuote();
		model.addAttribute("quote", quote);
		return "dashBoard";
	}

	@GetMapping("/logout")
	public String logout() {
		return "redirect:/";
	}

}
