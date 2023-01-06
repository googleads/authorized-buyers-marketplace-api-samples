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

package com.google.api.services.samples.authorizedbuyers.marketplace.v1.buyers.proposals.deals;

import com.google.api.services.authorizedbuyersmarketplace.v1.AuthorizedBuyersMarketplace;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Deal;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** This sample illustrates how to get a deal for the given buyer, proposal, and deal IDs. */
public class GetDeals {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Long accountId = parsedArgs.getLong("account_id");
    String proposalId = parsedArgs.getString("proposal_id");
    Long dealId = parsedArgs.getLong("deal_id");
    String name = String.format("buyers/%d/proposals/%s/deals/%d", accountId, proposalId, dealId);

    Deal deal = null;

    try {
      deal = marketplaceClient.buyers().proposals().deals().get(name).execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }

    System.out.printf("Found deal with name \"%s\":%n", name);
    Utils.printDeal(deal);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("GetDeals")
            .build()
            .defaultHelp(true)
            .description(("Get a deal for the given buyer, proposal, and deal IDs."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the deal is being retrieved "
                + "This will be used to construct the parent used as a path parameter for the "
                + "deals.get request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-d", "--deal_id")
        .help(
            "The resource ID of the buyers.proposals.deals resource that is being retrieved. This"
                + " will be used to construct the name used as a path parameter for the deals.get "
                + "request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-p", "--proposal_id")
        .help(
            "The resource ID of the buyers.proposals resource under which the deal is being"
                + " retrieved. This will be used to construct the name used as a path parameter for"
                + " the deals.get request.")
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
