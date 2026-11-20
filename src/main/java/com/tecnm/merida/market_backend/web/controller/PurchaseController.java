package com.tecnm.merida.market_backend.web.controller;

import com.tecnm.merida.market_backend.domain.Purchase;
import com.tecnm.merida.market_backend.domain.dto.PurchaseRequestDTO;
import com.tecnm.merida.market_backend.domain.dto.PurchaseUpdateDTO;
import com.tecnm.merida.market_backend.domain.service.PurchaseService;
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
@RequestMapping("/purchases")
@Tag(name = "Purchase", description = "Manage purchases (master-detail)")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("")
    @Operation(summary = "Get all purchases", description = "Return all purchases with their products")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of purchases")
    public ResponseEntity<List<Purchase>> getAll() {
        return new ResponseEntity<>(purchaseService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get purchase by ID", description = "Return a purchase with its products")
    @ApiResponse(responseCode = "200", description = "Purchase found")
    @ApiResponse(responseCode = "404", description = "Purchase not found")
    public ResponseEntity<Purchase> getPurchase(
            @Parameter(description = "ID of the purchase", example = "1", required = true)
            @PathVariable("id") int purchaseId) {
        return purchaseService.getPurchase(purchaseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{idClient}")
    @Operation(summary = "Get purchases by client", description = "Return all purchases of a client")
    @ApiResponse(responseCode = "200", description = "Purchases found")
    @ApiResponse(responseCode = "404", description = "Purchases not found")
    public ResponseEntity<List<Purchase>> getByClient(
            @Parameter(description = "ID of the client", example = "4546221", required = true)
            @PathVariable("idClient") String clientId) {
        return purchaseService.getByClient(clientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @Operation(
            summary = "Create a new purchase (master-detail)",
            description = "Receives the purchase header and its list of products in one JSON. "
                    + "Everything is saved in one transaction using cascade. "
                    + "medioPago accepts TARJETA / EFECTIVO (stored as T / E).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Example Purchase",
                                    value = """
                                            {
                                              "idCliente": "CLI-001",
                                              "medioPago": "TARJETA",
                                              "comentario": "Entregar por la tarde",
                                              "productosAdquiridos": [
                                                {
                                                  "idProducto": 15,
                                                  "cantidad": 2,
                                                  "total": 8000.0
                                                },
                                                {
                                                  "idProducto": 8,
                                                  "cantidad": 1,
                                                  "total": 150.0
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "201", description = "Purchase and its products created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data (client or product does not exist, empty list, etc.)")
    public ResponseEntity<Purchase> save(@RequestBody PurchaseRequestDTO request) {
        return new ResponseEntity<>(purchaseService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a purchase header",
            description = "Update payment method, comment and state of a purchase. Estado: P = pending, C = completed",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Example Purchase Update",
                                    value = """
                                            {
                                              "medioPago": "EFECTIVO",
                                              "comentario": "Entregado en tienda",
                                              "estado": "C"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "200", description = "Purchase updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data")
    @ApiResponse(responseCode = "404", description = "Purchase not found")
    public ResponseEntity<Purchase> update(
            @Parameter(description = "ID of the purchase to be updated", example = "2", required = true)
            @PathVariable("id") int purchaseId,
            @RequestBody PurchaseUpdateDTO request) {
        return purchaseService.update(purchaseId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a purchase by ID", description = "Deletes a purchase and, by cascade, all its products")
    @ApiResponse(responseCode = "200", description = "Purchase deleted successfully")
    @ApiResponse(responseCode = "404", description = "Purchase not found")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the purchase to be deleted", example = "2", required = true)
            @PathVariable("id") int purchaseId) {
        if (purchaseService.delete(purchaseId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}