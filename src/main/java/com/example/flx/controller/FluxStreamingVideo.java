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
	
	/**
	 * Using webflux-starter
	 * @param encodeUrl
	 * @param headers
	 * @return
	 * @throws MalformedURLException
	 */
	@GetMapping ("/watch/{encodeUrl}")
	public ResponseEntity<UrlResource> getVideo(@PathVariable("encodeUrl") String encodeUrl) throws MalformedURLException {
		String staticUrl = processUrlServ.getStaticUrl(String.format(UtilUrlProcess.ENCODE_URL_TEMPL, encodeUrl));
		return buildVideoResourceResponseEntity(staticUrl);
	}
	
	/**
	 * Using webflux-starter
	 * @param filename
	 * @return
	 * @throws MalformedURLException
	 */
	@GetMapping("/flux/{filename}")
	public ResponseEntity<UrlResource> downloadVideo(@PathVariable("filename") String filename) throws MalformedURLException{
		return buildVideoResourceResponseEntity(String.format(UtilUrlProcess.PATH_NAME2, filename));
	}
	
	private ResponseEntity<UrlResource> buildVideoResourceResponseEntity(String urlResource) throws MalformedURLException {
		UrlResource video = new UrlResource(urlResource);
		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.contentType(MediaTypeFactory.getMediaType(video).orElse(MediaType.APPLICATION_OCTET_STREAM))
				.body(video);
	}
}
