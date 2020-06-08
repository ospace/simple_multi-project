package com.tistory.ospace.simpleproject.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import com.tistory.ospace.core.data.Pair;

@Component
public class MonitoringService {
	@Autowired
	private BuildProperties buildProperties;
	
	private Pair<Date, String> lastError;

	public BuildProperties getBuildInfo() {
		return buildProperties;
	}

	public Pair<Date, String> getLastError() {
		return lastError;
	}

	public void setLastError(String lastError) {
		this.lastError = Pair.of(new Date(), lastError);
	}
	
}
