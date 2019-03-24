package org.project.common.ejb;

//import javax.ejb.LocalBean;
import java.util.HashMap;

import javax.ejb.Stateless;

//import org.project.common.router.CommonRouter;
//import org.project.common.router.CommonServerRouter;


/**
 * Session Bean implementation class CommonSessionBeanInterface
 */
@Stateless
//@LocalBean
public class CommonSessionBean implements CommonSessionBeanInterfaceRemote {

    /**
     * Default constructor. 
     */
    public CommonSessionBean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public HashMap<String,Object> entry(HashMap<String,Object>  txnMap) {
//CommonServerRouter router = new CommonServerRouter();
//txnMap = router.entry(txnMap);
		return txnMap;
	}

    
}
