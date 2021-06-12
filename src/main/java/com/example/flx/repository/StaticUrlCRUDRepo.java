package com.example.flx.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.flx.entity.StaticUrl;

@Repository
public interface StaticUrlCRUDRepo extends CrudRepository<StaticUrl, Integer> {
	StaticUrl findByEncodeUrl (String encodeUrl);
}
