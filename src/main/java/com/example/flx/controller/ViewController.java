package com.example.flx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.flx.service.IProcessUrl;

@Controller
public class ViewController {
	@Autowired
	private IProcessUrl processUrlServ;
	@GetMapping("/movie/{movieName}")
	public String home(@PathVariable("movieName") String movieName, Model model) {
		String encodeUrl = processUrlServ.createStaticUrl(String.format("%s.mp4", movieName));
		model.addAttribute("encodeUrl",encodeUrl);
		return "index";
	}
}
