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
import com.google.api.services.authorizedbuyersmarketplace.v1.model.SubscribeAuctionPackageRequest;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * This sample illustrates how to subscribe a given buyer account to a specified auction package.
 *
 * <p>Once subscribed, the bidder will begin receiving bid requests for the buyer account that
 * include the auction package deal ID and contain inventory matching the auction package's
 * targeting criteria.
 */
public class SubscribeToAuctionPackages {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Integer accountId = parsedArgs.getInt("account_id");
    String auctionPackageId = parsedArgs.getString("auction_package_id");
    String name = String.format("buyers/%d/auctionPackages/%s", accountId, auctionPackageId);

    AuctionPackage auctionPackage = null;

    try {
      auctionPackage =
          marketplaceClient
              .buyers()
              .auctionPackages()
              .subscribe(name, new SubscribeAuctionPackageRequest())
              .execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }

    System.out.printf(
        "Subscribing buyer with ID \"%d\" to auction package with ID \"%s\":%n",
        accountId, auctionPackageId);
    Utils.printAuctionPackage(auctionPackage);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("SubscribeToAuctionPackages")
            .build()
            .defaultHelp(true)
            .description(("Subscribe the given buyer account to the specified auction package."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource that will be subscribing to the auction"
                + " package. This will be used to construct the name used as a path parameter for"
                + " the auctionPackages.subscribe request.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("--auction_package_id")
        .help(
            "The resource ID of the buyers.auctionPackages resource that the buyer is subscribing"
                + " to. This will be used to construct the name used as a path parameter for the"
                + " auctionPackages.subscribe request.")
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
