/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.api.services.samples.authorizedbuyers.marketplace.v1;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.authorizedbuyersmarketplace.v1.AuthorizedBuyersMarketplace;
import com.google.api.services.authorizedbuyersmarketplace.v1.AuthorizedBuyersMarketplaceScopes;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Client;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A sample application that authenticates and runs a request against the Authorized Buyers
 * Marketplace API.
 */
public class FirstApiRequest {

  /**
   * Be sure to specify the name of your application. If the application name is {@code null} or
   * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
   */
  private static final String APPLICATION_NAME = "APPLICATION_NAME_HERE";

  // Full path to JSON Key file - include file name.
  private static final java.io.File JSON_FILE = new java.io.File("INSERT_PATH_TO_JSON_FILE");

  // Name of the buyer resource for which the API call is being made.
  private static final String BUYER_NAME = "INSERT_BUYER_RESOURCE_NAME";

  // Global instance of the HTTP transport.
  private static HttpTransport httpTransport;

  // Global instance of the JSON factory.
  private static final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

  public static void main(String[] args) throws Exception {
    // Create credentials using the JSON key file.
    GoogleCredentials credentials = null;

    try (FileInputStream serviceAccountStream = new FileInputStream((JSON_FILE))) {
      Set<String> scopes = new HashSet<>(AuthorizedBuyersMarketplaceScopes.all());
      credentials = ServiceAccountCredentials.fromStream(serviceAccountStream).createScoped(scopes);
    } catch (IOException ex) {
      System.out.println("Can't complete authorization step. Did you specify a JSON key file?");
      System.out.println(ex);
      System.exit(1);
    }

    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
    httpTransport = GoogleNetHttpTransport.newTrustedTransport();

    // Use the credentials to create a client for the API service.
    AuthorizedBuyersMarketplace marketplaceClient =
        new AuthorizedBuyersMarketplace.Builder(httpTransport, jsonFactory, requestInitializer)
            .setApplicationName(APPLICATION_NAME)
            .build();

    // Call the buyers.clients.list method to get a list of clients for the given buyer.
    List<Client> clients =
        marketplaceClient.buyers().clients().list(BUYER_NAME).execute().getClients();

    if (clients != null && clients.size() > 0) {
      System.out.printf("Listing of clients associated with buyer \"%s\"%n", BUYER_NAME);
      for (Client client : clients) {
        System.out.printf("* Client name: %s\n", client.getName());
      }
    } else {
      System.out.printf(
          "No clients were found that were associated with buyer \"%s\"%n.", BUYER_NAME);
    }
  }
}
