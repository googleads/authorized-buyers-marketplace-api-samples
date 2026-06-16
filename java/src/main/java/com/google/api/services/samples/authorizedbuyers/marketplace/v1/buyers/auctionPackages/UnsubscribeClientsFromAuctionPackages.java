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

package com.google.api.services.samples.authorizedbuyers.marketplace.v1.buyers.auctionPackages;

import com.google.api.services.authorizedbuyersmarketplace.v1.AuthorizedBuyersMarketplace;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.AuctionPackage;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.UnsubscribeClientsRequest;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * This sample illustrates how to unsubscribe one or more clients from a specified auction package.
 */
public class UnsubscribeClientsFromAuctionPackages {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Long accountId = parsedArgs.getLong("account_id");
    Long auctionPackageId = parsedArgs.getLong("auction_package_id");
    List<String> clientIds = parsedArgs.getList("client_ids");
    String name = String.format("buyers/%d/auctionPackages/%d", accountId, auctionPackageId);

    AuctionPackage auctionPackage = null;

    UnsubscribeClientsRequest unsubscribeClientsRequest = new UnsubscribeClientsRequest();
    unsubscribeClientsRequest.setClients(clientIds);

    try {
      auctionPackage =
          marketplaceClient
              .buyers()
              .auctionPackages()
              .unsubscribeClients(name, unsubscribeClientsRequest)
              .execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }

    System.out.printf(
        "Unsubscribing the following clients to for buyer \"%d\" from auction "
            + "package with ID \"%s\":%n",
        accountId, auctionPackageId);
    System.out.println("\t- " + String.join(String.format("%n\t- "), clientIds));
    Utils.printAuctionPackage(auctionPackage);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("UnsubscribeClientsFromAuctionPackages")
            .build()
            .defaultHelp(true)
            .description(("Unsubscribe one or more clients from the specified auction package."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the clients unsubscribing from the"
                + " auction package exist. This will be used to construct the name used as a path"
                + " parameter for the auctionPackages.unsubscribeClients request, and client names"
                + " that will be included in the body of the auctionPackages.unsubscribeClients"
                + " request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("--auction_package_id")
        .help(
            "The resource ID of the buyers.auctionPackages resource that the buyer is unsubscribing"
                + " their clients from. This will be used to construct the name used as a path"
                + " parameter for the auctionPackages.unsubscribeClients request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("--client_ids")
        .help(
            "The resource IDs of one or more buyers.clients resources that the buyer is"
                + " unsubscribing from an auction package. This will be used to construct client"
                + " names that will be included in the body of the"
                + " auctionPackages.unsubscribeClients request. Specify each client ID separated by"
                + " a space.")
        .required(true)
        .type(Long.class)
        .nargs("+");

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
