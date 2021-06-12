package com.example.flx.controller;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.flx.service.IProcessUrl;
import com.example.flx.util.UtilUrlProcess;

@RestController
public class FluxStreamingVideo {
	@Autowired
	private IProcessUrl processUrlServ;
	
	@GetMapping ("/watch/{encodeUrl}")
	public ResponseEntity<UrlResource> getVideo(@PathVariable("encodeUrl") String encodeUrl) {
		try {
			String staticUrl = processUrlServ.getStaticUrl(String.format(UtilUrlProcess.ENCODE_URL_TEMPL, encodeUrl));
			UrlResource video = new UrlResource(staticUrl);
			return ResponseEntity
					.status(HttpStatus.PARTIAL_CONTENT)
					.contentType(MediaTypeFactory.getMediaType(video).orElse(MediaType.APPLICATION_OCTET_STREAM))
					.body(video);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
