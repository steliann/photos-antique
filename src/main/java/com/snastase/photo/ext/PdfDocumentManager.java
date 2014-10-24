package com.snastase.photo.ext;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.extensions.ResourceManager;
import com.marklogic.client.extensions.ResourceServices;
import com.marklogic.client.io.BytesHandle;
import com.marklogic.client.util.RequestParameters;

public class PdfDocumentManager extends ResourceManager{
	
	static final public String NAME = "pdf";

    public PdfDocumentManager(DatabaseClient client) {
    	super();
 
    	// Initialize the Resource Manager via the Database Client
    	client.init(NAME, this);
    }
    
    // Service for reading PDF real-time created
    public void read(String uri, BytesHandle bytesHandle) {
    	
        //Build up the set of parameters for the service call
        RequestParameters params = new RequestParameters();
        
        // Add the pdf service parameter
        params.add("uri", uri);
        
        try{

	        // get the initialized service object from the base class
	        ResourceServices services = getServices();
	        
			services.get(params, bytesHandle);
		
        } catch(Exception ex) {
			throw new RuntimeException(ex);
		}
    }

}
