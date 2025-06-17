package com.example.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

public class LocationFinder {
	public static String checkLocation() {
		String isVpnConnected = "";
		try {
			Process process = Runtime.getRuntime().exec("ipconfig");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				// Check for VPN-related information in the output
				if (line.contains("VPN")) {
					isVpnConnected = "VPN connection detected.";
					System.out.println(isVpnConnected + Date.from(new Date().toInstant()));
					return "Unknown";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (isVpnConnected == "" || isVpnConnected.isEmpty()) {
			System.out.println("No VPN connection detected.");
			String country = getPublicIpAddress();
			System.out.println(country + Date.from(new Date().toInstant()));
			return country;
		}
		return "Unknown";
	}

	private static String getPublicIpAddress() {
		try {
			// Get public IP address
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ipAddress = in.readLine();

			// Use IP address to get location details
			URL ipApiUrl = new URL("http://ip-api.com/json/" + ipAddress);
			BufferedReader reader = new BufferedReader(new InputStreamReader(ipApiUrl.openStream()));
			StringBuilder json = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
			reader.close();

			// Parse JSON response and extract country
			String country = extractCountry(json.toString());
			return country;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Unknown";
	}

	// Method to extract country from JSON response
	private static String extractCountry(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(json);
			return rootNode.get("country").asText();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Unknown";
	}
}