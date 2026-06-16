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
import com.google.api.services.authorizedbuyersmarketplace.v1.model.ListDealsResponse;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** This sample illustrates how to list deals for the given buyer's proposal. */
public class ListDeals {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Long accountId = parsedArgs.getLong("account_id");
    String proposalId = parsedArgs.getString("proposal_id");
    Integer pageSize = parsedArgs.getInt("page_size");
    String parent = String.format("buyers/%d/proposals/%s", accountId, proposalId);
    String pageToken = null;

    System.out.printf("Found deals for proposal with name \"%s\":%n", parent);

    do {
      List<Deal> deals = null;

      try {
        ListDealsResponse response =
            marketplaceClient
                .buyers()
                .proposals()
                .deals()
                .list(parent)
                .setPageSize(pageSize)
                .setPageToken(pageToken)
                .execute();

        deals = response.getDeals();
        pageToken = response.getNextPageToken();
      } catch (IOException ex) {
        System.out.printf("Marketplace API returned error response:%n%s", ex);
        System.exit(1);
      }
      if (deals == null) {
        System.out.println("No proposals found.");
      } else {
        for (Deal deal : deals) {
          Utils.printDeal(deal);
        }
      }
    } while (pageToken != null);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("ListDeals")
            .build()
            .defaultHelp(true)
            .description(("Lists deals for the given buyer's proposal."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the deals are being retrieved. This"
                + " will be used to construct the parent used as a path parameter for the"
                + " deals.list request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("--proposal_id")
        .help(
            "The resource ID of the buyers.proposals resource under which the deals are being"
                + " retrieved. This will be used to construct the parent used as a path parameter"
                + " for the deals.list request.")
        .required(true);
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
