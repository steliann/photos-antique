package com.snastase.photo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.io.BytesHandle;
import com.snastase.photo.ext.PdfDocumentManager;

@Controller
@RequestMapping("/pdf")
public class PdfController {
	
	private static final Logger logger = LoggerFactory.getLogger(PdfController.class);

	@Autowired
	private DatabaseClient databaseClient;
	
	/**
	 * Get the JPEG image corresponding to the photo id.
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public void pdf(HttpServletResponse response, Locale locale, @PathVariable String id) throws IOException {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		byte[] contentBytes = new byte[0];
		
		try {
			// get a manager for binary documents
			PdfDocumentManager docMgr =  new PdfDocumentManager(databaseClient);
	
			// create a handle to receive the document content
			BytesHandle content = new BytesHandle();

			String uri = "/pdf/" + id + ".pdf";
			// read the document content
			docMgr.read(uri, content);
	
			// get the document content as a byte array
			contentBytes = content.get();

		} catch(Exception e){
			System.out.println(e.toString());
		} 
		
		InputStream input = new ByteArrayInputStream(contentBytes);
		OutputStream output = response.getOutputStream();
		byte[] buffer = new byte[10240];

		response.setContentType("application/pdf");
		response.setContentLength(contentBytes.length);

		try {
		    for (int length = 0; (length = input.read(buffer)) > 0;) {
		        output.write(buffer, 0, length);
		    }
		}
		finally {
		    try { output.close(); } catch (IOException ignore) {}
		    try { input.close(); } catch (IOException ignore) {}
		}
	}

}
