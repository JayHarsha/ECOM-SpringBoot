package com.ecom.ecommerce.utility;

import org.springframework.stereotype.Component;

/**
 * Utility layer for common usage
 */
@Component
public class CommonUtility {

    public boolean isEmpty(String inputString){
        return inputString == null || inputString.trim().isEmpty();
    }
}
