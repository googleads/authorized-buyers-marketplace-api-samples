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
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.lang3.RandomUtils;

/**
 * Creates a client user for the given client.
 *
 * <p>When a client user is created, the specified email address will receive an email to confirm
 * access to the Authorized Buyers UI. It will remain in the "INVITED" state and be unable to access
 * the UI until the specified email has approved of the change.
 */
public class CreateClientUsers {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Integer accountId = parsedArgs.getInt("account_id");
    String clientId = parsedArgs.getString("client_id");

    String parentClientName = String.format("buyers/%d/clients/%s", accountId, clientId);

    ClientUser newClientUser = new ClientUser();
    newClientUser.setEmail(parsedArgs.getString("email"));

    ClientUser clientUser = null;
    try {
      clientUser =
          marketplaceClient
              .buyers()
              .clients()
              .users()
              .create(parentClientName, newClientUser)
              .execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }

    System.out.printf("Created client user for client with name \"%s\":%n", parentClientName);
    Utils.printClientUser(clientUser);
  }

  public static void main(String[] args) {
    RandomUtils rng = new RandomUtils();

    ArgumentParser parser =
        ArgumentParsers.newFor("CreateClientUsers")
            .build()
            .defaultHelp(true)
            .description(("Creates a client user for the given buyer and client ID."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the client user is to be created."
                + " This will be used to construct the name used as a path parameter for the"
                + " users.create request.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-c", "--client_id")
        .help(
            "The resource ID of the buyers.clients resource under which the client user is to be"
                + " created. This will be used to construct the name used as a path parameter for"
                + " the users.create request.")
        .required(true);
    parser
        .addArgument("-e", "--email")
        .help(
            "The client user's email address that has to be unique across all client users for "
                + "a given client. By default, this will be set to a randomly generated email for "
                + "demonstration purposes.")
        .type(String.class)
        .setDefault(String.format("testemail%s@test.com", rng.nextInt(10000000, 99999999)));

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
