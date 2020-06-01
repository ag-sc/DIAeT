package edu.ubi.sc.haf;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

public class ServiceRequests implements HttpHandler {
	Gson gson = new Gson();

	private String readRequestBody(HttpServerExchange exchange) {
		// exchange.getRequestMethod();

		if (METHOD_POST.equals(exchange.getRequestMethod())) {

			StringBuilder builder = new StringBuilder();

			try (InputStreamReader isr = new InputStreamReader(exchange.getInputStream());
					BufferedReader reader = new BufferedReader(isr)) {

				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			String body = builder.toString();
			return body;
		} else {
			// not a HTTP POST
			return null;
		}
	}

	private Map<String, Object> handleApiRequest(Map<String, Object> requestData) {
		Map<String, Object> responseData = new HashMap<>();

		return responseData;
	}

	// private static HttpString METHOD_GET = new HttpString("GET");
	private static HttpString METHOD_POST = new HttpString("POST");

	public void handleRequestClinical(final HttpServerExchange exchange) throws Exception {
		System.out.println("Incoming request: " + exchange.getRequestURI());

		String requestURI = exchange.getRequestURI();

		if (requestURI.equals("/get_data")) {
			exchange.setStatusCode(200);
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
			String requestBody = readRequestBody(exchange);

			Map<String, Object> requestData = null;
			BackendInput backendInput = null;

			if (requestBody != null) {
				requestBody = requestBody.trim();

				// if you like you can replace Map.class with your own implementation
				// for more structured input parsing
				// see: https://www.mkyong.com/java/how-to-parse-json-with-gson/
				backendInput = gson.fromJson(requestBody, BackendInput.class);
				backendInput.drug1 = backendInput.drug1.replace(' ', '_');
				backendInput.drug2 = backendInput.drug2.replace(' ', '_');
				
				// generate response ///////////////////////////////////////////////////
				BackendOutput backendOutput;
				
				if(backendInput.disease.equalsIgnoreCase("Glaucoma")) {
					backendOutput = GlaucomaHAF.Main(backendInput);
				} else {
					backendOutput = DiabetesHAF.Main(backendInput);
				}

				exchange.getResponseSender().send(gson.toJson(backendOutput));
			}

		} else {

			sendNotFound(exchange);

		}
	}
		
		public void handleRequest(final HttpServerExchange exchange) throws Exception {
			System.out.println("Incoming request: " + exchange.getRequestURI());

			String requestURI = exchange.getRequestURI();

			if (requestURI.equals("/get_data")) {
				exchange.setStatusCode(200);
				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
				String requestBody = readRequestBody(exchange);

				Map<String, Object> requestData = null;
				BackendInputMachineReading backendInput = null;

				if (requestBody != null) {
					requestBody = requestBody.trim();

					// if you like you can replace Map.class with your own implementation
					// for more structured input parsing
					// see: https://www.mkyong.com/java/how-to-parse-json-with-gson/
					backendInput = gson.fromJson(requestBody, BackendInputMachineReading.class);
					
					// generate response ///////////////////////////////////////////////////
					BackendOutputMachineReading backendOutput = null;
					
					backendOutput = MachineReadingMain.Main(backendInput);

					exchange.getResponseSender().send(gson.toJson(backendOutput));
				}

			} else {

				sendNotFound(exchange);

			}
	}

	private void sendNotFound(HttpServerExchange exchange) {
		exchange.setStatusCode(404);
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
		exchange.getResponseSender().send("The requested resource was not found on this server.");
	}

}