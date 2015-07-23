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
		users.put("sdy", new User("sdy", "123", "�ζ�Ұ", "asss"));
		users.put("ldm", new User("ldm", "123", "������", "asss"));
		users.put("zyp", new User("zyp", "123", "������", "asss"));
		users.put("zww", new User("zww", "123", "������", "asss"));
		users.put("wt", new User("wt", "123", "����", "asss"));
	}

	/**
	 * ��ת��ǰ̨��½����
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("users", users);
		return "user/list";
	}

	// ҳ��ת����user�����add.jspʱ����get���󣬻�ִ�����´���
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(@ModelAttribute("user") User user) {

		return "user/add";

	}

	// ��ִ�о������Ӳ���ʱ����post���󣬻�ִ�����´���
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@Validated User user, BindingResult br,@RequestParam("attachs")MultipartFile[] attachs,HttpServletRequest req) throws IOException {// BindingResult��֤���
		if (br.hasErrors()) {
			// ����д���ֱ����ת��add��ͼ
			return "user/add";
		}
		String realpath = req.getSession().getServletContext().getRealPath("/resources/upload");
		System.out.println(realpath);
		for(MultipartFile attach:attachs) {
			if(attach.isEmpty()) continue;//���Ϊ��
			File f = new File(realpath+"/"+attach.getOriginalFilename());
			FileUtils.copyInputStreamToFile(attach.getInputStream(),f);
		}
		users.put(user.getUserName(), user);
		return "redirect:/user/users";

	}

	// ִ����ʾ����
	@RequestMapping(value = "/{userName}", method = RequestMethod.GET)
	public String show(@PathVariable String userName, Model model) {
		model.addAttribute(users.get(userName));
		return "user/show";
	}

	// ��ת��update����
	@RequestMapping(value = "/{userName}/update", method = RequestMethod.GET)
	public String update(@PathVariable String userName, Model model) {
		model.addAttribute(users.get(userName));
		System.out.println();
		return "user/update";
	}

	// ִ�о�����²���
	@RequestMapping(value = "/{userName}/update", method = RequestMethod.POST)
	public String update(@PathVariable String userName, @Validated User user,
			BindingResult br, Model model) {
		if (br.hasErrors()) {
			// ����д���ֱ����ת��add��ͼ
			return "user/update";
		}
		users.put(userName, user);
		return "redirect:/user/users";
	}

	// ת����ɾ��
	public String delete(@PathVariable String userName) {
		users.remove(userName);
		return "redirect:/user/users";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String userName, String password, HttpSession session) {
		if (users.containsKey(userName)) {
			throw new UserException("�û���������");
		}
		User u = users.get(userName);
		if (!u.getPassword().equals(password)) {
			throw new UserException("�û����벻��ȷ");
		}
		session.setAttribute("loginUser", u);
		return "user/users";
	}

	/** �ֲ��쳣���� *//*
	@ExceptionHandler(UserException.class)
	public String handlerExceprtion(UserException e, HttpServletRequest req) {
		req.setAttribute("e", e);
		return "error";
	}
*/
}
