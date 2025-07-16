package com.pruebaTecnica.msInventario;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pruebaTecnica.msInventario.dto.FullInventoryDTO;
import com.pruebaTecnica.msInventario.service.InventoryService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InventoryServiceIntegrationTest {

  // 1. Iniciar un servidor WireMock para simular la API de Productos
  static WireMockServer wireMockServer = new WireMockServer(8091); // Usar el puerto real de productos
  // para en caso usar puertos dinámicos
  // static WireMockServer wireMockServer = new
  // WireMockServer(options().dynamicPort());

  @Autowired
  private InventoryService inventoryService;

  // 2. Antes de todas las pruebas, iniciar el servidor WireMock
  @BeforeAll
  static void startWireMockServer() {
    wireMockServer.start();
  }

  // 3. Después de todas las pruebas, detener el servidor WireMock
  @AfterAll
  static void stopWireMockServer() {
    wireMockServer.stop();
  }

  // 4. Configurar las propiedades dinámicas para que el servicio use el WireMock
  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("services.products.url", wireMockServer::baseUrl);
    registry.add("services.products.api-key", () -> "mi-api-key-secreta");
  }

  @Test
  void whenGetInventoryDetails_thenCallsProductServiceAndReturnsCombinedData() {
    long productId = 1L;

    wireMockServer.stubFor(
        get(urlEqualTo("/api/v1/products/" + productId))
            // Verificar que la petición venga con la API Key correcta
            .withHeader("X-API-KEY", equalTo("mi-api-key-secreta"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                // Este es el cuerpo JSON que simula la respuesta de msProductos
                .withBody("{\"id\":1,\"nombre\":\"Producto de Prueba WireMock\",\"precio\":99.99}")));

    // llamar al servicio de inventario
    FullInventoryDTO result = inventoryService.getInventoryDetails(productId);

    assertThat(result).isNotNull();
    assertThat(result.getNombreProducto()).isEqualTo("Producto de Prueba WireMock");
    assertThat(result.getPrecioProducto()).isEqualTo(99.99);

    assertThat(result.getCantidad()).isEqualTo(0);

    wireMockServer.verify(1, getRequestedFor(urlEqualTo("/api/v1/products/" + productId)));
  }
}
