/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thirdy.blackmarket.core.ex;

/**
 *
 * @author thirdy
 */
public class BlackmarketRuntimeException extends RuntimeException {

    public BlackmarketRuntimeException(Exception ex) {
        super(ex);
    }
    
}
