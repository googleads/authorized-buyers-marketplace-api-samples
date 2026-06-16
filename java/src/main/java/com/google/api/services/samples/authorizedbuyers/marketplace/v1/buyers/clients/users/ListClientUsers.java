/*
 * Copyright 2022 Google LLC
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

package com.google.api.services.samples.authorizedbuyers.marketplace.v1.buyers.clients.users;

import com.google.api.services.authorizedbuyersmarketplace.v1.AuthorizedBuyersMarketplace;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.ClientUser;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.ListClientUsersResponse;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** This sample illustrates how to list client users for a given client. */
public class ListClientUsers {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Long accountId = parsedArgs.getLong("account_id");
    Long clientId = parsedArgs.getLong("client_id");
    Integer pageSize = parsedArgs.getInt("page_size");
    String parentClientName = String.format("buyers/%d/clients/%d", accountId, clientId);
    String pageToken = null;

    System.out.printf("Found client users for client with name \"%s\":%n", parentClientName);

    do {
      List<ClientUser> clientUsers = null;

      try {
        ListClientUsersResponse response =
            marketplaceClient
                .buyers()
                .clients()
                .users()
                .list(parentClientName)
                .setPageSize(pageSize)
                .setPageToken(pageToken)
                .execute();

        clientUsers = response.getClientUsers();
        pageToken = response.getNextPageToken();
      } catch (IOException ex) {
        System.out.printf("Marketplace API returned error response:%n%s", ex);
        System.exit(1);
      }
      if (clientUsers == null) {
        System.out.println("No client users found.");
      } else {
        for (ClientUser clientUser : clientUsers) {
          Utils.printClientUser(clientUser);
        }
      }
    } while (pageToken != null);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("ListClientUsers")
            .build()
            .defaultHelp(true)
            .description(("Lists client users associated with the given buyer account."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the parent client was created. "
                + "This will be used to construct the parent used as a path parameter for the "
                + "users.list request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-c", "--client_id")
        .help(
            "The resource ID of the buyers.clients resource we are retrieving client users on"
                + " behalf of. This will be used to construct the parent used as a path parameter"
                + " for the users.list request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-p", "--page_size")
        .help(
            "The number of rows to return per page. The server may return fewer rows than "
                + "specified.")
        .setDefault(Utils.getMaximumPageSize())
        .type(Integer.class);

    Namespace parsedArgs = null;
    try {
      parsedArgs = parser.parseArgs(args);
    } catch (ArgumentParserException ex) {
      parser.handleError(ex);
      System.exit(1);
    }

    AuthorizedBuyersMarketplace client = null;
    try {
      client = Utils.getMarketplaceClient();
    } catch (IOException ex) {
      System.out.printf("Unable to create Marketplace API service:%n%s", ex);
      System.out.println("Did you specify a valid path to a service account key file?");
      System.exit(1);
    } catch (GeneralSecurityException ex) {
      System.out.printf("Unable to establish secure HttpTransport:%n%s", ex);
      System.exit(1);
    }

    execute(client, parsedArgs);
  }
}
