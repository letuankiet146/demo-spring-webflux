package com.example.flx.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class StreamingVideo {
	public static final String VIDEO_PATH = "/static/videos";
	public static int BYTE_RANGE = 128;
	
	@GetMapping(value = "/text/streaming", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Object> textStreaming() {
		return Flux.interval(Duration.ofSeconds(1)).map(i->"Data steam line - " + i);
	}
	
	@GetMapping("/video/streaming/{filename}")
	public Mono<ResponseEntity<byte[]>> videoStreaming (
			@RequestHeader(value = "Range", required=false) String httpRangeList, 
			@PathVariable("filename") String fileName) {
		return Mono.just(getContent(VIDEO_PATH, fileName, httpRangeList, "video"));
	}

	 private ResponseEntity<byte[]> getContent(String location, String fileName, String range, String contentTypePrefix) {
	     long rangeStart = 0;
	     long rangeEnd;
	     byte[] data;
	     Long fileSize;
	     String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
	     try {
	        fileSize = Optional.ofNullable(fileName)
	              .map(file -> Paths.get(getFilePath(location), file))
	              .map(this::sizeFromFile)
	              .orElse(0L);
	        if (range == null) {
	           return ResponseEntity.status(HttpStatus.OK)
	                 .header("Content-Type", contentTypePrefix+"/" + fileType)
	                 .header("Content-Length", String.valueOf(fileSize))
	                 .body(readByteRange(location, fileName, rangeStart, fileSize - 1));
	        }
	        String[] ranges = range.split("-");
	        rangeStart = Long.parseLong(ranges[0].substring(6));
	        if (ranges.length > 1) {
	           rangeEnd = Long.parseLong(ranges[1]);
	        } else {
	           rangeEnd = fileSize - 1;
	        }
	        if (fileSize < rangeEnd) {
	           rangeEnd = fileSize - 1;
	        }
	        data = readByteRange(location, fileName, rangeStart, rangeEnd);
	     } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	     }
	     String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
	     return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
	           .header("Content-Type", contentTypePrefix+"/" + fileType)
	           .header("Accept-Ranges", "bytes")
	           .header("Content-Length", contentLength)
	           .header("Content-Range", "bytes" + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
	           .body(data);
	  }
	  public byte[] readByteRange(String location, String filename, long start, long end) throws IOException {
	     Path path = Paths.get(getFilePath(location), filename);
	     try (InputStream inputStream = (Files.newInputStream(path));
	         ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream()) {
	        byte[] data = new byte[BYTE_RANGE];
	        int nRead;
	        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
	           bufferedOutputStream.write(data, 0, nRead);
	        }
	        bufferedOutputStream.flush();
	        byte[] result = new byte[(int) (end - start) + 1];
	        System.arraycopy(bufferedOutputStream.toByteArray(), (int) start, result, 0, result.length);
	        return result;
	     }
	  }
	  private String getFilePath(String location) {
	     URL url = this.getClass().getResource(location);
	     return new File(url.getFile()).getAbsolutePath();
	  }
	  private Long sizeFromFile(Path path) {
	     try {
	        return Files.size(path);
	     } catch (IOException ex) {
	        ex.printStackTrace();
	     }
	     return 0L;
	  }
}
