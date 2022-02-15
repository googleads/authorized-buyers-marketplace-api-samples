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

package com.google.api.services.samples.authorizedbuyers.marketplace.v1.buyers.proposals;

import com.google.api.services.authorizedbuyersmarketplace.v1.AuthorizedBuyersMarketplace;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.ListProposalsResponse;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Proposal;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** This sample illustrates how to list proposals for a given buyer. */
public class ListProposals {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Integer accountId = parsedArgs.getInt("account_id");
    Integer pageSize = parsedArgs.getInt("page_size");
    String parentBuyerName = String.format("buyers/%d", accountId);
    String pageToken = null;

    System.out.printf("Found proposals for buyer account ID '%d':%n", accountId);

    do {
      List<Proposal> proposals = null;

      try {
        ListProposalsResponse response =
            marketplaceClient
                .buyers()
                .proposals()
                .list(parentBuyerName)
                .setFilter(parsedArgs.getString("filter"))
                .setPageSize(pageSize)
                .setPageToken(pageToken)
                .execute();

        proposals = response.getProposals();
        pageToken = response.getNextPageToken();
      } catch (IOException ex) {
        System.out.printf("Marketplace API returned error response:%n%s", ex);
        System.exit(1);
      }
      if (proposals == null) {
        System.out.println("No proposals found.");
      } else {
        for (Proposal proposal : proposals) {
          Utils.printProposal(proposal);
        }
      }
    } while (pageToken != null);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("ListProposals")
            .build()
            .defaultHelp(true)
            .description(("Lists proposals associated with the given buyer account."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the proposals are being retrieved."
                + " This will be used to construct the parent used as a path parameter for the"
                + " proposals.list request.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-f", "--filter")
        .help(
            "Query string to filter proposals. By default, this example will filter by deal type to"
                + " retrieve proposals including programmatic guaranteed deals to demonstrate "
                + "usage.")
        .setDefault("dealType = PROGRAMMATIC_GUARANTEED");
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
