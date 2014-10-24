package com.snastase.photo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.document.DocumentMetadataPatchBuilder.PatchHandle;
import com.marklogic.client.document.DocumentPatchBuilder;
import com.marklogic.client.document.DocumentPatchBuilder.Position;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.snastase.photo.ext.TagDocumentManager;
import com.snastase.photo.model.PhotoDetails;
import com.snastase.photo.model.Tag;


@Controller
@RequestMapping("/photo")
public class PhotoController {
	
	private static final Logger logger = LoggerFactory.getLogger(PhotoController.class);
	
	@Autowired
	private DatabaseClient databaseClient;
	
	/**
	 * Simply selects the photo view to render by returning its name.
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String photo(Locale locale, Model model, @PathVariable String id) {
		
		logger.info("Get photo! The client locale is {}.", locale);
		
		try {
			XMLDocumentManager xmlDocMgr = databaseClient.newXMLDocumentManager();
			
			// create a handle to receive the document content
			DOMHandle handle = new DOMHandle();
			
			// read the document content
			xmlDocMgr.read(getDocumentURI(id), handle);
			
			Document document = handle.get();
			
			PhotoDetails photo = new PhotoDetails(id);
			
			processResultDocument(document, photo);
			
			model.addAttribute("photo", photo);

		} catch(Exception e){
			System.out.println(e.toString());
		} 

		
		return "photo";
	}

	private void processResultDocument(Document document, PhotoDetails photo) {
		photo.setYear(Integer.parseInt(document.getElementsByTagName("year").item(0).getTextContent()));
		photo.setTitle(document.getElementsByTagName("title").item(0).getTextContent());
		photo.setAuthor(document.getElementsByTagName("author").item(0).getTextContent());
		photo.setCountry(document.getElementsByTagName("country").item(0).getTextContent());
		photo.setDescription(document.getElementsByTagName("description").item(0).getTextContent());
		
		if(document.getElementsByTagName("explicit").getLength() == 1)
			photo.setExplicit(Boolean.parseBoolean(document.getElementsByTagName("explicit").item(0).getTextContent()));
		
		List<String> tags = new ArrayList<String>();
		for (int i = 0; i < document.getElementsByTagName("tag").getLength(); i++) {
			tags.add(document.getElementsByTagName("tag").item(i).getTextContent());
		}
		
		photo.setTags(tags);
	}
	
	@RequestMapping(value = "/semtag/{id}", method = RequestMethod.POST)
	public String semTag(Locale locale, Model model, @PathVariable String id) {
		logger.info("Persist tag! The client locale is {}.", locale);
		
		try {
			// get a manager for binary documents
			TagDocumentManager docMgr =  new TagDocumentManager(databaseClient);

			String uri = getDocumentURI(id);
			// read the document content
			docMgr.tag(uri);

		} catch(Exception e){
			System.out.println(e.toString());
		} 
		
		return "redirect:/photo/" + id;
	}
	
	@RequestMapping(value = "/addtag/{id}", method = RequestMethod.POST)
	public String addTag(Locale locale, Tag tag, Model model, @PathVariable String id) {
		logger.info("Persist tag! The client locale is {}.", locale);
		
		try {
			XMLDocumentManager xmlDocMgr = databaseClient.newXMLDocumentManager();
			
			DocumentPatchBuilder xmlPatchBldr = xmlDocMgr.newPatchBuilder();
			
			if(hasTags(xmlDocMgr, id)) 
				xmlDocMgr.patch(getDocumentURI(id), buildTagPatch(tag, xmlPatchBldr));
			 else 
				xmlDocMgr.patch(getDocumentURI(id), buildTagsPatch(tag, xmlPatchBldr));
			
		} catch(Exception e){
			System.out.println(e.toString());
		} 
		
		return "redirect:/photo/" + id;
	}

	private PatchHandle buildTagsPatch(Tag tag,
			DocumentPatchBuilder xmlPatchBldr) {
		return xmlPatchBldr.insertFragment(
		    "/photo", 
		    Position.LAST_CHILD,
		    "<tags><tag>" + tag.getTagName() + "</tag></tags>")
		  .build();
	}

	private PatchHandle buildTagPatch(Tag tag, DocumentPatchBuilder xmlPatchBldr) {
		return xmlPatchBldr.insertFragment(
		    "/photo/tags", 
		    Position.LAST_CHILD,
		    "<tag>" + tag.getTagName() + "</tag>")
		  .build();
	}
	
	private boolean hasTags(XMLDocumentManager xmlDocMgr, String id) {
		
		DOMHandle handle = new DOMHandle();
		
		// read the document content
		xmlDocMgr.read(getDocumentURI(id), handle);
		
		Document document = handle.get();
		
		return document.getElementsByTagName("tag").getLength()>0;
	}

	private String getDocumentURI(String id) {
		String metadata_uri = "/metadata/" + id + ".xml";
		return metadata_uri;
	}

}
