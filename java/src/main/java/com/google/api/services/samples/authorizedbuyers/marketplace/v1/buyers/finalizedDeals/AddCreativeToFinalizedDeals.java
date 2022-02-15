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

package com.google.api.services.samples.authorizedbuyers.marketplace.v1.buyers.finalizedDeals;

import com.google.api.services.authorizedbuyersmarketplace.v1.AuthorizedBuyersMarketplace;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.AddCreativeRequest;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.FinalizedDeal;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * This sample illustrates how to add a creative to a given finalized deal that will be used in
 * bids.
 *
 * <p>It is recommended that those configuring programmatic guaranteed deals use this method to
 * associate at least one creative that is ready to be placed in bids with the deal before signaling
 * that the deal is ready to begin serving with finalizedDeals.setReadyToServe.
 *
 * <p>A buyer's creatives can be viewed with the Real-time Bidding API:
 * https://developers.google.com/authorized-buyers/apis/realtimebidding/reference/rest/v1/buyers.creatives
 */
public class AddCreativeToFinalizedDeals {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Integer accountId = parsedArgs.getInt("account_id");
    String creativeId = parsedArgs.getString("creative_id");
    String dealId = parsedArgs.getString("deal_id");
    String name = String.format("buyers/%d/finalizedDeals/%s", accountId, dealId);
    String creativeName = String.format("buyers/%d/creatives/%s", accountId, creativeId);

    AddCreativeRequest addCreativeRequest = new AddCreativeRequest();
    addCreativeRequest.setCreative(creativeName);

    FinalizedDeal finalizedDeal = null;

    try {
      finalizedDeal =
          marketplaceClient
              .buyers()
              .finalizedDeals()
              .addCreative(name, addCreativeRequest)
              .execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }

    System.out.printf(
        "Adding creative with name \"%s\" to finalized deal with name \"%s\":%n",
        creativeName, name);
    Utils.printFinalizedDeal(finalizedDeal);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("AddCreativeToFinalizedDeals")
            .build()
            .defaultHelp(true)
            .description(("Adds a creative to a given finalized deal that will be used in bids."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the finalized was created. This"
                + " will be used to construct the parent used as a path parameter for the"
                + " finalizedDeals.addCreative request, as well as the creative name included in"
                + " the request body.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-c", "--creative_id")
        .help(
            "The resource ID of the buyers.creatives resource that the buyer is adding to a"
                + " finalized deal. This will be used to construct the creative name included in"
                + " the request body.")
        .required(true);
    parser
        .addArgument("-d", "--deal_id")
        .help(
            "The resource ID of the buyers.finalizedDeals resource that the creative is being "
                + "added to. This will be used to construct the name used as a path parameter "
                + "for the finalizedDeals.addCreative request.")
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
