package com.snastase.photo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.SuggestDefinition;
import com.snastase.photo.model.Tag;

@Controller
@RequestMapping("/suggest")
public class SuggestController {

	
	private static final Logger logger = LoggerFactory.getLogger(SuggestController.class);
	
	@Autowired
	private DatabaseClient databaseClient;
	
	/**
	 * Simply selects the photo view to render by returning its name.
	 */
	@RequestMapping(value = "/tags", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Tag> tags(Locale locale, Model model, @RequestParam String tagName) {
		
		logger.info("Get tag suggest! The client locale is {}.", locale);
		
		List<Tag> suggestList = new ArrayList<Tag>();
		
		try {
			QueryManager queryMgr = databaseClient.newQueryManager();
			SuggestDefinition sd = queryMgr.newSuggestDefinition();
			
			sd.setStringCriteria(tagName);
			sd.setOptionsName("suggest-tag");
			sd.setLimit(5);
			
			String[] results = queryMgr.suggest(sd);
			
			for (String result : results) {
				suggestList.add(new Tag(result));
			}

			
		} catch(Exception e){
			System.out.println(e.toString());
		} 
		return suggestList;
	}
	

	
	/**
	 * Simply selects the photo view to render by returning its name.
	 */
	@RequestMapping(value = "/titles", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<String> titles(Locale locale, Model model, @RequestParam String terms) {
		
		logger.info("Get title suggest! The client locale is {}.", locale);
		
		List<String> suggestList = new ArrayList<String>();
		
		try {
			QueryManager queryMgr = databaseClient.newQueryManager();
			SuggestDefinition sd = queryMgr.newSuggestDefinition();
			
			sd.setStringCriteria(terms);
			sd.setOptionsName("suggest-title");
			sd.setLimit(5);
			
			String[] results = queryMgr.suggest(sd);
			
			for (String result : results) {
				suggestList.add(result);
			}

			
		} catch(Exception e){
			System.out.println(e.toString());
		} 
		return suggestList;
	}

}
