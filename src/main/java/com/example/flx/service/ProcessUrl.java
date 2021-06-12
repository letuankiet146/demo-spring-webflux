package com.example.flx.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.flx.entity.StaticUrl;
import com.example.flx.repository.StaticUrlCRUDRepo;
import com.example.flx.util.UtilUrlProcess;

import net.bytebuddy.utility.RandomString;

@Service
public class ProcessUrl implements IProcessUrl {
	@Autowired
	private StaticUrlCRUDRepo staticUrlRepo;

	@Override
	public String getStaticUrl(String encodeUrl) {
		StaticUrl staticUrl = staticUrlRepo.findByEncodeUrl(encodeUrl);
		if (staticUrl != null) {
			return staticUrl.getDecodeUrl();
		}
		return null;
	}

	@Override
	public String createStaticUrl(String filename) {
		String random = RandomString.make();
		StaticUrl staticUrl = new StaticUrl();
		staticUrl.setDecodeUrl(String.format(UtilUrlProcess.PATH_NAME, filename));
		staticUrl.setEncodeUrl(String.format(UtilUrlProcess.ENCODE_URL_TEMPL, random));
		staticUrl = staticUrlRepo.save(staticUrl);
		return staticUrl.getEncodeUrl();
	}
}
