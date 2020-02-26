/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.nic.nc;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
public class ClientController {

    @RequestMapping(value = "/cmd/1")
    public ResponseEntity sendSms() {
        return ResponseEntity.ok("123");
    }

}
