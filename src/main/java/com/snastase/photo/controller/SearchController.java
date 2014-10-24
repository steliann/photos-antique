package com.snastase.photo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.MatchLocation;
import com.marklogic.client.query.MatchSnippet;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;
import com.snastase.photo.model.PhotoResult;
import com.snastase.photo.model.Tag;

@Controller
@RequestMapping("/search")
public class SearchController {

	private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	private DatabaseClient databaseClient;
	
	private String terms = "";
	private Map<String, String> filters = new HashMap<String,String>(); 
	
	/**
	 * Simply selects the photo view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String search(Locale locale, Model model ) {
		
		logger.info("Get photos! The client locale is {}.", locale);
		
		try {
			QueryManager queryMgr = databaseClient.newQueryManager();
			queryMgr.setPageLength(100);

			// create a search definition
			StringQueryDefinition query = queryMgr.newStringDefinition("search-options");
			
			query.setCriteria(buildQuery());
			query.setDirectory("/metadata/");
			
			model.addAttribute("terms", terms);

			// create a handle for the search results
			SearchHandle resultsHandle = new SearchHandle();

			// run the search
			queryMgr.search(query, resultsHandle);

			model.addAttribute("facets", resultsHandle.getFacetResults());
			model.addAttribute("filters", filters);
			
			List<PhotoResult> searchResults = new ArrayList<PhotoResult>();
			
			
			
			for(MatchDocumentSummary result: resultsHandle.getMatchResults()) {
				
				PhotoResult searchResult = new PhotoResult(getPhotoID(result));
				
				searchResult.setTitle(getPhotoTitle(result));
				
				for (MatchLocation location: result.getMatchLocations()) {
					if(location.getPath().endsWith("/photo/description")) {
						
						String highlights = "";
						
						for (MatchSnippet snippet : location.getSnippets()) {
							boolean isHighlighted = snippet.isHighlighted();
	
							if (isHighlighted)
								highlights += "<b>";
							highlights += snippet.getText();
							if (isHighlighted)
								highlights += "</b>";
						}
						
						searchResult.setHighlights(highlights);
					}
				}
				
				searchResults.add(searchResult);
			}

			model.addAttribute("results",searchResults.toArray());
			
		} catch(Exception e){
			System.out.println(e.toString());
		} 
		
		model.addAttribute("query", new String());
		
		return "search";
	}

	private String getPhotoTitle(MatchDocumentSummary result) {
		return result.getMetadata().getDocumentElement().getChildNodes().item(1).getTextContent() +
		", " + result.getMetadata().getDocumentElement().getChildNodes().item(3).getTextContent();
	}

	private String getPhotoID(MatchDocumentSummary result) {
		return result.getUri().substring(0,result.getUri().indexOf('.')).substring(result.getUri().lastIndexOf('/') + 1);
	}
	
	@RequestMapping(value = "/terms", method = RequestMethod.POST)
	public String term(Locale locale, Tag tag, Model model, @RequestParam String terms) {
		logger.info("Persist tag! The client locale is {}.", locale);
		
		this.terms = terms;
		
		return "redirect:/search/";
	}
	
	@RequestMapping(value = "/filter/{filter}", method = RequestMethod.GET)
	public String filter(Locale locale, Tag tag, Model model, @PathVariable String filter) {
		logger.info("Persist tag! The client locale is {}.", locale);
		
		StringTokenizer st = new StringTokenizer(filter, ":"); 
		while(st.hasMoreTokens()) { 
			String key = st.nextToken(); 
			String val = st.nextToken(); 
			filters.put(key, val);
		} 
		
		return "redirect:/search/";
	}
	
	@RequestMapping(value = "/reset/{filter_class}", method = RequestMethod.GET)
	public String reset(Locale locale, Tag tag, Model model, @PathVariable String filter_class) {
		logger.info("Persist tag! The client locale is {}.", locale);
	
		filters.remove(filter_class);
		
		return "redirect:/search/";
	}
	
	private String buildQuery() {
		String query = terms;
		
		Iterator<Entry<String, String>> it = filters.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,String> pairs = (Entry<String, String>)it.next();
	        query = query + " " + pairs.getKey() + ":\"" + pairs.getValue() + "\"";
	    }
		return query;
	}
}
