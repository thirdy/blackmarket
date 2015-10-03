/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thirdy.blackmarket.ex;

/**
 *
 * @author thirdy
 */
public class BlackmarketRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BlackmarketRuntimeException(Exception ex) {
        super(ex);
    }
    
}
