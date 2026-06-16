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
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** Creates a client for the given buyer account ID. */
public class CreateClients {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Long accountId = parsedArgs.getLong("account_id");

    String parentBuyerName = String.format("buyers/%d", accountId);

    Client newClient = new Client();
    newClient.setDisplayName(parsedArgs.getString("display_name"));
    newClient.setRole(parsedArgs.getString("role"));
    newClient.setSellerVisible(parsedArgs.getBoolean("seller_visible"));

    String partnerClientId = parsedArgs.getString("partner_client_id");
    if (partnerClientId != null) {
      newClient.setPartnerClientId(partnerClientId);
    }

    Client client = null;
    try {
      client = marketplaceClient.buyers().clients().create(parentBuyerName, newClient).execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }

    System.out.printf("Created client for buyer Account ID '%d':%n", accountId);
    Utils.printClient(client);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("CreateClients")
            .build()
            .defaultHelp(true)
            .description(("Creates a client for the given buyer account ID."));
    parser
        .addArgument("-a", "--account_id")
        .help("The resource ID of the buyers resource under which the client is to be created. ")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-d", "--display_name")
        .help(
            "Display name shown to publishers. Must be unique for clients without "
                + "partnerClientId specified. Maximum length of 255 characters is allowed. By "
                + "default, this sample will specify a generated name.")
        .type(String.class)
        .setDefault(String.format("TEST_CLIENT_%s", UUID.randomUUID()));
    parser
        .addArgument("-p", "--partner_client_id")
        .help(
            "Arbitrary unique identifier provided by the buyer. This field can be used to associate"
                + " a client with an identifier in the namespace of the buyer. If present, it must"
                + " be unique across all the clients. By default, this sample will not specify a"
                + "partnerClientId.")
        .type(String.class);
    parser
        .addArgument("-r", "--role")
        .help(
            "The role assigned to the client, which determines its permissions. By default, this"
                + " will be set to CLIENT_DEAL_VIEWER. For more details on how to interpret the"
                + " different roles, see: "
                + "https://developers.google.com/authorized-buyers/apis/marketplace/reference/rest/v1/buyers.clients#ClientRole")
        .type(String.class)
        .setDefault("CLIENT_DEAL_VIEWER");
    parser
        .addArgument("-s", "--seller_visible")
        .help(
            "Whether the client will be visible to publishers. By default, this sample will "
                + "set this to false.")
        .type(Boolean.class)
        .setDefault(false);

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
