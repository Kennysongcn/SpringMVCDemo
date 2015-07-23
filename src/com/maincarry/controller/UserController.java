package com.maincarry.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.multi.MultiFileChooserUI;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.maincarry.exception.UserException;
import com.maincarry.model.User;

@Controller
@RequestMapping("/user")
public class UserController {

	Map<String, User> users = new HashMap<String, User>();

	public UserController() {
		users.put("sdy", new User("sdy", "123", "宋冬野", "asss"));
		users.put("ldm", new User("ldm", "123", "刘东明", "asss"));
		users.put("zyp", new User("zyp", "123", "周云蓬", "asss"));
		users.put("zww", new User("zww", "123", "张玮玮", "asss"));
		users.put("wt", new User("wt", "123", "吴吞", "asss"));
	}

	/**
	 * 跳转到前台登陆界面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("users", users);
		return "user/list";
	}

	// 页面转发到user下面的add.jsp时候是get请求，会执行以下代码
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(@ModelAttribute("user") User user) {

		return "user/add";

	}

	// 当执行具体的添加操作时候，是post请求，会执行以下代码
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@Validated User user, BindingResult br,@RequestParam("attachs")MultipartFile[] attachs,HttpServletRequest req) throws IOException {// BindingResult验证结果
		if (br.hasErrors()) {
			// 如果有错误直接跳转到add视图
			return "user/add";
		}
		String realpath = req.getSession().getServletContext().getRealPath("/resources/upload");
		System.out.println(realpath);
		for(MultipartFile attach:attachs) {
			if(attach.isEmpty()) continue;//如果为空
			File f = new File(realpath+"/"+attach.getOriginalFilename());
			FileUtils.copyInputStreamToFile(attach.getInputStream(),f);
		}
		users.put(user.getUserName(), user);
		return "redirect:/user/users";

	}

	// 执行显示详情
	@RequestMapping(value = "/{userName}", method = RequestMethod.GET)
	public String show(@PathVariable String userName, Model model) {
		model.addAttribute(users.get(userName));
		return "user/show";
	}

	// 跳转到update界面
	@RequestMapping(value = "/{userName}/update", method = RequestMethod.GET)
	public String update(@PathVariable String userName, Model model) {
		model.addAttribute(users.get(userName));
		System.out.println();
		return "user/update";
	}

	// 执行具体更新操作
	@RequestMapping(value = "/{userName}/update", method = RequestMethod.POST)
	public String update(@PathVariable String userName, @Validated User user,
			BindingResult br, Model model) {
		if (br.hasErrors()) {
			// 如果有错误直接跳转到add视图
			return "user/update";
		}
		users.put(userName, user);
		return "redirect:/user/users";
	}

	// 转发到删除
	public String delete(@PathVariable String userName) {
		users.remove(userName);
		return "redirect:/user/users";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String userName, String password, HttpSession session) {
		if (users.containsKey(userName)) {
			throw new UserException("用户名不存在");
		}
		User u = users.get(userName);
		if (!u.getPassword().equals(password)) {
			throw new UserException("用户密码不正确");
		}
		session.setAttribute("loginUser", u);
		return "user/users";
	}

	/** 局部异常处理 *//*
	@ExceptionHandler(UserException.class)
	public String handlerExceprtion(UserException e, HttpServletRequest req) {
		req.setAttribute("e", e);
		return "error";
	}
*/
}
