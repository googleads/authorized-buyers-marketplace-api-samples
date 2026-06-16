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
import com.google.api.services.authorizedbuyersmarketplace.v1.model.ListFinalizedDealsResponse;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** This sample illustrates how to list finalized deals for a given buyer and their clients. */
public class ListFinalizedDeals {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Long accountId = parsedArgs.getLong("account_id");
    Integer pageSize = parsedArgs.getInt("page_size");
    String parentBuyerName = String.format("buyers/%d", accountId);
    String pageToken = null;

    System.out.printf("Found finalized deals for buyer account ID '%d':%n", accountId);

    do {
      List<FinalizedDeal> finalizedDeals = null;

      try {
        ListFinalizedDealsResponse response =
            marketplaceClient
                .buyers()
                .finalizedDeals()
                .list(parentBuyerName)
                .setFilter(parsedArgs.getString("filter"))
                .setOrderBy(parsedArgs.getString("order_by"))
                .setPageSize(pageSize)
                .setPageToken(pageToken)
                .execute();

        finalizedDeals = response.getFinalizedDeals();
        pageToken = response.getNextPageToken();
      } catch (IOException ex) {
        System.out.printf("Marketplace API returned error response:%n%s", ex);
        System.exit(1);
      }
      if (finalizedDeals == null) {
        System.out.println("No finalized deals found.");
      } else {
        for (FinalizedDeal finalizedDeal : finalizedDeals) {
          Utils.printFinalizedDeal(finalizedDeal);
        }
      }
    } while (pageToken != null);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("ListFinalizedDeals")
            .build()
            .defaultHelp(true)
            .description(("Lists finalized deals associated with the given buyer account."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the finalized deals are being"
                + " retrieved. This will be used to construct the parent used as a path parameter"
                + " for the finalizedDeals.list request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-f", "--filter")
        .help(
            "Query string to filter finalized deals. By default, this example will filter by "
                + "deal type to retrieve programmatic guaranteed deals to demonstrate usage.")
        .setDefault("deal.dealType = PROGRAMMATIC_GUARANTEED");
    parser
        .addArgument("-o", "--order_by")
        .help(
            "Query string used to sort the response of the list method. By default, this "
                + "example will return deals in descending order of their flight start time to "
                + "demonstrate usage. To learn more about the syntax for this parameter, see: "
                + "https://cloud.google.com/apis/design/design_patterns#sorting_order")
        .setDefault("deal.flightStartTime desc");
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

