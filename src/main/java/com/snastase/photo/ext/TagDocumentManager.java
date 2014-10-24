package com.snastase.photo.ext;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.extensions.ResourceManager;
import com.marklogic.client.extensions.ResourceServices;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.io.marker.AbstractReadHandle;
import com.marklogic.client.io.marker.AbstractWriteHandle;
import com.marklogic.client.util.RequestParameters;

public class TagDocumentManager extends ResourceManager {
	
	static final public String NAME = "tag";

    public TagDocumentManager(DatabaseClient client) {
    	super();
 
    	// Initialize the Resource Manager via the Database Client
    	client.init(NAME, this);
    }
    
    // Service for reading PDF real-time created
    public void tag(String uri) {
    	
        //Build up the set of parameters for the service call
        RequestParameters params = new RequestParameters();
        
        // Add the tag service parameter
        params.add("uri", uri);
        
        try{

	        // get the initialized service object from the base class
	        ResourceServices services = getServices();
	        
	        AbstractWriteHandle input = new StringHandle("<input/>");
			AbstractReadHandle output = new StringHandle();
			services.put(params, input, output);
		
        } catch(Exception ex) {
			throw new RuntimeException(ex);
		}
    }
}
