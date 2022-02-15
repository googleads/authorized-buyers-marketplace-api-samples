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
import com.google.api.services.authorizedbuyersmarketplace.v1.model.FinalizedDeal;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.ResumeFinalizedDealRequest;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * This sample illustrates how to resume serving of a finalized deal.
 *
 * <p>Note that only programmatic guaranteed deals can be resumed by buyers at this time.
 */
public class ResumeFinalizedDeals {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Integer accountId = parsedArgs.getInt("account_id");
    String dealId = parsedArgs.getString("deal_id");
    String name = String.format("buyers/%d/finalizedDeals/%s", accountId, dealId);

    FinalizedDeal finalizedDeal = null;

    try {
      finalizedDeal =
          marketplaceClient
              .buyers()
              .finalizedDeals()
              .resume(name, new ResumeFinalizedDealRequest())
              .execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }

    System.out.printf(
        "Resumed finalized deal with ID \"%s\" for buyer account ID \"%d\":%n", dealId, accountId);
    Utils.printFinalizedDeal(finalizedDeal);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("ResumeFinalizedDeals")
            .build()
            .defaultHelp(true)
            .description(("Resume a finalized deal for the given buyer account ID and deal ID."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the finalized was created. "
                + "This will be used to construct the parent used as a path parameter for the "
                + "finalizedDeals.resume request.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-d", "--deal_id")
        .help(
            "The resource ID of the buyers.finalizedDeals resource that is being resumed. This "
                + "will be used to construct the name used as a path parameter for the "
                + "finalizedDeals.resume request.")
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
