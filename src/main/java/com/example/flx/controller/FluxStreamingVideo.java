package com.example.flx.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.flx.service.IProcessUrl;
import com.example.flx.util.UtilUrlProcess;



@RestController
public class FluxStreamingVideo {
	@Autowired
	private IProcessUrl processUrlServ;
	
	/**
	 * Using web-mvc starter
	 * @param encodeUrl
	 * @param headers
	 * @return
	 * @throws MalformedURLException
	 */
//	@GetMapping ("/watch/{encodeUrl}")
//	public ResponseEntity<ResourceRegion> getVideo(@PathVariable("encodeUrl") String encodeUrl, @RequestHeader HttpHeaders headers) throws MalformedURLException {
//		String staticUrl = processUrlServ.getStaticUrl(String.format(UtilUrlProcess.ENCODE_URL_TEMPL, encodeUrl));
//		return buildVideoResourceRegion(staticUrl, headers);
//	}
	
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
		return buildVideoResourceResponseEntity(String.format(UtilUrlProcess.PATH_NAME, filename));
	}
	
	private ResponseEntity<ResourceRegion> buildVideoResourceRegion(String staticUrl, HttpHeaders headers) throws MalformedURLException {
		UrlResource video = new UrlResource(staticUrl);
		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.contentType(MediaTypeFactory.getMediaType(video).orElse(MediaType.APPLICATION_OCTET_STREAM))
				.body(resourceRegion(video, headers));
	}
	
	private ResourceRegion resourceRegion (UrlResource video, HttpHeaders header) {
		try {
			long contentLength = video.contentLength();
			HttpRange range = header.getRange().stream().findFirst().orElse(null);
			ResourceRegion result = null;
			if (range!=null) {
				long start = range.getRangeStart(contentLength);
				long end = range.getRangeEnd(contentLength);
				long rangeLength = ((1*1024*1024) > (end-start+1))?(end-start+1):(1*1024*1024);
				result = new ResourceRegion(video, start, rangeLength);
			} else {
				long rangeLength = ((1*1024*1024) > contentLength)?contentLength:(1*1024*1024);
				result = new ResourceRegion(video, 0, rangeLength);
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private ResponseEntity<UrlResource> buildVideoResourceResponseEntity(String urlResource) throws MalformedURLException {
		UrlResource video = new UrlResource(urlResource);
		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.contentType(MediaTypeFactory.getMediaType(video).orElse(MediaType.APPLICATION_OCTET_STREAM))
				.body(video);
	}
}
