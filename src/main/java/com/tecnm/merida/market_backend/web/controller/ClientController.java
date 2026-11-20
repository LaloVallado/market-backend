package com.tecnm.merida.market_backend.web.controller;

import com.tecnm.merida.market_backend.domain.Client;
import com.tecnm.merida.market_backend.domain.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@Tag(name = "Client", description = "Manage your clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("")
    @Operation(summary = "Get all clients", description = "Return a list of all clients")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of clients")
    public ResponseEntity<List<Client>> getAll() {
        return new ResponseEntity<>(clientService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID", description = "Return a client by its ID if it exists")
    @ApiResponse(responseCode = "200", description = "Client found")
    @ApiResponse(responseCode = "404", description = "Client not found")
    public ResponseEntity<Client> getClient(
            @Parameter(description = "ID of the client", example = "4546221", required = true)
            @PathVariable("id") String clientId) {
        return clientService.getClient(clientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @Operation(
            summary = "Create a new client",
            description = "Register a new client. The clientId is provided by the user (max 20 characters)",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Example Client",
                                    value = """
                                            {
                                               "clientId" : "CLI-001",
                                               "name" : "Eduardo",
                                               "lastName" : "Vallado",
                                               "phone" : 9991234567,
                                               "address" : "Calle 60 # 123, Merida",
                                               "email" : "eduardo@correo.com"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "201", description = "Client created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data or the client already exists")
    public ResponseEntity<Client> save(@RequestBody Client client) {
        return new ResponseEntity<>(clientService.save(client), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a client",
            description = "Update the data of an existing client",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Example Client Update",
                                    value = """
                                            {
                                               "name" : "Eduardo",
                                               "lastName" : "Vallado Villamonte",
                                               "phone" : 9997654321,
                                               "address" : "Calle 21 # 45, Merida",
                                               "email" : "eduardo.vallado@correo.com"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "200", description = "Client updated successfully")
    @ApiResponse(responseCode = "404", description = "Client not found")
    public ResponseEntity<Client> update(
            @Parameter(description = "ID of the client to be updated", example = "CLI-001", required = true)
            @PathVariable("id") String clientId,
            @RequestBody Client client) {
        return clientService.update(clientId, client)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a client by ID", description = "Deletes a client if it exists and has no purchases")
    @ApiResponse(responseCode = "200", description = "Client deleted successfully")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @ApiResponse(responseCode = "409", description = "The client has purchases and cannot be deleted")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the client to be deleted", example = "CLI-001", required = true)
            @PathVariable("id") String clientId) {
        if (clientService.delete(clientId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}