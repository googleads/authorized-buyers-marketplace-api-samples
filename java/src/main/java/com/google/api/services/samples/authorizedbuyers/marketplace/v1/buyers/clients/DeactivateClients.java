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

package com.google.api.services.samples.authorizedbuyers.marketplace.v1.buyers.clients;

import com.google.api.services.authorizedbuyersmarketplace.v1.AuthorizedBuyersMarketplace;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Client;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.DeactivateClientRequest;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * This sample illustrates how to deactivate a client for the given buyer account ID and client ID.
 */
public class DeactivateClients {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Integer accountId = parsedArgs.getInt("account_id");
    String clientId = parsedArgs.getString("client_id");
    String name = String.format("buyers/%d/clients/%s", accountId, clientId);

    Client client = null;

    try {
      client =
          marketplaceClient
              .buyers()
              .clients()
              .deactivate(name, new DeactivateClientRequest())
              .execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }

    System.out.printf(
        "Deactivated Client with ID \"%s\" for buyer account ID '%d':%n", clientId, accountId);
    Utils.printClient(client);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("DeactivateClients")
            .build()
            .defaultHelp(true)
            .description(("Deactivate a client for the given buyer account ID and client ID."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the client was created. This "
                + "will be used to construct the parent used as a path parameter for the "
                + "clients.deactivate request.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-c", "--client_id")
        .help(
            "The resource ID of the buyers.clients resource for which the client was created "
                + "This will be used to construct the name used as a path parameter for the "
                + "clients.deactivate request.")
        .required(true);

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
