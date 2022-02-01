package it.unicam.cs.hyperledgerfabricwizard

import it.unicam.cs.hyperledgerfabricwizard.client.Network
import it.unicam.cs.hyperledgerfabricwizard.utils.fromJson
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@CrossOrigin("*")
@RestController
class Controller {
    @PostMapping("/submit", consumes = ["application/json"], produces = ["application/zip"])
    fun submit(@RequestBody networkJson: String): ResponseEntity<ByteArray>? {
        return networkJson.fromJson<Network>()?.let { Wizard.submit(it) }
    }
}